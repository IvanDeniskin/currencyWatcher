package com.development.moksha.currencywatcher.data.network;

import android.util.JsonReader;

import com.development.moksha.currencywatcher.data.Rate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import java.lang.annotation.Target;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExchangeApi {
    @GET("latest")
    Call<List<Rate>> getLatest();

    @GET("latest")
    Call<JsonObject> getLatestRaw();

    @GET("latest")
    Single<JsonObject> getLatestRx();

    @GET("{date}")
    Single<JsonObject> getRatesByDate(@Path("date") String date);
}
