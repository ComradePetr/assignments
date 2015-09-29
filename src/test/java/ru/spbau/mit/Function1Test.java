package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    @Test
    public void testSimple() {
        Function1<Integer, Integer> plus3 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 3;
            }
        };
        Function1<Integer, Integer> mult5 = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x * 5;
            }
        };
        Function1<Integer, Integer> f = plus3.compose(mult5);

        assertEquals(25, (int)f.apply(2));
    }
}
