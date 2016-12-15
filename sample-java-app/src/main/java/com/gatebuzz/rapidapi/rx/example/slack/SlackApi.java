package com.gatebuzz.rapidapi.rx.example.slack;

import com.gatebuzz.rapidapi.rx.ApiPackage;
import com.gatebuzz.rapidapi.rx.DefaultParameters;
import com.gatebuzz.rapidapi.rx.Named;
import rx.Observable;

import java.io.File;
import java.util.Map;

@ApiPackage("Slack")
@DefaultParameters("token")
public interface SlackApi {

    Observable<Map<String, Object>> uploadFile(
            @Named("file") File upload,
            @Named("filename") String filename
    );

}
