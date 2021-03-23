package com.firoz.mahmud.d_box;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.FragmentActivity;


public class MainActivity extends Activity {

    int height,width;
    WindowManager wm;
    boolean isHome=true;
    MainFragment mf=null;


    public static boolean starttime=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoView vv=new VideoView(this);
        setContentView(vv);
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro);
        vv.setVideoURI(uri);
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setContentView(R.layout.activity_main);
                loadAdditionalData();
            }
        });
        if(getSharedPreferences(Api.storage_name,MODE_PRIVATE).getBoolean(Settings.splash_key,true)||starttime) {
            starttime=false;
            vv.start();
        }else{
            setContentView(R.layout.activity_main);
            loadAdditionalData();
        }

    }

    FragmentTransaction ft;



    private void loadAdditionalData(){

        wm=getWindowManager();
        height=wm.getDefaultDisplay().getHeight();
        width=wm.getDefaultDisplay().getWidth();

        ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFram,new Home(height,width,this));
        ft.commit();




    }

    @Override
    public void onBackPressed() {
        if(isHome) {
            super.onBackPressed();
        }else{
            if (mf!=null){
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFram, mf);
                ft.commit();
                mf=null;
            }else {
                isHome = true;
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainFram, new Home(height, width, this));
                ft.commit();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Toast.makeText(this, "onkeydown " + event.getKeyCode(), Toast.LENGTH_SHORT).show();
        return super.onKeyDown(keyCode, event);
    }

}