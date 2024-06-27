package com.example.weather_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app.Activitis.FutureActivity;
import com.example.weather_app.Activitis.Weather;
import com.example.weather_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Weather> weatherList;
    private OnItemClickListener listener;

    public CustomAdapter(Context context, ArrayList<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    public interface OnItemClickListener {
        void onItemClick(Weather weather);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_future, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.txtDay.setText(weather.getDay());
        holder.statusTxt.setText(weather.getStatus());
        holder.templowTxt.setText(weather.getTempMin() + "°C");
        holder.temphighTxt.setText(weather.getTempMax() + "°C");
        Picasso.get().load("https://openweathermap.org/img/wn/" + weather.getIcon() + ".png").into(holder.pic);

        // Bắt sự kiện nhấn vào từng mục trong RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(weather);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDay, statusTxt, templowTxt, temphighTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txtDay);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            templowTxt = itemView.findViewById(R.id.templowTxt);
            temphighTxt = itemView.findViewById(R.id.temphighTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
