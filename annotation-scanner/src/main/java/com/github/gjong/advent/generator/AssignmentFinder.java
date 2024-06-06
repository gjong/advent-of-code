package com.github.gjong.advent.generator;

import com.github.gjong.advent.Day;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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

        processingEnv.getMessager()
                .printMessage(Diagnostic.Kind.OTHER, "Searching for assignments in the codebase.");

        var solutionInstructions = roundEnv.getElementsAnnotatedWith(Day.class)
                .stream()
                .map(this::convertToInstruction)
                .toList();

        createAssignmentClass(solutionInstructions);

        processCompleted = true;
        return true;
    }

    private String convertToInstruction(Element element) {
        var assignmentPackage = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName();
        var className = element.getSimpleName().toString();
        return "new %s.%s()".formatted(assignmentPackage, className);
    }

    private void createAssignmentClass(List<String> solutionInstructions) {
        try (var writer = new PrintWriter(processingEnv.getFiler()
                .createSourceFile("com.github.gjong.advent.ResolvedAssignment")
                .openWriter())) {

            writer.println("package com.github.gjong.advent;");
            writer.println("import com.github.gjong.advent.DaySolver;");
            writer.println();
            writer.println("public final class ResolvedAssignment {");

            writer.println("    public static void initialize() {");
            writer.println("        // Add the assignments here.");
            for (var instruction : solutionInstructions) {
                writer.println("        DaySolver.KNOWN_DAYS.add(%s);".formatted(instruction));
            }
            writer.println("    }");
            writer.println("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
