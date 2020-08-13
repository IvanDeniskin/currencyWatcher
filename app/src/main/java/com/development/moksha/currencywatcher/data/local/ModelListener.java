package com.development.moksha.currencywatcher.data.local;

import com.development.moksha.currencywatcher.data.Rate;

import java.util.List;

public interface ModelListener {
    void onResponse(List<Rate> result);
    void onError(final String error);
}
