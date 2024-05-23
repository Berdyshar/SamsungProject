package com.example.samsungprojectlanglearner;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Translation {

    private static final String BASE_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    private static final String API_KEY = "AQVN3ID6r-JfkNZecKZxfZJunTaah7fK69mY1HGp";

    private final TranslateService translateService;

    public Translation() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        translateService = retrofit.create(TranslateService.class);
    }

    public void translate(String text, String lang, Callback<List<String>> callback) {
        translateService.translateText(API_KEY, text, lang).enqueue(new retrofit2.Callback<TranslationResponse>() {
            @Override
            public void onResponse(@NonNull Call<TranslationResponse> call, @NonNull Response<TranslationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    callback.onSuccess(response.body().getTranslatedText());
                } else {
                    callback.onError(new Exception("Failed to translate text"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TranslationResponse> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface Callback<T> {
        void onSuccess(T result);

        void onError(Throwable t);
    }
}
