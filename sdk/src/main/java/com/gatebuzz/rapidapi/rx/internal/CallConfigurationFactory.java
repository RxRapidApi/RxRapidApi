package com.gatebuzz.rapidapi.rx.internal;

import com.gatebuzz.rapidapi.rx.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class CallConfigurationFactory {
    public static CallConfiguration newInstance(
            Application classApplicationAnnotation, Application methodApplicationAnnotation,
            ApiPackage classApiPackageAnnotation, ApiPackage methodApiPackageAnnotation,
            Method method, String project, String key, String apiPackage,
            Map<String, String> classLevelDefaults, Map<String, String> methodLevelDefaults,
            DefaultParameters classDefaultValueNamesAnnotation, DefaultParameters methodDefaultValueNamesAnnotation) {
        String resolvedProject = fromAnnotation(Application::project,
                project, methodApplicationAnnotation, classApplicationAnnotation,
                "Project name not found (check the @Application annotation).");

        String resolvedKey = fromAnnotation(Application::key,
                key, methodApplicationAnnotation, classApplicationAnnotation,
                "API key not found (check the @Application annotation).");

        String resolvedApiPackage = fromAnnotation(ApiPackage::value,
                apiPackage, methodApiPackageAnnotation, classApiPackageAnnotation,
                "API package not found - check the @ApiPackage " + "annotation on the interface or on " + method.getName() + "().");

        String name = collectRemoteMethodName(method);
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("API method name not found on " + method.getName() + "() " + "(check the @Named annotation on your interface method).");
        }

        List<String> parameters = collectParameterNames(method);
        if (parameters.size() != method.getParameterTypes().length) {
            throw new IllegalArgumentException("Incorrect number of @Named parameters on " + method.getName() + "() - expecting " + method.getParameterTypes().length + ", found " + parameters.size() + ".");
        }

        List<String> defaultValueNames = collectDefaultValueNames(classDefaultValueNamesAnnotation, methodDefaultValueNamesAnnotation);

        Set<String> urlEncodedParameters = collectUrlEncodedParameters(method, parameters);
        return new CallConfiguration(resolvedProject, resolvedKey, resolvedApiPackage,
                name.trim(), parameters, urlEncodedParameters,
                classLevelDefaults, methodLevelDefaults, defaultValueNames);
    }

    private static List<String> collectDefaultValueNames(DefaultParameters classDefaultValueNamesAnnotation,
                                                         DefaultParameters methodDefaultValueNamesAnnotation) {
        List<String> classDefaultValueNames = classDefaultValueNamesAnnotation != null ?
                Arrays.asList(classDefaultValueNamesAnnotation.value()) : Collections.emptyList();
        List<String> methodDefaultValueNames = methodDefaultValueNamesAnnotation != null ?
                Arrays.asList(methodDefaultValueNamesAnnotation.value()) : Collections.emptyList();
        List<String> defaultValueNames = new ArrayList<>();
        defaultValueNames.addAll(classDefaultValueNames);
        defaultValueNames.addAll(methodDefaultValueNames);
        return defaultValueNames;
    }

    private static String collectRemoteMethodName(Method method) {
        Named methodNameAnnotation = method.getAnnotation(Named.class);
        return (methodNameAnnotation != null) ? methodNameAnnotation.value() : method.getName();
    }

    private static List<String> collectParameterNames(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        List<String> parameters = new ArrayList<>();
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Named.class)) {
                    parameters.add(((Named) annotation).value().trim());
                }
            }
        }
        return parameters;
    }

    private static Set<String> collectUrlEncodedParameters(Method method, List<String> names) {
        Set<String> parameters = new HashSet<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(UrlEncoded.class)) {
                    parameters.add(names.get(i));
                }
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
