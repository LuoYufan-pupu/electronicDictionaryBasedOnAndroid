package com.example.dicitionary_01;

import org.litepal.crud.DataSupport;

public class NewWords extends DataSupport {
    private String time;
    private String word;

    public NewWords(String time, String word) {
        this.time = time;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
