package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {
    @Test
    public void testOrAnd() {
        Predicate<Integer> even = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };
        Predicate<Integer> mod3Is1 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 3 == 1;
            }
        };

        assertTrue(mod3Is1.and(even).apply(4));
        assertFalse(mod3Is1.and(even).apply(6));
        assertFalse(mod3Is1.and(even).apply(7));
        assertFalse(mod3Is1.and(even).apply(9));
        assertTrue(mod3Is1.or(even).apply(4));
        assertTrue(mod3Is1.or(even).apply(6));
        assertTrue(mod3Is1.or(even).apply(7));
        assertFalse(mod3Is1.or(even).apply(9));
    }
}
