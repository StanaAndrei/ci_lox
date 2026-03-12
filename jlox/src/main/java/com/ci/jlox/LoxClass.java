package com.ci.jlox;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class LoxClass implements LoxCallable {
    final String name;
    final private Map<String, LoxFunction> methods;

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

    LoxFunction findMethod(final String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        return null;
    }
}
