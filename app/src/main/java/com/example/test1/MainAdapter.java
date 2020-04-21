package com.example.test1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {


    private ArrayList<MainData> arrayList;
    MediaPlayer mediaPlayer;

    public MainAdapter(ArrayList<MainData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override //처음 생명주기
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override //추가될 떄
    public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder holder, final int position) {
        holder.iv_profile1.setImageResource(arrayList.get(position).getIv_profile1());
        holder.iv_name1.setText(arrayList.get(position).getIv_name1());
        /*holder.iv_profile2.setImageResource(arrayList.get(position).getIv_profile2());
        holder.iv_profile3.setImageResource(arrayList.get(position).getIv_profile3());
        holder.iv_profile4.setImageResource(arrayList.get(position).getIv_profile4());*/

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.iv_name1.getText().toString();
                Toast.makeText(v.getContext(), curName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), Recycler_result.class);
                //String key = holder.iv_name1.getText().toString();
                intent.putExtra("name", curName);
                intent.putExtra("music", arrayList.get(position).getMediaPlayer1());
                v.getContext().startActivity(intent);
            }
        });

        /*holder.iv_profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String curName = holder.tv_name.getText().toString();
                Toast.makeText(v.getContext(), "content 2", Toast.LENGTH_SHORT).show();
            }
        });

        holder.iv_profile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String curName = holder.tv_name.getText().toString();
                Toast.makeText(v.getContext(), "content 3", Toast.LENGTH_SHORT).show();
            }
        });

        holder.iv_profile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String curName = holder.tv_name.getText().toString();
                Toast.makeText(v.getContext(), "content 4", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    /*public void remove(int position){
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }*/


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView iv_profile1;
        protected TextView iv_name1;
        /*protected ImageView iv_profile2;
        protected ImageView iv_profile3;
        protected ImageView iv_profile4;*/

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile1 = (ImageView)itemView.findViewById(R.id.iv_profile1);
            this.iv_name1 = (TextView)itemView.findViewById(R.id.iv_name1);
            /*this.iv_profile2 = (ImageView)itemView.findViewById(R.id.iv_profile2);
            this.iv_profile3 = (ImageView)itemView.findViewById(R.id.iv_profile3);
            this.iv_profile4 = (ImageView)itemView.findViewById(R.id.iv_profile4);*/
        }
    }

}
