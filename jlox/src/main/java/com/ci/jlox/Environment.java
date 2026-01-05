package com.ci.jlox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();

    Object get(final Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    public void define(final String name, final Object object) {
        values.put(name, object);
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
}
