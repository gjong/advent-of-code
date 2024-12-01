package com.github.gjong.advent.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

/**
 * The DefinitionWriter class is responsible for generating source code for a CDI bean based on a given TypeDependencyResolver definition.
 * It provides methods to write the source file for the generated bean class.
 */
class DefinitionWriter {
    private final TypeDependencyResolver.Definition definition;
    private final String definedClassName;
    private final Function<TypeMirror, String> parameterResolver;

    public DefinitionWriter(TypeDependencyResolver.Definition definition) {
        this.definition = definition;
        this.definedClassName = "$%s$Definition".formatted(definition.type().getSimpleName());
        this.parameterResolver = "provider.provide(%s.class)"::formatted;
    }

    /**
     * Constructor for DefinitionWriter class.
     *
     * @param definition the TypeDependencyResolver.Definition object containing type and dependencies
     * @param parameterResolver a Function to resolve a TypeMirror to a corresponding string representation based on type
     */
    public DefinitionWriter(TypeDependencyResolver.Definition definition, Function<TypeMirror, String> parameterResolver) {
        this.definition = definition;
        this.definedClassName = "$%s$Definition".formatted(definition.type().getSimpleName());
        this.parameterResolver = parameterResolver;
    }

    /**
     * Writes a source file based on the provided ProcessingEnvironment.
     *
     * @param environment the ProcessingEnvironment object to use for creating the source file
     */
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
