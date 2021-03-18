package com.example.testplayer;

import android.graphics.SurfaceTexture;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;

import java.io.File;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    SuperPlayerView superPlayerView;
    TextureView textureView;
    private Surface surface;
    MediaPlayer mediaPlayer;

    Button switchButton;
    ImageView imageView;
    PointsSeekBar seekBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                .enableLeft();

        switchButton = findViewById(R.id.buttonId);
        imageView = findViewById(R.id.imageviewId);
        seekBar = findViewById(R.id.seekbarId);
        /*superPlayerView = findViewById(R.id.SuperPlayerViewId);
        SuperPlayerModel model = new SuperPlayerModel();

        model.url = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";

        superPlayerView.playWithModel(model);*/
        textureView = findViewById(R.id.textureViewId);
        textureView.setSurfaceTextureListener(this);//设置监听函数  重写4个方法

        seekBar.setMax(100);
        handler.sendEmptyMessageDelayed(1,3000);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getVisibility() == View.GONE){
                    imageView.setVisibility(View.VISIBLE);
                }else {
                    imageView.setVisibility(View.GONE);
                }
            }
        });
    }

    int progress = 0;
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            seekBar.setProgress(progress++);
            msg = obtainMessage();
            sendMessageDelayed(msg,1000);
        }
    };

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        surface=new Surface(surfaceTexture);
        new PlayerVideo().start();//开启一个线程去播放视频
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        surfaceTexture=null;
        surface=null;
        mediaPlayer.stop();
        mediaPlayer.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }
    private class PlayerVideo extends Thread {
        @Override
        public void run() {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4");
                mediaPlayer.setSurface(surface);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
