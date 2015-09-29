package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionsTest {
    @Test
    public void testSimple() {
        Function2<Integer, Integer, Integer> minus = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x - y;
            }
        };
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
}
