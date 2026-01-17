package com.ci.jlox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function decl;
    public LoxFunction(final Stmt.Function decl) {
        this.decl = decl;
    }

    @Override
    public int arity() {
        return decl.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        final Environment env = new Environment(interpreter.globals);
        for (int i = 0; i < decl.params.size(); i++) {
            env.define(decl.params.get(i).lexeme, args.get(i));
        }
        interpreter.executeBlock(decl.body, env);
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + decl.name.lexeme + '>';
    }
}
