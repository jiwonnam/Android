package com.example.test1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Transition_animation extends AppCompatActivity {

    TextView tv_name;
    ImageView iv_image;
    Button btn_begin;
    String music;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_animation);

        tv_name = findViewById(R.id.tv_name);
        iv_image = findViewById(R.id.iv_image);
        btn_begin = findViewById(R.id.btn_begin);

        Intent intent = getIntent();
        music = intent.getStringExtra("music");
        tv_name.setText(intent.getStringExtra("name"));
        iv_image.setImageResource(intent.getIntExtra("image", R.drawable.flower1));
        iv_image.setTransitionName(MainAdapter.SHARE_VIEW_NAME);

        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Transition_animation.this, Recycler_result.class);
                intent1.putExtra("music", music);
                startActivity(intent1);
            }
        });

    }
}
