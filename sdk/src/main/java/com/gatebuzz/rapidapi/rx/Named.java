package com.gatebuzz.rapidapi.rx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>By default the interface method name is used as the Rapid Api "block".  At the method level,
 * this annotation overrides this default behavior to specify the block and allow freedom of what
 * java method name to use in the interface.</p>
 *
 * <p>Every method parameter <b>must</b> be annotated with a name.  Missing a parameter will result
 * in an <code>IllegalArgumentException</code>.</p>
 *
 * @see java.lang.IllegalArgumentException
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Named {
    String value();
}
