package com.example.weather_app.Activitis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather_app.Adapters.CustomAdapter;
import com.example.weather_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FutureActivity extends AppCompatActivity {

    String tenthanhpho = "";
    ImageView imgBack, pic;
    RecyclerView recyclerView;
    TextView txtCityName, txtTemp, txtDay, txtRain, txtWind, txtHumidity, txtday;
    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArray;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);

        pic =findViewById(R.id.pic);
        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.list);
        txtCityName = findViewById(R.id.txtCityName);
        txtTemp = findViewById(R.id.txtTemp);
        txtDay = findViewById(R.id.txtDay);
        txtRain = findViewById(R.id.txtRain);
        txtWind = findViewById(R.id.txtWind);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtday = findViewById(R.id.txtday);
        weatherArray = new ArrayList<>();
        customAdapter = new CustomAdapter(this, weatherArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);

        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        //Log.d("ketqua", "Dữ liệu truyền qua: " + city);
        if (city == null || city.equals("")) {
            tenthanhpho = "Saigon";
        } else {
            tenthanhpho = city;
        }
        Get7DaysData(tenthanhpho);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void Get7DaysData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=56&appid=48d34576ad87840b7f38187a804d0101";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txtCityName.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");

                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                                String formattedDate = simpleDateFormat.format(date);
                                txtDay.setText(formattedDate);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                                String temp = jsonObjectTemp.getString("temp");
                                String max = jsonObjectTemp.getString("temp_max");
                                String min = jsonObjectTemp.getString("temp_min");
                                String doam = jsonObjectTemp.getString("humidity");

                                Double a = Double.valueOf(max);
                                String Nhietdomax = String.valueOf(a.intValue());
                                Double b = Double.valueOf(min);
                                String Nhietdomin = String.valueOf(b.intValue());
                                Double c = Double.valueOf(temp);
                                String Nhietdo = String.valueOf(c.intValue());
                                txtTemp.setText(Nhietdo + "°C");
                                txtHumidity.setText(doam+"%");

                                JSONObject jsonObjectWind = jsonObjectList.getJSONObject("wind");
                                String gio = jsonObjectWind.getString("speed");
                                txtWind.setText(gio+"m/s");

                                JSONObject jsonObjectRain = jsonObjectList.getJSONObject("clouds");
                                String mua = jsonObjectRain.getString("all");
                                txtRain.setText(mua+"%");

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                weatherArray.add(new Weather(formattedDate, status, icon, Nhietdomax, Nhietdomin, Nhietdo));
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            //Log.e("ketqua", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("ketqua", "Error: " + error.getMessage());
                        Toast.makeText(FutureActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
}
