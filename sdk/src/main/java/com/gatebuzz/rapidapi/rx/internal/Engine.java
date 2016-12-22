package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.gatebuzz.rapidapi.rx.internal.model.CallConfiguration;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterValue;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

import java.util.List;
import java.util.Map;

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
    private final List<ParameterValue> body;
    private Subscriber<? super T> subscriber;

    Engine(CallConfiguration callConfiguration, List<ParameterValue> body) {
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
        this.subscriber = subscriber;
        try {
            Request request = new Request.Builder()
                    .url(String.format(URL_FORMAT, callConfiguration.server.serverUrl, callConfiguration.pack, callConfiguration.block))
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(AUTHORIZATION, Credentials.basic(callConfiguration.project, callConfiguration.key))
                    .post(createMultipartBody())
                    .build();
            Response response = callConfiguration.server.okHttpClient.newCall(request).execute();
            JsonParser parser = new JsonParser();
            JsonObject rootObject = parser.parse(response.body().string()).getAsJsonObject();
            JsonElement payloadElement = rootObject.get(PAYLOAD);

            if (response.code() != 200) {
                sendErrorWithPayload(this.subscriber, callConfiguration.server.gson, payloadElement);
            } else {
                String outcome = rootObject.get(OUTCOME).getAsString();
                if (ERROR_OUTCOME.equals(outcome)) {
                    sendErrorWithPayload(this.subscriber, callConfiguration.server.gson, payloadElement);
                } else {
                    this.subscriber.onNext((T) callConfiguration.responseProcessor.process(callConfiguration.server.gson, payloadElement));
                    this.subscriber.onCompleted();
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

    private RequestBody createMultipartBody() {
        if (body.isEmpty()) {
            return EMPTY;
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (ParameterValue entry : body) {
            entry.visit(builder);
        }

        return builder.build();
    }
}
