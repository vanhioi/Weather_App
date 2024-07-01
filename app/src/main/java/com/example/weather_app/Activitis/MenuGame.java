package com.example.weather_app.Activitis;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather_app.R;


public class MenuGame extends AppCompatActivity {

    Button btnPlay , btnExit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_game);

        btnPlay = findViewById(R.id.btnPlay) ;
        btnExit = findViewById(R.id.btnExit) ;

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btnExit = new Intent(MenuGame.this ,MainActivity.class) ;
                startActivity(btnExit);
                finish();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btnPlay = new Intent(MenuGame.this ,Game.class) ;
                startActivity(btnPlay);
            }
        });
    }


}