package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Collection;

public class Collections {
    public static <A, B, T extends Iterable<A> & Collection<A>> Collection<B> map(Function1<? super A, B> f, T a) {
        Collection<B> b = new ArrayList<B>();
        for (A x : a) {
            b.add(f.apply(x));
        }
        return b;
    }

    public static <A, T extends Iterable<A> & Collection<A>> Collection<A> filter(Predicate<? super A> p, T a) {
        Collection<A> b = new ArrayList<A>();
        for (A x : a) {
            if (p.apply(x)) {
                b.add(x);
            }
        }
        return b;
    }

    public static <A, T extends Iterable<A> & Collection<A>> Collection<A> takeWhile(Predicate<? super A> p, T a) {
        Collection<A> b = new ArrayList<A>();
        for (A x : a) {
            if (!p.apply(x)) {
                break;
            }
            b.add(x);
        }
        return b;
    }

    public static <A, T extends Iterable<A> & Collection<A>> Collection<A> takeUnless(Predicate<? super A> p, T a) {
        return takeWhile(p.not(), a);
    }

    public static <A, B, C, T extends Iterable<A> & Collection<A>> C foldl(Function2<? super C, ? super A, C> f, C start, T a) {
        for (A x : a) {
            start = f.apply(start, x);
        }
        return start;
    }

    public static <A, B, C, T extends Iterable<A> & Collection<A>> C foldr(Function2<? super A, ? super C, C> f, C start, T a) {
        Object[] data = a.toArray();
        for (int i = data.length - 1; i >= 0; i--) {
            start = f.apply((A) data[i], start);
        }
        return start;
    }
}
