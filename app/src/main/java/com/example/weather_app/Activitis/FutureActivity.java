package com.example.weather_app.Activitis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FutureActivity extends AppCompatActivity {

    String tenthanhpho = "";
    ImageView imgBack, pic, backgroundImageView;
    RecyclerView recyclerView;
    TextView txtCityName, txtTemp, txtStatus, txtDay, txtRain, txtWind, txtHumidity, dayTxt;
    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArray;
    int currentBackgroundIndex = 0;
    int[] backgroundImages = {R.drawable.mua, R.drawable.night};


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);

        backgroundImageView = findViewById(R.id.backgroundImageView);
        pic = findViewById(R.id.pic);
        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.list);
        txtCityName = findViewById(R.id.txtCityName);
        txtTemp = findViewById(R.id.txtTemp);
        txtDay = findViewById(R.id.txtDay);
        txtRain = findViewById(R.id.txtRain);
        txtWind = findViewById(R.id.txtWind);
        txtHumidity = findViewById(R.id.txtHumidity);
        dayTxt = findViewById(R.id.dayTxt);
        txtStatus = findViewById(R.id.txtStatus);

        weatherArray = new ArrayList<>();
        customAdapter = new CustomAdapter(this, weatherArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);

        Intent intent = getIntent();
        String city = intent.getStringExtra("name");

        if (city == null || city.equals("")) {
            tenthanhpho = "Saigon";
        } else {
            tenthanhpho = city;
        }

        GetCurrentWeatherData(tenthanhpho);

        Get7DaysData(tenthanhpho);

        backgroundImageView.setImageResource(backgroundImages[currentBackgroundIndex]);

        //chỉnh thời gian giao diện background thay đổi sau 15s
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateBackgroundChange();
                handler.postDelayed(this, 15000);
            }
        }, 15000);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Weather weather) {
                updateWeatherDetails(weather);
            }
        });
    }

    //thay đổi background
    private void animateBackgroundChange() {
        currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundImages.length;
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(backgroundImageView, "alpha", 1f, 0f);
        fadeOut.setDuration(1000);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                backgroundImageView.setImageResource(backgroundImages[currentBackgroundIndex]);
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(backgroundImageView, "alpha", 0f, 1f);
                fadeIn.setDuration(1000);
                fadeIn.start();
            }
        });
        fadeOut.start();
    }

    private void updateWeatherDetails(Weather weather) {
        txtDay.setText(weather.getDay());
        txtTemp.setText(weather.getTemp() + "°C");
        txtRain.setText(weather.getRain() + "%");
        txtWind.setText(weather.getWind() + "m/s");
        txtHumidity.setText(weather.getHumidity() + "%");
        txtStatus.setText(weather.getStatus());
        Picasso.get().load("https://openweathermap.org/img/wn/" + weather.getIcon() + ".png").into(pic);
    }

    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(FutureActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=48d34576ad87840b7f38187a804d0101";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Chuyển đổi chuỗi json thành jsonOject
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");

                            long time = jsonObject.getLong("dt") * 1000;
                            Date date = new Date(time);
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE | dd-MM-yyyy", Locale.getDefault());
                            String formattedDate = sdf.format(date);
                            dayTxt.setText(formattedDate);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());

                            txtTemp.setText(Nhietdo + "°C");
                            txtHumidity.setText(doam + "%");
                            txtStatus.setText(status);

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio + "m/s");

                            JSONObject jsonObjectRain = jsonObject.getJSONObject("clouds");
                            String mua = jsonObjectRain.getString("all");
                            txtRain.setText(mua + "%");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FutureActivity.this, "Lỗi: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void Get7DaysData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=40&appid=48d34576ad87840b7f38187a804d0101";

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
                            weatherArray.clear();
                            for (int i = 0; i < jsonArrayList.length(); i += 8) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");

                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                                String formattedDate = simpleDateFormat.format(date);

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

                                JSONObject jsonObjectWind = jsonObjectList.getJSONObject("wind");
                                String gio = jsonObjectWind.getString("speed");

                                JSONObject jsonObjectRain = jsonObjectList.getJSONObject("clouds");
                                String mua = jsonObjectRain.getString("all");

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                weatherArray.add(new Weather(formattedDate, status, icon, Nhietdomax, Nhietdomin, Nhietdo, gio, mua, doam));
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("Error", "JSON Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error: " + error.getMessage());
                        Toast.makeText(FutureActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
}
