package com.gatebuzz.rapidapi.rx;

import com.gatebuzz.rapidapi.rx.internal.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unchecked"})
public class RxRapidApiBuilder {

    public static final Type MAP_STRING_TO_OBJECT = new TypeToken<Map<String, Object>>() {
    }.getType();

    private final CallHandlerFactory callHandlerFactory;
    private final Map<String, String> classLevelDefaults = new HashMap<>();
    private final Map<String, Map<String, String>> methodLevelDefaults = new HashMap<>();
    private OkHttpClient okHttpClient;
    private String server;
    private Gson gson;
    private Class<?> interfaceClass;
    private String project;
    private String key;
    private String apiPackage;

    public RxRapidApiBuilder() {
        this(new CallHandlerFactory() {
            @Override
            public <T> T newInstance(Class<?> interfaceClass, Map<String, CallConfiguration> configurationMap) {
                return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                        new Class[]{interfaceClass}, new CallHandler(configurationMap));
            }
        });
    }

    RxRapidApiBuilder(CallHandlerFactory callHandlerFactory) {
        this.callHandlerFactory = callHandlerFactory;
        this.server = "https://rapidapi.io/connect";
        this.okHttpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    public static <I> I from(Class<I> interfaceClass) {
        return new RxRapidApiBuilder().endpoint(interfaceClass).build();
    }

    public RxRapidApiBuilder endpoint(Class<?> endpoint) {
        this.interfaceClass = endpoint;
        return this;
    }

    /**
     * Configure the server where Rapid API is deployed.
     *
     * @param server the server where Rapid API is deployed
     * @return this builder
     */
    public RxRapidApiBuilder server(String server) {
        this.server = server;
        return this;
    }

    /**
     * Configure the GSon parser used for service calls.
     *
     * @param gson the parser to use
     * @return this builder
     */
    public RxRapidApiBuilder gson(Gson gson) {
        this.gson = gson;
        return this;
    }

    /**
     * Configure the OkHttpClient used for service calls.
     *
     * @param okHttpClient the client to use
     * @return this builder
     */
    public RxRapidApiBuilder okHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * Override the service interface <code>@Application</code> annotation to provide the
     * Rapid API <code>project</code> and <code>api key</code>.
     *
     * @param project the Rapid API <code>project</code>
     * @param key     the Rapid API <code>api key</code>
     * @return this builder
     * @see Application
     */
    public RxRapidApiBuilder application(String project, String key) {
        this.project = project;
        this.key = key;
        return this;
    }

    /**
     * Override the service interface <code>@ApiPackage</code> annotation to provide the
     * Rapid API package.
     *
     * @param apiPackage the Rapid API api package
     * @return this builder
     * @see ApiPackage
     */
    public RxRapidApiBuilder apiPackage(String apiPackage) {
        this.apiPackage = apiPackage;
        return this;
    }

    /**
     * Provide the value for a documented default parameter.
     *
     * @param key   the parameter name
     * @param value the parameter value
     * @return this builder
     * @see DefaultParameters
     */
    public RxRapidApiBuilder defaultValue(String key, String value) {
        classLevelDefaults.put(key, value);
        return this;
    }

    /**
     * Provide the value for a documented default parameter, bound to a particular service method
     * on the interface.
     *
     * @param method the scope for this default parameter value
     * @param key    the parameter name
     * @param value  the parameter value
     * @return this builder
     * @see DefaultParameters
     */
    public RxRapidApiBuilder defaultValue(String method, String key, String value) {
        if (!methodLevelDefaults.containsKey(method)) {
            methodLevelDefaults.put(method, new HashMap<>());
        }
        Map<String, String> defaults = methodLevelDefaults.get(method);
        defaults.put(key, value);
        return this;
    }

    /**
     * Provide values for documented default parameters.
     *
     * @param defaultValues map of key / value pairs containing the default parameter values.
     * @return this builder
     * @see DefaultParameters
     */
    public RxRapidApiBuilder defaultValues(HashMap<String, String> defaultValues) {
        classLevelDefaults.putAll(defaultValues);
        return this;
    }

    /**
     * Provide values for documented default parameters, bound to a particular service method
     * on the interface.
     *
     * @param method        the service method
     * @param defaultValues map of key / value pairs containing the default parameter values.
     * @return this builder
     * @see DefaultParameters
     */
    public RxRapidApiBuilder defaultValues(String method, HashMap<String, String> defaultValues) {
        if (!methodLevelDefaults.containsKey(method)) {
            methodLevelDefaults.put(method, new HashMap<>());
        }
        Map<String, String> defaults = methodLevelDefaults.get(method);
        defaults.putAll(defaultValues);
        return this;
    }

    /**
     * @param <T> The service interface type
     * @return a fully configured service interface
     */
    public <T> T build() {
        Application applicationAnnotation = interfaceClass.getAnnotation(Application.class);
        ApiPackage apiPackageAnnotation = interfaceClass.getAnnotation(ApiPackage.class);
        DefaultParameters defaultParametersAnnotation = interfaceClass.getAnnotation(DefaultParameters.class);

        Map<String, CallConfiguration> callConfigurationMap = new HashMap<>();
        for (Method method : interfaceClass.getMethods()) {
            Application methodAppAnnotation = method.getAnnotation(Application.class);
            ApiPackage methodApiPackageAnnotation = method.getAnnotation(ApiPackage.class);
            DefaultParameters methodDefaultParametersAnnotation = method.getAnnotation(DefaultParameters.class);

            CallConfiguration configuration = CallConfigurationFactory.newInstance(
                    applicationAnnotation, methodAppAnnotation,
                    apiPackageAnnotation, methodApiPackageAnnotation,
                    method, project, key, apiPackage, classLevelDefaults,
                    getMethodLevelDefaultsOrEmpty(method),
                    defaultParametersAnnotation, methodDefaultParametersAnnotation,
                    new CallConfiguration.Server(server, okHttpClient, gson),
                    configureResponseProcessor(method));

            callConfigurationMap.put(method.getName(), configuration);
        }

        return callHandlerFactory.newInstance(interfaceClass, callConfigurationMap);
    }

    private ResponseProcessor configureResponseProcessor(Method method) {
        Type declaredReturnType = method.getGenericReturnType();
        if (declaredReturnType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) declaredReturnType).getActualTypeArguments();
            if (typeArguments != null && typeArguments.length > 0) {
                if (MAP_STRING_TO_OBJECT.equals(typeArguments[0])) {
                    return KeyValueMapProcessor.success();
                } else {
                    return new CustomTypeResponseProcessor(typeArguments[0]);
                }
            }
        }
        throw new IllegalArgumentException("Missing parameterized type on " + method.getName() + "().");
    }

    private Map<String, String> getMethodLevelDefaultsOrEmpty(Method method) {
        Map<String, String> map = methodLevelDefaults.get(method.getName());
        return map != null ? map : Collections.emptyMap();
    }
}
