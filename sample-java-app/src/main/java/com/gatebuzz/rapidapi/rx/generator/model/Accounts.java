package com.gatebuzz.rapidapi.rx.generator.model;

import java.util.ArrayList;
import java.util.List;

public class Accounts {
    private String domain;
    private List<String> credentials = new ArrayList<>();

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<String> credentials) {
        this.credentials = credentials;
    }

}
