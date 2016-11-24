# RxRapidApi
RapidApi with a sprinkle of RxAndroid + Retrofit style flavoring.

To save embedding the application and API key in every service interface, I like to pull them out into the build config:
```java
    defaultConfig {
        .
        .
        .
        buildConfigField "String", "PROJECT", "\"<your project name>\""
        buildConfigField "String", "API_KEY", "\"<your api key>\""
    }
```
## Example Service
Calling a service is as easy as defining a Java interface with a few annotations:

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
Then later you make the service call.  Multiple services can be combined, responses transformed and all the other wonderful Rx goodness!

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

## Configuration

### ````@Application``` annotation

This annotation is required either at the class level (on the ```interface``` declaration) or on a method.  Note: if the annotation is present in both locations, the method level annotation will take precedence.  The method level annotation means you can mix & match applications and api keys in the same interface (although that is not recommended).

For example the following 2 declarations are equivalent:

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
and
```java
public interface SpotifyApi {
    @ApiPackage("SpotifyPublicAPI")
    @Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
    Observable<Map<String, Object>> searchAlbums(
            @Named("query") String query,
            @Named("market") String market,
            @Named("limit") String limit,
            @Named("offset") String offset
    );
}
```
### Method names

The ```@ApiPackage``` and the method name equate to the "package" and "block" parameters RapidApi needs to call your API.  If you want to
override this default behavior you can specify an ```@Named``` annotation on the Java interface method.  For example

```java
@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
public interface SpotifyApi {
    @ApiPackage("SpotifyPublicAPI")
    @Named("searchAlbums")
    Observable<Map<String, Object>> doThatSpotifyThing(
            @Named("query") String query,
            @Named("market") String market,
            @Named("limit") String limit,
            @Named("offset") String offset
    );
}
```

### Method parameters

The method parameter names are there for convenience and have no bearing on the parameters that are passed through to RapidApi.
Each parameter needs an ```@Named``` annotation.  They can be specified in any order in the interface.


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
