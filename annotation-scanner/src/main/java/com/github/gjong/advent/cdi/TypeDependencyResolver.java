package com.github.gjong.advent.cdi;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.List;

import static javax.tools.Diagnostic.Kind.ERROR;

public class TypeDependencyResolver {
    private static final IllegalStateException RESOLVE_FAILED = new IllegalStateException("Failed to resolve dependency.");

    public record Definition(TypeElement type, List<TypeMirror> dependencies) {

        public Bean getBean() {
            if (!type.getAnnotationMirrors().isEmpty()) {
                return type.getAnnotationMirrors().getFirst().getAnnotationType().asElement().getAnnotation(Bean.class);
            }
            return type.getAnnotation(Bean.class);
        }
    }

    public Definition resolve(TypeElement typeElement, Messager messager) {
        if (typeElement.getKind().isClass() && !typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            return resolveClass(typeElement, messager);
        }

        messager.printMessage(ERROR, "Class %s must not be abstract".formatted(typeElement));
        throw RESOLVE_FAILED;
    }

    private Definition resolveClass(TypeElement typeElement, Messager messager) {
        var constructors = ElementFilter.constructorsIn(typeElement.getEnclosedElements());
        if (constructors.size() != 1) {
            messager.printMessage(ERROR, "Class %s must have exactly one constructor".formatted(typeElement));
            throw RESOLVE_FAILED;
        }

        var dependencies = constructors.getFirst()
                .getParameters()
                .stream()
                .map(VariableElement::asType)
                .toList();

        return new Definition(typeElement, dependencies);
    }
}
