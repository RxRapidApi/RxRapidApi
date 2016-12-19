package com.gatebuzz.rapidapi.rx.internal;

class Pair<A, B> {
    static final String TYPE_DATA = "data";
    static final String TYPE_FILE = "file";

    final A first;
    final B second;

    private Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static Pair<String,String> data(String value) {
        return new Pair<>(TYPE_DATA, value);
    }

    public static Pair<String,String> file(String value) {
        return new Pair<>(TYPE_FILE, value);
    }
}
