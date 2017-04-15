package com.gatebuzz.rapidapi.rx.example;

import com.gatebuzz.rapidapi.rx.RxRapidApiBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import com.gatebuzz.rapidapi.rx.example.hackernews.HackerNewsApi;
import com.gatebuzz.rapidapi.rx.example.hackernews.Story;
import com.gatebuzz.rapidapi.rx.example.slack.SlackApi;
import com.gatebuzz.rapidapi.rx.example.slack.SlackFileResponse;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@SuppressWarnings("unchecked")
public class ExampleApp extends Application {

    private HackerNewsApi hackerNewsApi;
    private SlackApi slackApi;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Application configured using annotations in the service interface.
        hackerNewsApi = RxRapidApiBuilder.from(HackerNewsApi.class);

        // Setup logging of requests + responses
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

				// Application absent from the service interface, allowing it to be shared.
        slackApi = new RxRapidApiBuilder()
                .application("RxRapidApi_Demo", "e21f74d4-1cb0-4476-92fd-a81fb29d6fa0")
                .endpoint(SlackApi.class)
                .defaultValue("token", System.getProperty("slack.api.key"))
                .okHttpClient(client)
                .build();

        Button btn1 = new Button();
        btn1.setText("Load hacker news");
        btn1.setOnAction(event -> tryTheHackerNewsApi());

        Button btn2 = new Button();
        btn2.setText("Upload Slack File");
        btn2.setOnAction(event -> tryTheSlackApi());

        VBox box = new VBox();
        box.getChildren().add(btn1);
        box.getChildren().add(btn2);

        StackPane root = new StackPane();
        root.getChildren().add(box);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("RxRapidApi Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void tryTheHackerNewsApi() {
        System.out.println("Calling RapidApi method from Java, snagging top 5 news stories");

        hackerNewsApi.getNewStories()
                .subscribeOn(Schedulers.newThread())
                .flatMap(Observable::fromIterable)
                .take(5)
                .map(Double::longValue)
                .flatMap(id -> hackerNewsApi.getItem(id))
                .map(r -> new Story((Map<String, Object>) r.get("success")))
                .observeOn(JavaFxScheduler.platform())
                .subscribe(story -> System.out.println("story = " + story));
    }

    private void tryTheSlackApi() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Upload File");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                slackApi.uploadFile(new FileInputStream(file), null, file.getName(), null, null, null)
                        .map(result -> (String)result.get("success"))
                        .map(SlackFileResponse::fromJson)
                        .map(sfr -> sfr.file)
                        .doOnNext(System.out::println)
                        .subscribe();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
