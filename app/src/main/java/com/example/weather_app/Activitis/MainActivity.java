package com.example.weather_app.Activitis;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation;
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;
    private List<String> quotes = new ArrayList<>();
    ImageButton btnMap, btnUser;
    SeekBar musicTime;
    EditText edtSearch;
    TextView txtcity, txtcountry, txtNext, txtStatus, txtday, txtTemp, txtTempH, txtTempL, txtRain, txtWind, txtHumidity, tempTxt, hourTxt, dayofweekTxt, dayTxt, txtQuotes;
    ImageView imgIcon, btnSearch, btnSoundToggle;
    String city = "";
    HourlyAdapters hourlyAdapters;
    ArrayList<Hourly> hourlyArrayList;
    private static final String CHANNEL_ID = "Thời tiết hôm nay thế nào?";
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
        musicTime = findViewById(R.id.musicTime);
        bottom_navigation = findViewById(R.id.bottom_navigation);

       createNotificationChannel();

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        btnSoundToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound();
            }
        });

        // Khởi tạo danh sách và adapter cho RecyclerView để hiển thị dữ liệu theo giờ
        hourlyArrayList = new ArrayList<>();
        hourlyAdapters = new HourlyAdapters(hourlyArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(hourlyAdapters);

        GetCurrentWeatherData("Saigon");
        GetHourlyData("Saigon");

        addQuotes();
        String randomQuote = getRandomQuote();
        displayQuote(randomQuote);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Hourly_chart.class);
                String cityName = edtSearch.getText().toString();
                intent.putExtra("name", cityName);
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
                Intent intent = new Intent(MainActivity.this, FutureActivity.   class);
                String city = edtSearch.getText().toString();
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });

        // Thiết lập SeekBar và xử lý sự kiện khi người dùng kéo SeekBar
        musicTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress); // Đặt thời gian của MediaPlayer tới vị trí mới
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void showPopupMenu(View view) {
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        popupMenu.getMenu().add(0, 1, 0, "Map");
        popupMenu.getMenu().add(0, 2, 1, "Trò chơi");
        popupMenu.getMenu().add(0, 3, 2, "Thoát");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        Intent map = new Intent(MainActivity.this, GGMap.class);
                        startActivity(map);
                        return true;
                    case 2:
                        Intent trochoi = new Intent(MainActivity.this, MenuGame.class);
                        startActivity(trochoi);
                        return true;
                    case 3:
                        Intent thoat = new Intent(MainActivity.this, sign_in.class);
                        startActivity(thoat);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }





    private void addQuotes() {
        quotes.add("“Bạn không cần phải xuất sắc để bắt đầu, nhưng bạn phải bắt đầu để trở nên xuất sắc.”");
        quotes.add("“Để không thể bị thay thế, người ta phải luôn nổi bật.”");
        quotes.add("“Sự thanh lịch không phải là để thu hút sự chú ý, mà là để để lại ấn tượng lâu dài.”");
        quotes.add("“Tốc độ của bạn không quan trọng miễn là bạn tiếp tục tiến về phía trước.”");
        quotes.add("“Thất bại chỉ tồn tại khi bạn ngừng nỗ lực.”");
        quotes.add("“Điều duy nhất chúng ta cần sợ là sự sợ hãi chính mình.”");
        quotes.add("“Tại sao chúng ta ngã? Để chúng ta có thể học cách đứng lên.”");
        quotes.add("“Bạn càng nỗ lực nhiều, bạn càng trở nên may mắn.”");
        quotes.add("“Hãy làm hoặc không làm, không có chuyện thử.”");
        quotes.add("“Không bao giờ là quá muộn để trở thành người bạn có thể trở thành.”");
        quotes.add("“Bạn không thể tua lại và thay đổi phần đầu, nhưng bạn có thể bắt đầu từ vị trí hiện tại và sửa đổi phần kết.”");
        quotes.add("“Em hãy mỉm cười, để đời hôm nay là một ngày đẹp trời.”");
        quotes.add("“Cuộc đời này, nếu không thử thách thì còn gì đáng sống.”");
        quotes.add("“Nếu bạn không thể bay, hãy đi bộ; nếu không thể chạy, hãy đi bộ; nếu đi bộ cũng khó khăn, hãy bò. Tuy nhiên, bạn phải tiến lên không ngừng.”");
        quotes.add("“Trên hành trình đến thành công, không có dấu hiệu của sự lười biếng.”");
        quotes.add("“Niềm tự hào sâu sắc nhất trong cuộc đời không phải là không bao giờ vấp ngã mà là đứng vững sau mỗi lần vấp ngã.”");
        quotes.add("“Đừng ngồi đó chờ cơ hội; hãy tự tạo ra chúng.”");
        quotes.add("“Hãy giữ cho khuôn mặt của bạn luôn hướng về phía ánh nắng, và bóng tối sẽ đổ về phía sau bạn.”");
        quotes.add("“Nếu tôi không thể làm những điều lớn lao, tôi có thể làm những điều nhỏ bé một cách tuyệt vời.”");
        quotes.add("“Cuộc sống của bạn chỉ trở nên tốt đẹp hơn khi bạn trở nên tốt hơn.”");
        quotes.add("“Bạn không bao giờ quá già để đặt ra mục tiêu khác hoặc mơ một giấc mơ mới.”");
        quotes.add("“Tôi không thể thay đổi hướng gió, nhưng tôi có thể điều chỉnh cánh buồm của mình để luôn đến đích.” ");
        quotes.add("“Hãy tin rằng bạn có thể làm được và bạn đã đi được nửa chặng đường.");
        quotes.add("“Tất cả những giấc mơ của chúng ta đều có thể trở thành hiện thực nếu chúng ta có đủ can đảm để theo đuổi chúng.” ");
        quotes.add("“Hạnh phúc không phải là thứ được làm sẵn. Nó đến từ hành động của chính bạn.” ");
        quotes.add("“Chúng ta trở thành những gì chúng ta nghĩ về.” ");
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

    private void createNotificationChannel() {
        // Tạo NotificationChannel, nhưng chỉ trên API 26+ vì
        // lớp NotificationChannel mới có trong thư viện hỗ trợ từ API 26 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Đặt tên kênh thông báo
            CharSequence name = "Thời tiết hôm nay thế nào?";
            // Mô tả kênh thông báo
            String description = "Thời tiết hôm nay";
            // Đặt mức độ quan trọng của thông báo là cao
            int importance = NotificationManager.IMPORTANCE_HIGH;
            // Tạo một đối tượng NotificationChannel mới với ID, tên và mức độ quan trọng
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Đặt mô tả cho kênh thông báo
            channel.setDescription(description);
            // Đăng ký kênh với hệ thống; không thể thay đổi mức độ quan trọng
            // hoặc hành vi thông báo khác sau khi đăng ký
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d("NotificationChannel", "Notification channel created.");
        } else {
            Log.d("NotificationChannel", "Notification channel not needed.");
        }
    }

    private void sendWeatherAlert(String weatherCondition, double currentTemp) {
        Log.d("WeatherAlert", "sendWeatherAlert called with condition: " + weatherCondition + " and temperature: " + currentTemp);

        // Thông báo luôn được gửi đi, không cần điều kiện
        //String alertMessage = "Thời tiết hôm nay thế nào?";
        String alertMessage;
        if (weatherCondition.contains("Rain")) {
            alertMessage = "Trời mưa rồi đó, nhớ mang theo ô!";
        } else if (currentTemp > 35) {
            alertMessage = "Trời nắng nóng gay gắt, nhớ mang theo nước và đội mũ!";
        } else {
            alertMessage = "Một ngày đẹp trời, chúc bạn có một ngày tốt lành!";
        }

        // Tạo một đối tượng NotificationCompat.Builder mới với kênh thông báo đã tạo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                // Đặt biểu tượng nhỏ cho thông báo
                .setSmallIcon(R.drawable.start1_preview_rev_1)
                // Đặt tiêu đề cho thông báo là "Thời tiết hôm nay thế nào?"
                .setContentTitle("Thời tiết hôm nay thế nào?")
                .setContentText(alertMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Đặt thông báo tự động hủy khi người dùng nhấn vào
                .setAutoCancel(true);

        // Tạo một Intent để mở MainActivity khi người dùng nhấn vào thông báo
        Intent intent = new Intent(this, MainActivity.class);
        // Tạo một PendingIntent để gắn với Intent vừa tạo
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Gán PendingIntent cho thông báo
        builder.setContentIntent(pendingIntent);

        // Lấy đối tượng NotificationManager từ hệ thống
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Gửi thông báo với ID là 0
        notificationManager.notify(0, builder.build());
        Log.d("WeatherAlert", "Notification sent: " + alertMessage);
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

                            txtTempH.setText("H:" + Nhietdocaonhat + "°C");
                            txtTempL.setText("L:" + Nhietdothapnhat + "°C");
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

                            sendWeatherAlert(status, a);

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
                imgIcon.setImageResource(R.drawable.clear_sky);
                mainLayout.setBackgroundResource(R.drawable.night);
                break;
            case "02d":
                imgIcon.setImageResource(R.drawable.fewclouds);
                mainLayout.setBackgroundResource(R.drawable.nang);
                break;
            case "02n":
                imgIcon.setImageResource(R.drawable.n_fewclouds);
                mainLayout.setBackgroundResource(R.drawable.night);
                break;
            case "03d":
                imgIcon.setImageResource(R.drawable.fewclouds);
                mainLayout.setBackgroundResource(R.drawable.nhieumay);
                break;
            case "03n":
                imgIcon.setImageResource(R.drawable.n_fewclouds);
                mainLayout.setBackgroundResource(R.drawable.night);
                break;
            case "04d":
                imgIcon.setImageResource(R.drawable.scatteredclouds);
                mainLayout.setBackgroundResource(R.drawable.nhieumay);
                break;
            case "04n":
                imgIcon.setImageResource(R.drawable.scatteredclouds);
                mainLayout.setBackgroundResource(R.drawable.night);
                break;
            case "09d":
                imgIcon.setImageResource(R.drawable.rain_mua);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "09n":
                imgIcon.setImageResource(R.drawable.n_showerrain);
                mainLayout.setBackgroundResource(R.drawable.night);
                break;
            case "10d":
                imgIcon.setImageResource(R.drawable.rain_mua);
                mainLayout.setBackgroundResource(R.drawable.mua);
                break;
            case "10n":
                imgIcon.setImageResource(R.drawable.n_showerrain);
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
                imgIcon.setImageResource(R.drawable.fewclouds);
                mainLayout.setBackgroundResource(R.drawable.night);
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

                                //lấy tên ngày trong tuần
                                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                                //ngày và tháng
                                SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                                //giờ và phút
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
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sunny);
                break;
            case "01n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
            case "02d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sunny);
                break;
            case "02n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
            case "03d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sunny);
                break;
            case "03n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
            case "04d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "04n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "09d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "09n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
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
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
            case "13n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
            case "50d":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rainy);
                break;
            case "50n":
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
            default:
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.night);
                break;
        }

        mediaPlayer.setLooping(true);
        if (isMuted) {
            mediaPlayer.setVolume(0, 0);
        }
        mediaPlayer.start();

        // Khởi tạo thanh SeekBar với độ dài tương ứng với thời lượng của bài hát
        musicTime.setMax(mediaPlayer.getDuration());

        // Cập nhật thanh SeekBar mỗi khi thời gian của bài hát thay đổi
        new Thread(() -> {
            while (mediaPlayer != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(this::updateSeekBar);
            }
        }).start();
    }

    // Phương thức cập nhật thanh SeekBar
    private void updateSeekBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            musicTime.setProgress(mediaPlayer.getCurrentPosition());
        }
    }

    private void toggleSound() {
        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.setVolume(1, 1);
                btnSoundToggle.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                mediaPlayer.setVolume(0, 0);
                btnSoundToggle.setImageResource(android.R.drawable.ic_media_play);
            }
            isMuted = !isMuted;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            btnSoundToggle.stopNestedScroll();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}