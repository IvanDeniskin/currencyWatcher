package com.development.moksha.currencywatcher.data;

import android.app.Application;

import com.development.moksha.currencywatcher.config.ConfigData;
import com.development.moksha.currencywatcher.data.local.DatabaseAdapter;
import com.development.moksha.currencywatcher.data.local.IDatabaseAdapter;
import com.development.moksha.currencywatcher.data.local.ModelListener;
import com.development.moksha.currencywatcher.data.network.ExchangeApi;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import androidx.lifecycle.AndroidViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatesModel extends AndroidViewModel {
    Retrofit mRetrofit;
    ExchangeApi mApi;
    IDatabaseAdapter mDatabase;
    LinkedList<Rate> rates;

    public RatesModel(Application application) {
        super(application);
        mDatabase = new DatabaseAdapter(application);
        mRetrofit = new Retrofit.Builder().baseUrl(ConfigData.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApi = mRetrofit.create(ExchangeApi.class);
        rates = new LinkedList<>();
    }

    public  void update(final ModelListener listener){
        if(rates.isEmpty()) {
            mApi.getLatestRx().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<JsonObject>() {
                        @Override
                        public void onSuccess(JsonObject value) {
                            rates.clear();
                            Set<Map.Entry<String, JsonElement>> set = value.getAsJsonObject("rates").entrySet();

                            for (Map.Entry entry : set) {
                                Rate rate = new Rate();
                                rate.setCurrency(entry.getKey().toString());
                                rate.setValue(Float.valueOf(entry.getValue().toString()));
                                rates.add(rate);
                            }
                            mDatabase.updateRatesList(rates).subscribeOn(Schedulers.io()).subscribe();
                            listener.onResponse(rates);
                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onError(e.toString());
                            mDatabase.getRatesList()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(list -> {
                                        listener.onResponse(list);});
                        }
                    });
        }
        else
            listener.onResponse(rates);
    }

    public void updateByDate(final ModelListener listener, Calendar date){
        mApi.getRatesByDate(convertDate(date)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject value) {
                        rates.clear();
                        Set<Map.Entry<String,JsonElement>> set = value.getAsJsonObject("rates").entrySet();

                        for(Map.Entry entry : set)
                        {
                            Rate rate = new Rate();
                            rate.setCurrency(entry.getKey().toString());
                            rate.setValue(Float.valueOf(entry.getValue().toString()));
                            rates.add(rate);
                        }
                        listener.onResponse(rates);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.toString());
                    }
                });
    }

    private String convertDate(Calendar date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return formatter.format(date.getTime());
    }




}
