package com.firoz.mahmud.d_box;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class Home extends Fragment {


    //interface
    LinearLayout home,settings,apps,tv;

    Fragment homeview,settingsview,appsview;
    MainActivity ma;
    int height,width;
    Handler h;
    Api api;
    public Home(int height,int width,MainActivity ma){
        this.ma=ma;
        this.height=height;


        this.width=width;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        //interface declearation
        home=view.findViewById(R.id.home_icon);
        settings=view.findViewById(R.id.settings_icon);
        tv=view.findViewById(R.id.tv_icon);
        apps=view.findViewById(R.id.all_app_icon);
        h=new Handler();
        api=new Api(getContext(),h);
        Thread th=new Thread() {
            @Override
            public void run() {
                try {
//                    api.Check();
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
        settings.setAlpha((float)0.5);
        apps.setAlpha((float)0.5);
        tv.setAlpha((float)0.5);
        homeview=new HomeView(height,width);
        settingsview=new Settings(getContext());
        appsview=new AllApps();
        apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlfha(1);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                fm.replace(R.id.home_fragmanet_layout_toreplace,appsview);
                fm.commit();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlfha(0);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                fm.replace(R.id.home_fragmanet_layout_toreplace,homeview);
                fm.commit();
            }
        });
        FragmentTransaction fm=getFragmentManager().beginTransaction();
        fm.replace(R.id.home_fragmanet_layout_toreplace,homeview);
        fm.commit();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlfha(2);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                fm.replace(R.id.home_fragmanet_layout_toreplace,settingsview);
                fm.commit();
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma.isHome=false;
                updateAlfha(3);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                fm.replace(R.id.mainFram,new MainFragment(height,width,ma,null,null,null));
                fm.commit();
            }
        });

        return view;
    }


    private void updateAlfha(int pos){
        switch (pos){
            case 0:
                home.setAlpha((float)1);
                apps.setAlpha((float)0.5);
                settings.setAlpha((float)0.5);
                tv.setAlpha((float)0.5);
                break;
            case 1:
                home.setAlpha((float)0.5);
                apps.setAlpha((float)1);
                settings.setAlpha((float)0.5);
                tv.setAlpha((float)0.5);
                break;
            case 2:
                home.setAlpha((float)0.5);
                apps.setAlpha((float)0.5);
                settings.setAlpha((float)1);
                tv.setAlpha((float)0.5);
                break;
            case 3:
                home.setAlpha((float)0.5);
                apps.setAlpha((float)0.5);
                settings.setAlpha((float)0.5);
                tv.setAlpha((float)1);
                break;
        }
    }
}