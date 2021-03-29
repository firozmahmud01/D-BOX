package com.firoz.mahmud.d_box;

import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class AllApps extends Fragment {


    GridView gv;
    BaseAdapter ba;
    PackageManager pm;
    Api api;
    MainActivity ma;
    public AllApps(MainActivity ma){
        this.ma=ma;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_apps, container, false);
        gv=view.findViewById(R.id.all_app_gridview);
        pm=getContext().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        final List<PackageInfo> packages = new ArrayList<PackageInfo>();
        api=new Api(getContext(),new Handler());
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
                if(cv==null) {
                    cv = inflater.inflate(R.layout.allappsitem, null);
                }
                ImageView iv=cv.findViewById(R.id.all_app_item_icon);
                iv.setFocusable(true);
                iv.setFocusableInTouchMode(true);
                try {
                    Glide.with(getContext()).load(pm.getApplicationIcon(packages.get(position).packageName)).into(iv);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                TextView tv=cv.findViewById(R.id.all_app_item_name);
                tv.setText(pm.getApplicationLabel(packages.get(position).applicationInfo));
                api.changeSizeofView(iv,ma.p,20);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            getContext().startActivity(pm.getLaunchIntentForPackage(packages.get(position).packageName));
                        }catch (Exception e){
                            Toast.makeText(getContext(), "Failed to start this app", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cv.setFocusable(false);
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