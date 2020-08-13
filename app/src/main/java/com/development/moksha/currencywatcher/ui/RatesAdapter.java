package com.development.moksha.currencywatcher.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.moksha.currencywatcher.R;
import com.development.moksha.currencywatcher.data.Rate;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder> {

    List<Rate> data;

    void update(List<Rate> data_){
        data = data_;
        notifyDataSetChanged();
    }

    public RatesAdapter(){
        data = new LinkedList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(data != null && position >= 0 && position < data.size()) {
            holder.tvName.setText(data.get(position).getCurrency());
            holder.tvValue.setText(String.valueOf(data.get(position).getValue()));
        }
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }

}
