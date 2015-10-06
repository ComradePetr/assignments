package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CollectionsTest {
    @Test
    public void testMap() {
        Function1<Integer, Integer> plus3 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 3;
            }
        };
        List<Integer> a = Arrays.asList(1, 2, 3, 5, 7);
        List<Integer> b = Arrays.asList(4, 5, 6, 8, 10);

        assertTrue(b.equals(Collections.map(plus3, a)));
    }

    @Test
    public void testSuper() {
        class A {
        }
        class B extends A {
        }

        Function1<A, Integer> const3 = new Function1<A, Integer>() {
            @Override
            public Integer apply(A x) {
                return 3;
            }
        };
        List<B> a = Arrays.asList(new B(), new B());
        List<Integer> b = Arrays.asList(3, 3);
        assertTrue(b.equals(Collections.map(const3, a)));
    }

    @Test
    public void testFilter() {
        Predicate<Integer> even = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };
        List<Integer> a = Arrays.asList(10, 1, 2, 3, 5, 8, 7);
        List<Integer> b = Arrays.asList(10, 2, 8);

        assertTrue(b.equals(Collections.filter(even, a)));
    }

    @Test
    public void testTakeUnless() {
        Predicate<Integer> beetween5And7 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x >= 5 && x <= 7;
            }
        };
        List<Integer> a = Arrays.asList(10, 1, 2, 3, 5, 8, 7);
        List<Integer> b = Arrays.asList(10, 1, 2, 3);

        assertTrue(b.equals(Collections.takeUnless(beetween5And7, a)));
    }

    @Test
    public void testTakeWhile() {
        Predicate<Integer> lessThan4 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x < 4;
            }
        };
        List<Integer> a = Arrays.asList(1, 2, 3, 5, 8, 7);
        List<Integer> b = Arrays.asList(1, 2, 3);

        assertTrue(b.equals(Collections.takeWhile(lessThan4, a)));
        assertTrue(b.equals(Collections.takeWhile(lessThan4, b)));
    }

    @Test
    public void testFoldlFoldr() {
        Function2<ArrayList<Integer>, Integer, ArrayList<Integer>> appendL = new Function2<ArrayList<Integer>, Integer, ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> apply(ArrayList<Integer> x, Integer y) {
                ArrayList<Integer> x2 = (ArrayList<Integer>) x.clone();
                x2.add(y);
                return x2;
            }
        };
        Function2<Integer, ArrayList<Integer>, ArrayList<Integer>> appendR = new Function2<Integer, ArrayList<Integer>, ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> apply(Integer y, ArrayList<Integer> x) {
                ArrayList<Integer> x2 = (ArrayList<Integer>) x.clone();
                x2.add(y);
                return x2;
            }
        };
        ArrayList<Integer> a = new ArrayList<Integer>(), b = new ArrayList<Integer>();
        a.add(3);
        a.add(5);
        b.add(5);
        b.add(3);

        assertTrue(a.equals(Collections.foldl(appendL, new ArrayList<Integer>(), a)));
        assertTrue(b.equals(Collections.foldr(appendR, new ArrayList<Integer>(), a)));
    }
}
