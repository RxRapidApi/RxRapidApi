package com.gatebuzz.rapidapi.rx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify a Rapid API package to call.  Annotated at the class lewel, this
 * value is applied to every method in the service interface.  Method level
 * annotations will override the class level one.  Note: the builder's <code>apiPackage()</code>
 * method will override any annotations on the service interface.
 *
 * @see RxRapidApiBuilder#apiPackage(String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiPackage {
    String value();
}
