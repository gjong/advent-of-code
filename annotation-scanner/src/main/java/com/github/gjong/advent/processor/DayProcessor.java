package com.github.gjong.advent.processor;

import com.github.gjong.advent.Day;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.PrintWriter;
import java.util.Set;
import java.util.function.Function;

/**
 * A processor responsible for generating code for Advent of Code solutions marked with the {@link Day} annotation.
 * The DayProcessor is an implementation of the AbstractProcessor class and is capable of processing annotations during compilation.
 * It generates injectable CdiBean versions of the annotated exercise classes based on specified parameters.
 * <p>
 * The primary method {@link #process} handles the processing logic by calling {@link #processBeans} and setting processCompleted to true when completed.
 * The {@link #processBeans} method processes annotated elements, resolves dependencies, and writes generated code and services to specified locations.
 * The {@link #parameterResolver} method determines the parameters needed for bean definitions based on the annotation values.
 * It is recommended to annotate Advent of Code solutions with the {@link Day} annotation for the DayProcessor to properly generate code.
 */
@SupportedAnnotationTypes({"com.github.gjong.advent.Day"}) // 1
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class DayProcessor extends AbstractProcessor {

    private boolean processCompleted;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (processCompleted) {
            return false;
        }

        try {
            processBeans(roundEnv);
            processCompleted = true;
        } catch (Exception e) {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, "Exception occurred %s".formatted(e));
        }
        return false;
    }

    private void processBeans(RoundEnvironment roundEnv) {
        var annotated = roundEnv.getElementsAnnotatedWith(Day.class);
        var types = ElementFilter.typesIn(annotated);
        var resolver = new TypeDependencyResolver();

        var beanDefinitions = types.stream()
                .map(t -> resolver.resolve(t, processingEnv.getMessager()))
                .toList();

        beanDefinitions
                .forEach(type -> new DefinitionWriter(type, parameterResolver(type.type().getAnnotation(Day.class)))
                        .writeSourceFile(processingEnv));

        try (var serviceWriter = new PrintWriter(processingEnv.getFiler()
                .createResource(StandardLocation.CLASS_OUTPUT,
                        "",
                        "META-INF/services/com.github.gjong.advent.cdi.CdiBean")
                .openWriter())) {
            beanDefinitions.forEach(type -> serviceWriter.println(
                    "%s.$%s$Definition".formatted(
                            processingEnv.getElementUtils().getPackageOf(type.type()).getQualifiedName(),
                            type.type().getSimpleName())));
        } catch (Exception e) {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, "Exception occurred %s".formatted(e));
        }
    }

    /**
     * Resolves the parameter to a specific string representation based on the given Day.
     * This map has overrides for specifically the InputLoader and Validator class.
     *
     * @param day the Day object used to determine the parameter's value
     * @return a Function that maps a TypeMirror to a corresponding string representation based on the type
     */
    private Function<TypeMirror, String> parameterResolver(Day day) {
        return type -> switch (type.toString()) {
            case "com.github.gjong.advent.common.InputLoader" ->
                    "new com.github.gjong.advent.common.InputLoader(%d,%d)".formatted(day.year(), day.day());
            case "com.github.gjong.advent.common.Validator" -> "new com.github.gjong.advent.common.Validator(%d,%d)".formatted(day.year(), day.day());
            default -> "provider.provide(%s.class)".formatted(type);
        };
    }
}
