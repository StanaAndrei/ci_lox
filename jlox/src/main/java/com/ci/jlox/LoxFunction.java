package com.ci.jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function decl;
    private final Environment closure;
    public LoxFunction(final Stmt.Function decl, final Environment closure) {
        this.closure = closure;
        this.decl = decl;
    }

    @Override
    public int arity() {
        return decl.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        final var env = new Environment(closure);
        for (int i = 0; i < decl.params.size(); i++) {
            env.define(decl.params.get(i).lexeme, args.get(i));
        }
        try {
            interpreter.executeBlock(decl.body, env);
        } catch (Return retVal) {
            return retVal.value;
        }
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + decl.name.lexeme + '>';
    }
}
