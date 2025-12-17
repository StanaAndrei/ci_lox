package com.ci.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    private static void defineType(
            final PrintWriter writer, final String baseName,
            final String className, final String fieldList
    ) {
        writer.println("  public static class " + className + " extends " + baseName + " {");

        // Constructor
        writer.println("    " + className + "(" + fieldList + ") {");

        final String[] fields = fieldList.split(", ");
        for (final var field : fields) {
            final var name = field.split(" ")[1];
            // FIX 1: Assign 'name', not the whole 'field' string
            writer.println("      this." + name + " = " + name + ";");
        }
        writer.println("    }");

        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");

        // Fields
        writer.println();
        for (final var field : fields) {
            // FIX 2: Generate a field declaration, not a method stub
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }

    private static void defineVisitor(
            final PrintWriter writer, final String baseName,
            final List<String> types
    ) {
        writer.println("  interface Visitor<R> {");
        for (final var type : types) {
            final var typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println("  }");
    }

    private static void defineAst(
            final String outDir, final String baseName,
            final List<String> types
    ) throws IOException {
        final String path = outDir + '/' + baseName + ".java";
        var writer = new PrintWriter(path, StandardCharsets.UTF_8);

        // FIX 3: Added semicolons and fixed 'List' capitalization
        writer.println("package com.ci.jlox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        for (final var type : types) {
            final String className = type.split(":")[0].trim();
            final String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // The base accept() method.
        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <out_dir>");
            System.exit(64);
        }
        final String outDir = args[0];
        System.out.println("Generating " + outDir + " ...");
        defineAst(outDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }
}