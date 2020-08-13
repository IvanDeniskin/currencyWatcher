package com.development.moksha.currencywatcher.ui;

import com.development.moksha.currencywatcher.data.Rate;

import java.util.List;

public interface IView {
    void showLoading();
    void hideLoading();
    void update(List<Rate> data);
    void showErrorMessage(final String message);

}
