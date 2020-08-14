package com.development.moksha.currencywatcher.ui;

import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.moksha.currencywatcher.R;
import com.development.moksha.currencywatcher.data.Rate;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends ComponentActivity implements IView {

    IPresenter mPresenter;
    RatesAdapter mAdapter;
    EditText etDate;
    Calendar mCalendar;
    private ProgressDialog progressDialog;
    final String etDateTextID = "etDateText";
    SwipeRefreshLayout mSwipeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new Presenter(this);
        RecyclerView rvRates = (RecyclerView)findViewById(R.id.rvRates);
        rvRates.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RatesAdapter();
        rvRates.setAdapter(mAdapter);
        mPresenter.init();
        mCalendar = new GregorianCalendar();
        etDate = (EditText)findViewById(R.id.etDate);
        etDate.addTextChangedListener(new DateWatcher(etDate));
        etDate.setOnEditorActionListener(onFinishListener);
        refreshDate();

        mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        prepareSwipeLayout();
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.dateChanged(mCalendar);
            }
        });
    }

    private void refreshDate(){
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH)+1;
        String text = (day > 9 ? String.valueOf(day) : ("0" + String.valueOf(day)))
                + (month > 9 ? String.valueOf(month) : ("0" + String.valueOf(month))) + mCalendar.get(Calendar.YEAR);
        etDate.setText(text);
    }


    @Override
    public void showLoading() {
        hideLoading();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void update(List<Rate> data) {
        mAdapter.update(data);
        if(mSwipeLayout.isRefreshing())
            mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void showErrorMessage(final String message){
        Snackbar.make(findViewById(R.id.rootLayout), message, Snackbar.LENGTH_LONG).show();
        if(mSwipeLayout.isRefreshing())
            mSwipeLayout.setRefreshing(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle data) {
        super.onSaveInstanceState(data);
        if(data != null)
            data.putString(etDateTextID,etDate.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle data){
        if(data != null)
            etDate.setText(data.getString(etDateTextID,""));
    }

    public void selectDate(View v) {
        new DatePickerDialog(MainActivity.this, d,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR,year);
            mCalendar.set(Calendar.MONTH,monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            refreshDate();
            mPresenter.dateChanged(mCalendar);

        }
    };

    void handleText(CharSequence text){
        String str = text.toString();
        try {
            int day = Integer.parseInt(str.substring(0, 2));
            int mon = Integer.parseInt(str.substring(3, 5));
            int year = Integer.parseInt(str.substring(6, 10));
            mCalendar.set(Calendar.YEAR,year);
            mCalendar.set(Calendar.MONTH,mon);
            mCalendar.set(Calendar.DAY_OF_MONTH,day);
            mPresenter.dateChanged(mCalendar);
        }
        catch (NumberFormatException e){
            showErrorMessage(getString(R.string.date_error));
        }
    }

    void prepareSwipeLayout(){
        //Hide busy indicator
        try{
            Field f = mSwipeLayout.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView)f.get(mSwipeLayout);
            img.setAlpha(0.0f);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    EditText.OnEditorActionListener onFinishListener = new EditText.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if(i == EditorInfo.IME_ACTION_DONE) {
                handleText(textView.getText());
                return false;
            }
            else
                return true;
        }
    };
}