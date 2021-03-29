package com.firoz.mahmud.d_box;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;


import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRowPresenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class HomeView extends Fragment {
    RecyclerView rv;
    RecyclerView favorite;
    int height,width;



    public HomeView(int height,int width){
        this.height=height;
        this.width=width;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_home_view, container, false);
        rv=view.findViewById(R.id.home_view_recyler);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new RecylerAdapter(getContext(),height,width));

        favorite=view.findViewById(R.id.home_view_recyler);
        favorite.setLayoutManager(layoutManager);
        Thread th=new Thread(){
            @Override
            public void run() {
                Api api=new Api(getContext(),view.getHandler());
                try {
                    final FavoriteRecyler fr=new FavoriteRecyler(getContext(),api.getFavoriteList());
                    getView().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            favorite.setAdapter(fr);
                        }
                    });
                } catch (Exception e) {
                    view.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Failed to load favorite list", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        };
        th.start();




        return view;
    }
}