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

    public <T extends A> Predicate<T> or(final Predicate<T> p) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) || p.apply(x);
            }
        };
    }

    public <T extends A> Predicate<T> and(final Predicate<T> p) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
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
