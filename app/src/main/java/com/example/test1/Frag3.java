package com.example.test1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Frag3 extends Fragment {

    private View view;
    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    public Frag3(){
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(getContext(), arrayList);
        recyclerView.setAdapter(mainAdapter);

        // List 1
        MainData mainData = new MainData(R.drawable.meditation1, "flower1", "http://192.168.1.6/meditation.mp3");
        arrayList.add(mainData);

        // List 2
        mainData = new MainData(R.drawable.meditation2,"flower2", "http://192.168.1.6/meditation.mp3");
        arrayList.add(mainData);

        // List 3
        mainData = new MainData(R.drawable.flower3,"flower3", "http://192.168.1.6/meditation.mp3");
        arrayList.add(mainData);

        // List 4
        mainData = new MainData(R.drawable.flower4,"flower4", "http://192.168.1.6/meditation.mp3");
        arrayList.add(mainData);

        // List 5
        mainData = new MainData(R.drawable.flower5,"flower5", "http://192.168.1.6/meditation.mp3");
        arrayList.add(mainData);

        // List 6
        mainData = new MainData(R.drawable.flower6, "flower6", "http://192.168.1.6/meditation.mp3");
        arrayList.add(mainData);

        mainAdapter.notifyDataSetChanged();

        return view;
    }
}
