package com.github.gjong.advent.cdi;

import java.util.function.Supplier;

public interface CdiBean<T> {

    interface SingletonProvider<T> {
        T provide();

        static <T> SingletonProvider<T> of(T instance) {
            return () -> instance;
        }

        static <T> SingletonProvider<T> of(Supplier<T> supplier) {
            return supplier::get;
        }
    }

    T create(BeanProvider provider);

    Class<T> type();
}
