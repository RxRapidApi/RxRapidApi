package com.gatebuzz.rapidapi.rx;

import com.gatebuzz.rapidapi.rx.internal.InvocationHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationAnnotation.class,
        ApiPackageNaming.class,
        MethodNaming.class,
        MethodParameterNaming.class,
        InvocationHandlerTest.class
})
public class RxRapidApiBuilderTest {
}