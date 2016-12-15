package com.gatebuzz.rapidapi.rx.internal;

import rx.Observable;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CallHandler implements java.lang.reflect.InvocationHandler {
    private static final String FILE = "file";
    private static final String DATA = "data";
    private static final String UTF_8 = "UTF-8";
    private static final String SINGLE = "Single";
    private final Map<String, CallConfiguration> callConfigurationMap;
    private final EngineYard engineYard;

    public CallHandler(Map<String, CallConfiguration> callConfigurationMap) {
        this(callConfigurationMap, new DefaultEngineYard());
    }

    CallHandler(Map<String, CallConfiguration> callConfigurationMap, EngineYard engineYard) {
        this.callConfigurationMap = callConfigurationMap;
        this.engineYard = engineYard;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] parameterValues) throws Throwable {
        final CallConfiguration configuration = callConfigurationMap.get(method.getName());
        final Map<String, Pair<String, String>> body = new HashMap<>();

        for (int i = 0; i < configuration.parameters.size(); i++) {
            String parameter = configuration.parameters.get(i);
            putBody(configuration, body, parameter, parameterValues[i]);
        }

        if (configuration.defaultValueNames != null) {
            for (String parameter : configuration.defaultValueNames) {
                String methodValue = configuration.methodLevelDefaults.get(parameter);
                String classValue = configuration.classLevelDefaults.get(parameter);
                String value = methodValue != null ? methodValue : classValue != null ? classValue : null;
                if (value != null) {
                    putBody(configuration, body, parameter, value);
                }
            }
        }

        Observable<Map<String, Object>> observable = Observable.create(engineYard.newInstance(configuration, body));
        return SINGLE.equals(method.getReturnType().getSimpleName()) ? observable.toSingle() : observable;
    }

    private void putBody(CallConfiguration configuration, Map<String, Pair<String, String>> body,
                         String parameter, Object parameterValue) throws UnsupportedEncodingException {
        if (parameterValue instanceof File) {
            body.put(parameter, new Pair<>(FILE, ((File) parameterValue).getAbsolutePath()));
        } else {
            String value = String.valueOf(parameterValue);
            if (configuration.urlEncoded.contains(parameter)) {
                value = URLEncoder.encode(value, UTF_8);
            }
            body.put(parameter, new Pair<>(DATA, value));
        }
    }

    interface EngineYard {
        Engine newInstance(CallConfiguration configuration, Map<String, Pair<String, String>> body);
    }

    private static class DefaultEngineYard implements EngineYard {
        @Override
        public Engine newInstance(CallConfiguration configuration, Map<String, Pair<String, String>> body) {
            return new Engine(configuration, body);
        }
    }
}
