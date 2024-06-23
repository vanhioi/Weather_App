package com.example.weather_app.Activitis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather_app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /*ArrayList<Hourly>hourlyArrayList;
    HourlyAdapters hourlyAdapters;*/
    //private RecyclerView.Adapter adapterHourly;
    /*private RecyclerView recyclerView;
    private List<String> randomQuotes;*/
    private List<String> quotes = new ArrayList<>();
    ImageButton btnMap, btnUser, btnSearch;
    EditText edtSearch;
    TextView txtcity, txtcountry, txtNext, txtStatus, txtday, txtTemp, txtTempH, txtTempL, txtRain, txtWind, txtHumidity, tempTxt, hourTxt, txtQuotes;
    ImageView imgIcon;
    String city = "";
    String tenthanhpho = "";
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
        txtQuotes = findViewById(R.id.txtQuotes);

        GetCurrentWeatherData("Saigon");

        // Thêm các câu nói vào danh sách
        addQuotes();

        // Lấy câu nói ngẫu nhiên
        String randomQuote = getRandomQuote();

        // Hiển thị câu nói ngẫu nhiên
        displayQuote(randomQuote);

        /*hourlyArrayList = new ArrayList<>();
        hourlyAdapters = new HourlyAdapters(hourlyArrayList, this);*/
        //recyclerView = findViewById(R.id.view1);
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<Hourly> items = new ArrayList<>();*/
        //adapterHourly=new HourlyAdapters(items);
        //recyclerView.setAdapter(adapterHourly);

        /*recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        randomQuotes = Arrays.asList(
                "Success is not final, failure is not fatal: It is the courage to continue that counts.",
                "The only limit to our realization of tomorrow will be our doubts of today.",
                "The way to get started is to quit talking and begin doing.",
                "Don't watch the clock; do what it does. Keep going.",
                "It's not whether you get knocked down, it's whether you get up.",
                "We may encounter many defeats but we must not be defeated.",
                "The only way to do great work is to love what you do.",
                "If you can dream it, you can achieve it.",
                "In order to succeed, we must first believe that we can.",
                "Start where you are. Use what you have. Do what you can."
        );

        // Setup RecyclerView
        QuoteAdapter adapter = new QuoteAdapter(randomQuotes);
        recyclerView.setAdapter(adapter);*/

        /*Get7DaysData("");
        String city = edtSearch.getText().toString().trim();
        Get7DaysData(city);
        if (city == null || city.equals("")) {
            tenthanhpho = "Saigon";
        } else {
            tenthanhpho = city;
        }
        Get7DaysData(tenthanhpho);*/

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

    private void addQuotes() {
        quotes.add("Hãy sống như ngày mai bạn sẽ chết.");
        quotes.add("Đừng bao giờ từ bỏ ước mơ.");
        quotes.add("Thành công không phải là đích đến.");
        quotes.add("Cuộc sống là một cuộc hành trình.");
        quotes.add("Yêu thương là điều quý giá nhất.");
        // Thêm các câu nói khác nếu cần
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

                            long time = jsonObject.getLong("dt") * 1000; // Lấy thời gian và chuyển đổi sang milliseconds
                            Date date = new Date(time);
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE | dd-MM-yyyy | HH:mm:ss", Locale.getDefault());
                            String formattedDate = sdf.format(date);
                            txtday.setText(formattedDate);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            // Tìm ImageView trong bố cục giao diện
                            ImageView imgIcon = findViewById(R.id.imgIcon);
                            ViewGroup.LayoutParams layoutParams = imgIcon.getLayoutParams();
                            layoutParams.width = 500; // Chiều rộng theo đơn vị pixel
                            layoutParams.height = 500; // Chiều cao theo đơn vị pixel
                            imgIcon.setLayoutParams(layoutParams);
                            // Sử dụng hình ảnh của riêng bạn từ thư mục drawable
                            switch (icon) {
                                case "01d":
                                    Picasso.get().load(R.drawable.clearsky).into(imgIcon);
                                    setBackground(R.drawable.nang);
                                    break;
                                case "02d":
                                    Picasso.get().load(R.drawable.fewclouds).into(imgIcon);
                                    setBackground(R.drawable.nang);
                                    break;
                                case "03d":
                                    Picasso.get().load(R.drawable.scatteredclouds).into(imgIcon);
                                    setBackground(R.drawable.nhieumay);
                                    break;
                                case "04d":
                                    Picasso.get().load(R.drawable.scatteredclouds).into(imgIcon);
                                    setBackground(R.drawable.nhieumay);
                                    break;
                                case "09d":
                                    Picasso.get().load(R.drawable.rain_mua).into(imgIcon);
                                    setBackground(R.drawable.mua);
                                    break;
                                case "10d":
                                    Picasso.get().load(R.drawable.rain_mua).into(imgIcon);
                                    setBackground(R.drawable.mua);
                                    break;
                                case "11d":
                                    Picasso.get().load(R.drawable.thunderstorm).into(imgIcon);
                                    setBackground(R.drawable.mua);
                                    break;
                                case "13d":
                                    Picasso.get().load(R.drawable.snowy).into(imgIcon);
                                    break;
                                case "50d":
                                    Picasso.get().load(R.drawable.storm).into(imgIcon);
                                    break;
                            }

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

    private void setBackground(int drawableId) {
        ConstraintLayout layout = findViewById(R.id.main); // mainLayout là id của layout chính
        layout.setBackgroundResource(drawableId);
    }

    //Lỗi hiển thị dữ liệu chỗ Today
    private void Get7DaysData(String data) {

        /*ArrayList<Hourly> items = new ArrayList<>();*/

        /*items.add(new Hourly("9pm", 28,"cloudy"));
        items.add(new Hourly("11pm", 29,"sunny"));
        items.add(new Hourly("12pm", 30,"wind"));
        items.add(new Hourly("1pm", 28,"rainy"));
        items.add(new Hourly("2pm", 27,"storn"));*/

        /*recyclerView=findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
*/
        /*adapterHourly=new HourlyAdapters(items);
        recyclerView.setAdapter(adapterHourly);*/

        /*if (data == null || data.isEmpty()) {
            data = "Saigon";
        }
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=48d34576ad87840b7f38187a804d0101";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //Log.d("URL", url); lấy dữ liệu APIs về logcat
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    Log.d("ketqua7day","JSON"+response); //Logcat hiển thị APIs nhưng màn hình k hiển thị
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
                                if (hourTxt != null) {
                                    hourTxt.setText(formattedDate);
                                }

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                                String temp = jsonObjectTemp.getString("temp");
                                int temperature = (int) Math.round(Double.parseDouble(temp));

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String icon = jsonObjectWeather.getString("icon");

                                items.add(new Hourly(formattedDate, temperature, icon));
                            }
                            //adapterHourly = new HourlyAdapters(items);
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


        requestQueue.add(stringRequest);*/
    }
}