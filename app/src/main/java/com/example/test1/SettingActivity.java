package com.example.test1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SettingActivity extends AppCompatActivity {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private PreferenceFragment frag;
    UserInfo user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        user = getIntent().getParcelableExtra("userInfo");

        frag = new PreferenceFragment();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable("userInfo", user);

        ft.replace(R.id.set_fragment, frag);
        frag.setArguments(bundle);
        ft.commit();

    }
}
