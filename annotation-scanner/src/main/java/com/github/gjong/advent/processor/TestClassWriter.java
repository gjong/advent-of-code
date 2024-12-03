package com.github.gjong.advent.processor;

import com.github.gjong.advent.Day;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Consumer;

class TestClassWriter {
    private final TypeDependencyResolver.Definition definition;
    private final Messager messager;
    private final List<String> testCases;

    public TestClassWriter(List<String> testCases, TypeDependencyResolver.Definition definition, Messager messager) {
        this.definition = definition;
        this.messager = messager;
        this.testCases = testCases;
    }

    public void writeTestClass(ProcessingEnvironment environment) {
        var packageName = environment.getElementUtils().getPackageOf(definition.type()).getQualifiedName();
        var className = definition.type().getSimpleName() + "Test";
        var day = definition.type().getAnnotation(Day.class);

        try (var writer = new PrintWriter(environment.getFiler().createSourceFile(packageName + "." + className).openWriter())) {
            writer.println("package " + packageName + ";");
            writer.println();
            writer.println("import org.junit.jupiter.api.BeforeEach;");
            writer.println("import org.junit.jupiter.api.Test;");
            writer.println("import org.junit.jupiter.api.DisplayName;");
            writer.println();
            writer.println("import com.github.gjong.advent.cdi.BeanContext;");
            writer.println("import com.github.gjong.advent.cdi.BeanProvider;");
            writer.println("import com.github.gjong.advent.cdi.CdiBean;");
            writer.println("import com.github.gjong.advent.common.InputLoader;");
            writer.println("import com.github.gjong.advent.common.Validator;");
            writer.println();
            writer.println("import org.slf4j.Logger;");
            writer.println("import org.slf4j.LoggerFactory;");
            writer.println();
            writer.println("import static org.junit.jupiter.api.Assertions.*;");
            writer.println();
            writer.println("@DisplayName(\"Running %d day %02d: %s\")".formatted(day.year(), day.day(), day.name()));
            writer.println("public class " + className + " {");
            writer.println();
            writer.println("    private BeanContext beanContext;");
            writer.println("    private Logger log = LoggerFactory.getLogger(getClass());");
            writer.println();
            writeSetup(writer);
            writer.println();
            for (var testCase : testCases) {
                writeTestCase(environment, testCase, writer);
            }
            writer.println("}");
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Failed to write test class: " + e.getMessage());
        }
    }

    private void writeSetup(PrintWriter writer) {
        writer.println("    @BeforeEach");
        writer.println("    void setUp() {");
        writer.println("        beanContext = new BeanContext();");
        writer.print("        beanContext.register(");
        writeCdiBean(definition.type(), writer, this::createConstructor);
        writer.println(");");
        writer.println("    }");
        writer.println();
    }

    private void writeTestCase(ProcessingEnvironment environment, String testCase, PrintWriter writer) {
        writePartCase(environment, testCase, 1, writer);
        writePartCase(environment, testCase, 2, writer);
    }

    private void writePartCase(ProcessingEnvironment environment, String testCase, int part, PrintWriter writer) {
        var day = definition.type().getAnnotation(Day.class);
        var testCaseResource = "/input/%d/day_%02d_%s.txt".formatted(
                day.year(),
                day.day(),
                testCase);

        writer.println("    @Test");
        writer.println("    @DisplayName(\"Case '%s': Part %d\")".formatted(testCase, part));
        writer.println("    void %sTestCase_part%d() {".formatted(testCase, part));
        writer.print("        beanContext.register(");
        writeCdiBean(
                environment.getElementUtils().getTypeElement("com.github.gjong.advent.common.InputLoader"),
                writer, w -> w.print("new InputLoader(\"%s\")".formatted(testCaseResource)));
        writer.println(");");
        writer.print("        beanContext.register(");
        writeCdiBean(
                environment.getElementUtils().getTypeElement("com.github.gjong.advent.common.Validator"),
                writer, w -> {
                    w.println("new Validator(%d, %d) {".formatted(day.year(), day.day()));
                    w.println("                   protected <T> boolean validate(String key, T answer) {");
                    w.println("                       if (!super.hasAnswer(key + \"_%s\")) { ".formatted(testCase));
                    w.println("                         log.info(\"Test case '%s' part %d has no answer.\");".formatted(testCase, day.day()));
                    w.println("                         return true;");
                    w.println("                       }");
                    w.println("                       assertTrue(");
                    w.println("                               !super.hasAnswer(key + \"_%s\") || super.validate(key + \"_%s\", answer),".formatted(testCase, testCase));
                    w.println("                               \"Validation failed for %s\".formatted(key));");
                    w.println("                       return true;");
                    w.println("                   }");
                    w.print("}");
                });
        writer.println(");");
        writer.println("        var daySolver = beanContext.provide(%s.class);".formatted(definition.type().getSimpleName()));
        writer.println("        daySolver.part%d();".formatted(part));
        writer.println("    }");
        writer.println();
    }

    private void writeCdiBean(TypeElement forElement, PrintWriter writer, Consumer<PrintWriter> constructorWriter) {
        writer.println("new CdiBean<%s>() {".formatted(forElement.getSimpleName()));
        writer.println("            public %s create(BeanProvider provider) {".formatted(forElement.getSimpleName()));
        writer.print("                return ");
        constructorWriter.accept(writer);
        writer.println(";");
        writer.println("            }");
        writer.println();
        writer.println("            public Class<%s> type() {".formatted(forElement.getSimpleName()));
        writer.println("                return %s.class;".formatted(forElement.getSimpleName()));
        writer.println("            }");
        writer.print("        }");
    }

    private void createConstructor(PrintWriter writer) {
        writer.print("new %s(".formatted(definition.type().getSimpleName()));
        for (var it = definition.dependencies().iterator(); it.hasNext(); ) {
            writer.print("provider.provide(%s.class)".formatted(it.next()));
            if (it.hasNext()) {
                writer.print(",");
            }
        }
        writer.print(")");
    }
}
