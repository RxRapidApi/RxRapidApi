package com.gatebuzz.rapidapi.rx;

import org.junit.Test;

import java.util.Map;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ApplicationAnnotationTest {
    @Test
    public void missingApplicationAnnotation() {
        try {
            RxRapidApiBuilder.from(MissingApplicationAnnotation.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Project name not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyProjectInApplicationAnnotation() {
        try {
            RxRapidApiBuilder.from(EmptyProjectInApplicationAnnotation.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Project name not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyKeyInApplicationAnnotation() {
        try {
            RxRapidApiBuilder.from(EmptyKeyInApplicationAnnotation.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API key not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyProjectInMethodApplicationAnnotationDefaultsToClassLevel() {
        try {
            RxRapidApiBuilder.from(OverriddenProjectIsMissing.class);
        } catch (IllegalArgumentException e) {
            fail("No exception expected");
        }
    }

    @Test
    public void emptyKeyInMethodApplicationAnnotationDefaultsToClassLevel() {
        try {
            RxRapidApiBuilder.from(OverriddenKeyIsMissing.class);
        } catch (IllegalArgumentException e) {
            fail("No exception expected");
        }
    }


    private interface MissingApplicationAnnotation {
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "", key = "")
    private interface EmptyProjectInApplicationAnnotation {
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "")
    private interface EmptyKeyInApplicationAnnotation {
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "a")
    private interface OverriddenProjectIsMissing {
        @Application(project = "", key = "a")
        @ApiPackage("c")
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "a")
    private interface OverriddenKeyIsMissing {
        @Application(project = "b", key = "")
        @ApiPackage("c")
        Observable<Map<String, Object>> someMethod();
    }
}
