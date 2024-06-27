package com.example.weather_app.Activitis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;
    private List<String> quotes = new ArrayList<>();
    ImageButton btnMap, btnUser, btnSoundToggle;
    EditText edtSearch;
    TextView txtcity, txtcountry, txtNext, txtStatus, txtday, txtTemp, txtTempH, txtTempL, txtRain, txtWind, txtHumidity, tempTxt, hourTxt, dayofweekTxt, dayTxt, txtQuotes;
    ImageView imgIcon, btnSearch;
    String city = "";
    HourlyAdapters hourlyAdapters;
    ArrayList<Hourly> hourlyArrayList;
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
        dayTxt =findViewById(R.id.dayTxt);
        dayofweekTxt =findViewById(R.id.dayofweekTxt);
        txtQuotes = findViewById(R.id.txtQuotes);
        recyclerView = findViewById(R.id.view1);
        btnSoundToggle = findViewById(R.id.btnSoundToggle);

        btnSoundToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound();
            }
        });

        hourlyArrayList = new ArrayList<>();
        hourlyAdapters = new HourlyAdapters(hourlyArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(hourlyAdapters);

        GetCurrentWeatherData("Saigon");
        GetHourlyData("Saigon");

        //Q
        addQuotes();
        String randomQuote = getRandomQuote();
        displayQuote(randomQuote);

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
                String City = city.equals("") ? "Saigon" : city;
                GetCurrentWeatherData(City);
                GetHourlyData(City);
            }
        });

        txtNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FutureActivity.class);
                String city = edtSearch.getText().toString();
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }

    private void addQuotes() {
        quotes.add("Hãy sống như ngày mai bạn sẽ chết.");
        quotes.add("“Phấn đấu không phải để thành công, mà là để trở nên có giá trị.”");
        quotes.add("“Vinh quang lớn nhất trong cuộc sống không phải là không bao giờ gục ngã, mà là vươn lên sau mỗi lần vấp ngã.”");
        quotes.add("“Hạnh phúc không phải là thứ có sẵn. Nó đến từ hành động của chính bạn.”");
        quotes.add("Yêu thương là điều quý giá nhất.");
        quotes.add("“Cách duy nhất để làm những công việc tuyệt vời là yêu những gì bạn làm.”");
        // Thêm các câu nói khác
    }

    private String getRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.size());
        return quotes.get(index);
    }

    private void displayQuote(String quote) {
        TextView textView = findViewById(R.id.txtQuotes);
        textView.setText(quote);
    }

    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=48d34576ad87840b7f38187a804d0101";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String cityname = jsonObject.getString("name");
                            txtcity.setText(cityname);

                            long time = jsonObject.getLong("dt") * 1000;
                            Date date = new Date(time);
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE | dd-MM-yyyy", Locale.getDefault());
                            String formattedDate = sdf.format(date);
                            txtday.setText(formattedDate);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            setWeatherIconAndBackground(icon);

                            playMusicForWeather(icon);

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

                            txtTempH.setText("H:" + Nhietdocaonhat);
                            txtTempL.setText("L:" + Nhietdothapnhat);
                            txtTemp.setText(Nhietdo + "°C");
                            txtHumidity.setText(doam + "%");
                            txtStatus.setText(status);

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio + "m/s");

                            JSONObject jsonObjectRain = jsonObject.getJSONObject("clouds");
                            String mua = jsonObjectRain.getString("all");
                            txtRain.setText(mua + "%");

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
                        Toast.makeText(MainActivity.this, "Lỗi: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void setWeatherIconAndBackground(String icon) {
        ImageView imgIcon = findViewById(R.id.imgIcon);
        ViewGroup.LayoutParams layoutParams = imgIcon.getLayoutParams();
        layoutParams.width = 500;
        layoutParams.height = 500;
        imgIcon.setLayoutParams(layoutParams);

        ConstraintLayout mainLayout = findViewById(R.id.main);

        switch (icon) {
            //n: đêm, d: ngày
            case "01d":
                imgIcon.setImageResource(R.drawable.clearsky);
                mainLayout.setBackgroundResource(R.drawable.nang);
                break;
            case "01n":
                imgIcon.setImageResource(R.drawable.clearsky);
                mainLayout.setBackgroundResource(R.drawable.nang);
                break;
            case "02d":
                imgIcon.setImageResource(R.drawable.fewclouds);
                mainLayout.setBackgroundResource(R.drawable.nang);
                break;
            case "02n":
                imgIcon.setImageResource(R.drawable.fewclouds);
                mainLayout.setBackgroundResource(R.drawable.nang);
                break;
            case "03d":
                imgIcon.setImageResource(R.drawable.scatteredclouds);
                mainLayout.setBackgroundResource(R.drawable.nhieumay);
                break;
            case "03n":
                imgIcon.setImageResource(R.drawable.scatteredclouds);
                mainLayout.setBackgroundResource(R.drawable.nhieumay);
                break;
            case "04d":
                imgIcon.setImageResource(R.drawable.scatteredclouds);
                mainLayout.setBackgroundResource(R.drawable.nhieumay);
                break;
            case "04n":
                imgIcon.setImageResource(R.drawable.scatteredclouds);
                mainLayout.setBackgroundResource(R.drawable.nhieumay);
                break;
            case "09d":
                imgIcon.setImageResource(R.drawable.rain_mua);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "09n":
                imgIcon.setImageResource(R.drawable.rain_mua);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "10d":
                imgIcon.setImageResource(R.drawable.rain_mua);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "10n":
                imgIcon.setImageResource(R.drawable.rain_mua);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "11d":
                imgIcon.setImageResource(R.drawable.thunderstorm);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "11n":
                imgIcon.setImageResource(R.drawable.thunderstorm);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "13d":
                imgIcon.setImageResource(R.drawable.snowy);
                break;
            case "13n":
                imgIcon.setImageResource(R.drawable.snowy);
                break;
            case "50d":
                imgIcon.setImageResource(R.drawable.storm);
                break;
            case "50n":
                imgIcon.setImageResource(R.drawable.storm);
                break;
            default:
                // Set a default icon and background
                imgIcon.setImageResource(R.drawable.fewclouds);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
        }
    }

    public void GetHourlyData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&appid=48d34576ad87840b7f38187a804d0101";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            hourlyArrayList.clear();

                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);

                                String dt_txt = jsonObjectList.getString("dt_txt");

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                Date date = dateFormat.parse(dt_txt);

                                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                                SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                                String dayOfWeek = dayFormat.format(date);
                                String dateOnly = dateOnlyFormat.format(date);
                                String time = timeFormat.format(date);

                                JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                                //String status = jsonObjectList.getString("main");
                                String nhietdo = jsonObjectMain.getString("temp");
                                int temperature = (int) Math.round(Double.parseDouble(nhietdo));
                                //txtStatus.setText(status);

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String icon = jsonObjectWeather.getString("icon");

                                hourlyArrayList.add(new Hourly(dayOfWeek, dateOnly, time, temperature, icon));
                            }
                            hourlyAdapters.notifyDataSetChanged();
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Please enter a valid city", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void playMusicForWeather(String icon) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        switch (icon) {
            case "01d":
            case "01n":
            case "02d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "02n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "03d":
            case "03n":
            case "04d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "04n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "09d":
            case "09n":
            case "10d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "10n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "11d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "11n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "13d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "13n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "50d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "50n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            default:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
        }

        mediaPlayer.setLooping(true);
        if (isMuted) {
            mediaPlayer.setVolume(0, 0);
        }
        mediaPlayer.start();
    }

    private void toggleSound() {
        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.setVolume(1, 1);
                btnSoundToggle.setImageResource(R.drawable.ic_volume_on);
            } else {
                mediaPlayer.setVolume(0, 0);
                btnSoundToggle.setImageResource(R.drawable.ic_volume_off);
            }
            isMuted = !isMuted;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
