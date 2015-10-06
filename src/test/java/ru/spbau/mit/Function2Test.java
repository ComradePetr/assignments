package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Function2Test {
    @Test
    public void testCompose() {
        Function2<Integer, Integer, Integer> prod = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x * y;
            }
        };
        Function1<Integer, Integer> plus5 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 5;
            }
        };
        Function2<Integer, Integer, Integer> f = prod.compose(plus5);

        assertEquals(19, (int) f.apply(2, 7));
    }

    @Test
    public void testCurryBind() {
        Function2<Integer, Integer, Integer> minus = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x - y;
            }
        };
        Function1<Integer, Integer> xMinusFive = minus.bind2(5),
                fiveMinusX = minus.bind1(5);
        Function1<Integer, Function1<Integer, Integer>> curried = minus.curry();

        assertEquals(8, (int) xMinusFive.apply(13));
        assertEquals(-2, (int) fiveMinusX.apply(7));
        assertEquals(30, (int) curried.apply(50).apply(20));
    }
}
