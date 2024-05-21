package com.example.samsungprojectlanglearner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslationResponse {

    @SerializedName("text")
    private List<String> translatedText;

    public List<String> getTranslatedText() {
        return translatedText;
    }
}
