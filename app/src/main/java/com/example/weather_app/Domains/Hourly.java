package com.example.weather_app.Domains;

public class Hourly {

    private final String day;
    private final String dayofweek;
    private String hour;
    private int temp;
    private String picPath;
    //private String status;



    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

   /* public String getstatus() {
        return status;
    }

    public void setstatus(String picPath) {
        this.status = status;
    }*/
    public Hourly(String dayofweek, String day, String hour, int temp, String picPath) {
        this.hour = hour;
        this.day = day;
        this.dayofweek = dayofweek;
        this.temp = temp;
        this.picPath = picPath;
        //this.status = status;
    }

    public String getDay() {
        return day;
    }

    public String getDayofweek() {
        return dayofweek;
    }
}
