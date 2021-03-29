package com.firoz.mahmud.d_box;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


@SuppressLint("ValidFragment")
public class AllSettings extends Fragment {
    int width,height;


    ImageView settings,server;


    public AllSettings(int width,int height){
        this.width=width;
        this.height=height;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_all_settings, container, false);
        settings=view.findViewById(R.id.phonesettings);
        server=view.findViewById(R.id.serversettings);
        changeSize(server,false);
        changeSize(settings,false);

        settings.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeSize(v,hasFocus);
            }
        });
        server.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeSize(v,hasFocus);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_SETTINGS);
//                Uri uri = Uri.fromParts("package","com.firoz.mahmud.d_box", null);
//                intent.setData(uri);
                getContext().startActivity(intent);
            }
        });

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.home_fragmanet_layout_toreplace,new Settings(getContext())).commit();
            }
        });

        return view;
    }

    private void changeSize(View view,boolean selected){
        ViewGroup.LayoutParams lp=view.getLayoutParams();
        int a=width/4;
        if (!selected){
            lp.width=a;
            lp.height=a+100;
        }else{
            lp.width=a+100;
            lp.height=a+200;
        }
        view.setLayoutParams(lp);
    }

}