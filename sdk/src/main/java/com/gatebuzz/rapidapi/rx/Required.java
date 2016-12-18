package com.gatebuzz.rapidapi.rx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter as <b>required</b> - at runtime the service call will fail
 * with an <code>IllegalArgumentException</code> if this parameter is <code>null</code>.
 *
 * @see java.lang.IllegalArgumentException
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Required {
}
