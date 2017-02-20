package com.gatebuzz.rapidapi.rx.generator;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;
import com.gatebuzz.rapidapi.rx.utils.SuccessMapper;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.Map;

public class Indexer {

    private final Github github;

    public static void main(String[] args) {
        Indexer indexer = new Indexer();
        indexer.index();
    }

    public Indexer() {
        github = new RxRapidApiBuilder()
                .application("RxRapidApi_Demo", "e21f74d4-1cb0-4476-92fd-a81fb29d6fa0")
                .defaultValue("accessToken", System.getProperty("github.access.token"))
                .endpoint(Github.class)
                .build();
    }

    public void index() {
        github.getOrganizationRepositories("RapidSoftwareSolutions", "public", null, null)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate())
                .flatMap(Observable::from)
                .filter(stringObjectMap -> ((String) stringObjectMap.get("name")).toLowerCase().startsWith("marketplace"))
                .flatMap(m -> github.getTree("RapidSoftwareSolutions", (String) m.get("name"), "master", "1"))
                .doOnNext(m -> System.out.println("m = " + m))
                .map(new SuccessMapper<Map<String, Object>>())
                .flatMap(m -> Observable.from(((List<Map<String, Object>>) m.get("tree"))))
                .filter(m -> ((String)m.get("path")).endsWith("metadata.json"))
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(Map<String, Object> stringObjectMap) {
                        System.out.println("stringObjectMap = " + stringObjectMap);
                    }
                });
    }
}
