package com.ci.jlox;

public class LoxInstance {
    private final LoxClass klass;

    public LoxInstance(final LoxClass klass) {
        this.klass = klass;
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
