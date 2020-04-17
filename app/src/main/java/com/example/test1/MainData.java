package com.example.test1;


import android.media.MediaPlayer;
import android.net.Uri;

public class MainData {

    private int iv_profile1;
    private String iv_name1;
    /*private int iv_profile2;
    private int iv_profile3;
    private int iv_profile4;*/
    private int mediaPlayer1;

    public MainData(int iv_profile1, String iv_name1, int mediaPlayer1){
        this.iv_profile1 = iv_profile1;
        this.iv_name1 = iv_name1;
        this.mediaPlayer1 = mediaPlayer1;
        /*this.iv_profile2 = iv_profile2;
        this.iv_profile3 = iv_profile3;
        this.iv_profile4 = iv_profile4;*/

    }

    public int getIv_profile1() {
        return iv_profile1;
    }

    public void setIv_profile1(int iv_profile1) {
        this.iv_profile1 = iv_profile1;
    }

    public String getIv_name1() {
        return iv_name1;
    }

    public void setIv_name1(String iv_name1) {
        this.iv_name1 = iv_name1;
    }

    public int getMediaPlayer1() {
        return mediaPlayer1;
    }

    public void setMediaPlayer1(int mediaPlayer1) {
        this.mediaPlayer1 = mediaPlayer1;
    }



    /*public int getIv_profile2() {
        return iv_profile2;
    }

    public void setIv_profile2(int iv_profile2) {
        this.iv_profile2 = iv_profile2;
    }

    public int getIv_profile3() {
        return iv_profile3;
    }

    public void setIv_profile3(int iv_profile3) {
        this.iv_profile3 = iv_profile3;
    }

    public int getIv_profile4() {
        return iv_profile4;
    }

    public void setIv_profile4(int iv_profile4) {
        this.iv_profile4 = iv_profile4;
    }*/


}
