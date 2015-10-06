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

    @Test
    public void testShortCircuit() {
        Predicate<Integer> even = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };
        Predicate<Integer> err = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x){
                assertTrue(false);
                return true;
            }
        };
        assertFalse(even.and(err).apply(45));
        assertFalse(Predicate.ALWAYS_FALSE.and(err).apply(0));
        assertTrue(even.or(err).apply(4));
        assertTrue(Predicate.ALWAYS_TRUE.or(err).apply(0));
    }

    @Test(expected=AssertionError.class)
    public void testShortCircuitAnd() {
        Predicate<Integer> even = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };
        Predicate<Integer> err = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x){
                assertTrue(false);
                return true;
            }
        };
        even.and(err).apply(4);
    }

    @Test(expected=AssertionError.class)
    public void testShortCircuitOr() {
        Predicate<Integer> even = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };
        Predicate<Integer> err = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x){
                assertTrue(false);
                return true;
            }
        };
        even.or(err).apply(45);
    }
}
