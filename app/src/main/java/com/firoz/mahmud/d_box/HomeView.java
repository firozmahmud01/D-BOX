package com.firoz.mahmud.d_box;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;


import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

@SuppressLint("ValidFragment")
public class HomeView extends Fragment {
    public static String apps_key="AllAppAdded";


    RecyclerView rv;
    RecyclerView favorite;
    int height,width;
    FragmentTransaction fm;


    public HomeView(int height,int width){
        this.height=height;
        this.width=width;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_home_view, container, false);
        rv=view.findViewById(R.id.home_view_recyler);
        fm=getFragmentManager().beginTransaction();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        try {
            rv.setAdapter(new RecylerAdapter(getContext(), height, width,fm));
        }catch (Exception e){}
        favorite=view.findViewById(R.id.home_fevorite_recyclerview);
        favorite.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        Thread th=new Thread(){
            @Override
            public void run() {
                Api api=new Api(getContext(),view.getHandler());
                try {
                    final List<Movie> movie=api.getFavoriteList();
                    final FavoriteRecyler fr=new FavoriteRecyler(getContext(),movie);
                    view.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            favorite.setAdapter(fr);
                            if (movie.size()>0){
                                view.findViewById(R.id.favorite_movie_textvirw).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } catch (Exception e) {
                    try {
                        view.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Failed to load favorite list", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (Exception er){}
                }

            }
        };
        th.start();




        return view;
    }
}