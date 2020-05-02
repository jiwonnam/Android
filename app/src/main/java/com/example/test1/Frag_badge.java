package com.example.test1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Frag_badge extends Fragment {
    public Frag_badge(){

    }
    private View view;
    UserInfo user;
    TextView tv_name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_badge, container, false);
        tv_name = view.findViewById(R.id.name);

        Bundle get_bundle = getArguments();
        user = get_bundle.getParcelable("userInfo");

        tv_name.setText(user.getName());

        return view;
    }
}
