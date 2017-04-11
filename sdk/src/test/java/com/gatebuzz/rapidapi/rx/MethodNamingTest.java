package com.gatebuzz.rapidapi.rx;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MethodNamingTest {
    @Test
    public void overriddenMethodNameMissing() {
        try {
            RxRapidApiBuilder.from(MethodNameMissing.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API method name not found on someMethod() (check the @Named annotation on your interface method).", e.getMessage());
        }
    }

    @Application(project = "a", key = "a")
    @ApiPackage("c")
    private interface MethodNameMissing {
        @Named("")
        Observable<Map<String, Object>> someMethod();
    }
}
