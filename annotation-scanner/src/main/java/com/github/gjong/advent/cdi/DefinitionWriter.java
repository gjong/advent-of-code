package com.github.gjong.advent.cdi;

import com.github.gjong.advent.Day;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

class DefinitionWriter {
    private final TypeDependencyResolver.Definition definition;
    private final String definedClassName;
    private final Function<TypeMirror, String> parameterResolver;

    public DefinitionWriter(TypeDependencyResolver.Definition definition) {
        this.definition = definition;
        this.definedClassName = "$%s$Definition".formatted(definition.type().getSimpleName());
        this.parameterResolver = "provider.provide(%s.class)"::formatted;
    }

    public DefinitionWriter(TypeDependencyResolver.Definition definition, Function<TypeMirror, String> parameterResolver) {
        this.definition = definition;
        this.definedClassName = "$%s$Definition".formatted(definition.type().getSimpleName());
        this.parameterResolver = parameterResolver;
    }

    void writeSourceFile(ProcessingEnvironment environment) {
        var packageName = environment.getElementUtils().getPackageOf(definition.type()).getQualifiedName();
        var fileName = packageName + "." + definedClassName;
        var solutionClass = definition.type();
        var bean = definition.getBean();

        try (var writer = new PrintWriter(environment.getFiler().createSourceFile(fileName).openWriter())) {
            writer.println("package %s;".formatted(packageName));
            writer.println();
            writer.println("import com.github.gjong.advent.cdi.BeanProvider;");
            writer.println("import com.github.gjong.advent.cdi.CdiBean;");
            writer.println("import com.github.gjong.advent.cdi.BeanProvider;");
            writer.println();
            writer.println("import %s;".formatted(solutionClass));
            writer.println();
            writer.println("public class %s implements CdiBean<%s> {".formatted(definedClassName, solutionClass.getSimpleName()));
            writer.println();

            writer.println("    private final SingletonProvider<%s> singletonProvider;".formatted(solutionClass.getSimpleName()));

            writer.println();
            writer.println("    public %s() {".formatted(definedClassName));
            if (bean.singleton()) {
                writer.print("        singletonProvider = SingletonProvider.of(");
                createConstructor(writer, solutionClass);
                writer.println(");");
            } else {
                writer.print("        singletonProvider = SingletonProvider.of(() -> ");
                createConstructor(writer, solutionClass);
                writer.println(");");
            }
            writer.println("    }");

            writer.println("    public %s create(BeanProvider provider) {".formatted(solutionClass.getSimpleName()));
            writer.print("        return singletonProvider.provide();");
            writer.println("    }");
            writer.println();
            writer.println("    public Class<%s> type() {".formatted(solutionClass.getSimpleName()));
            writer.println("        return %s.class;".formatted(solutionClass.getSimpleName()));
            writer.println("    }");
            writer.println("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createConstructor(PrintWriter writer, TypeElement solutionClass) {
        writer.print("new %s(".formatted(solutionClass.getSimpleName()));
        for (var it = definition.dependencies().iterator(); it.hasNext(); ) {
            writer.print(parameterResolver.apply(it.next()));
            if (it.hasNext()) {
                writer.print(",");
            }
        }
        writer.println(")");
    }
}
