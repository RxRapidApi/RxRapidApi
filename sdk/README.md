# Getting Started

## Create an application on Rapid API

Jump over to [RapidApi](https://www.rapidapi.com/) and create a project - you will need the project name and the API key to define your first interface.

## Define a service API interface

Calling a service is as easy as defining a Java interface with a few annotations:

```java
@Application(project = "<your project name>", key = "<your api key>")
@ApiPackage("SpotifyPublicAPI")
public interface SpotifyApi {

    Single<Map<String, Object>> searchAlbums(
            @Named("query") @Required String query,
            @Named("market") String market,
            @Named("limit") String limit,
            @Named("offset") String offset
    );

}
```

You will want to create the service interface and hold on to it:

```java
SpotifyApi serviceApi = RxRapidApiBuilder.from(SpotifyApi.class);
```

Then when you want the data, you can make the service call:

```java
spotifyApi.searchAlbums("The Rolling Stones", "", "", "")
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Map<String, Object>>() {
            @Override
            public void call(Map<String, Object> result) {
                // Use the data
            }
        });
```

More documentation is available on the [wiki](https://github.com/RxRapidApi/RxRapidApi/wiki).