package com.gatebuzz.rapidapi.rx.internal;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Process the payload part of the return envelope from RapidAPI and return it in a key/value map
 * with the key of "success" (matching the production SDK).
 *
 * @see com.gatebuzz.rapidapi.rx.utils.SuccessMapper
 * @see com.gatebuzz.rapidapi.rx.utils.DrillDown
 */
public class KeyValueMapProcessor implements ResponseProcessor {
    public static final Type MAP_STRING_TO_OBJECT = new TypeToken<Map<String, Object>>() {
    }.getType();
    private final String resultKey;

    public KeyValueMapProcessor(String resultKey) {
        this.resultKey = resultKey;
    }

    @Override
    public Object process(Gson gson, JsonElement payloadElement) {
        Map<String, Object> result = new HashMap<>();
        Object value = gson.fromJson(payloadElement, MAP_STRING_TO_OBJECT);
        result.put(resultKey, value);
        return result;
    }
}
