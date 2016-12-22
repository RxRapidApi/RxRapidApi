package com.gatebuzz.rapidapi.rx.internal.model;

import java.util.Objects;

public class ParameterSpec {
    public final String name;
    public final boolean urlEncoded;
    public boolean required;

    public ParameterSpec(String name) {
        this(name, false, false);
    }

    public ParameterSpec(String name, boolean urlEncoded, boolean required) {
        this.name = name;
        this.urlEncoded = urlEncoded;
        this.required = required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterSpec parameter = (ParameterSpec) o;
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
