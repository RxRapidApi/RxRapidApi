package com.gatebuzz.rapidapi.rx.internal.model;

import okhttp3.MultipartBody;

public class DataParameter extends ParameterValue<String> {
    DataParameter(String name, String value) {
        super(name, value);
    }

    @Override
    public void visit(MultipartBody.Builder builder) {
        builder.addFormDataPart(name, value);
    }
}
