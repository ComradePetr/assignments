package ru.spbau.mit;

import java.util.*;

public class TreeSetImpl<E> extends AbstractSet<E> {
    private static final Random random = new Random();

    private class Pair {
        public Node x, y;

        Pair(Node a, Node b) {
            x = a;
            y = b;
        }
    }

    private class Node {
        E x;
        int y = random.nextInt(), size = 1;
        Node l = null, r = null, p = null;

        Node(E x) {
            this.x = x;
        }

        private Node next() {
            Node cur = this;
            if (cur.r == null) {
                while (cur.p != null) {
                    Node curp = cur.p;
                    if (curp.l == cur) {
                        return curp;
                    }
                    cur = curp;
                }
                return null;
            } else {
                cur = cur.r;
                while (cur.l != null) {
                    cur = cur.l;
                }
                return cur;
            }
        }

        private Node minElement() {
            Node cur = this;
            while (cur.l != null) {
                cur = cur.l;
            }
            return cur;
        }
    }

    private class TreeIterator implements Iterator<E> {
        private Node cur = null;

        public TreeIterator(Node start) {
            cur = start;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return cur == null ? root != null : cur.next() != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if (cur == null) {
                if (root == null) {
                    throw new NoSuchElementException();
                } else {
                    cur = root.minElement();
                }
            } else {
                cur = cur.next();
                if (cur == null) {
                    throw new NoSuchElementException();
                }
            }
            return cur.x;
        }
    }

    private Node root = null;
    private final Comparator<E> cmp;

    public TreeSetImpl(Comparator<E> comparator) {
        cmp = comparator;
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return new TreeIterator(null);
    }

    @Override
    public int size() {
        return root == null ? 0 : root.size;
    }

    private Node recalc(Node x) {
        if (x != null) {
            x.p = null;
            x.size = 1;
            if (x.l != null) {
                x.l.p = x;
                x.size += x.l.size;
            }
            if (x.r != null) {
                x.r.p = x;
                x.size += x.r.size;
            }
        }
        return x;
    }

    private Pair split(Node v, E x, boolean equalsLeft) {
        if (v == null) {
            return new Pair(null, null);
        } else {
            int c = cmp.compare(x, v.x);
            if (c < 0 || (c == 0 && !equalsLeft)) {
                Pair p = split(v.l, x, equalsLeft);
                v.l = p.y;
                return new Pair(recalc(p.x), recalc(v));
            } else {
                Pair p = split(v.r, x, equalsLeft);
                v.r = p.x;
                return new Pair(recalc(v), recalc(p.y));
            }
        }
    }

    private Pair split(Node v, E x) {
        return split(v, x, false);
    }

    private Node merge(Node l, Node r) {
        if (l == null) {
            return r;
        } else if (r == null) {
            return l;
        } else if (l.y < r.y) {
            l.r = merge(l.r, r);
            return recalc(l);
        } else {
            r.l = merge(l, r.l);
            return recalc(r);
        }
    }

    @Override
    public boolean contains(Object eo) {
        E e = (E) eo;
        Pair p = split(root, e);
        if (p.y == null) {
            return false;
        }
        boolean res = cmp.compare(p.y.minElement().x, e) == 0;
        root = merge(p.x, p.y);
        return res;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        Pair p = split(root, e);
        root = merge(merge(p.x, new Node(e)), p.y);
        return true;
    }

    @Override
    public boolean remove(Object eo) {
        E e = (E) eo;
        Pair p = split(root, e);
        Pair q = split(p.y, e, true);
        root = merge(p.x, q.y);
        return q.x != null;
    }
}
