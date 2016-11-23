package com.gatebuzz.rapidapi.rx;

import android.util.Log;

import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class RxRapidApiBuilder {

    private final String key;
    private final String project;
    private final List<String> parameters;
    private final String pack;
    private final String block;

    public RxRapidApiBuilder(String key, String project, List<String> parameters, String pack, String block) {
        this.key = key;
        this.project = project;
        this.parameters = parameters;
        this.pack = pack;
        this.block = block;
    }

    @SuppressWarnings("unchecked")
    public static <I> I from(Class<I> interfaceClass) {
        Map<String, RxRapidApiBuilder> builders = new HashMap<>();
        Application applicationAnnotation = interfaceClass.getAnnotation(Application.class);
        String appProject = applicationAnnotation != null ? applicationAnnotation.project() : null;
        String appKey = applicationAnnotation != null ? applicationAnnotation.key() : null;
        for (Method method : interfaceClass.getMethods()) {
            ApiPackage packageAnnotation = method.getAnnotation(ApiPackage.class);
            Application methodAppAnnotation = method.getAnnotation(Application.class);
            String project = appProject;
            String key = appKey;
            if (methodAppAnnotation != null) {
                project = methodAppAnnotation.project();
                key = methodAppAnnotation.key();
            }
            Named methodNameAnnotation = method.getAnnotation(Named.class);
            String name = method.getName();
            if (methodAppAnnotation != null) {
                name = methodNameAnnotation.value();
            }
            List<String> parameters = new ArrayList<>();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(Named.class)) {
                        parameters.add(((Named)annotation).value());
                    }
                }
            }

            builders.put(method.getName(), new RxRapidApiBuilder(key, project, parameters, packageAnnotation.value(), name));
        }
        return (I) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, new RapidApiConnectInvocationHandler(builders));
    }

    private static class RapidApiConnectInvocationHandler implements InvocationHandler {
        private Map<String, RxRapidApiBuilder> builders;

        RapidApiConnectInvocationHandler(Map<String, RxRapidApiBuilder> builders) {
            this.builders = builders;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            final RxRapidApiBuilder builder = builders.get(method.getName());

            final Map<String, Argument> body = new HashMap<>();
            for (int i = 0; i < builder.parameters.size(); i++) {
                body.put(builder.parameters.get(i), new Argument("data", String.valueOf(objects[i])));
            }

            return Observable.create(new Engine(builder, body));
        }
    }

    @SuppressWarnings("unchecked")
    private static class Engine implements Observable.OnSubscribe<Map<String, Object>> {
        private final RxRapidApiBuilder builder;
        private final Map<String, Argument> body;

        private Engine(RxRapidApiBuilder builder, Map<String, Argument> body) {
            this.builder = builder;
            this.body = body;
        }

        @Override
        public void call(Subscriber<? super Map<String, Object>> subscriber) {
            RapidApiConnect rapidApiConnect = new RapidApiConnect(builder.project, builder.key);
            Map<String, Object> response;
            try {
                Log.e("Example", "Project = "+builder.project+", Key = "+builder.key);
                Log.e("Example", "Pack = "+builder.pack+", Block = "+builder.block);
                Log.e("Example", "Body = "+body);
                response = (Map<String, Object>) rapidApiConnect.call(builder.pack, builder.block, body);
                if (response.get("success") != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new FailedCallException(response));
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }
    }

}
