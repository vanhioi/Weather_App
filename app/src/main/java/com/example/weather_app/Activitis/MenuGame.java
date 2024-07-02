package com.example.weather_app.Activitis;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather_app.R;


public class MenuGame extends AppCompatActivity {

    Button btnPlay , btnExit ;

    //MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_game);

        btnPlay = findViewById(R.id.btnPlay) ;
        btnExit = findViewById(R.id.btnExit) ;

        // Phát nhạc nền
        /*mediaPlayer = MediaPlayer.create(this, R.raw.rainy);
        mediaPlayer.setLooping(true); // Lặp lại nhạc nền
        mediaPlayer.start(); // Bắt đầu phát nhạc*/

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
    /*protected void onDestroy() {
        super.onDestroy();
        // Dừng phát nhạc khi Activity bị hủy
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }*/

}