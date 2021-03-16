package com.firoz.mahmud.d_box;

import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;


import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class HomeView extends Fragment {
    RecyclerView rv;

    int height,width;



    public HomeView(int height,int width){
        this.height=height;
        this.width=width;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home_view, container, false);
        rv=view.findViewById(R.id.home_view_recyler);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new RecylerAdapter(getContext(),height,width));



        return view;
    }
}