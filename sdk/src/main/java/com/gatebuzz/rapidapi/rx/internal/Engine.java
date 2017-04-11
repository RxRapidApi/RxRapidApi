package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.gatebuzz.rapidapi.rx.internal.model.CallConfiguration;
import com.gatebuzz.rapidapi.rx.internal.model.ParameterValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.*;
import okhttp3.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
class Engine {
    private static final String URL_FORMAT = "%1$s/%2$s/%3$s";
    private static final String USER_AGENT = "User-Agent";
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_AGENT_VALUE = "RapidAPIConnect_RxRapidApi";
    private static final String OUTCOME = "outcome";
    private static final String PAYLOAD = "payload";
    private static final String ERROR_OUTCOME = "error";
    private static final RequestBody EMPTY = RequestBody.create(MediaType.parse("text/plain"), "");
    private static final ResponseProcessor ERROR_PROCESSOR = new KeyValueMapProcessor("error");

    private final CallConfiguration callConfiguration;
    private final List<ParameterValue> body;

    Engine(CallConfiguration callConfiguration, List<ParameterValue> body) {
        this.callConfiguration = callConfiguration;
        this.body = body;
    }

    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    <T> Single<T> getSingle() {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(SingleEmitter<T> singleEmitter) throws Exception {
                executeCall(singleEmitter);
            }
        });
    }

    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    <T> Observable<T> getObservable() {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> observableEmitter) throws Exception {
                executeCall(observableEmitter);
            }
        });
    }

    private <T> void executeCall(SingleEmitter<T> singleEmitter) {
        executeCall(new Emitter<T>() {
            @Override
            public void onNext(T t) {
                singleEmitter.onSuccess(t);
            }

            @Override
            public void onError(Throwable throwable) {
                singleEmitter.onError(throwable);
            }

            @Override
            public void onComplete() {
                // deliberate no-op
            }
        });
    }

    private <T> void executeCall(final Emitter<T> subscriber) {
        try {
            Request request = new Request.Builder()
                    .url(String.format(URL_FORMAT, callConfiguration.server.serverUrl, callConfiguration.pack, callConfiguration.block))
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(AUTHORIZATION, Credentials.basic(callConfiguration.project, callConfiguration.key))
                    .post(createMultipartBody())
                    .build();
            Response response = callConfiguration.server.okHttpClient.newCall(request).execute();

            // So it looks like the server doesn't sent well-formed JSON back if the block isn't found.
            // In that case, synthesize a return value that clients can understand, rather than a
            // strange "JSON Parse Exception"
            if (response.code() == 404) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", callConfiguration.pack + "." + callConfiguration.block + " not found.");
                subscriber.onError(new FailedCallException(error));
                return;
            }

            JsonParser parser = new JsonParser();
            JsonObject rootObject = parser.parse(response.body().charStream()).getAsJsonObject();
            JsonElement payloadElement = rootObject.get(PAYLOAD);

            if (response.code() != 200) {
                subscriber.onError(new FailedCallException(parseErrorPayload(payloadElement)));
            } else {
                String outcome = rootObject.get(OUTCOME).getAsString();
                if (ERROR_OUTCOME.equals(outcome)) {
                    subscriber.onError(new FailedCallException(parseErrorPayload(payloadElement)));
                } else {
                    subscriber.onNext((T) callConfiguration.responseProcessor.process(callConfiguration.server.gson, payloadElement));
                    subscriber.onComplete();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    private Map<String, Object> parseErrorPayload(JsonElement payloadElement) {
        return (Map<String, Object>) ERROR_PROCESSOR.process(callConfiguration.server.gson, payloadElement);
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
