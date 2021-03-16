package com.firoz.mahmud.d_box;

import android.content.Context;
import android.content.Intent;
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
    Context context;
    int height,width;
    public RecylerAdapter(Context context,int height,int width) {
        this.context=context;
        this.height=height;
        this.width=width;
    }
    private void updateSize(View v,boolean isSelected){
        ViewGroup.LayoutParams lp= v.getLayoutParams();
        if (isSelected){
            lp.width=width/4;
            lp.height=height/4;
        }else{
            lp.width=width/6;
            lp.height=height/6;
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
                updateSize(v,hasFocus);
                ImageView iv=v.findViewById(R.id.onlyimageImageview);
                updateSize(iv,false);
            }
        });
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        updateSize(view,false);
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 5;
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
