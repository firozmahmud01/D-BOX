package com.firoz.mahmud.d_box;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavoriteRecyler extends RecyclerView.Adapter<RecylerAdapter.ViewHolder> {




    private Context context;
    private List<Movie> movie;
    public FavoriteRecyler(Context context,List<Movie>movie){
        this.context=context;
        this.movie=movie;
    }

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;
    @Override
    public RecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        sDefaultBackgroundColor =
                ContextCompat.getColor(context, R.color.default_background);
        sSelectedBackgroundColor =
                ContextCompat.getColor(context, R.color.selected_background);

        mDefaultCardImage = ContextCompat.getDrawable(context, R.drawable.dbox);

        final ImageCardView cardView =
                new ImageCardView(context);
        cardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateSize(v,hasFocus);

            }
        });

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateSize(cardView,false);

        return new RecylerAdapter.ViewHolder(cardView);

    }

    private void updateSize(View view ,boolean selected){
        //todo need to check

//        ViewGroup.LayoutParams lp=view.getLayoutParams();
//        if (selected){
//            int a=CARD_WIDTH/5;
//            lp.width=CARD_WIDTH+a;
//            lp.width=CARD_HEIGHT+((a*CARD_HEIGHT)/CARD_WIDTH);
//        }else{
//            lp.width=CARD_WIDTH;
//            lp.width=CARD_HEIGHT;
//        }
//        view.setLayoutParams(lp);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerAdapter.ViewHolder holder, int position) {
        ImageCardView icv=(ImageCardView) holder.getView();
        icv.setTitleText(movie.get(position).getTitle());
        icv.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        icv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Glide.with(context)
                .load(movie.get(position).getCardImageUrl()==null?"":movie)
                .centerCrop()
                .error(mDefaultCardImage)
                .into(icv.getMainImageView());


    }


    @Override
    public int getItemCount() {
        return movie.size();
    }




}
