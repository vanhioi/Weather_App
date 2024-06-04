package com.example.weather_app.Activitis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weather_app.R;

public class MainActivity extends AppCompatActivity {

    ImageButton btnMap, btnUser;
    TextView txtLocation, txtNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMap = findViewById(R.id.btnMap);
        btnUser = findViewById(R.id.btnUser);
        /*txtLocation = findViewById(R.id.txtLocation);
        txtNext  = findViewById(R.id.txtNext);*/

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(MainActivity.this, sign_in.class);
                startActivities(new Intent[]{signin});
            }
        });
    }

}