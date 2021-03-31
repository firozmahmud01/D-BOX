package com.firoz.mahmud.d_box;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class VideoPlayer extends FragmentActivity {
    public static String name="NameKey",description="DescriptionKey",link="LinkKey",fav="favorite";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player_activity);
        if (savedInstanceState == null) {
            Intent in=getIntent();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Player(in.getStringExtra(name),
                            in.getStringExtra(description),in.getStringExtra(link),in.getStringExtra(fav)))
                    .commitNow();
        }
    }
}