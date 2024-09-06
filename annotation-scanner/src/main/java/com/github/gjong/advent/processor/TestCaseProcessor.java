package com.github.gjong.advent.processor;

import com.github.gjong.advent.Day;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("com.github.gjong.advent.GeneratedTest")
public class TestCaseProcessor extends AbstractProcessor {

    private boolean processCompleted;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processCompleted) {
            return false;
        }

        var relevantBeans = locateDaySolvers();
        var resolver = new TypeDependencyResolver();
        for (var daySolverElement : relevantBeans) {
            var day = daySolverElement.getAnnotation(Day.class);
            var testCases = resolveTestCases(daySolverElement, day.year(), day.day());

            if (testCases.isEmpty()) {
                continue;
            }

            var definition = resolver.resolve(daySolverElement, processingEnv.getMessager());

            new TestClassWriter(testCases, definition, processingEnv.getMessager())
                    .writeTestClass(processingEnv);
        }

        processCompleted = true;
        return true;
    }

    private List<String> resolveTestCases(TypeElement daySolver, int year, int day) {
        try {
            var daySolverPath = processingEnv.getElementUtils().getFileObjectOf(daySolver).toUri().toString();
            var yearPath = Paths.get(new URI(daySolverPath.substring(0, daySolverPath.indexOf("build/classes/java")) + "src/test/resources/input/" + year));
            if (!Files.exists(yearPath)) {
                return List.of();
            }

            return Files.walk(yearPath)
                    .filter(path -> path.getFileName().toString().startsWith("day_%02d_".formatted(day)))
                    .map(path -> path.getFileName().toString()
                            .replace("day_%02d_".formatted(day), "")
                            .replace(".txt", ""))
                    .toList();
        } catch (IOException | URISyntaxException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to resolve test cases.");
            throw new IllegalStateException(e);
        }
    }

    private List<TypeElement> locateDaySolvers() {
        try (var inputStream = processingEnv.getFiler().getResource(
                StandardLocation.CLASS_PATH,
                "",
                "META-INF/services/com.github.gjong.advent.cdi.CdiBean").openInputStream()) {
            var loaderContents = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return Stream.of(loaderContents.split(System.lineSeparator()))
                    .map(className -> {
                        var daySolverClassName = processingEnv.getElementUtils()
                                .getTypeElement(className)
                                .getInterfaces()
                                .getFirst()
                                .toString()
                                .split("[<>]")[1];

                        var typeElement = processingEnv.getElementUtils()
                                .getTypeElement(daySolverClassName);

                        return typeElement.getAnnotation(Day.class) != null
                                ? typeElement
                                : null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Unable to process bean context.");
            throw new IllegalStateException(e);
        }
    }

}
