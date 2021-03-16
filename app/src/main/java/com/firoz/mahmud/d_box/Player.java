package com.firoz.mahmud.d_box;

import android.net.Uri;
import android.os.Bundle;



import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.PlaybackControlsRow;

public class Player extends VideoSupportFragment {

    private PlaybackTransportControlGlue<MediaPlayerAdapter> mTransportControlGlue;
    String name,description;
    String link;
    public Player(String name,String description,String link){
        this.name=name;
        this.description=description;
        this.link=link;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        VideoSupportFragmentGlueHost glueHost =
                new VideoSupportFragmentGlueHost(this);

        final MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getContext());
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

        mTransportControlGlue = new PlaybackTransportControlGlue<>(getContext(), playerAdapter);
        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle(name);
        mTransportControlGlue.setSubtitle(description);
        mTransportControlGlue.playWhenPrepared();
        try {
            playerAdapter.setDataSource(Uri.parse(link));
        }catch (Exception e){}
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }

}