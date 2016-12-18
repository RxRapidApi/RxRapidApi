package com.gatebuzz.rapidapi.rx;

import java.lang.annotation.*;

/**
 * Configure the Rapid API application, specifying the <code>project</code> and <code>Api Key</code>.  These
 * values can be overridden with the builder's <code>application()</code> method.
 *
 * @see RxRapidApiBuilder#application(String, String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Application {
    String project();
    String key();
}
