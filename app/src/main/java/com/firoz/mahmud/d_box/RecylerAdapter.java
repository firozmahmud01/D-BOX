package com.firoz.mahmud.d_box;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final View view;
        public ViewHolder(View view) {
            super(view);
            this.view=view;
            imageView= view.findViewById(R.id.onlyimageImageview);
        }

        public View getView(){
            return this.view;
        }

        public ImageView getImageView() {
            return this.imageView;
        }

    }

    private SharedPreferences sp;
    private PackageManager pm;
    JSONArray arr;
    Context context;
    private FragmentTransaction ft;
    int height,width;
    MainActivity ma;
    public RecylerAdapter(Context context,int height,int width,FragmentTransaction ft) throws JSONException {
        this.context=context;
        this.ma=ma;
        this.ft=ft;
        this.height=height;
        this.width=width;
        sp=context.getSharedPreferences(Api.storage_name,Context.MODE_PRIVATE);
        pm=context.getPackageManager();
        arr=new JSONArray(sp.getString(HomeView.apps_key,"[]"));
    }
    private void updateSize(View v,boolean isSelected){
        ViewGroup.LayoutParams lp= v.getLayoutParams();
        if (isSelected){
            lp.width=width/5;
            lp.height=height/5;
        }else{
            lp.width=width/6;
            lp.height=height/6;
        }
        v.setLayoutParams(lp);
    }
    private void updatewidth(View v,boolean isSelected){
        ViewGroup.LayoutParams lp= v.getLayoutParams();
        if (isSelected){
            lp.width=width/5;
            lp.height=height/5;
        }else{
            lp.width=width/6;
            lp.height=height/5;
        }
        v.setLayoutParams(lp);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.onlyimage, viewGroup, false);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updatewidth(v,hasFocus);
                ImageView iv=v.findViewById(R.id.onlyimageImageview);
                updateSize(iv,hasFocus);
            }
        });
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        updatewidth(view,false);
        ImageView iv=view.findViewById(R.id.onlyimageImageview);
        updateSize(iv,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecylerAdapter.ViewHolder holder, int position) {


        switch (position) {
            case 0:
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pack="com.google.android.youtube.tv";
                        context.startActivity(getIntent(pack));
                    }
                });
                Glide.with(context).load(R.drawable.youtube).centerCrop().into(holder.getImageView());
                break;
            case 1:
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pack="com.disney.disneyplus";
                        context.startActivity(getIntent(pack));
                    }
                });
                Glide.with(context).load(R.drawable.diney).centerCrop().into(holder.getImageView());
                break;
            case 2:
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(getIntent("com.android.vending"));
                    }
                });
                Glide.with(context).load(R.drawable.playstore).centerCrop().into(holder.getImageView());
                break;
            case 3:
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pack="cm.aptoide.pt";
                        context.startActivity(getIntent(pack));
                    }
                });
                Glide.with(context).load(R.drawable.aptoide).centerCrop().into(holder.getImageView());
                break;
            case 4:
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pack="com.teamviewer.teamviewer.market.mobile";
                        context.startActivity(getIntent(pack));
                    }
                });
                Glide.with(context).load(R.drawable.tamviewer).centerCrop().into(holder.getImageView());
                break;
        }
        if (position<=4)return;
        final int pos=position-5;
        if (pos>=arr.length()){
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ft.replace(R.id.home_fragmanet_layout_toreplace,new AppLister(width,height)).commit();
                }
            });
            Glide.with(context).load(R.drawable.ic_addmoreapp).centerCrop().into(holder.getImageView());
            return;
        }


        try {
            final String pack = arr.getString(pos);
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(getIntent(pack));
            }
        });

        Glide.with(context).load(pm.getApplicationBanner(pack)).error(pm.getApplicationIcon(pack))
                .centerCrop().into(holder.getImageView());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 6+arr.length();
    }


    private Intent getIntent(String pack){
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(pack);
        if (launchIntent != null) {
            return launchIntent;
        }else{
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+pack));
            intent.setPackage("com.android.vending");
            return intent;
        }
    }

}
