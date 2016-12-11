package com.gatebuzz.rapidapi.rx;

import org.junit.Test;
import rx.Observable;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ApiPackageNamingTest {
    @Test
    public void annotationMissing() {
        try {
            RxRapidApiBuilder.from(ApiPackageMissing.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found - check the @ApiPackage annotation on the interface or on someMethod().", e.getMessage());
        }
    }

    @Test
    public void annotationValueEmpty() {
        try {
            RxRapidApiBuilder.from(ApiPackageEmpty.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found - check the @ApiPackage annotation on the interface or on someMethod().", e.getMessage());
        }
    }

    @Test
    public void annotationValueAllSpaces() {
        try {
            RxRapidApiBuilder.from(ApiPackageSpaces.class);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found - check the @ApiPackage annotation on the interface or on someMethod().", e.getMessage());
        }
    }

    @Test
    public void annotationOnClass() {
        try {
            RxRapidApiBuilder.from(ApiPackageOnClass.class);
        } catch (Exception e) {
            fail("No exception expected");
        }
    }

    @Application(project = "a", key = "a")
    @ApiPackage("c")
    private interface ApiPackageOnClass {
        Observable<Map<String, Object>> someMethod(@Named("a") String data);
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
