package com.example.dicitionary_01;

import org.litepal.crud.DataSupport;

public class Words extends DataSupport {
    private String word;
    private String pronunciation;
    private String translate;
    public Words(String word,String pronunciation,String translate){
        this.word = word;
        this.pronunciation = pronunciation;
        this.translate = translate;
    }


    public void setPronunciation(String pronunciation){
        this.pronunciation = pronunciation;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }


    public String getTranslate() {
        return translate;
    }

    public String getWord() {
        return word;
    }

}
