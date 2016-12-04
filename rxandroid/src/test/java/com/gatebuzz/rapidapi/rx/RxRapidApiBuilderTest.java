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

    //region Builder methods
    @Test
    public void simpleBuilder() {
        RxRapidApiBuilder builder = new RxRapidApiBuilder();

        RxRapidApiBuilder stepTwo = builder.endpoint(BasicInterface.class);
        assertSame(builder, stepTwo);

        RxRapidApiBuilder stepThree = builder.application("c", "d");
        assertSame(builder, stepThree);

        BasicInterface result = stepThree.build();
        assertNotNull(result);
    }

    @Test
    public void applicationMethodOnBuilderProvidesProject() {
        try {
            new RxRapidApiBuilder()
                    .endpoint(MissingApplicationAnnotation.class)
                    .application("a", "b")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception expected");
        }
    }

    @Test
    public void applicationMethodOnBuilderHandlesEmptyProject() {
        try {
            new RxRapidApiBuilder()
                    .endpoint(MissingApplicationAnnotation.class)
                    .application(" ", "b")
                    .build();
        } catch (Exception e) {
            assertEquals("Project name not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void applicationMethodOnBuilderHandlesEmptyKey() {
        try {
            new RxRapidApiBuilder()
                    .endpoint(MissingApplicationAnnotation.class)
                    .application("a", " ")
                    .build();
        } catch (Exception e) {
            assertEquals("API key not found (check the @Application annotation).", e.getMessage());
        }
    }

    @Test
    public void apiPackageMethodOnBuilderProvidesThePackage() {
        try {
            new RxRapidApiBuilder()
                    .endpoint(ApiPackageMissing.class)
                    .apiPackage("a")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            fail("No exception expected");
        }
    }
    //endregion

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
    public void emptyProjectInMethodApplicationAnnotationDefaultsToClassLevel() {
        try {
            new RxRapidApiBuilder().endpoint(OverriddenProjectIsMissingDefaultsToClassLevel.class).build();
        } catch (IllegalArgumentException e) {
            fail("No exception expected");
        }
    }

    @Test
    public void emptyKeyInMethodApplicationAnnotationDefaultsToClassLevelValue() {
        try {
            new RxRapidApiBuilder().endpoint(OverriddenKeyIsMissingDefaultsToClassLevel.class).build();
        } catch (IllegalArgumentException e) {
            fail("No exception expected");
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
        @ApiPackage("c")
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
    private interface OverriddenProjectIsMissingDefaultsToClassLevel {
        @Application(project = "", key = "a")
        @ApiPackage("c")
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "a")
    private interface OverriddenKeyIsMissingDefaultsToClassLevel {
        @Application(project = "b", key = "")
        @ApiPackage("c")
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