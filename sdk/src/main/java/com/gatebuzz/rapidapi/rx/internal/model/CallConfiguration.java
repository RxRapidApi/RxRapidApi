package com.gatebuzz.rapidapi.rx.internal.model;

import com.gatebuzz.rapidapi.rx.internal.ResponseProcessor;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterSpec;
import com.gatebuzz.rapidapi.rx.internal.model.Server;

import java.util.List;
import java.util.Map;

public class CallConfiguration {
    public final Server server;
    public final String project;
    public final String key;
    public final Map<String, String> classLevelDefaults;
    public final Map<String, String> methodLevelDefaults;
    public final List<ParameterSpec> defaultParameters;
    public final String pack;
    public final String block;
    public final List<ParameterSpec> parameters;
    public final ResponseProcessor responseProcessor;

    public CallConfiguration(Server server, String project, String key, String pack, String block, List<ParameterSpec> parameters,
                      Map<String, String> classLevelDefaults, Map<String, String> methodLevelDefaults,
                      List<ParameterSpec> defaultParameters, ResponseProcessor responseProcessor) {
        this.server = server;
        this.project = project;
        this.key = key;
        this.pack = pack;
        this.block = block;
        this.parameters = parameters;
        this.classLevelDefaults = classLevelDefaults;
        this.methodLevelDefaults = methodLevelDefaults;
        this.defaultParameters = defaultParameters;
        this.responseProcessor = responseProcessor;
    }
}
