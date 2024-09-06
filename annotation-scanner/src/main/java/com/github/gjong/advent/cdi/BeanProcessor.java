package com.github.gjong.advent.cdi;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("com.github.gjong.advent.cdi.Bean") // 1
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class BeanProcessor extends AbstractProcessor {

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
        var types = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(Bean.class));
        var resolver = new TypeDependencyResolver();

        var beanDefinitions = types.stream()
                .map(t -> resolver.resolve(t, processingEnv.getMessager()))
                .toList();

        beanDefinitions
                .forEach(type -> new DefinitionWriter(type)
                        .writeSourceFile(processingEnv));
    }
}
