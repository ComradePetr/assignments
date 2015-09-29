package ru.spbau.mit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Пётр on 22.09.2015.
 */


public class StringSetImpl implements StringSet, StreamSerializable {
    public static class Node {
        private static final int MAX_CHAR_CODE = 128;
        private static final byte BYTE_GO_UP = -1;
        public boolean isTerminal = false;
        public int sum = 0;
        private Node[] next = new Node[MAX_CHAR_CODE];

        public Node nextForced(char c) {
            if (next[c] == null) {
                next[c] = new Node();
            }
            return next[c];
        }

        public Node next(char c) {
            return next[c];
        }

        public void serialize(OutputStream out) {
            try {
                out.write(isTerminal ? 1 : 0);
                for (int i = 0; i < MAX_CHAR_CODE; i++) {
                    if (next[i] != null) {
                        out.write(i);
                        next[i].serialize(out);
                        out.write(BYTE_GO_UP);
                    }
                }
            } catch (IOException e) {
                throw new SerializationException("Can't write to OutputStream");
            }
        }

        public int deserialize(InputStream in) {
            try {
                sum = in.read();
                isTerminal = sum == 1;
                for (int i = 0; i < MAX_CHAR_CODE; i++) {
                    next[i] = null;
                }
                byte c = (byte) in.read();
                while (c != BYTE_GO_UP) {
                    next[c] = new Node();
                    sum += next[c].deserialize(in);
                    c = (byte) in.read();
                }
                return sum;
            } catch (IOException e) {
                throw new SerializationException("Can't read from InputStream");
            }
        }
    }

    private final Node root = new Node();

    private Node find(String element) {
        Node cur = root;
        for (int i = 0; cur != null && i < element.length(); i++) {
            cur = cur.next(element.charAt(i));
        }
        return cur;
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @param element
     */
    public boolean contains(String element) {
        Node cur = find(element);
        return cur != null && cur.isTerminal;
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @param element
     * @return <tt>true</tt> if this set did not already contain the specified
     * element
     */
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node cur = root;
        for (int i = 0; i < element.length(); i++) {
            ++cur.sum;
            cur = cur.nextForced(element.charAt(i));
        }
        ++cur.sum;
        cur.isTerminal = true;
        return true;
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @param element
     * @return <tt>true</tt> if this set contained the specified element
     */
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        Node cur = root;
        for (int i = 0; cur != null && i < element.length(); i++) {
            --cur.sum;
            cur = cur.next(element.charAt(i));
        }
        --cur.sum;
        cur.isTerminal = false;
        return true;
    }

    /**
     * Expected complexity: O(1)
     */
    public int size() {
        return root.sum;
    }

    /**
     * Expected complexity: O(|prefix|)
     *
     * @param prefix
     */
    public int howManyStartsWithPrefix(String prefix) {
        Node cur = find(prefix);
        return cur == null ? 0 : cur.sum;
    }

    /**
     * @param out
     * @throws SerializationException in case of IOException during serialization
     */
    public void serialize(OutputStream out) {
        root.serialize(out);
    }

    /**
     * Replace current state with data from input stream containing serialized data
     *
     * @param in
     * @throws SerializationException in case of IOException during deserialization
     */
    public void deserialize(InputStream in) {
        root.deserialize(in);
    }
}
