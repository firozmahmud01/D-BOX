package com.firoz.mahmud.d_box;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Home extends Fragment {


    //interface
    LinearLayout home,settings,apps,tv;

    Fragment homeview,settingsview,appsview;
    MainActivity ma;
    int height,width;
    Api api;
    ImageView hiv,setiv,appiv,tviv,wifi,update,vod;


    Handler h;
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
        hiv=view.findViewById(R.id.home_home_imageciew);
        setiv=view.findViewById(R.id.home_settings_imageview);
        appiv=view.findViewById(R.id.home_app_imageview);
        tviv=view.findViewById(R.id.home_tv_imageview);
        wifi=view.findViewById(R.id.home_view_wifi_imageview);
        api=new Api(getContext(),h);
        vod=view.findViewById(R.id.home_vod_imageview);

        api.changeSizeofView(hiv,ma.p,20);
        api.changeSizeofView(setiv,ma.p,20);
        api.changeSizeofView(appiv,ma.p,20);
        api.changeSizeofView(tviv,ma.p,20);
        api.changeSizeofView(vod,ma.p,20);
        api.changeSizeofView(wifi,ma.p,20);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });


        update=view.findViewById(R.id.home_view_update_imageview);
        api.changeSizeofView(update,ma.p,20);
        vod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                MainFragment mf=new MainFragment(height,width,ma,null,null,null,false);
                fm.replace(R.id.mainFram,mf);
                fm.commit();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Need a server to check", Toast.LENGTH_SHORT).show();
            }
        });

        h=new Handler();





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
        appsview=new AllApps(ma);
        appiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlfha(1);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                fm.replace(R.id.home_fragmanet_layout_toreplace,appsview);
                fm.commit();
            }
        });

        hiv.setOnClickListener(new View.OnClickListener() {
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
        setiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlfha(2);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                fm.replace(R.id.home_fragmanet_layout_toreplace,settingsview);
                fm.commit();
            }
        });

        tviv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma.isHome=false;
                updateAlfha(3);
                FragmentTransaction fm=getFragmentManager().beginTransaction();
                MainFragment mf=new MainFragment(height,width,ma,null,null,null,true);
                fm.replace(R.id.mainFram,mf);
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