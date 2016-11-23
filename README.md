# RxRapidApi
RxAndroid + RapidApi with a sprinkle of Retrofit style flavoring.

```java
    defaultConfig {
        .
        .
        .
        buildConfigField "String", "PROJECT", "\"<your project name>\""
        buildConfigField "String", "API_KEY", "\"<your api key>\""
    }
```

```java
@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
public interface SpotifyApi {

    @ApiPackage("SpotifyPublicAPI")
    Observable<Map<String, Object>> searchAlbums(
            @Named("query") String query,
            @Named("market") String market,
            @Named("limit") String limit,
            @Named("offset") String offset
    );

}
```

```java
RxRapidApiBuilder.from(SpotifyApi.class).searchAlbums("panic at the disco", "", "", "")
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Map<String, Object>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof FailedCallException) {
                    Map<String, Object> response = ((FailedCallException) e).getResponse();
                    Log.e("Example", "Call failed: " + response);
                } else {
                    Log.e("Example", "Call failed badly.", e);
                }
            }

            @Override
            public void onNext(Map<String, Object> response) {
                Log.e("Example", "Call success: " + response);
                Object success = response.get("success");
                Log.e("Example", "Success Key Type = " + success.getClass().getSimpleName());
            }
        });
```