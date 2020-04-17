package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Recycler_result extends AppCompatActivity {

    TextView textView;
    MediaPlayer mediaPlayer;
    Button btn_start;
    Button btn_stop;
    int music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_result);

        Intent intent = getIntent();
        textView = findViewById(R.id.textView2);
        String key = intent.getStringExtra("name");
        textView.setText(key);

        music = intent.getIntExtra("music", R.raw.test);

        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), music);
                mediaPlayer.start();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        });



    }
}
