package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Collection;

public class Collections {
    public static <A> Collection<?> map(Function1<? super A, ?> f, Iterable<A> a) {
        Collection<Object> b = new ArrayList<>();
        for (A x : a) {
            b.add(f.apply(x));
        }
        return b;
    }

    public static <A> Collection<A> filter(Predicate<? super A> p, Iterable<A> a) {
        Collection<A> b = new ArrayList<A>();
        for (A x : a) {
            if (p.apply(x)) {
                b.add(x);
            }
        }
        return b;
    }

    public static <A> Collection<A> takeWhile(Predicate<? super A> p, Iterable<A> a) {
        Collection<A> b = new ArrayList<A>();
        for (A x : a) {
            if (!p.apply(x)) {
                break;
            }
            b.add(x);
        }
        return b;
    }

    public static <A> Collection<A> takeUnless(Predicate<? super A> p, Iterable<A> a) {
        return takeWhile(p.not(), a);
    }

    public static <A, B, C> C foldl(Function2<? super C, ? super A, C> f, C start, Iterable<A> a) {
        for (A x : a) {
            start = f.apply(start, x);
        }
        return start;
    }

    public static <A, B, C> C foldr(Function2<? super A, ? super C, C> f, C start, Iterable<A> a) {
        ArrayList<A> data = new ArrayList<A>();
        for (A x : a) {
            data.add(x);
        }
        for (int i = data.size() - 1; i >= 0; i--) {
            start = f.apply(data.get(i), start);
        }
        return start;
    }
}
