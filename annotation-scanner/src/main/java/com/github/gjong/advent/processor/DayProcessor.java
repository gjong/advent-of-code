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

    private Function<TypeMirror, String> parameterResolver(Day day) {
        return type -> switch (type.toString()) {
            case "com.github.gjong.advent.common.InputLoader" ->
                    "new com.github.gjong.advent.common.InputLoader(%d,%d)".formatted(day.year(), day.day());
            case "com.github.gjong.advent.common.Validator" -> "new com.github.gjong.advent.common.Validator(%d,%d)".formatted(day.year(), day.day());
            default -> "provider.provide(%s.class)".formatted(type);
        };
    }
}
