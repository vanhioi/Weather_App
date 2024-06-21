package com.example.weather_app.Activitis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather_app.Adapters.HourlyAdapters;
import com.example.weather_app.Domains.Hourly;
import com.example.weather_app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    HourlyAdapters hourlyAdapters;
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
    ImageButton btnMap, btnUser, btnSearch;
    EditText edtSearch;
    TextView txtcity, txtcountry, txtNext, txtStatus, txtday, txtTemp, txtTempH, txtTempL, txtRain, txtWind, txtHumidity, tempTxt, hourTxt;
    ImageView imgIcon;
    String city = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMap = findViewById(R.id.btnMap);
        btnUser = findViewById(R.id.btnUser);
        txtcity = findViewById(R.id.txtcity);
        txtcountry = findViewById(R.id.txtcountry);
        txtNext  = findViewById(R.id.txtNext);
        txtStatus  = findViewById(R.id.txtStatus);
        txtday = findViewById(R.id.txtday);
        txtTemp = findViewById(R.id.txtTemp);
        txtTempH = findViewById(R.id.txtTempH);
        txtTempL = findViewById(R.id.txtTempL);
        txtRain = findViewById(R.id.txtRain);
        txtWind = findViewById(R.id.txtWind);
        txtHumidity = findViewById(R.id.txtHumidity);
        imgIcon = findViewById(R.id.imgIcon);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tempTxt=findViewById(R.id.tempTxt);
        hourTxt = findViewById(R.id.hourTxt);
        GetCurrentWeatherData("Saigon");
        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Get7DaysData("");
        /*// Nhận Intent và lấy dữ liệu city
        Intent intent = getIntent();
        String data = intent.getStringExtra("city");

        if (data != null && !data.isEmpty()) {
            // Gọi hàm để lấy dữ liệu thời tiết hiện tại
            GetCurrentWeatherData(data);
        }*/
        /*btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(MainActivity.this, sign_in.class);
                startActivities(new Intent[]{search});
                *//*String city = edtSearch.getText().toString();
                GetCurrentWeatherData(city);*//*
            }
        });*/

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GGMap.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtSearch.getText().toString();
                String City;
                if (city.equals("")) {
                    City = "Saigon";

                    GetCurrentWeatherData(City);
                } else {
                    City = city;
                    GetCurrentWeatherData(City);
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FutureActivity.class);
                String city = edtSearch.getText().toString();
                intent.putExtra("name",city);
                startActivities(new Intent[]{intent});
            }
        });
    }



    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=48d34576ad87840b7f38187a804d0101";
        //Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String cityname = jsonObject.getString("name");
                            txtcity.setText(cityname);

                            long l =  Long.valueOf(day);
                            Date date = new Date(1*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                            String Day = simpleDateFormat.format(date);

                            txtday.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.get().load("https://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String nhietdocaonhat = jsonObjectMain.getString("temp_max");
                            String nhietdothapnhat = jsonObjectMain.getString("temp_min");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());
                            Double b = Double.valueOf(nhietdocaonhat);
                            String Nhietdocaonhat = String.valueOf(b.intValue());
                            Double c = Double.valueOf(nhietdothapnhat);
                            String Nhietdothapnhat = String.valueOf(c.intValue());

                            txtTempH.setText("H:"+Nhietdocaonhat);
                            txtTempL.setText("L:"+Nhietdothapnhat);
                            txtTemp.setText(Nhietdo+"°C");
                            txtHumidity.setText(doam+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+"m/s");

                            JSONObject jsonObjectRain = jsonObject.getJSONObject("clouds");
                            String mua = jsonObjectRain.getString("all");
                            txtRain.setText(mua+"%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtcountry.setText(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }
    private void Get7DaysData(String Data) {

        /*ArrayList<Hourly> items = new ArrayList<>();

        items.add(new Hourly("9pm", 28,"cloudy"));
        items.add(new Hourly("11pm", 29,"sunny"));
        items.add(new Hourly("12pm", 30,"wind"));
        items.add(new Hourly("1pm", 28,"rainy"));
        items.add(new Hourly("2pm", 27,"storn"));

        recyclerView=findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterHourly=new HourlyAdapters(items);
        recyclerView.setAdapter(adapterHourly);*/
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+Data+"&units=metric&cnt=7&appid=48d34576ad87840b7f38187a804d0101";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            ArrayList<Hourly> items = new ArrayList<>();
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");

                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                                String formattedDate = simpleDateFormat.format(date);
                                hourTxt.setText(formattedDate);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                                String temp = jsonObjectTemp.getString("temp");
                                int temperature = Double.valueOf(temp).intValue();
                                items.add(new Hourly(formattedDate, temperature));
                            }
                            adapterHourly = new HourlyAdapters(items);
                            recyclerView.setAdapter(adapterHourly);
                            adapterHourly.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("ketqua", "Lỗi phân tích JSON: " + e.getMessage());
                        }
                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Lỗi không xác định";
                        if (error != null && error.getMessage() != null) {
                            message = error.getMessage();
                        } else if (error != null && error.networkResponse != null) {
                            message = "Mã trạng thái: " + error.networkResponse.statusCode;
                            // Bạn có thể thêm các thông tin khác từ networkResponse như header nếu cần
                        }
                        Log.e("ketqua", "Lỗi: " + message);
                        Toast.makeText(MainActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                    }
                });


        requestQueue.add(stringRequest);
    }
}