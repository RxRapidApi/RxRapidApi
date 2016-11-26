package com.gatebuzz.rapidapi.rx;

import org.junit.Test;

import java.util.Map;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ApiPackageNaming {
    @Test
    public void annotationMissing() {
        try {
            RxRapidApiBuilder.from(ApiPackageMissing.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found on someMethod() (check the @ApiPackage annotation).", e.getMessage());
        }
    }

    @Test
    public void annotationValueEmpty() {
        try {
            RxRapidApiBuilder.from(ApiPackageEmpty.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found on someMethod() (check the @ApiPackage annotation).", e.getMessage());
        }
    }

    @Test
    public void annotationValueAllSpaces() {
        try {
            RxRapidApiBuilder.from(ApiPackageSpaces.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found on someMethod() (check the @ApiPackage annotation).", e.getMessage());
        }
    }

    @Application(project = "a", key = "a")
    private interface ApiPackageMissing {
        Observable<Map<String, Object>> someMethod(@Named("a") String data);
    }

    @Application(project = "a", key = "a")
    private interface ApiPackageEmpty {
        @ApiPackage("")
        Observable<Map<String, Object>> someMethod(@Named("a") String data);
    }

    @Application(project = "a", key = "a")
    private interface ApiPackageSpaces {
        @ApiPackage("  ")
        Observable<Map<String, Object>> someMethod(@Named("a") String data);
    }
}
