package ru.spbau.mit;

public abstract class Predicate<A> extends Function1<A, Boolean> {
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object x) {
            return true;
        }
    };
    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object x) {
            return false;
        }
    };

    public Predicate<A> or(final Predicate<A> p) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A x) {
                return Predicate.this.apply(x) || p.apply(x);
            }
        };
    }

    public Predicate<A> and(final Predicate<A> p) {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A x) {
                return Predicate.this.apply(x) && p.apply(x);
            }
        };
    }

    public Predicate<A> not() {
        return new Predicate<A>() {
            @Override
            public Boolean apply(A x) {
                return !Predicate.this.apply(x);
            }
        };
    }
}
