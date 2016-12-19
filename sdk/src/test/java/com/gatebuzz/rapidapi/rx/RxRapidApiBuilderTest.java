package com.gatebuzz.rapidapi.rx;

import com.gatebuzz.rapidapi.rx.internal.CallConfiguration;
import com.gatebuzz.rapidapi.rx.internal.CallConfiguration.Parameter;
import com.gatebuzz.rapidapi.rx.internal.CallHandlerFactory;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.Single;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class RxRapidApiBuilderTest {

    @Mock
    private CallHandlerFactory callHandlerFactory;
    @Captor
    private ArgumentCaptor<Map<String, CallConfiguration>> captor;

    //region Builder methods
    @Test
    public void simpleBuilder() {
        RxRapidApiBuilder builder = new RxRapidApiBuilder();

        RxRapidApiBuilder stepTwo = builder.endpoint(BasicInterface.class);
        assertSame(builder, stepTwo);

        RxRapidApiBuilder stepThree = builder.application("c", "d");
        assertSame(builder, stepThree);

        RxRapidApiBuilder stepFour = builder.defaultValue("e", "f");
        assertSame(builder, stepFour);

        RxRapidApiBuilder stepFive = builder.defaultValue("g", "h", "i");
        assertSame(builder, stepFive);

        RxRapidApiBuilder stepSix = builder.defaultValues(new HashMap<>());
        assertSame(builder, stepSix);

        RxRapidApiBuilder stepSeven = builder.defaultValues("g", new HashMap<>());
        assertSame(builder, stepSeven);

        RxRapidApiBuilder stepEight = builder.server("http://foo/");
        assertSame(builder, stepEight);

        RxRapidApiBuilder stepNine = builder.gson(new Gson());
        assertSame(builder, stepNine);

        RxRapidApiBuilder stepTen = builder.okHttpClient(new OkHttpClient());
        assertSame(builder, stepTen);

        BasicInterface result = builder.build();
        assertNotNull(result);
    }

    @Test
    public void malformedServerUrlIsDetected() {
        try {
            new RxRapidApiBuilder().endpoint(BasicInterface.class).server("foo").build();
            fail("Exception expected for invalid URL");
        } catch (IllegalArgumentException e) {
            assertEquals("Malformed server URL: \"foo\"", e.getMessage());
        }
    }

    @Test
    public void messedUpGsonIsDetected() {
        try {
            new RxRapidApiBuilder().endpoint(BasicInterface.class).gson(null).build();
            fail("Exception expected for invalid Gson");
        } catch (IllegalArgumentException e) {
            assertEquals("Gson parser cannot be null", e.getMessage());
        }
    }

    @Test
    public void messedUpOkHttpClientIsDetected() {
        try {
            new RxRapidApiBuilder().endpoint(BasicInterface.class).okHttpClient(null).build();
            fail("Exception expected for invalid OkHttpClient");
        } catch (IllegalArgumentException e) {
            assertEquals("OkHttpClient cannot be null", e.getMessage());
        }
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

    @Test
    public void singleClassLevelDefaultPassedThrough() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValue("key", "value")
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        Map<String, CallConfiguration> configMap = captor.getValue();
        assertEquals(1, configMap.size());

        CallConfiguration config = configMap.get("someMethod");
        assertEquals("a", config.project);
        assertEquals("b", config.key);
        assertEquals(1, config.classLevelDefaults.size());
        assertEquals("value", config.classLevelDefaults.get("key"));
    }

    @Test
    public void multipleClassLevelDefaults() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValue("key1", "value1")
                .defaultValue("key2", "value2")
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        Map<String, CallConfiguration> configMap = captor.getValue();
        assertEquals(1, configMap.size());

        CallConfiguration config = configMap.get("someMethod");
        assertEquals("a", config.project);
        assertEquals("b", config.key);
        assertEquals(2, config.classLevelDefaults.size());
        assertEquals("value1", config.classLevelDefaults.get("key1"));
        assertEquals("value2", config.classLevelDefaults.get("key2"));
    }

    @Test
    public void multipleClassLevelDefaultsFromMap() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValues(new HashMap<String, String>() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        Map<String, CallConfiguration> configMap = captor.getValue();
        assertEquals(1, configMap.size());

        CallConfiguration config = configMap.get("someMethod");
        assertEquals("a", config.project);
        assertEquals("b", config.key);
        assertEquals(2, config.classLevelDefaults.size());
        assertEquals("value1", config.classLevelDefaults.get("key1"));
        assertEquals("value2", config.classLevelDefaults.get("key2"));
    }

    @Test
    public void multipleClassLevelDefaultsFromMapAndSingleValues() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValues(new HashMap<String, String>() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .defaultValue("key3", "value3")
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        Map<String, CallConfiguration> configMap = captor.getValue();
        assertEquals(1, configMap.size());

        CallConfiguration config = configMap.get("someMethod");
        assertEquals("a", config.project);
        assertEquals("b", config.key);
        assertEquals(3, config.classLevelDefaults.size());
        assertEquals("value1", config.classLevelDefaults.get("key1"));
        assertEquals("value2", config.classLevelDefaults.get("key2"));
        assertEquals("value3", config.classLevelDefaults.get("key3"));
    }

    @Test
    public void singleMethodLevelDefaultValue() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValue("someMethod", "key", "value")
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(1, config.methodLevelDefaults.size());
        assertEquals("value", config.methodLevelDefaults.get("key"));
    }

    @Test
    public void multipleMethodLevelDefaultsFromMap() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValues("someMethod", new HashMap<String, String>() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(2, config.methodLevelDefaults.size());
        assertEquals("value1", config.methodLevelDefaults.get("key1"));
        assertEquals("value2", config.methodLevelDefaults.get("key2"));
    }

    @Test
    public void multipleMethodLevelDefaultsFromMapAndSingleValues() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .defaultValues("someMethod", new HashMap<String, String>() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .defaultValue("someMethod", "key3", "value3")
                .build();

        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(3, config.methodLevelDefaults.size());
        assertEquals("value1", config.methodLevelDefaults.get("key1"));
        assertEquals("value2", config.methodLevelDefaults.get("key2"));
        assertEquals("value3", config.methodLevelDefaults.get("key3"));
    }

    @Test
    public void classLevelDefaultValueNamesPassedDownToConfig() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleClassLevelDefaultBuilderTestClass.class)
                .apiPackage("c")
                .build();
        verify(callHandlerFactory).newInstance(eq(SingleClassLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(Collections.singletonList(new Parameter("key")), config.defaultParameters);
    }

    @Test
    public void multipleClassLevelDefaultValueNamesPassedDownToConfig() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(MultipleClassLevelDefaultBuilderTestClass.class)
                .build();
        verify(callHandlerFactory).newInstance(eq(MultipleClassLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(Arrays.asList(new Parameter("key1"), new Parameter("key2")), config.defaultParameters);
    }

    @Test
    public void methodLevelDefaultValueNamesPassedDownToConfig() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(SingleMethodLevelDefaultBuilderTestClass.class)
                .build();
        verify(callHandlerFactory).newInstance(eq(SingleMethodLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(Collections.singletonList(new Parameter("key")), config.defaultParameters);
    }

    @Test
    public void multipleMethodLevelDefaultValueNamesPassedDownToConfig() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(MultipleMethodLevelDefaultBuilderTestClass.class)
                .build();
        verify(callHandlerFactory).newInstance(eq(MultipleMethodLevelDefaultBuilderTestClass.class), captor.capture());
        CallConfiguration config = captor.getValue().get("someMethod");
        assertEquals(Arrays.asList(new Parameter("key1"), new Parameter("key2")), config.defaultParameters);
    }

    @Test
    public void multipleDefaultValueNamesPassedDownToConfig() {
        new RxRapidApiBuilder(callHandlerFactory)
                .endpoint(MultipleDefaultNamesBuilderTestClass.class)
                .build();
        verify(callHandlerFactory).newInstance(eq(MultipleDefaultNamesBuilderTestClass.class), captor.capture());
        assertEquals(Arrays.asList(new Parameter("key1"), new Parameter("key2"),
                new Parameter("key3"), new Parameter("key4")), captor.getValue().get("someMethod").defaultParameters);
        assertEquals(Arrays.asList(new Parameter("key1"), new Parameter("key2"),
                new Parameter("key5")), captor.getValue().get("someOtherMethod").defaultParameters);
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
        } catch (Exception e) {
            fail("No exception expected");
        }
    }
    //endregion

    //region Builder interfaces
    @Application(project = "a", key = "b")
    @DefaultParameters("key")
    private interface SingleClassLevelDefaultBuilderTestClass {
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "b")
    private interface SingleMethodLevelDefaultBuilderTestClass {
        @ApiPackage("c")
        @DefaultParameters("key")
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "b")
    @DefaultParameters({"key1", "key2"})
    private interface MultipleClassLevelDefaultBuilderTestClass {
        @ApiPackage("c")
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "b")
    private interface MultipleMethodLevelDefaultBuilderTestClass {
        @ApiPackage("c")
        @DefaultParameters({"key1", "key2"})
        Observable<Map<String, Object>> someMethod();
    }

    @Application(project = "a", key = "b")
    @DefaultParameters({"key1", "key2"})
    @ApiPackage("c")
    private interface MultipleDefaultNamesBuilderTestClass {
        @DefaultParameters({"key3", "key4"})
        Observable<Map<String, Object>> someMethod();

        @DefaultParameters("key5")
        Observable<Map<String, Object>> someOtherMethod();
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