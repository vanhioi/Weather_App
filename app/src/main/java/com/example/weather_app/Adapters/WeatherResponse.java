package com.example.weather_app.Adapters;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    public List<Hourly> list;

    public class Hourly {
        public Main main;
        public List<Weather> weather;
        public Rain rain;
        public Wind wind;
        public String dt_txt;

        public class Main {
            public float temp;
        }

        public class Weather {
            @SerializedName("main")
            public String main;
            @SerializedName("pop")
            public float probabilityOfPrecipitation;
        }

        public class Rain {
            @SerializedName("3h")
            public float ThreeHour;
        }

        public class Wind {
            public float speed;
        }
    }
}
