package com.development.moksha.currencywatcher.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.moksha.currencywatcher.R;
import com.development.moksha.currencywatcher.config.ConfigData;
import com.development.moksha.currencywatcher.data.Rate;
import com.squareup.picasso.Picasso;

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
            Picasso.with(holder.ivIcon.getContext())
                    .load(getUrlFromCurrencyName(data.get(position).getCurrency()))
                    .transform(new RoundTransform())
                    .into(holder.ivIcon);
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
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvValue = itemView.findViewById(R.id.tvValue);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    String getUrlFromCurrencyName(String currency){
        String country = currency.substring(0,2).toLowerCase();
        String url = ConfigData.ICONS_ENPOINT_PRE + country + ConfigData.ICONS_ENDPOINT_POST;
        return url;
    }

}
