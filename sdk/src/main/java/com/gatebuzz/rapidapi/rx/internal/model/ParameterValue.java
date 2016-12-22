package com.gatebuzz.rapidapi.rx.internal.model;

import okhttp3.MultipartBody;

import java.io.File;
import java.io.InputStream;

public abstract class ParameterValue<T> {
    public final String name;
    public final T value;

    ParameterValue(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public static ParameterValue data(String name, String value) {
        return new DataParameter(name, value);
    }

    public static ParameterValue file(String name, File value) {
        return new FileParameter(name, value);
    }

    public static ParameterValue stream(String name, InputStream value) {
        return new InputStreamParameter(name, value);
    }

    public abstract void visit(MultipartBody.Builder builder);
}
