package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.gatebuzz.rapidapi.rx.internal.Pair.TYPE_DATA;

class Engine {
    private static final String URL_FORMAT = "%1$s/%2$s/%3$s";
    private static final String USER_AGENT = "User-Agent";
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_AGENT_VALUE = "RapidAPIConnect_RxRapidApi";
    private static final String OUTCOME = "outcome";
    private static final String PAYLOAD = "payload";
    private static final String SUCCESS_RESULT = "success";
    private static final String ERROR_RESULT = "error";
    private static final String ERROR_OUTCOME = "error";
    private static final RequestBody EMPTY = RequestBody.create(MediaType.parse("text/plain"), "");
    private static final Type MAP_STRING_TO_OBJECT = new TypeToken<Map<String, Object>>() {
    }.getType();

    private final CallConfiguration callConfiguration;
    private final Map<String, Pair<String, String>> body;

    Engine(CallConfiguration callConfiguration, Map<String, Pair<String, String>> body) {
        this.callConfiguration = callConfiguration;
        this.body = body;
    }

    Single<Map<String, Object>> getSingle() {
        return getObservable().toSingle();
    }

    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    Observable<Map<String, Object>> getObservable() {
        return Observable.create(new Observable.OnSubscribe<Map<String, Object>>() {
            @Override
            public void call(Subscriber<? super Map<String, Object>> subscriber) {
                executeCall(subscriber);
            }
        });
    }

    private void executeCall(Subscriber<? super Map<String, Object>> subscriber) {
        try {
            RequestBody requestBody = createMultipartBody();

            Request request = new Request.Builder()
                    .url(String.format(URL_FORMAT, callConfiguration.server.serverUrl, callConfiguration.pack, callConfiguration.block))
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(AUTHORIZATION, Credentials.basic(callConfiguration.project, callConfiguration.key))
                    .post(requestBody)
                    .build();

            Response response = callConfiguration.server.okHttpClient.newCall(request).execute();
            Map<String, Object> map = fromJson(response.body().string());

            Map<String, Object> result = new HashMap<>();
            if (response.code() != 200 || ERROR_OUTCOME.equals(map.get(OUTCOME))) {
                result.put(ERROR_RESULT, map.get(PAYLOAD));
                subscriber.onError(new FailedCallException(result));
            } else {
                result.put(SUCCESS_RESULT, map.get(PAYLOAD));
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }

    private Map<String, Object> fromJson(String string) {
        return callConfiguration.server.gson.fromJson(string, MAP_STRING_TO_OBJECT);
    }

    @SuppressWarnings("unchecked")
    private RequestBody createMultipartBody() throws FileNotFoundException {
        if (body.isEmpty()) {
            return EMPTY;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        Set<Map.Entry<String, Pair<String, String>>> entrySet = ((Map) body).entrySet();
        for (Map.Entry<String, Pair<String, String>> entry : entrySet) {
            Pair<String, String> argument = entry.getValue();
            if (TYPE_DATA.equals(argument.first)) {
                builder.addFormDataPart(entry.getKey(), argument.second);
            } else {
                File file = new File(argument.second);
                if (file.exists() && file.isFile()) {
                    builder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(MultipartBody.FORM, file));
                } else {
                    throw new FileNotFoundException(argument.second);
                }
            }
        }
        return builder.build();
    }
}
