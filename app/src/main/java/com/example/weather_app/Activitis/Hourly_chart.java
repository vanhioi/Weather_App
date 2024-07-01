package com.example.weather_app.Activitis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather_app.R;
import com.example.weather_app.Adapters.WeatherResponse;
import com.example.weather_app.Adapters.WeatherService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Hourly_chart extends AppCompatActivity {

    String tenthanhpho = "";
    ImageView btnBack;
    BarChart barChart;
    LineChart lineChart;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchat);

        btnBack = findViewById(R.id.btnback);
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Hourly_chart.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String cityName = intent.getStringExtra("name");

        if (cityName == null || cityName.equals("")) {
            tenthanhpho = "Saigon";
        } else {
            tenthanhpho = cityName;
        }

        getWeatherData();
    }

    // Hàm để lấy dữ liệu thời tiết từ API
    private void getWeatherData() {
        // Khởi tạo Retrofit để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi dữ liệu JSON
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        // Gọi API để lấy dữ liệu thời tiết hàng giờ
        Call<WeatherResponse> call = service.getHourlyForecast(tenthanhpho, "48d34576ad87840b7f38187a804d0101", "metric");

        // Xử lý kết quả trả về từ API
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    // Nếu lấy dữ liệu thành công, cập nhật biểu đồ
                    WeatherResponse weather = response.body();
                    if (weather != null) {
                        updateChart(weather);
                    }
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch data", t);
            }
        });
    }

    // Hàm cập nhật biểu đồ với dữ liệu thời tiết
    private void updateChart(WeatherResponse weather) {
        ArrayList<BarEntry> rainEntries = new ArrayList<>();
        ArrayList<Entry> tempEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Lặp qua dữ liệu thời tiết và tạo các điểm dữ liệu
        for (int i = 0; i < weather.list.size() && i < 12; i++) {
            WeatherResponse.Hourly hourly = weather.list.get(i);
            float rainVolume = hourly.rain != null ? hourly.rain.ThreeHour : 0f;
            rainEntries.add(new BarEntry(i, rainVolume));
            tempEntries.add(new Entry(i, hourly.main.temp));
            labels.add(hourly.dt_txt.split(" ")[1].substring(0, 5));
        }

        // Tính tổng lượng mưa, nắng, gió cho biểu đồ tròn
        float totalRain = 0f;
        float totalSunshine = 0f;
        float totalWind = 0f;

        // Tính toán dữ liệu từ weather.list và cập nhật biểu đồ tròn
        for (WeatherResponse.Hourly hourly : weather.list) {
            if (hourly.rain != null) {
                totalRain += hourly.rain.ThreeHour;
            }

            if (hourly.weather.size() > 0) {
                String weatherType = hourly.weather.get(0).main;
                if (weatherType.equalsIgnoreCase("Clear")) {
                    totalSunshine += 1f;
                } else if (weatherType.equalsIgnoreCase("Clouds")) {
                    totalSunshine += 0.5f;
                }
            }

            totalWind += hourly.wind.speed;
        }

        // Thêm dữ liệu vào biểu đồ tròn
        pieEntries.add(new PieEntry(totalRain, "Mưa"));
        pieEntries.add(new PieEntry(totalSunshine, "Nắng"));
        pieEntries.add(new PieEntry(totalWind, "Gió"));

        // Tạo và cấu hình bộ dữ liệu cho biểu đồ cột
        BarDataSet rainDataSet = new BarDataSet(rainEntries, "Lượng mưa (mm)");
        rainDataSet.setValueTextColor(Color.WHITE);
        rainDataSet.setColor(getResources().getColor(R.color.blue));
        rainDataSet.setValueTextSize(12f);

        // Tạo và cấu hình bộ dữ liệu cho biểu đồ đường
        LineDataSet tempDataSet = new LineDataSet(tempEntries, "Nhiệt độ (°C)");
        tempDataSet.setValueTextColor(Color.WHITE);
        tempDataSet.setColor(getResources().getColor(R.color.orange));
        tempDataSet.setCircleColor(getResources().getColor(R.color.green));
        tempDataSet.setValueTextSize(12f);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "So sánh thời tiết");
        pieDataSet.setColors(new int[]{
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.gray)
        });
        pieDataSet.setValueFormatter(new PercentFormatter(pieChart)); // Định dạng giá trị thành phần trăm
        pieDataSet.setValueTextSize(12f); // Kích thước chữ của giá trị
        pieDataSet.setValueTextColor(Color.WHITE); // Màu chữ của giá trị

        // Tạo dữ liệu cho biểu đồ và thiết lập biểu đồ
        BarData barData = new BarData(rainDataSet);
        LineData lineData = new LineData(tempDataSet);
        PieData pieData = new PieData(pieDataSet);

        barChart.setData(barData);
        lineChart.setData(lineData);
        pieChart.setData(pieData);

        // Cấu hình trục X cho biểu đồ cột
        XAxis barXAxis = barChart.getXAxis();
        barXAxis.setTextColor(Color.WHITE);
        barXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        barXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barXAxis.setGranularity(1f);
        barXAxis.setGranularityEnabled(true);
        barXAxis.setTextSize(14f);

        // Cấu hình trục X cho biểu đồ đường
        XAxis lineXAxis = lineChart.getXAxis();
        lineXAxis.setTextColor(Color.WHITE);
        lineXAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        lineXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineXAxis.setGranularity(1f);
        lineXAxis.setGranularityEnabled(true);
        lineXAxis.setTextSize(14f);

        // Cấu hình trục Y cho biểu đồ cột
        YAxis barLeftAxis = barChart.getAxisLeft();
        barLeftAxis.setTextColor(Color.WHITE);
        barLeftAxis.setAxisMinimum(0f);
        barLeftAxis.setTextSize(14f);

        // Cấu hình trục Y cho biểu đồ đường
        YAxis lineLeftAxis = lineChart.getAxisLeft();
        lineLeftAxis.setTextColor(Color.WHITE);
        lineLeftAxis.setAxisMinimum(0f);
        lineLeftAxis.setTextSize(14f);

        // không hiển thị trục Y bên phải cho cả hai biểu đồ
        barChart.getAxisRight().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        // Thiết lập mô tả cho biểu đồ cột
        Description barDescription = new Description();
        barDescription.setText("Lượng mưa (mm)");
        barDescription.setTextSize(14f);
        barDescription.setTextColor(Color.WHITE);
        barChart.setDescription(barDescription);
        barChart.animateY(1400); // thiết lập hiệu ứng animation cho biểu đồ cột.

        // Thiết lập mô tả cho biểu đồ đường
        Description lineDescription = new Description();
        lineDescription.setText("Nhiệt độ (°C)");
        lineDescription.setTextSize(14f);
        lineDescription.setTextColor(Color.WHITE);
        lineChart.setDescription(lineDescription);
        lineChart.animateY(1400); // thiết lập hiệu ứng animation cho biểu đồ đường.

        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.animateY(1400); // thiết lập hiệu ứng animation cho biểu đồ tròn.

        // Thiết lập màu chú thích cho biểu đồ cột
        Legend barLegend = barChart.getLegend();
        barLegend.setTextColor(Color.WHITE);

        // Thiết lập màu chú thích cho biểu đồ đường
        Legend lineLegend = lineChart.getLegend();
        lineLegend.setTextColor(Color.WHITE);

        // Thiết lập màu chú thích cho biểu đồ tròn
        Legend pieLegend = pieChart.getLegend();
        pieLegend.setTextColor(Color.WHITE);

        // Làm mới biểu đồ để hiển thị dữ liệu mới
        barChart.invalidate();
        lineChart.invalidate();
        pieChart.invalidate();
    }
}
