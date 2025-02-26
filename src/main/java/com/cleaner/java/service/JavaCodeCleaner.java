package com.cleaner.java.service;

import com.cleaner.java.models.JavaCode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import org.springframework.stereotype.Service;

@Service
public class JavaCodeCleaner {
    public String cleanCode(JavaCode javaCode) {
        CompilationUnit compilationUnit = StaticJavaParser.parse(javaCode.getCode());
        compilationUnit = cleanPackageDeclaration(compilationUnit);
        compilationUnit = cleanImports(compilationUnit);
        compilationUnit = cleanComments(compilationUnit);
        compilationUnit = cleanSystemPrints(compilationUnit);
        compilationUnit = cleanLogs(compilationUnit);
        compilationUnit = cleanToString(compilationUnit);
        return compilationUnit.toString();
    }


    public CompilationUnit cleanPackageDeclaration(CompilationUnit compilationUnit) {
        compilationUnit.removePackageDeclaration();
        return compilationUnit;
    }
    public CompilationUnit cleanImports(CompilationUnit compilationUnit) {
        compilationUnit.findAll(ImportDeclaration.class)
                .forEach(ImportDeclaration::remove);
        return compilationUnit;
    }

    public CompilationUnit cleanComments(CompilationUnit compilationUnit) {
        compilationUnit.findAll(Comment.class)
                .forEach(Comment::remove);
        return compilationUnit;
    }

    // Remove System.out.print() and System.out.println()
    public CompilationUnit cleanSystemPrints(CompilationUnit compilationUnit) {
        compilationUnit.findAll(MethodCallExpr.class).stream()
                .filter(m -> m.getScope().isPresent() &&
                        m.getScope().get().toString().equals("System.out") &&
                        (m.getNameAsString().equals("print") || m.getNameAsString().equals("println")))
                .forEach(methodCall -> {
                    if (methodCall.getParentNode().isPresent() && methodCall.getParentNode().get() instanceof ExpressionStmt) {
                        methodCall.getParentNode().get().remove();
                    }
                });
        return compilationUnit;
    }

    // Remove Logger statements (info, warn, error, debug, trace, etc.)
    public CompilationUnit cleanLogs(CompilationUnit compilationUnit) {
        compilationUnit.findAll(MethodCallExpr.class).stream()
                .filter(m -> m.getScope().isPresent() &&
                        m.getScope().get().toString().matches(".*Logger.*") && // Matches Logger or log variable
                        m.getNameAsString().matches("info|warn|error|debug|trace"))
                .forEach(methodCall -> {
                    if (methodCall.getParentNode().isPresent() && methodCall.getParentNode().get() instanceof ExpressionStmt) {
                        methodCall.getParentNode().get().remove();
                    }
                });
        return compilationUnit;
    }

    // Remove toString() method definitions
    public CompilationUnit cleanToString(CompilationUnit compilationUnit) {
        compilationUnit.findAll(MethodDeclaration.class).stream()
                .filter(method -> method.getNameAsString().equals("toString") &&
                        method.getParameters().isEmpty()) // Ensures it's a no-arg toString method
                .forEach(MethodDeclaration::remove);
        return compilationUnit;
    }
}
