package com.firoz.mahmud.d_box;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class AppLister extends Fragment {
    SharedPreferences sp;
    PackageManager pm;
    Point p;

    public AppLister(int width,int height){
        this.p=new Point();
        p.x=width;
        p.y=height;
    }


    JSONArray arr;
    String packag;
    BaseAdapter ba;
    Api api;
    Handler handler;
    GridView gv;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_app_lister,null);
        context=view.getContext();
        sp=context.getSharedPreferences(Api.storage_name,Context.MODE_PRIVATE);
        gv=view.findViewById(R.id.listed_app_gridview);
        handler=new Handler();

        try{
            packag=sp.getString(HomeView.apps_key,"[]");
            arr=new JSONArray(packag);
        }catch (Exception e){}
        pm=context.getPackageManager();
        api=new Api(context,handler);
        final List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        final List<PackageInfo> packages = new ArrayList<PackageInfo>();

        for(PackageInfo pi: packageInfoList){
            if(!isSystemPackage(pi)){
                packages.add(pi);
            }
        }

        ba=new BaseAdapter() {
            @Override
            public int getCount() {
                return packages.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View cv, ViewGroup parent) {
                if (cv==null){
                    cv=inflater.inflate(R.layout.allappsitem,null);
                }

                ImageView iv=cv.findViewById(R.id.all_app_item_icon);
                iv.setFocusable(true);
                iv.setFocusableInTouchMode(true);
                try {
                    Glide.with(context).load(pm.getApplicationIcon(packages.get(position).packageName)).into(iv);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                TextView tv=cv.findViewById(R.id.all_app_item_name);
                tv.setText(pm.getApplicationLabel(packages.get(position).applicationInfo));
                api.changeSizeofView(iv,p,20);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pack=packages.get(position).packageName;
                        if (packag.contains(pack)){
                            for(int i=0;i<arr.length();i++){
                                try {
                                    if (arr.getString(i).equals(pack)){
                                        arr.remove(i);
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            packag=arr.toString();
                            SharedPreferences.Editor spe=sp.edit();
                            spe.putString(HomeView.apps_key,packag);
                            spe.commit();
                            ba.notifyDataSetChanged();
                        }else{
                            arr.put(pack);
                            packag=arr.toString();
                            SharedPreferences.Editor spe=sp.edit();
                            spe.putString(HomeView.apps_key,packag);
                            spe.commit();
                            ba.notifyDataSetChanged();
                        }
                    }
                });
                cv.setFocusable(false);
                if (packag.contains(packages.get(position).packageName)){
                    cv.setBackgroundColor(Color.RED);
                }else{
                    cv.setBackground(null);
                }
                return cv;
            }
        };

        gv.setAdapter(ba);
        gv.setClickable(false);
        gv.setEnabled(false);
        gv.setFocusable(false);



        return view;
    }

    boolean isSystemPackage(PackageInfo pkgInfo){
        if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0)
            return true;
        else if(pkgInfo.packageName.equals("com.firoz.mahmud.d_box"))
            return true;
        else if(pm.getLaunchIntentForPackage(pkgInfo.packageName)==null)
            return true;
        else if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0)
            return false;
        else if((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_INSTALLED)!=0)
            return false;
        else return true;
    }
}