package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.internal.CallConfiguration.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "Duplicates"})
@RunWith(MockitoJUnitRunner.class)
public class CallHandlerTest {

    @Mock
    private Engine engine;
    @Mock
    private CallHandler.EngineYard engineYard;
    @Captor
    private ArgumentCaptor<Map<String, Pair<String, String>>> args;

    private List<Parameter> params;
    private List<Parameter> defaultParameters;
    private Map<String, String> defaultValues;

    @Before
    public void setUp() throws Exception {
        params = Arrays.asList(new Parameter("first"), new Parameter("second"));
        defaultParameters = Arrays.asList(new Parameter("key1"), new Parameter("key2"));
        defaultValues = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }};
    }

    @Test
    public void engineConfiguredAsExpected() throws Throwable {
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", params, emptyMap(), emptyMap(), emptyList());
        String expectedFirstParam = "d";
        String expectedSecondParam = "e";

        when(engineYard.newInstance(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{expectedFirstParam, expectedSecondParam});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").first);
        assertEquals(expectedFirstParam, args.getValue().get("first").second);
        assertEquals("data", args.getValue().get("second").first);
        assertEquals(expectedSecondParam, args.getValue().get("second").second);
    }

    @Test
    public void optionalNullValuesAreSkipped() throws Throwable {
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", params, emptyMap(), emptyMap(), emptyList());

        when(engineYard.newInstance(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{null, "present"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertFalse(args.getValue().containsKey("first"));
        assertEquals("data", args.getValue().get("second").first);
        assertEquals("present", args.getValue().get("second").second);
    }

    @Test
    public void requiredNullValuesThrowException() throws Throwable {
        params.get(0).required = true;
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", params, emptyMap(), emptyMap(), emptyList());

        when(engineYard.newInstance(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        try {
            handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                    new Object[]{null, "present"});
            fail("Required (but null) parameter should throw exception");
        } catch (IllegalArgumentException iar) {
            assertEquals("Calling \"someMethod\" - required parameter \"first\" is null.", iar.getMessage());
        }
    }

    @Test
    public void defaultParametersConfiguredFromClassLevelValues() throws Throwable {
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", params, defaultValues, emptyMap(), defaultParameters);

        when(engineYard.newInstance(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").first);
        assertEquals("a", args.getValue().get("first").second);
        assertEquals("data", args.getValue().get("second").first);
        assertEquals("b", args.getValue().get("second").second);
        assertEquals("data", args.getValue().get("key1").first);
        assertEquals("value1", args.getValue().get("key1").second);
        assertEquals("data", args.getValue().get("key2").first);
        assertEquals("value2", args.getValue().get("key2").second);
    }

    @Test
    public void methodLevelDefaultValues() throws Throwable {
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", params, emptyMap(), defaultValues, defaultParameters);

        when(engineYard.newInstance(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").first);
        assertEquals("a", args.getValue().get("first").second);
        assertEquals("data", args.getValue().get("second").first);
        assertEquals("b", args.getValue().get("second").second);
        assertEquals("data", args.getValue().get("key1").first);
        assertEquals("value1", args.getValue().get("key1").second);
        assertEquals("data", args.getValue().get("key2").first);
        assertEquals("value2", args.getValue().get("key2").second);
    }

    @Test
    public void methodLevelDefaultValuesOverrideClassLevel() throws Throwable {
        Map<String, String> classDefaults = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
        Map<String, String> methodLevelDefaults = new HashMap<String, String>() {{
            put("key1", "m_value1");
        }};

        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", params, classDefaults, methodLevelDefaults, defaultParameters);

        when(engineYard.newInstance(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").first);
        assertEquals("a", args.getValue().get("first").second);
        assertEquals("data", args.getValue().get("second").first);
        assertEquals("b", args.getValue().get("second").second);
        assertEquals("data", args.getValue().get("key1").first);
        assertEquals("m_value1", args.getValue().get("key1").second);
        assertEquals("data", args.getValue().get("key2").first);
        assertEquals("value2", args.getValue().get("key2").second);
    }

    @Application(project = "a", key = "b")
    interface SomeInterface {
        @ApiPackage("c")
        Observable<Map<String, Object>> someMethod(
                @Named("first") String f,
                @Named("second") String s
        );
    }
}