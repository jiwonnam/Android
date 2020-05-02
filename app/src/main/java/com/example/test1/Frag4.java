package com.example.test1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class Frag4 extends Fragment{
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag_mood frag_mood;
    private Frag_progress frag_progress;
    UserInfo user;
    TabLayout tabLayout;
    private View view;

    public Frag4(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);

        Bundle get_bundle = getArguments();
        user = get_bundle.getParcelable("userInfo");

        frag_mood = new Frag_mood();
        frag_progress = new Frag_progress();
        setFrag(0,user);

        setFrag(0,user);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        setFrag(0, user);
                        break;
                    case 1:
                        setFrag(1, user);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        setFrag(0, user);
                        break;
                    case 1:
                        setFrag(1, user);
                        break;
                }
            }
        });
        return view;
    }


    private void setFrag(int n, UserInfo user){
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();

        Bundle put_bundle = new Bundle();
        put_bundle.putParcelable("userInfo", user);

        switch (n) {
            case 0:
                ft.replace(R.id.excel_frame, frag_mood);
                frag_mood.setArguments(put_bundle);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.excel_frame, frag_progress);
                frag_progress.setArguments(put_bundle);
                ft.commit();
                break;
        }
    }

}
