package com.gatebuzz.rapidapi.rx.internal;

import android.support.annotation.VisibleForTesting;

import com.gatebuzz.rapidapi.rx.FailedCallException;
import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;

@SuppressWarnings("unchecked")
class Engine implements Observable.OnSubscribe<Map<String, Object>> {
    private final CallConfiguration callConfiguration;
    private final Map<String, Argument> body;
    private final ConnectFactory factory;

    Engine(CallConfiguration callConfiguration, Map<String, Argument> body) {
        this(callConfiguration, body, new DefaultConnectFactory());
    }

    @VisibleForTesting
    Engine(CallConfiguration callConfiguration, Map<String, Argument> body, ConnectFactory factory) {
        this.callConfiguration = callConfiguration;
        this.body = body;
        this.factory = factory;
    }

    @Override
    public void call(Subscriber<? super Map<String, Object>> subscriber) {
        RapidApiConnect rapidApiConnect = factory.create(callConfiguration.project, callConfiguration.key);
        Map<String, Object> response;
        try {
            response = (Map<String, Object>) rapidApiConnect.call(callConfiguration.pack, callConfiguration.block, body);
            if (response.get("success") != null) {
                subscriber.onNext(response);
                subscriber.onCompleted();
            } else {
                subscriber.onError(new FailedCallException(response));
            }
        } catch (Exception e) {
            subscriber.onError(e);
        }
    }

    interface ConnectFactory {
        RapidApiConnect create(String project, String key);
    }

    private static class DefaultConnectFactory implements ConnectFactory {
        @Override
        public RapidApiConnect create(String project, String key) {
            return new RapidApiConnect(project, key);
        }
    }
}
