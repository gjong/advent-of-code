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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("com.github.gjong.advent.GeneratedTest")
public class TestGenerator extends AbstractProcessor {

    private boolean processCompleted;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processCompleted) {
            return false;
        }

        locateDaySolvers()
                .forEach(this::createTestsForYear);

        processCompleted = true;
        return true;
    }

    private void createTestsForYear(int year, List<TypeElement> daySolvers) {
        try {
            var sourceFile = processingEnv.getFiler()
                    .createSourceFile("com.github.gjong.advent.AdventOfCode%dTest".formatted(year));
            try (var writer = new PrintWriter(sourceFile.openWriter())) {
                writer.println("package com.github.gjong.advent;");
                writer.println();
                writer.println("import com.github.gjong.advent.common.InputLoader;");
                writer.println("import com.github.gjong.advent.common.Validator;");
                writer.println();
                writer.println("import org.junit.jupiter.api.DisplayName;");
                writer.println("import org.junit.jupiter.api.Test;");
                writer.println("import org.junit.jupiter.api.Assertions;");
                writer.println();
                writer.println("import java.util.stream.Stream;");
                writer.println();
                writer.println("@DisplayName(\"Advent Of Code: Year %d\")".formatted(year));
                writer.println("public class AdventOfCode%dTest {".formatted(year));
                writer.println();

                writer.println("   private void injectInput(DaySolver daySolver, String inputPath, String sample) throws Exception {");
                writer.println("       var day = daySolver.getClass().getAnnotation(Day.class);");
                writer.println("       for (var field : daySolver.getClass().getDeclaredFields()) {");
                writer.println("           if (field.getType() == InputLoader.class) {");
                writer.println("               field.setAccessible(true);");
                writer.println("               field.set(daySolver, new InputLoader(inputPath));");
                writer.println("           }");
                writer.println("           if (field.getType() == Validator.class) {");
                writer.println("               field.setAccessible(true);");
                writer.println("               field.set(daySolver, new Validator(day.year(), day.day()) {");
                writer.println("                   @Override");
                writer.println("                   protected <T> boolean validate(String key, T answer) {");
                writer.println("                       Assertions.assertTrue(");
                writer.println("                               super.validate(key + \"_\" + sample, answer),");
                writer.println("                               \"Validation failed for %s\".formatted(key));");
                writer.println("                       return true;");
                writer.println("                   }");
                writer.println("               });");
                writer.println("           }");
                writer.println("       }");
                writer.println("   }");

                for (var daySolver : daySolvers) {
                    createTestsForDaySolver(writer, daySolver);
                }

                writer.println("}");
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTestsForDaySolver(PrintWriter writer, TypeElement daySolver) throws IOException, URISyntaxException {
        var dayAnnotation = daySolver.getAnnotation(Day.class);
        var daySolverPath = processingEnv.getElementUtils().getFileObjectOf(daySolver).toUri().toString();
        var yearPath = Paths.get(new URI(daySolverPath.substring(0, daySolverPath.indexOf("build/classes/java")) + "src/test/resources/input/" + dayAnnotation.year()));
        if (!Files.exists(yearPath)) {
            return;
        }

        var day = dayAnnotation.day();
        var testCases = Files.walk(yearPath)
                .filter(path -> path.getFileName().toString().startsWith("day_%02d_".formatted(day)))
                .map(path -> path.getFileName().toString()
                        .replace("day_%02d_".formatted(day), "")
                        .replace(".txt", ""))
                .toList();

        for (var testCase : testCases) {
            var testCaseResource = "/input/%d/day_%02d_%s.txt".formatted(
                    dayAnnotation.year(),
                    day,
                    testCase);

            writer.println("   @Test");
            writer.println("   @DisplayName(\"Day %02d: %s - [Case '%s']\")".formatted(day, dayAnnotation.name(), testCase));
            writer.println("   public void day%d_%s() throws Exception {".formatted(day, testCase));
            writer.println("       // given");
            writer.println("       var daySolver = new %s();".formatted(daySolver.getQualifiedName()));
            writer.println("       injectInput(daySolver, \"%s\", \"%s\");".formatted(testCaseResource, testCase));
            writer.println("       // when");
            writer.println("       daySolver.part1();");
            writer.println("       daySolver.part2();");
            writer.println("   }");
        }
    }

    private Map<Integer, List<TypeElement>> locateDaySolvers() {
        try (var inputStream = processingEnv.getFiler().getResource(
                StandardLocation.CLASS_PATH,
                "",
                "META-INF/services/com.github.gjong.advent.DaySolver").openInputStream()) {
            var loaderContents = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return Stream.of(loaderContents.split(System.lineSeparator()))
                    .map(className -> processingEnv.getElementUtils().getTypeElement(className))
                    .collect(Collectors.groupingBy(
                            element -> element.getAnnotation(Day.class).year(),
                            Collectors.toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
