package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.internal.model.CallConfiguration;

import java.util.Map;

public interface CallHandlerFactory {
    <T> T newInstance(Class<?> interfaceClass, Map<String, CallConfiguration> configurationMap);
}
