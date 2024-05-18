package com.example.samsungprojectlanglearner.data;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TranslateService {
    @FormUrlEncoded
    @POST("language/translate/v2")
    Call<TranslationResponse> translateText(
            @Field("q") String text,
            @Field("source") String sourceLanguage,
            @Field("target") String targetLanguage,
            @Field("key") String apiKey
    );
}
