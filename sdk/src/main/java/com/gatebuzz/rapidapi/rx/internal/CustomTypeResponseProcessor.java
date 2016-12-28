package com.gatebuzz.rapidapi.rx.internal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Process the payload part of the response envelope from RapidAPI and return it directly to the client,
 * the type determined from the return type of the declared interface method.
 */
public class CustomTypeResponseProcessor implements ResponseProcessor {
    private final Type returnType;

    public CustomTypeResponseProcessor(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public Object process(Gson gson, JsonElement payloadElement) {
        return gson.fromJson(payloadElement, returnType);
    }
}
