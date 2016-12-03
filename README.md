# RxRapidApi
[![Build Status](https://travis-ci.org/psh/RxRapidApi.svg?branch=master)](https://travis-ci.org/psh/RxRapidApi) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) 

[RapidApi](https://www.rapidapi.com/) with a sprinkle of [RxAndroid](https://github.com/ReactiveX/RxAndroid) and just a touch of [Retrofit](https://github.com/square/retrofit) inspired syntax.

## Download

Files will soon be published on **jcenter** and available via Gradle in due course.  Watch this space!

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
Then later you call the service as if its just a local method.  Multiple services can be combined, responses transformed and all the other wonderful Rx goodness!

```java
// You will want to create the service interface and hold on to it
SpotifyApi serviceApi = RxRapidApiBuilder.from(SpotifyApi.class);
.
.
.
// Make the service call
Observable<Map<String, Object>> observable = serviceApi.searchAlbums("panic at the disco", "", "", "");
```
Remember, you will want to subscribe in order to actually execute the call (and run it on a background thread if you're on Android) but this is all standard Rx.

```java
observable
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
### Use ```BuildConfig```
While its not an absolute requirement, I like to pull configuration details like the API key and project into my ```build.gradle``` so that they are easy to change for different build variants, and it gets rid of hard-coded strings in the interfaces.
```java
    defaultConfig {
        .
        .
        .
        buildConfigField "String", "PROJECT", "\"<your project name>\""
        buildConfigField "String", "API_KEY", "\"<your api key>\""
    }
```
### ```@Application``` annotation

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

### ```@ApiPackage```

The ```@ApiPackage``` annotation can also be specified at the class level to reduce noise on a given interface.  For example

```java
@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
@ApiPackage("HackerNews")
public interface HackerNewsApi {
    Observable<Map<String, Object>> getNewStories();

    Observable<Map<String, Object>> getBestStories();

    Observable<Map<String, Object>> getAskStories();

    Observable<Map<String, Object>> getShowStories();

    Observable<Map<String, Object>> getJobStories();

    Observable<Map<String, Object>> getUpdates();

    Observable<Map<String, Object>> getUser(@Named("username") String username);

    Observable<Map<String, Object>> getItem(@Named("itemId") String username);
}
```
### Method parameters

The method parameter names are there for convenience and have no bearing on the parameters that are passed through to RapidApi.
Each parameter needs an ```@Named``` annotation.  They can be specified in any order in the interface.  Optionally parameters
can be marked ```@UrlEncoded``` if the service you are trying to call requires data in this format.  For example

```java
@Application(project = BuildConfig.PROJECT, key = BuildConfig.API_KEY)
public interface ZillowApi {

    @ApiPackage("Zillow")
    Observable<Map<String, Object>> getSearchResults(
            @Named("rentzestimate") String rentEstimate,
            @Named("zwsId") String zillowWebServiceId,
            @Named("address") @UrlEncoded String address,
            @Named("citystatezip") String cityStateZip
    );

}
```

# License
    Copyright 2016 Paul S Hawke

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
