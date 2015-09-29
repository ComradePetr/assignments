package ru.spbau.mit;

public abstract class Function2<A, B, C> {
    public abstract C apply(A x, B y);

    public <D> Function2<A, B, D> compose(final Function1<? super C, D> g) {
        return new Function2<A, B, D>() {
            @Override
            public D apply(A x, B y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    public Function1<B, C> bind1(final A x) {
        return new Function1<B, C>() {
            @Override
            public C apply(B y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    public Function1<A, C> bind2(final B y) {
        return new Function1<A, C>() {
            @Override
            public C apply(A x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    public Function1<A, Function1<B, C>> curry() {
        return new Function1<A, Function1<B, C>>() {
            @Override
            public Function1<B, C> apply(A x) {
                return Function2.this.bind1(x);
            }
        };
    }
}