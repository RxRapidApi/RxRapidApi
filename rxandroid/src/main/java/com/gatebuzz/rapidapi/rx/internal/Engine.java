package com.gatebuzz.rapidapi.rx.internal;

import android.support.v4.util.Pair;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

class Engine implements Observable.OnSubscribe<Map<String, Object>> {
    private static final String URL_FORMAT = "https://rapidapi.io/connect/%1$s/%2$s";
    private static final RequestBody EMPTY = RequestBody.create(MediaType.parse("text/plain"), "");

    private final OkHttpClient client;
    private final CallConfiguration callConfiguration;
    private final Map<String, Pair<String, String>> body;
    private final Gson gson;

    Engine(CallConfiguration callConfiguration, Map<String, Pair<String, String>> body) {
        this.callConfiguration = callConfiguration;
        this.body = body;
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    @Override
    public void call(Subscriber<? super Map<String, Object>> subscriber) {
        try {
            RequestBody requestBody = createMultipartBody();

            Request request = new Request.Builder()
                    .url(String.format(URL_FORMAT, callConfiguration.pack, callConfiguration.block))
                    .addHeader("User-Agent", "RapidAPIConnect_RxRapidApi")
                    .addHeader("Authorization", Credentials.basic(callConfiguration.project, callConfiguration.key))
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            Map<String, Object> map = fromJson(response.body().string());

            Map<String, Object> result = new HashMap<>();
            if (response.code() != 200 || "error".equals(map.get("outcome"))) {
                result.put("error", map.get("payload"));
                subscriber.onError(new FailedCallException(result));
            } else {
                result.put("success", map.get("payload"));
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }

    private Map<String, Object> fromJson(String string) {
        return gson.fromJson(string, new TypeToken<Map<String, Object>>() {
        }.getType());
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
            if ("data".equals(argument.first)) {
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
