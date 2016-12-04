package com.gatebuzz.rapidapi.rx;

import android.support.annotation.Keep;

import com.gatebuzz.rapidapi.rx.internal.CallConfiguration;
import com.gatebuzz.rapidapi.rx.internal.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Keep
public class RxRapidApiBuilder {

    private Class<?> interfaceClass;
    private String project;
    private String key;
    private String apiPackage;

    @SuppressWarnings("unchecked")
    public static <I> I from(Class<I> interfaceClass) {
        return new RxRapidApiBuilder().endpoint(interfaceClass).build();
    }

    public RxRapidApiBuilder endpoint(Class<?> endpoint) {
        this.interfaceClass = endpoint;
        return this;
    }

    public RxRapidApiBuilder application(String project, String key) {
        this.project = project;
        this.key = key;
        return this;
    }

    public RxRapidApiBuilder apiPackage(String apiPackage) {
        this.apiPackage = apiPackage;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T build() {
        Application applicationAnnotation = interfaceClass.getAnnotation(Application.class);
        ApiPackage apiPackageAnnotation = interfaceClass.getAnnotation(ApiPackage.class);

        Map<String, CallConfiguration> callConfigurationMap = new HashMap<>();
        for (Method method : interfaceClass.getMethods()) {
            Application methodAppAnnotation = method.getAnnotation(Application.class);
            ApiPackage methodApiPackageAnnotation = method.getAnnotation(ApiPackage.class);
            CallConfiguration configuration = CallConfiguration.newInstance(
                    applicationAnnotation, methodAppAnnotation,
                    apiPackageAnnotation, methodApiPackageAnnotation,
                    method, project, key, apiPackage);
            callConfigurationMap.put(method.getName(), configuration);
        }

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, new InvocationHandler(callConfigurationMap));
    }

}
