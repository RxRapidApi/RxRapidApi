package com.gatebuzz.rapidapi.rx;

import org.junit.Test;

import java.util.Map;

import rx.Observable;
import rx.Single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@SuppressWarnings("unused")
public class RxRapidApiBuilderTest {
    @Test
    public void simpleBuilder() {
        RxRapidApiBuilder builder = new RxRapidApiBuilder();

        RxRapidApiBuilder stepTwo = builder.endpoint(BasicInterface.class);
        assertSame(builder, stepTwo);

        BasicInterface result = stepTwo.build();
        assertNotNull(result);
    }

    //region @Application annotation
    @Test
    public void missingApplicationAnnotation() {
        try {
            new RxRapidApiBuilder().endpoint(MissingApplicationAnnotation.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Project name not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyProjectInApplicationAnnotation() {
        try {
            new RxRapidApiBuilder().endpoint(EmptyProjectInApplicationAnnotation.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Project name not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyKeyInApplicationAnnotation() {
        try {
            new RxRapidApiBuilder().endpoint(EmptyKeyInApplicationAnnotation.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API key not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyProjectInMethodApplicationAnnotation() {
        try {
            new RxRapidApiBuilder().endpoint(OverriddenProjectIsMissing.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Project name not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void emptyKeyInMethodApplicationAnnotation() {
        try {
            new RxRapidApiBuilder().endpoint(OverriddenKeyIsMissing.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API key not found (check the @Application annotation).", e.getMessage());
        }
    }
    //endregion

    //region @ApiPackage annotation
    @Test
    public void annotationMissing() {
        try {
            new RxRapidApiBuilder().endpoint(ApiPackageMissing.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found - check the @ApiPackage annotation on the interface or on someMethod().", e.getMessage());
        }
    }

    @Test
    public void annotationValueEmpty() {
        try {
            new RxRapidApiBuilder().endpoint(ApiPackageEmpty.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found - check the @ApiPackage annotation on the interface or on someMethod().", e.getMessage());
        }
    }

    @Test
    public void annotationValueAllSpaces() {
        try {
            new RxRapidApiBuilder().endpoint(ApiPackageSpaces.class).build();
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("API package not found - check the @ApiPackage annotation on the interface or on someMethod().", e.getMessage());
        }
    }

    @Test
    public void annotationOnClass() {
        try {
            new RxRapidApiBuilder().endpoint(ApiPackageOnClass.class).build();
        } catch(Exception e) {
            fail("No exception expected");
        }
    }
    //endregion

    //region @Application annotation interfaces
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
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "a")
    private interface OverriddenKeyIsMissing {
        @Application(project = "b", key = "")
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "b")
    private interface BasicInterface {
        @ApiPackage("c")
        Single<Map<String, Object>> someMethod(@Named("d") String data);
    }
    //endregion

    //region @ApiPackage annotation interfaces
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
    //endregion
}