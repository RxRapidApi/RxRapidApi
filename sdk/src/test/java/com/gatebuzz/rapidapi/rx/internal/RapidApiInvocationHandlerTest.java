package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;
import com.gatebuzz.rapidapi.rx.internal.model.CallConfiguration;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterSpec;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterValue;
import com.gatebuzz.rapidapi.rx.internal.model.Server;
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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "Duplicates"})
@RunWith(MockitoJUnitRunner.class)
public class RapidApiInvocationHandlerTest {

    @Mock
    private Server server;
    @Mock
    private Engine engine;
    @Mock
    private RapidApiInvocationHandler.EngineYard engineYard;
    @Mock
    private ResponseProcessor processor;
    @Captor
    private ArgumentCaptor<List<ParameterValue>> args;

    private List<ParameterSpec> params;
    private List<ParameterSpec> defaultParameters;
    private Map<String, String> defaultValues;

    @Before
    public void setUp() throws Exception {
        params = Arrays.asList(new ParameterSpec("name"), new ParameterSpec("value"));
        defaultParameters = Arrays.asList(new ParameterSpec("key1"), new ParameterSpec("key2"));
        defaultValues = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }};
    }

    @Test
    public void engineConfiguredAsExpected() throws Throwable {
        CallConfiguration configuration = new CallConfiguration(server, "a", "b", "c", "someMethod", params, emptyMap(), emptyMap(), emptyList(), processor);
        String expectedFirstParam = "d";
        String expectedSecondParam = "e";

        when(engineYard.newInstance(isA(CallConfiguration.class), anyListOf(ParameterValue.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        RapidApiInvocationHandler handler = new RapidApiInvocationHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{expectedFirstParam, expectedSecondParam});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertParameter(args.getValue(), "name", expectedFirstParam);
        assertParameter(args.getValue(), "value", expectedSecondParam);
    }

    @Test
    public void optionalNullValuesAreSkipped() throws Throwable {
        CallConfiguration configuration = new CallConfiguration(server, "a", "b", "c", "someMethod", params, emptyMap(), emptyMap(), emptyList(), processor);

        when(engineYard.newInstance(isA(CallConfiguration.class), anyListOf(ParameterValue.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        RapidApiInvocationHandler handler = new RapidApiInvocationHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{null, "present"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertEquals(1, args.getValue().size());
        assertParameter(args.getValue(), "value", "present");
    }

    @Test
    public void requiredNullValuesThrowException() throws Throwable {
        params.get(0).required = true;
        CallConfiguration configuration = new CallConfiguration(server, "a", "b", "c", "someMethod", params, emptyMap(), emptyMap(), emptyList(), processor);

        when(engineYard.newInstance(isA(CallConfiguration.class), anyListOf(ParameterValue.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        RapidApiInvocationHandler handler = new RapidApiInvocationHandler(config, engineYard);
        try {
            handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                    new Object[]{null, "present"});
            fail("Required (but null) parameter should throw exception");
        } catch (IllegalArgumentException iar) {
            assertEquals("Calling \"someMethod\" - required parameter \"name\" is null.", iar.getMessage());
        }
    }

    @Test
    public void defaultParametersConfiguredFromClassLevelValues() throws Throwable {
        CallConfiguration configuration = new CallConfiguration(server, "a", "b", "c", "someMethod", params, defaultValues, emptyMap(), defaultParameters, processor);

        when(engineYard.newInstance(isA(CallConfiguration.class), anyListOf(ParameterValue.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        RapidApiInvocationHandler handler = new RapidApiInvocationHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertParameter(args.getValue(), "name", "a");
        assertParameter(args.getValue(), "value", "b");
        assertParameter(args.getValue(), "key1", "value1");
        assertParameter(args.getValue(), "key2", "value2");
    }

    @Test
    public void methodLevelDefaultValues() throws Throwable {
        CallConfiguration configuration = new CallConfiguration(server, "a", "b", "c", "someMethod", params, emptyMap(), defaultValues, defaultParameters, processor);

        when(engineYard.newInstance(isA(CallConfiguration.class), anyListOf(ParameterValue.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        RapidApiInvocationHandler handler = new RapidApiInvocationHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertParameter(args.getValue(), "name", "a");
        assertParameter(args.getValue(), "value", "b");
        assertParameter(args.getValue(), "key1", "value1");
        assertParameter(args.getValue(), "key2", "value2");
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

        CallConfiguration configuration = new CallConfiguration(server, "a", "b", "c", "someMethod", params, classDefaults, methodLevelDefaults, defaultParameters, processor);

        when(engineYard.newInstance(isA(CallConfiguration.class), anyListOf(ParameterValue.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        RapidApiInvocationHandler handler = new RapidApiInvocationHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class),
                new Object[]{"a", "b"});

        verify(engineYard).newInstance(same(configuration), args.capture());
        assertParameter(args.getValue(), "name", "a");
        assertParameter(args.getValue(), "value", "b");
        assertParameter(args.getValue(), "key1", "m_value1");
        assertParameter(args.getValue(), "key2", "value2");
    }

    private void assertParameter(List<ParameterValue> parameters, String name, String expected) {
        for (ParameterValue parameter : parameters) {
            if (parameter.name.equals(name)) {
                assertEquals(expected, parameter.value);
                return;
            }
        }
        fail("Parameter \"" + name + "\" doesn't exist.");
    }

    @Application(project = "a", key = "b")
    interface SomeInterface {
        @ApiPackage("c")
        Observable<Map<String, Object>> someMethod(
                @Named("name") String f,
                @Named("value") String s
        );
    }
}