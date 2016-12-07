package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;

import java.util.List;
import java.util.Map;

import rx.Observable;

@SuppressWarnings("unchecked")
public class ExampleApp {

    public static void main(String[] args) {
        System.out.println("Calling RapidApi method from Java, snagging top 5 news stories");

        HackerNewsApi api = RxRapidApiBuilder.from(HackerNewsApi.class);

        api.getNewStories()
                .flatMap(r -> Observable.from((List<Double>) r.get("success")))
                .take(5)
                .map(Double::longValue)
                .flatMap(api::getItem)
                .map(r -> new Story((Map<String, Object>) r.get("success")))
                .subscribe(System.out::println);
    }

}
