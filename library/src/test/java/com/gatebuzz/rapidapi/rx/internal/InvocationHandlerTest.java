package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.Application;
import com.gatebuzz.rapidapi.rx.Named;
import com.rapidapi.rapidconnect.Argument;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class InvocationHandlerTest {

    @Mock
    private Engine engine;
    @Mock
    private InvocationHandler.EngineYard engineYard;
    @Captor
    private ArgumentCaptor<Map<String, Argument>> args;

    @Test
    public void engineConfiguredAsExpected() throws Throwable {
        CallConfiguration configuration = new CallConfiguration("a", "b", "c", "someMethod", Arrays.asList("first", "second"));
        String expectedFirstParam = "d";
        String expectedSecondParam = "e";

        when(engineYard.create(isA(CallConfiguration.class), isA(Map.class))).thenReturn(engine);
        Map<String, CallConfiguration> config = new HashMap<>();
        config.put("someMethod", configuration);
        InvocationHandler handler = new InvocationHandler(config, engineYard);
        handler.invoke(null, SomeInterface.class.getMethod("someMethod", String.class, String.class), new Object[]{expectedFirstParam, expectedSecondParam});

        verify(engineYard).create(same(configuration), args.capture());
        assertEquals("data", args.getValue().get("first").getType());
        assertEquals(expectedFirstParam, args.getValue().get("first").getValue());
        assertEquals("data", args.getValue().get("second").getType());
        assertEquals(expectedSecondParam, args.getValue().get("second").getValue());
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