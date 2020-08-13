package com.development.moksha.currencywatcher.data.local;

import com.development.moksha.currencywatcher.data.Rate;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Single;
import io.reactivex.Completable;

@Dao
public interface RatesDao {

    @Query("SELECT * FROM rates")
    Single<List<Rate>> getRates();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRates(List<Rate> rates);
}
