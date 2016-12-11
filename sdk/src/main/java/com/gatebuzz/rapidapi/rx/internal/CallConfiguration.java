package com.gatebuzz.rapidapi.rx.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CallConfiguration {
    public final String project;
    public final String key;
    public final Map<String, String> classLevelDefaults;
    public final Map<String, String> methodLevelDefaults;
    public final List<String> defaultValueNames;
    final String pack;
    final String block;
    final List<String> parameters;
    final Set<String> urlEncoded;

    CallConfiguration(String project, String key, String pack, String block, List<String> parameters,
                      Set<String> urlEncoded, Map<String, String> classLevelDefaults,
                      Map<String, String> methodLevelDefaults, List<String> defaultValueNames) {
        this.project = project;
        this.key = key;
        this.pack = pack;
        this.block = block;
        this.parameters = parameters;
        this.urlEncoded = urlEncoded;
        this.classLevelDefaults = classLevelDefaults;
        this.methodLevelDefaults = methodLevelDefaults;
        this.defaultValueNames = defaultValueNames;
    }
}
