package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EngineTest {

    @Mock private Engine.ConnectFactory connectFactory;
    @Mock private RapidApiConnect apiConnect;
    private HashMap<String, Object> result;
    private Map<String, Argument> params;
    private Engine engine;
    private TestSubscriber<Map<String, Object>> testSubscriber;

    @Before
    public void setUp() throws Exception {
        when(connectFactory.create("a", "b")).thenReturn(apiConnect);

        result = new HashMap<>();
        when(apiConnect.call(eq("c"), eq("d"), any(Map.class))).thenReturn(result);

        params = new HashMap<String, Argument>() {{
            put("one", new Argument("data", "1"));
            put("two", new Argument("data", "2"));
        }};

        testSubscriber = new TestSubscriber<>();

        engine = new Engine(new CallConfiguration("a", "b", "c", "d", Arrays.asList("one", "two")), params, connectFactory);
    }

    @Test
    public void happyPath() throws Exception {
        result.put("success", "1");

        engine.call(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        assertSame(result, testSubscriber.getOnNextEvents().get(0));
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void serverReportedAnError() throws Exception {
        result.put("no-success-for-you", "1");

        engine.call(testSubscriber);

        testSubscriber.assertNoValues();
        Throwable error = testSubscriber.getOnErrorEvents().get(0);
        assertSame(result, ((FailedCallException) error).getResponse());
    }

    @Test
    public void somethingBadHappenedMakingTheCall() throws Exception {
        RuntimeException exception = new RuntimeException();
        when(apiConnect.call(eq("c"), eq("d"), any(Map.class))).thenThrow(exception);

        engine.call(testSubscriber);

        testSubscriber.assertNoValues();
        assertSame(exception, testSubscriber.getOnErrorEvents().get(0));
    }
}