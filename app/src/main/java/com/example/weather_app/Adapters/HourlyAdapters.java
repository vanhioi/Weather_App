package com.example.weather_app.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.weather_app.Activitis.FutureActivity;
import com.example.weather_app.Activitis.MainActivity;
import com.example.weather_app.Domains.Hourly;
import com.example.weather_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyAdapters extends RecyclerView.Adapter<HourlyAdapters.viewHolder> {
    private ArrayList<Hourly> items;
    private Context context;


    public HourlyAdapters(ArrayList<Hourly> items, MainActivity mainActivity) {
        this.items = items;
        this.context = context;
    }

    public HourlyAdapters(ArrayList<Hourly> items) {
    }


    @NonNull
    @Override
    public HourlyAdapters.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_hourly, parent, false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapters.viewHolder holder, int position) {
        Hourly Items = items.get(position);
        holder.dayTxt.setText(Items.getDay());
        holder.dayofweek.setText(Items.getDayofweek());
        holder.hourTxt.setText(Items.getHour());
        holder.tempTxt.setText(Items.getTemp()+"°C");

        Picasso.get().load("https://openweathermap.org/img/wn/" + Items.getPicPath() + ".png").into(holder.pic);

        int drawableResourceId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getPicPath(), "drawable", holder.itemView.getContext().getPackageName());

        //holder.itemView.setBackgroundColor(android.graphics.Color.parseColor(Items.getColor()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FutureActivity.class);
                intent.putExtra("dayOfWeek", Items.getDayofweek());
                intent.putExtra("day", Items.getDay());
                intent.putExtra("time", Items.getHour());
                intent.putExtra("temperature", Items.getTemp());
                intent.putExtra("icon", Items.getPicPath());
                //intent.putExtra("status", Items.getstatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView hourTxt, tempTxt, dayTxt, dayofweek;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayTxt=itemView.findViewById(R.id.dayTxt);
            dayofweek=itemView.findViewById(R.id.dayofweekTxt);
            hourTxt=itemView.findViewById(R.id.hourTxt);
            tempTxt=itemView.findViewById(R.id.tempTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}


