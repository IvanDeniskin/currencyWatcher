package com.development.moksha.currencywatcher.data.local;

import com.development.moksha.currencywatcher.data.Rate;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface IDatabaseAdapter {
    Single<List<Rate>> getRatesList();
    Completable updateRatesList(List<Rate> rates);
}
