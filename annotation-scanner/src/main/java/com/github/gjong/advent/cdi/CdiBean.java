package com.github.gjong.advent.cdi;

import java.util.function.Supplier;

/**
 * Represents a CDI (Contexts and Dependency Injection) bean.
 * Allows for the creation of instances of a specified type and provides access to the type class.
 *
 * @param <T> the type of the bean
 */
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

    /**
     * Creates an instance of a specified type, using the provided BeanProvider for any required constructor arguments.
     *
     * @param provider the BeanProvider used to create the instance
     * @return an instance of the specified type
     */
    T create(BeanProvider provider);

    /**
     * Retrieves the class object representing the type parameter of this CDI bean.
     *
     * @return the class object representing the type parameter of this CDI bean
     */
    Class<T> type();
}
