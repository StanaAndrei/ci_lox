package com.ci.jlox;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Object> {

    private Environment environment = new Environment();


    public void interpret(final List<Stmt> statements) {
        try {
            for (final var statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            LoxErr.runtimeError(error);
        }
    }

    private void execute(final Stmt stmt) {
        stmt.accept(this);
    }

    private String stringify(final Object o) {
        if (o instanceof Double) {
            var txt = o.toString();
            if (txt.endsWith(".0")) {
                txt = txt.substring(0, txt.length() - 2);
            }
            return txt;
        }
        return ((o == null) ? "nil" : o.toString());
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        final Object val = evaluate(expr.value);
        environment.assign(expr.name, val);
        return val;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        final Object left = evaluate(expr.left);
        final Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case GREATER:
                checkNrOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNrOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNrOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNrOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case MINUS:
                checkNrOperands(expr.operator, left, right);
                return (double) left - (double) right;
            case SLASH:
                checkNrOperands(expr.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                checkNrOperands(expr.operator, left, right);
                return (double) left * (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
            default:
                return null; // unreachable
        }
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);
        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else {
            if (!isTruthy(left)) return left;
        }
        return evaluate(expr.right);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case MINUS:
                checkNrOperand(expr.operator, right);
                return  -(double) right;
            case BANG:
                return !isTruthy(right);
        };
        return null; //unreachable
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return environment.get(expr.name);
    }

    private boolean isTruthy(final Object object) {
        if (object instanceof Boolean) {
            return (boolean)object;
        }
        return (object != null);
    }

    private boolean isEqual(final Object a, final Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    private Object evaluate(final Expr expr) {
        return expr.accept(this);
    }

    private void checkNrOperand(final Token operator, final Object operand) {
        if (operand instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNrOperands(Token operator,
                                     Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    @Override
    public Object visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    void executeBlock(final List<Stmt> statements, final Environment environment) {
        final Environment prev = this.environment;
        try {
            this.environment = environment;
            for (final var statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = prev;
        }
    }

    @Override
    public Object visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Object visitIfStmt(Stmt.If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Object visitPrintStmt(Stmt.Print stmt) {
        final Object val = evaluate(stmt.expression);
        IO.println(stringify(val));
        return null;
    }

    @Override
    public Object visitVarStmt(Stmt.Var stmt) {
        Object val = null;
        if (stmt.initializer != null) {
            val = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, val);
        return null;
    }

    @Override
    public Object visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }
}
