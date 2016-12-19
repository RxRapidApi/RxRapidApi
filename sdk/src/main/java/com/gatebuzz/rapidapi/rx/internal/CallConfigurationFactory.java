package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.*;
import com.gatebuzz.rapidapi.rx.internal.CallConfiguration.Parameter;
import com.gatebuzz.rapidapi.rx.internal.CallConfiguration.Server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CallConfigurationFactory {

    private static final String[] EMPTY = new String[0];

    public static CallConfiguration newInstance(
            Application classApplicationAnnotation, Application methodApplicationAnnotation,
            ApiPackage classApiPackageAnnotation, ApiPackage methodApiPackageAnnotation,
            Method method, String project, String key, String apiPackage,
            Map<String, String> classLevelDefaults, Map<String, String> methodLevelDefaults,
            DefaultParameters classDefaultValueNamesAnnotation, DefaultParameters methodDefaultValueNamesAnnotation,
            Server server) {
        validateServer(server);

        String resolvedProject = fromAnnotation(Application::project,
                project, methodApplicationAnnotation, classApplicationAnnotation,
                "Project name not found (check the @Application annotation).");

        String resolvedKey = fromAnnotation(Application::key,
                key, methodApplicationAnnotation, classApplicationAnnotation,
                "API key not found (check the @Application annotation).");

        String resolvedApiPackage = fromAnnotation(ApiPackage::value,
                apiPackage, methodApiPackageAnnotation, classApiPackageAnnotation,
                "API package not found - check the @ApiPackage annotation on the interface or on " + method.getName() + "().");

        String name = collectRemoteMethodName(method).trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("API method name not found on " +
                    method.getName() + "() " + "(check the @Named annotation on your interface method).");
        }

        List<Parameter> parameters = collectAnnotatedParameters(method);
        if (parameters.size() != method.getParameterTypes().length) {
            throw new IllegalArgumentException("Incorrect number of @Named parameters on " +
                    method.getName() + "() - expecting " + method.getParameterTypes().length +
                    ", found " + parameters.size() + ".");
        }

        List<Parameter> defaultValueNames = collectDefaultValueNames(classDefaultValueNamesAnnotation, methodDefaultValueNamesAnnotation);

        return new CallConfiguration(server, resolvedProject, resolvedKey, resolvedApiPackage,
                name, parameters, classLevelDefaults, methodLevelDefaults, defaultValueNames);
    }

    private static void validateServer(Server server) {
        try {
            new URL(server.serverUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed server URL: \"" + server.serverUrl+"\"");
        }

        if (server.gson == null) {
            throw new IllegalArgumentException("Gson parser cannot be null");
        }

        if (server.okHttpClient == null) {
            throw new IllegalArgumentException("OkHttpClient cannot be null");
        }
    }

    private static List<Parameter> collectDefaultValueNames(DefaultParameters classDefault, DefaultParameters methodDefault) {
        List<Parameter> defaultValueNames = new ArrayList<>();
        defaultValueNames.addAll(getParameters(classDefault));
        defaultValueNames.addAll(getParameters(methodDefault));
        return defaultValueNames;
    }

    private static List<Parameter> getParameters(DefaultParameters dp) {
        String[] strings = dp != null ? dp.value() : EMPTY;
        List<Parameter> list = new ArrayList<>();
        for (String s : strings) {
            list.add(new Parameter(s));
        }
        return list;
    }

    private static String collectRemoteMethodName(Method method) {
        Named methodNameAnnotation = method.getAnnotation(Named.class);
        return (methodNameAnnotation != null) ? methodNameAnnotation.value() : method.getName();
    }

    private static List<Parameter> collectAnnotatedParameters(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        List<Parameter> parameters = new ArrayList<>();
        for (Annotation[] annotations : parameterAnnotations) {
            String name = null;
            boolean urlEncoded = false;
            boolean required = false;
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (Named.class.equals(annotationType)) {
                    name = ((Named) annotation).value();
                } else if (UrlEncoded.class.equals(annotationType)) {
                    urlEncoded = true;
                } else if (Required.class.equals(annotationType)) {
                    required = true;
                }
            }
            if (name != null) {
                parameters.add(new Parameter(name, urlEncoded, required));
            }
        }
        return parameters;
    }

    private static <A> String fromAnnotation(AnnotationValueExtractor<A> extractor, String builderValue, A methodAnnotation, A classAnnotation, String errorMessage) {
        String classValue = classAnnotation != null ? extractor.getValue(classAnnotation) : null;
        String methodValue = methodAnnotation != null ? extractor.getValue(methodAnnotation) : null;
        String resolvedValue = notNullOrEmpty(builderValue) ? builderValue : notNullOrEmpty(methodValue) ? methodValue : notNullOrEmpty(classValue) ? classValue : null;
        if (resolvedValue == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        return resolvedValue;
    }

    private static boolean notNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private interface AnnotationValueExtractor<A> {
        String getValue(A a);
    }
}
