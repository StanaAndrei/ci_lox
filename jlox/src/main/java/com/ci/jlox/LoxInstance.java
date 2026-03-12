package com.ci.jlox;

import java.util.HashMap;
import java.util.Map;

public class LoxInstance {
    private final LoxClass klass;
    private final Map<String, Object> fields;

    public LoxInstance(final LoxClass klass) {
        this.klass = klass;
        fields = new HashMap<>();
    }

    Object get(final Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        LoxFunction method = klass.findMethod(name.lexeme);
        if (method != null) {
            return method;
        }
        throw new RuntimeError(name,
                "Undefined property '" + name.lexeme + "'.");
    }

    void set(final Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
