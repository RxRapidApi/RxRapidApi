package com.gatebuzz.rapidapi.rx.internal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Process the payload part of the response envelope and return it as some kind of object.
 */
public interface ResponseProcessor {
    Object process(Gson gson, JsonElement payloadElement);
}
