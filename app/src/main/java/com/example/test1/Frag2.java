package com.example.test1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Frag2 extends Fragment {

    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private View view;
    //MediaPlayer mediaPlayer;
    private ImageView im_view1;


    public Frag2(){

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag2, container, false);

        /*im_view1 = view.findViewById(R.id.iv_profile1);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beauty_beast);
        im_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }else {
                    mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beauty_beast);
                    mediaPlayer.start();
                }
            }
        });*/

        /*im_view1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
                return true;
            }
        });*/

        recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        // List 1
        MainData mainData = new MainData(R.drawable.flower1, "flower1", R.raw.beauty_beast);
        arrayList.add(mainData);

        // List 2
        mainData = new MainData(R.drawable.flower2,"flower2", R.raw.test);
        arrayList.add(mainData);

        // List 3
        mainData = new MainData(R.drawable.flower3,"flower3",R.raw.beauty_beast);
        arrayList.add(mainData);

        // List 4
        mainData = new MainData(R.drawable.flower4,"flower4",R.raw.test);
        arrayList.add(mainData);

        // List 5
        mainData = new MainData(R.drawable.flower5,"flower5",R.raw.beauty_beast);
        arrayList.add(mainData);

        // List 6
        mainData = new MainData(R.drawable.flower6, "flower6",R.raw.test);
        arrayList.add(mainData);

        mainAdapter.notifyDataSetChanged();

        return view;
    }

}
