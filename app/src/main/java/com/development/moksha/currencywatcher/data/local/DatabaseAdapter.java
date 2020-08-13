package com.development.moksha.currencywatcher.data.local;

import android.content.Context;

import com.development.moksha.currencywatcher.data.Rate;

import java.util.List;

import androidx.room.Room;
import io.reactivex.Completable;
import io.reactivex.Single;

public class DatabaseAdapter implements IDatabaseAdapter{

    private RatesDao mDao;

    public DatabaseAdapter(Context appContext){
        RatesDatabase db = Room.databaseBuilder(appContext, RatesDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
        mDao = db.ratesDao();
    }

    @Override
    public Single<List<Rate>> getRatesList(){
        return mDao.getRates();
    }

    @Override
    public Completable updateRatesList(List<Rate> rates){
        return mDao.insertRates(rates);
    }
}
