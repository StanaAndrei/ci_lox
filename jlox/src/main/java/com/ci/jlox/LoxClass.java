package com.ci.jlox;

import java.util.List;

public class LoxClass implements LoxCallable {
    final String name;

    LoxClass(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        final var instance = new LoxInstance(this);
        return instance;
    }
}
