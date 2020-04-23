package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.Http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;

import static java.lang.Integer.parseInt;

public class Recycler_result extends AppCompatActivity {

    FrameLayout frame;
    //TextView textView;
    TextView playTime;
    MediaPlayer mediaPlayer;
    //Button btn_download;
    ImageView btn_pause;
    ImageView btn_replay;
    ImageView btn_forward;
    Uri MusicUri;
    SeekBar seekBar;
    Thread thread;
    //boolean isPlaying = false;
    private ProgressDialog progressBar;
    static final int PERMISSION_REQUEST_CODE = 1;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    private File outputFile; //파일명까지 포함한 경로
    private File path;//디렉토리경로


    private boolean hasPermissions(String[] permissions) {
        int res = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                //퍼미션 허가 안된 경우
                return false;
            }
        }
        //퍼미션이 허가된 경우
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            thread.interrupt();
        }
        //mediaPlayer.stop();
        //mediaPlayer.reset();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_result);

        frame = (FrameLayout)findViewById(R.id.path_frame);
        View view = new Draw(getApplication());
        Animation animation1 = AnimationUtils.loadAnimation(getApplication(), R.anim.translate);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        frame.addView(view);
        //frame.startAnimation(animation1);

        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        } else {
            //이미 사용자에게 퍼미션 허가를 받음.
        }

        progressBar=new ProgressDialog(Recycler_result.this);
        progressBar.setMessage("다운로드중");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(true);

        seekBar = (SeekBar)findViewById(R.id.playbar);
        seekBar.setVisibility(ProgressBar.VISIBLE);
        playTime = findViewById(R.id.playtime);


        Intent intent = getIntent();
        final String fileURL = intent.getStringExtra("music");
        path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        outputFile= new File(path, "meditation.mp3"); //파일명까지 포함함 경로의 File 객체 생성

        if (outputFile.exists()) { //이미 다운로드 되어 있는 경우

        } else { //새로 다운로드 받는 경우
            final DownloadFilesTask downloadTask = new DownloadFilesTask(Recycler_result.this);
            downloadTask.execute(fileURL);
            progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });
        }

        /*btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //1
                //웹브라우저에 아래 링크를 입력하면 Alight.avi 파일이 다운로드됨.
                final String fileURL = "http://192.168.1.6/meditation.mp3";

                path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile= new File(path, "meditation.mp3"); //파일명까지 포함함 경로의 File 객체 생성

                if (outputFile.exists()) { //이미 다운로드 되어 있는 경우

                    AlertDialog.Builder builder = new AlertDialog.Builder(Recycler_result.this);
                    builder.setTitle("파일 다운로드");
                    builder.setMessage("이미 SD 카드에 존재합니다. 다시 다운로드 받을까요?");
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(getApplicationContext(),"기존 파일을 플레이해주세요.",Toast.LENGTH_LONG).show();
                                    //playVideo(outputFile.getPath());
                                }
                            });
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    outputFile.delete(); //파일 삭제
                                    final DownloadFilesTask downloadTask = new DownloadFilesTask(Recycler_result.this);
                                    downloadTask.execute(fileURL);

                                    progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            downloadTask.cancel(true);
                                        }
                                    });
                                }
                            });
                    builder.show();

                } else { //새로 다운로드 받는 경우
                    final DownloadFilesTask downloadTask = new DownloadFilesTask(Recycler_result.this);
                    downloadTask.execute(fileURL);

                    progressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            downloadTask.cancel(true);
                        }
                    });
                }
            }
        });*/

        path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        outputFile= new File(path, "meditation.mp3");
        MusicUri = Uri.fromFile(outputFile);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), MusicUri);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                int remain_time = seekBar.getMax() - progress;
                int m = remain_time / 60000;
                int s = (remain_time % 60000) / 1000;
                            /*int m = progress / 60000;
                            int s = (progress % 60000) / 1000;*/
                String strTime = String.format("%02d:%02d", m, s);
                playTime.setText(strTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mediaPlayer.start();
        btn_pause = findViewById(R.id.btn_pause);
        btn_pause.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        //isPlaying = true;
        Thread();

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    btn_pause.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                    mediaPlayer.pause();
                }else{
                    btn_pause.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                    mediaPlayer.start();
                    Thread();
                }
            }
        });
        btn_replay = findViewById(R.id.btn_replay);
        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                    if(mediaPlayer.isPlaying()){
                        Thread();
                    }else{
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            }
        });
        btn_forward = findViewById(R.id.btn_forward);
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                    if(mediaPlayer.isPlaying()){
                        Thread();
                    }else{
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            }
        });

        /*btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile= new File(path, "meditation.mp3");
                if(outputFile.exists()){
                    //MusicUri = Uri.fromFile(new File(outputFile.getPath()));
                    MusicUri = Uri.fromFile(outputFile);
                    if(isPlaying)
                    {

                    }else{
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), MusicUri);
                        isPlaying = true;
                    }

                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser) {
                                mediaPlayer.seekTo(progress);
                            }
                            int remain_time = seekBar.getMax() - progress;
                            int m = remain_time / 60000;
                            int s = (remain_time % 60000) / 1000;
                            String strTime = String.format("%02d:%02d", m, s);
                            playTime.setText(strTime);
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                    mediaPlayer.start();
                    Thread();

                }else {
                    Toast.makeText(getApplicationContext(), "Download first", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

}

    public void Thread(){
        Runnable task = new Runnable(){

            public void run(){
                // 음악이 재생중일때
                try {
                    while (mediaPlayer.isPlaying()) {
                        Thread.sleep(1000);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        thread = new Thread(task);
        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if ( !readAccepted || !writeAccepted  )
                        {
                            showDialogforPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showDialogforPermission(String msg) {

        final AlertDialog.Builder myDialog = new AlertDialog.Builder(  Recycler_result.this);
        myDialog.setTitle("알림");
        myDialog.setMessage(msg);
        myDialog.setCancelable(false);
        myDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }

            }
        });
        myDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        myDialog.show();
    }

    private void playVideo(String path) {
        MusicUri = Uri.fromFile(new File(path));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), MusicUri);
        /*Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(videoUri, "video/*");
        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(videoIntent, null));
        }*/
    }

        /*Intent intent = getIntent();
        textView = findViewById(R.id.textView2);
        String key = intent.getStringExtra("name");
        textView.setText(key);

        music = intent.getIntExtra("music", R.raw.test);// if fail, set R.raw.test as default

        btn_download = findViewById(R.id.btn_download);




        btn_start = findViewById(R.id.btn_start);
        btn_pause = findViewById(R.id.btn_pause);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), music);
                mediaPlayer.start();
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        });*/

    private class DownloadFilesTask extends AsyncTask<String, String, Long> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadFilesTask(Context context) {
            this.context = context;
        }


        //파일 다운로드를 시작하기 전에 프로그레스바를 화면에 보여줍니다.
        @Override
        protected void onPreExecute() { //2
            super.onPreExecute();
            //사용자가 다운로드 중 파워 버튼을 누르더라도 CPU가 잠들지 않도록 해서
            //다시 파워버튼 누르면 그동안 다운로드가 진행되고 있게 됩니다.
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();

            progressBar.show();
        }


        //파일 다운로드를 진행합니다.
        @Override
        protected Long doInBackground(String... string_url) { //3
            int count;
            long FileSize = -1;
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(string_url[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();


                //파일 크기를 가져옴
                FileSize = connection.getContentLength();

                //URL 주소로부터 파일다운로드하기 위한 input stream
                //input = connection.getInputStream();
                input = new BufferedInputStream(url.openStream(), 8192);

                path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                outputFile= new File(path, "meditation.mp3"); //파일명까지 포함함 경로의 File 객체 생성
                // SD카드에 저장하기 위한 Output stream
                output = new FileOutputStream(outputFile);


                byte data[] = new byte[1024];
                long downloadedSize = 0;
                while ((count = input.read(data)) != -1) {
                    //사용자가 BACK 버튼 누르면 취소가능
                    if (isCancelled()) {
                        input.close();
                        return Long.valueOf(-1);
                    }

                    downloadedSize += count;

                    if (FileSize > 0) {
                        float per = ((float)downloadedSize/FileSize) * 100;
                        String str = "Downloaded " + downloadedSize + "KB / " + FileSize + "KB (" + (int)per + "%)";
                        publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);

                    }

                    //파일에 데이터를 기록합니다.
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();

                // Close streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                mWakeLock.release();

            }
            return FileSize;
        }


        //다운로드 중 프로그레스바 업데이트
        @Override
        protected void onProgressUpdate(String... progress) { //4
            super.onProgressUpdate(progress);

            // if we get here, length is known, now set indeterminate to false
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setProgress(parseInt(progress[0]));
            progressBar.setMessage(progress[1]);
        }

        //파일 다운로드 완료 후
        @Override
        protected void onPostExecute(Long size) { //5
            super.onPostExecute(size);

            progressBar.dismiss();

            //if( result != null)
            if ( size > 0) {
                //Toast.makeText(getApplicationContext(), "다운로드 완료되었습니다. 파일 크기=" + size.toString(), Toast.LENGTH_LONG).show();

                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(outputFile));
                sendBroadcast(mediaScanIntent);
                Toast.makeText(getApplicationContext(), "Download Success", Toast.LENGTH_LONG).show();
                //playVideo(outputFile.getPath());

            }
            else {
                Toast.makeText(getApplicationContext(), "Download Fail", Toast.LENGTH_LONG).show();
            }
        }

    }

}
