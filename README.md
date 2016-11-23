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

# License
>    Copyright 2016 Paul S Hawke
>
>   Licensed under the Apache License, Version 2.0 (the "License");
>   you may not use this file except in compliance with the License.
>   You may obtain a copy of the License at
>
>       http://www.apache.org/licenses/LICENSE-2.0
>
>   Unless required by applicable law or agreed to in writing, software
>   distributed under the License is distributed on an "AS IS" BASIS,
>   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>   See the License for the specific language governing permissions and
>   limitations under the License.
