package com.gatebuzz.rapidapi.rx.internal;

import rx.Single;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.gatebuzz.rapidapi.rx.internal.CallConfiguration.Parameter;

public class CallHandler implements java.lang.reflect.InvocationHandler {
    private static final String UTF_8 = "UTF-8";
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
            Parameter parameter = configuration.parameters.get(i);
            if (parameterValues[i] != null) {
                putBody(body, parameter, parameterValues[i]);
            } else {
                if (parameter.required) {
                    throw new IllegalArgumentException("Calling \"" + method.getName() + "\" - required parameter \"" + parameter + "\" is null.");
                }
            }
        }

        if (configuration.defaultParameters != null) {
            for (Parameter parameter : configuration.defaultParameters) {
                String methodValue = configuration.methodLevelDefaults.get(parameter.name);
                String classValue = configuration.classLevelDefaults.get(parameter.name);
                String value = methodValue != null ? methodValue : classValue != null ? classValue : null;
                if (value != null) {
                    putBody(body, parameter, value);
                }
            }
        }

        Engine engine = engineYard.newInstance(configuration, body);
        return Single.class.equals(method.getReturnType()) ? engine.getSingle() : engine.getObservable();
    }

    private void putBody(Map<String, Pair<String, String>> body, Parameter parameter, Object parameterValue) {
        if (parameterValue instanceof File) {
            body.put(parameter.name, Pair.file(((File) parameterValue).getAbsolutePath()));
        } else {
            String value = String.valueOf(parameterValue);
            if (parameter.urlEncoded) {
                try {
                    value = URLEncoder.encode(value, UTF_8);
                } catch (UnsupportedEncodingException ignored) {
                }
            }
            body.put(parameter.name, Pair.data(value));
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
