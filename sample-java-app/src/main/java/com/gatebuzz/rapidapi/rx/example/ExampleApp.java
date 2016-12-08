package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;

import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rx.Observable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

public class ExampleApp extends Application {

    private HackerNewsApi hackerNewsApi;

    @Override
    public void start(Stage primaryStage) {
        hackerNewsApi = RxRapidApiBuilder.from(HackerNewsApi.class);

        Button btn = new Button();
        btn.setText("Load hacker news");
        btn.setOnAction(event -> tryTheApi());

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("RxRapidApi Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void tryTheApi() {
        System.out.println("Calling RapidApi method from Java, snagging top 5 news stories");

        hackerNewsApi.getNewStories()
                .subscribeOn(Schedulers.newThread())
                .flatMap(map -> Observable.from((List<Double>) map.get("success")))
                .take(5)
                .map(Double::longValue)
                .flatMap(id -> hackerNewsApi.getItem(id))
                .map(r -> new Story((Map<String, Object>) r.get("success")))
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(System.out::println);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
