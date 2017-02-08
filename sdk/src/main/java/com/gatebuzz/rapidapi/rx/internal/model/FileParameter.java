package com.gatebuzz.rapidapi.rx.internal.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;

public class FileParameter extends ParameterValue<File> {
    FileParameter(String name, File value) {
        super(name, value);
    }

    @Override
    public void visit(MultipartBody.Builder builder) {
        if (value.exists() && value.isFile()) {
            builder.addFormDataPart(name, value.getName(), RequestBody.create(MultipartBody.FORM, value));
        } else {
            throw new IllegalArgumentException("File not found: " + value.getAbsolutePath());
        }
    }
}
