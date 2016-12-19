package com.gatebuzz.rapidapi.rx.internal;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CallConfiguration {
    public final Server server;
    public final String project;
    public final String key;
    public final Map<String, String> classLevelDefaults;
    public final Map<String, String> methodLevelDefaults;
    public final List<Parameter> defaultParameters;
    public final String pack;
    public final String block;
    public final List<Parameter> parameters;

    CallConfiguration(Server server, String project, String key, String pack, String block, List<Parameter> parameters,
                      Map<String, String> classLevelDefaults, Map<String, String> methodLevelDefaults,
                      List<Parameter> defaultParameters) {
        this.server = server;
        this.project = project;
        this.key = key;
        this.pack = pack;
        this.block = block;
        this.parameters = parameters;
        this.classLevelDefaults = classLevelDefaults;
        this.methodLevelDefaults = methodLevelDefaults;
        this.defaultParameters = defaultParameters;
    }

    public static class Server {
        final Gson gson;
        final OkHttpClient okHttpClient;
        final String serverUrl;

        public Server(String serverUrl, OkHttpClient okHttpClient, Gson gson) {
            this.okHttpClient = okHttpClient;
            this.serverUrl = serverUrl;
            this.gson = gson;
        }
    }

    public static class Parameter {
        String name;
        boolean urlEncoded;
        boolean required;

        public Parameter(String name) {
            this(name, false, false);
        }

        Parameter(String name, boolean urlEncoded, boolean required) {
            this.name = name;
            this.urlEncoded = urlEncoded;
            this.required = required;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Parameter parameter = (Parameter) o;
            return urlEncoded == parameter.urlEncoded &&
                    required == parameter.required &&
                    Objects.equals(name, parameter.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, urlEncoded, required);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
