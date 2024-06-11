package com.github.gjong.advent.generator;

import com.github.gjong.advent.Day;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("com.github.gjong.advent.Day")
public class AssignmentFinder extends AbstractProcessor {

    private boolean processCompleted;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processCompleted) {
            return false;
        }

        var packages = new HashMap<String, Set<Integer>>();

        try (var serviceWriter = new PrintWriter(processingEnv.getFiler()
                .createResource(StandardLocation.CLASS_OUTPUT,
                        "",
                        "META-INF/services/com.github.gjong.advent.DaySolver")
                .openWriter())) {

            for (var element : roundEnv.getElementsAnnotatedWith(Day.class)) {
                if (element.getSimpleName().contentEquals("DayTemplate")) {
                    continue;
                }

                var assignmentPackage = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName();
                var className = element.getSimpleName().toString();

                packages.putIfAbsent(assignmentPackage.toString(), new HashSet<>());
                packages.get(assignmentPackage.toString())
                        .add(element.getAnnotation(Day.class).day());

                serviceWriter.println("%s.%s".formatted(assignmentPackage, className));
            }

            packages.forEach(this::createInputLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        processCompleted = true;
        return true;
    }

    private void createInputLoader(String packageName, Set<Integer> days) {
        var year = packageName.substring(packageName.lastIndexOf('.') + 2);
        try (var inputLoaderWriter = new PrintWriter(processingEnv.getFiler()
                .createSourceFile("%s.DayLoader".formatted(packageName))
                .openWriter())) {

            inputLoaderWriter.println("package %s;".formatted(packageName));
            inputLoaderWriter.println();
            inputLoaderWriter.println("import com.github.gjong.advent.common.InputLoader;");
            inputLoaderWriter.println("import com.github.gjong.advent.common.Validator;");
            inputLoaderWriter.println("import javax.annotation.processing.Generated;");
            inputLoaderWriter.println();
            inputLoaderWriter.println("@Generated(\"%s\")".formatted(getClass().getCanonicalName()));
            inputLoaderWriter.println("class DayLoader {");
            inputLoaderWriter.println();
            for (var day : days) {
                inputLoaderWriter.println("    public static InputLoader inputDay%d() {".formatted(day));
                inputLoaderWriter.println("        return new InputLoader(%s, %d);".formatted(year, day));
                inputLoaderWriter.println("    }");
                inputLoaderWriter.println();
                inputLoaderWriter.println("    public static Validator validatorDay%d() {".formatted(day));
                inputLoaderWriter.println("        return new Validator(%s, %d);".formatted(year, day));
                inputLoaderWriter.println("    }");
                inputLoaderWriter.println();
            }
            inputLoaderWriter.println("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
