package com.example.weather_app.Activitis;

public class Weather {
    private String day;
    private String status;
    private String icon;
    private String tempMax;
    private String tempMin;
    private String temp;
    private String wind;
    private String rain;
    private String humidity;

    public Weather(String day, String status, String icon, String tempMax, String tempMin, String temp, String wind, String rain, String humidity) {
        this.day = day;
        this.status = status;
        this.icon = icon;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.temp = temp;
        this.wind = wind;
        this.rain = rain;
        this.humidity = humidity;
    }

    public String getDay() {
        return day;
    }

    public String getStatus() {
        return status;
    }

    public String getIcon() {
        return icon;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTemp() {
        return temp;
    }

    public String getWind() {
        return wind;
    }

    public String getRain() {
        return rain;
    }

    public String getHumidity() {
        return humidity;
    }
}
