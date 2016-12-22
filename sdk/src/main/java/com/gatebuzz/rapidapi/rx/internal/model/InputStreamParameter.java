package com.gatebuzz.rapidapi.rx.internal.model;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamParameter extends ParameterValue<InputStream> {
    InputStreamParameter(String name, InputStream value) {
        super(name, value);
    }

    @Override
    public void visit(MultipartBody.Builder builder) {
        builder.addFormDataPart(name, name, new StreamBody(value));
    }

    private static class StreamBody extends RequestBody {
        private final InputStream inputStream;

        StreamBody(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public MediaType contentType() {
            return MultipartBody.FORM;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            sink.writeAll(Okio.source(inputStream));
        }
    }
}
