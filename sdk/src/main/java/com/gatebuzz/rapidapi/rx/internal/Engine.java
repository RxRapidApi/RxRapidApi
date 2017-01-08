package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import static com.gatebuzz.rapidapi.rx.internal.Pair.TYPE_DATA;

@SuppressWarnings("unchecked")
class Engine<T> {
    private static final String URL_FORMAT = "%1$s/%2$s/%3$s";
    private static final String USER_AGENT = "User-Agent";
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_AGENT_VALUE = "RapidAPIConnect_RxRapidApi";
    private static final String OUTCOME = "outcome";
    private static final String PAYLOAD = "payload";
    private static final String ERROR_OUTCOME = "error";
    private static final RequestBody EMPTY = RequestBody.create(MediaType.parse("text/plain"), "");
    private static final ResponseProcessor ERROR_PROCESSOR = KeyValueMapProcessor.error();

    private final CallConfiguration callConfiguration;
    private final Map<String, Pair<String, String>> body;

    Engine(CallConfiguration callConfiguration, Map<String, Pair<String, String>> body) {
        this.callConfiguration = callConfiguration;
        this.body = body;
    }

    Single<T> getSingle() {
        return getObservable().toSingle();
    }

    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    Observable<T> getObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                executeCall(subscriber);
            }
        });
    }

    private void executeCall(Subscriber<? super T> subscriber) {
        try {
            Request request = new Request.Builder()
                    .url(String.format(URL_FORMAT, callConfiguration.server.serverUrl, callConfiguration.pack, callConfiguration.block))
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(AUTHORIZATION, Credentials.basic(callConfiguration.project, callConfiguration.key))
                    .post(createMultipartBody()).build();

            Response response = callConfiguration.server.okHttpClient.newCall(request).execute();
            Gson gson = callConfiguration.server.gson;
            String string = response.body().string();
            JsonParser parser = new JsonParser();
            JsonObject rootObject = parser.parse(string).getAsJsonObject();
            JsonElement payloadElement = rootObject.get(PAYLOAD);

            if (response.code() != 200) {
                sendErrorWithPayload(subscriber, gson, payloadElement);
            } else {
                String outcome = rootObject.get(OUTCOME).getAsString();
                if (ERROR_OUTCOME.equals(outcome)) {
                    sendErrorWithPayload(subscriber, gson, payloadElement);
                } else {
                    subscriber.onNext((T) callConfiguration.responseProcessor.process(gson, payloadElement));
                    subscriber.onCompleted();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    private void sendErrorWithPayload(Subscriber<? super T> subscriber, Gson gson, JsonElement payloadElement) {
        subscriber.onError(new FailedCallException((Map<String, Object>) ERROR_PROCESSOR.process(gson, payloadElement)));
    }

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
