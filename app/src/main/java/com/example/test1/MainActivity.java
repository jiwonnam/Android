package com.example.test1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.test1.Frag1;
import com.example.test1.Frag2;
import com.example.test1.Frag3;
import com.example.test1.Frag4;
import com.example.test1.Frag5;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;
    private Frag5 frag5;
    private long bakBtnTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); // get intent from Login_Activity
        final String userEmail = intent.getStringExtra("userEmail");
        // String userPass = intent.getStringExtra("userPass");

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        setFrag(0, userEmail);
                        break;
                    case R.id.action_today:
                        setFrag(1, userEmail);
                        break;
                    case R.id.action_night:
                        setFrag(2, userEmail);
                        break;
                    case R.id.action_mood:
                        setFrag(3, userEmail);
                        break;
                    case R.id.action_person:
                        setFrag(4, userEmail);
                        break;
                }
                return true;
            }
        });
        frag1 = new Frag1();
        frag2 = new Frag2();
        frag3 = new Frag3();
        frag4 = new Frag4();
        frag5 = new Frag5();
        setFrag(0, userEmail); //Initialize frag1 as main
    }

    private void setFrag(int n, String userID){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);

        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, frag3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, frag4);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, frag5);
                frag5.setArguments(bundle);
                ft.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        long curTime = System.currentTimeMillis();
        long gapTime = curTime - bakBtnTime;

        if(gapTime >= 0 && gapTime <= 2000){
            super.onBackPressed();
        }
        else{
            bakBtnTime = curTime;
            Toast.makeText(this, "Press one more time for termination", Toast.LENGTH_SHORT).show();
        }
    }
}


