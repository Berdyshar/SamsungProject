package com.example.samsungprojectlanglearner;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TranslateService {

    @FormUrlEncoded
    @POST("translate")
    Call<TranslationResponse> translateText(
            @Field("key") String apiKey,
            @Field("text") String text,
            @Field("lang") String lang
    );
}
