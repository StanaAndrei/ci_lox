package com.ci.jlox;

public class AstPrinter implements Expr.Visitor<String> {

    String print(final Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return "";
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return paranthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return "";
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return paranthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        return expr.value == null ? "nil" : expr.value.toString();
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return "";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return paranthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return "";
    }

    private String paranthesize(final String name, final Expr... exprs) {
        final var builder = new StringBuilder();
        builder.append("(").append(name);
        for (final var expr : exprs) {
            final var temp = " " + expr.accept(this);
            builder.append(temp);
        }
        builder.append(")");
        return builder.toString();
    }
}
