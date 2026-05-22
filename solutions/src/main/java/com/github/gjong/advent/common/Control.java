package com.github.gjong.advent.common;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public sealed interface Control<T> {

    static <T> Control<T> of(T value) {
        if (value == null) {
            return new None<>();
        }
        return new Some<>(value);
    }

    static <T> Control<T> empty() {
        return new None<>();
    }

    record Some<T> (T value) implements Control<T> {}

    record None<T>() implements Control<T> {}

    default T get() {
        return switch (this) {
            case Some(T actual) -> actual;
            case None() -> throw new NoSuchElementException("No value present");
        };
    }

    default T orElse(T other) {
        return switch (this) {
            case Some(T actual) -> actual;
            case None() -> other;
        };
    }

    default Control<T> filter(Predicate<T> predicate) {
        return switch (this) {
            case Some(T actual) when (predicate.test(actual)) -> this;
            case Some(T _) -> new None<>();
            case None() -> this;
        };
    }

    default <U> Control<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Some(T actual) -> new Some<>(mapper.apply(actual));
            case None() -> new None<>();
        };
    }
}
