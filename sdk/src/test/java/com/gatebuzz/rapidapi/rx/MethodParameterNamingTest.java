package com.gatebuzz.rapidapi.rx;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MethodParameterNamingTest {
    @Test
    public void methodParameterNameMissing() {
        try {
            RxRapidApiBuilder.from(MethodParameterNameMissing.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Incorrect number of @Named model on someMethod() - expecting 1, found 0.", e.getMessage());
        }
    }

    @Application(project = "a", key = "a")
    @ApiPackage("c")
    private interface MethodParameterNameMissing {
        Observable<Map<String, Object>> someMethod(String missingAnnotation);
    }
}
