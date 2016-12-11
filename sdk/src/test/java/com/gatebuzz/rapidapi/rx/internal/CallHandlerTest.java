package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class CallHandlerTest {

    @Mock private Engine engine;
    @Mock private CallHandler.EngineYard engineYard;
    @Captor private ArgumentCaptor<Map<String, Pair<String, String>>> args;

    @Test
    public void engineConfiguredAsExpected() throws Throwable {
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod",
                Arrays.asList("first", "second"), new HashSet<>(),
                Collections.emptyMap(), Collections.emptyMap(), Collections.emptyList());
        String expectedFirstParam = "d";
        String expectedSecondParam = "e";

        when(engineYard.create(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{expectedFirstParam, expectedSecondParam});

        verify(engineYard).create(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").first);
        assertEquals(expectedFirstParam, args.getValue().get("first").second);
        assertEquals("data", args.getValue().get("second").first);
        assertEquals(expectedSecondParam, args.getValue().get("second").second);
    }

    @Test
    public void defaultParametersConfiguredFromClassLevelValues() throws Throwable {
        Map<String, String> classLevelDefaults = createDefaultValues();

        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod",
                Arrays.asList("first", "second"), new HashSet<>(),
                classLevelDefaults, Collections.emptyMap(), Arrays.asList("key1", "key2"));

        when(engineYard.create(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).create(same(configuration), args.capture());
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
        Map<String, String> defaults = createDefaultValues();

        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod",
                Arrays.asList("first", "second"), new HashSet<>(),
                Collections.emptyMap(), defaults, Arrays.asList("key1", "key2"));

        when(engineYard.create(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).create(same(configuration), args.capture());
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
        Map<String, String> classDefaults = new HashMap<String, String>(){{
            put("key1", "value1");
            put("key2", "value2");
        }};
        Map<String, String> defaults = new HashMap<String, String>(){{
            put("key1", "m_value1");
        }};

        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod",
                Arrays.asList("first", "second"), new HashSet<>(),
                classDefaults, defaults, Arrays.asList("key1", "key2"));

        when(engineYard.create(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        CallHandler handler = new CallHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).create(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").first);
        assertEquals("a", args.getValue().get("first").second);
        assertEquals("data", args.getValue().get("second").first);
        assertEquals("b", args.getValue().get("second").second);
        assertEquals("data", args.getValue().get("key1").first);
        assertEquals("m_value1", args.getValue().get("key1").second);
        assertEquals("data", args.getValue().get("key2").first);
        assertEquals("value2", args.getValue().get("key2").second);
    }

    private Map<String, String> createDefaultValues() {
        return new HashMap<String, String>(){{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }};
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