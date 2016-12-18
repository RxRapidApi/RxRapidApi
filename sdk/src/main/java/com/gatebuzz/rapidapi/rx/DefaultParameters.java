package com.gatebuzz.rapidapi.rx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

/**
 * <p>At the class level, this annotation indicates that every documented service method
 * will pass this list of parameters in addition to those that are part of the method
 * signature.</p>
 *
 * <p>At the method level, this annotation indicates that the method will pass this
 * list of additional parameters in addition to those that are part of the method
 * sigature.</p>
 *
 * <p>Defaults are merged: class level defaults may be overridden at the method level.</p>
 *
 * <p>For example, <code>@DefaultParameters("p1", "p2")</code> at the class level and
 * <code>@DefaultParameters("p2", "p3")</code> at the method level will result in three
 * default parameters <code>p1, p2, p3</code> being passes to the service.</p>
 *
 * <p>Values for the declared defaults are configured through the Builder's
 * <code>defaultValue()</code> / <code>defaultValues()</code> methods.</p>
 *
 * @see RxRapidApiBuilder#defaultValue(String, String)
 * @see RxRapidApiBuilder#defaultValue(String, String, String)
 * @see RxRapidApiBuilder#defaultValues(HashMap)
 * @see RxRapidApiBuilder#defaultValues(String, HashMap)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DefaultParameters {
    String[] value();
}
