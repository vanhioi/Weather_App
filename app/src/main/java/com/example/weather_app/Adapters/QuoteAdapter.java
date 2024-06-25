package com.example.weather_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app.R;

import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {
    private List<String> quotes;

    public QuoteAdapter(List<String> quotes) {
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public QuoteAdapter.QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteAdapter.QuoteViewHolder holder, int position) {
        String quote = quotes.get(position);
        holder.txtQuote.setText(quote);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }


    public static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuote;

        public QuoteViewHolder(View itemView) {
            super(itemView);
            txtQuote = itemView.findViewById(R.id.txtQuote);
        }
    }
}
