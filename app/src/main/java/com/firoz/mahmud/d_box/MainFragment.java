package com.firoz.mahmud.d_box;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ValidFragment")
public class MainFragment extends BrowseFragment {


    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;



    private String mBackgroundUri;
    private Api api;
    int height;
    int width;
    MainActivity ma;

    boolean shouldback=false;

    private String type,genry;
    private String name;
    private boolean fromtv;
    private Handler handler;

    public MainFragment(int height, int width, MainActivity ma, String type, String genry, String name, boolean fromtv){
        this.type=type;
        this.fromtv=fromtv;
        this.name=name;
        this.genry=genry;
        this.ma=ma;
        this.height=height;
        this.width=width;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        setupUIElements();

        handler=new Handler();
        api=new Api(getContext(),handler);

        loadRows();

        setupEventListeners();
    }



    private void loadRows() {
        final ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading....");
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ma.isHome=true;
                getFragmentManager().beginTransaction().replace(R.id.mainFram,new Home(height,width,ma)).commit();
            }
        });
        pd.show();
        Thread th=new Thread() {
            @Override
            public void run() {
                List<Movie>tv,video,radio,favorite;
                while(true){

                        if (type != null) {
                            for (int i=1;true;i++) {
                                try {
                                    tv = api.getListByCatagory(genry, type,i);
                                    if (tv.size()<=0)break;
                                    CardPresenter cardPresenter = new CardPresenter();
                                    ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                                    HeaderItem header = new HeaderItem(0, name+" Page-"+i);
                                    listRowAdapter.addAll(0, tv);
                                    rowsAdapter.add(new ListRow(header, listRowAdapter));
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            pd.dismiss();
                                        }
                                    });
                                } catch (Exception e) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            pd.dismiss();
                                        }
                                    });
                                    break;
                                }
                            }
                        }else {
                                try {

                            CardPresenter cardPresenter = new CardPresenter();
                                ArrayObjectAdapter listRowAdapter ;
                                    HeaderItem header;
                                    if(fromtv) {
                                        tv = api.getTvCatagory();
                                listRowAdapter= new ArrayObjectAdapter(cardPresenter);
                                header= new HeaderItem(0, "TV");
                                listRowAdapter.addAll(0, tv);
                                rowsAdapter.add(new ListRow(header, listRowAdapter));
                            }else {
                                        video = api.getVideoCatagory();
                                        listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                                        header = new HeaderItem(0, "Video Club");
                                        listRowAdapter.addAll(0, video);
                                        rowsAdapter.add(new ListRow(header, listRowAdapter));
                                    }


                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                            }catch (Exception e){
                        try {
                            Thread.sleep(5000);
                        }catch (Exception err){

                        }
                    }
                        }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    });
                        break;


                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });

            }
        };
        th.start();
            setAdapter(rowsAdapter);

        }



    private void setupUIElements() {


        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getContext(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getContext(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearch();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }




    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, final Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            final Movie movie=(Movie)item;
            final MainFragment mf=new MainFragment(height,width,ma,
                    movie.getType(),""+movie.getId(),movie.getTitle(),false);
            Thread th=new Thread(){
                @Override
                public void run() {
                    try {
                        if (movie.getVideoUrl()==null){
                            shouldback=true;
                            ma.mf=MainFragment.this;
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.mainFram,mf).commit();
                        }else {
                            Intent intent = new Intent(getContext(), VideoPlayer.class);
                            String link = api.getVideoLink(movie.getVideoUrl(),type);
                            intent.putExtra(VideoPlayer.name,movie.getTitle());
                            intent.putExtra(VideoPlayer.description,movie.getDescription());
                            intent.putExtra(VideoPlayer.link,link);
                            getContext().startActivity(intent);
                        }
                    }catch (Exception e){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Failed to load link", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            };
            th.start();
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof Movie) {
                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
            }
        }
    }


    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

    public void startSearch(){

    }
}