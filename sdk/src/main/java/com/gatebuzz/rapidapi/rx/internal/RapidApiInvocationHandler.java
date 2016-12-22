package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.internal.model.CallConfiguration;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterSpec;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterValue;
import rx.Single;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RapidApiInvocationHandler implements InvocationHandler {
    private static final String UTF_8 = "UTF-8";
    private final Map<String, CallConfiguration> callConfigurationMap;
    private final EngineYard engineYard;

    public RapidApiInvocationHandler(Map<String, CallConfiguration> callConfigurationMap) {
        this(callConfigurationMap, new DefaultEngineYard());
    }

    RapidApiInvocationHandler(Map<String, CallConfiguration> callConfigurationMap, EngineYard engineYard) {
        this.callConfigurationMap = callConfigurationMap;
        this.engineYard = engineYard;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] parameterValues) throws Throwable {
        final CallConfiguration configuration = callConfigurationMap.get(method.getName());
        final List<ParameterValue> body = new ArrayList<>();

        for (int i = 0; i < configuration.parameters.size(); i++) {
            ParameterSpec parameter = configuration.parameters.get(i);
            if (parameterValues[i] != null) {
                putBody(body, parameter, parameterValues[i]);
            } else {
                if (parameter.required) {
                    throw new IllegalArgumentException("Calling \"" + method.getName() + "\" - required parameter \"" + parameter + "\" is null.");
                }
            }
        }

        if (configuration.defaultParameters != null) {
            for (ParameterSpec parameter : configuration.defaultParameters) {
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

    @SuppressWarnings("unchecked")
    private void putBody(List<ParameterValue> body, ParameterSpec parameter, Object parameterValue) {
        if (parameterValue instanceof InputStream) {
            body.add(ParameterValue.stream(parameter.name, (InputStream) parameterValue));
        } else if (parameterValue instanceof File) {
            body.add(ParameterValue.file(parameter.name, (File) parameterValue));
        } else {
            String value = String.valueOf(parameterValue);
            if (parameter.urlEncoded) {
                try {
                    value = URLEncoder.encode(value, UTF_8);
                } catch (UnsupportedEncodingException ignored) {
                }
            }
            body.add(ParameterValue.data(parameter.name, value));
        }
    }

    interface EngineYard {
        Engine newInstance(CallConfiguration configuration, List<ParameterValue> body);
    }

    private static class DefaultEngineYard implements EngineYard {
        @Override
        public Engine newInstance(CallConfiguration configuration, List<ParameterValue> body) {
            return new Engine(configuration, body);
        }
    }
}
