package com.development.moksha.currencywatcher.data.local;

import com.development.moksha.currencywatcher.data.Rate;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Rate.class}, version = 1)
public abstract class RatesDatabase extends RoomDatabase {
    public abstract RatesDao ratesDao();
}

