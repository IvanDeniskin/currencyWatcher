package com.development.moksha.currencywatcher.ui;

import com.development.moksha.currencywatcher.data.RatesModel;
import com.development.moksha.currencywatcher.data.Rate;
import com.development.moksha.currencywatcher.data.local.ModelListener;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;


public class Presenter implements IPresenter {
    IView mView;
    RatesModel mData;

    public Presenter(MainActivity activity){
        mData = new ViewModelProvider(activity).get(RatesModel.class);
        mView = activity;
    }

    @Override
    public void init() {
        mView.showLoading();
        mData.update(listener);
    }

    @Override
    public void dateChanged(Calendar date) {
        mView.showLoading();
        mData.updateByDate(listener, date);
    }


    ModelListener listener = new ModelListener() {
        @Override
        public void onResponse(List<Rate> result) {
            mView.update(result);
            mView.hideLoading();
        }

        @Override
        public void onError(String error) {
            mView.hideLoading();
            mView.showErrorMessage("Network connection error: " + error);
        }
    };

}
