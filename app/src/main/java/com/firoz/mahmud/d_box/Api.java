package com.firoz.mahmud.d_box;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Api {
    //static content
    public static String storage_name="Storage";
    public static String mac_key="MACKey";
    public static String username_key="UserNameKey";
    public static String password_key="PasswordKey";
    public static String aid_key="AidKey";
    public static String serial_key="SerialKey";

    //private variable
    private Context context;
    private String token="";
    private String mac_address="";
    private boolean isshowing=false;
    Handler h;




    public Api(Context context,Handler h){
        this.context=context;
        this.h=h;
    }
    public void Check() throws Exception {
        GET("http://s3.starone.pw/stalker_portal/server/load.php?type=watchdog&action=get_events&cur_play_type=0&event_active_id=0&init=1&JsHttpRequest=1-xml");
    }

    public void changeSizeofView(final View view, final Point p,final int part){
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                change(v,p,hasFocus,part);
        }
        });
        change(view,p,false,part);
    }
    private void change(View v,Point p,boolean hasFocus,int part){
        ViewGroup.LayoutParams lp=v.getLayoutParams();
        int x=p.x/part;
        int y=x;
        if (hasFocus) {
            int a=x/2;
            lp.width =x+a;
            lp.height =x+a;
        }else{
            lp.width=x;
            lp.height=y;
        }
        v.setLayoutParams(lp);
    }

    public List<Movie> loadFavoriteList() throws Exception {
        List<Movie>list=new ArrayList<>();
        String tv="http://s3.starone.pw:80/stalker_portal/server/load.php?type=itv&action=get_ordered_list&sortby=fav&fav=1" +
                "&genre=*&JsHttpRequest=1-xml";
        String vod="http://s3.starone.pw:80/stalker_portal/server/load.php?type=vod&action=get_ordered_list&sortby=fav&fav=1" +
                "&category=*&JsHttpRequest=1-xml";
        String radio="http://s3.starone.pw:80/stalker_portal/server/load.php?type=radio&action=get_ordered_list&sortby=fav&fav=1" +
                "&JsHttpRequest=1-xml";
        JSONObject obj=new JSONObject(GET(tv));
        JSONArray ob=obj.getJSONObject("js").getJSONArray("data");
        for (int i =0;i<ob.length();i++){
            obj=ob.getJSONObject(i);
            Movie m=new Movie();
            m.setVideoUrl(obj.getString("cmd"));
            m.setType("itv");
            m.setTitle(obj.getString("name"));
            if (obj.has("description")){
                m.setDescription(obj.getString("description"));
            }
            list.add(m);
        }
        obj=new JSONObject(GET(vod));
        ob=obj.getJSONObject("js").getJSONArray("data");
        for (int i =0;i<ob.length();i++){
            obj=ob.getJSONObject(i);
            Movie m=new Movie();
            m.setVideoUrl(obj.getString("cmd"));
            m.setType("vod");
            m.setTitle(obj.getString("name"));
            if (obj.has("description")){
                m.setDescription(obj.getString("description"));
            }
            list.add(m);
        }
        obj=new JSONObject(GET(radio));
        ob=obj.getJSONObject("js").getJSONArray("data");
        for (int i =0;i<ob.length();i++){
            obj=ob.getJSONObject(i);
            Movie m=new Movie();
            m.setVideoUrl(obj.getString("cmd"));
            m.setType("radio");
            m.setTitle(obj.getString("name"));
            if (obj.has("description")){
                m.setDescription(obj.getString("description"));
            }
            list.add(m);
        }

        return list;
    }


    private void Login() throws Exception {
        SharedPreferences sp=context.getSharedPreferences(storage_name,Context.MODE_PRIVATE);
        String data=GET("http://s3.starone.pw:80/stalker_portal/server/load.php?type=stb&action=do_auth&login=" +
                 sp.getString(username_key,"")+
                "&password=" +sp.getString(password_key,"")+
                "&device_id&device_id2&JsHttpRequest=1-xml");
        JSONObject js=new JSONObject(data);
        if (js.has("js")&&js.get("js") instanceof Boolean &&!js.getBoolean("js")){
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Failed to login.\nPlease check your login data.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            handShake();
        }
    }

    private void handShake() throws Exception {
        String result=GET("http://s3.starone.pw:80/stalker_portal/server/load.php?type=stb&action=handshake&token=&prehash=&JsHttpRequest=1-xml");
        JSONObject obj=new JSONObject(result);
        if (obj.has("js")) {
            obj= obj.getJSONObject("js");
            if(obj.has("token")&&obj.has("random")){
                token="Bearer "+obj.getString("token");
                afterHandshake(obj.getString("random"));
            }else{
                handShake();
            }
        }else{
            handShake();
        }
    }

    public List<Movie>getListByCatagory(String gen,String type,int page) throws Exception {
        List<Movie> list=new ArrayList<>();
        String data=GET("http://s3.starone.pw:80/stalker_portal/server/load.php?type="+
                type+"&action=get_ordered_list&p="+page+"&sortby="+
                (type.equals("vod")?"added&category="+page:"number&&genre="+gen)+"&JsHttpRequest=1-xml");
        JSONObject obj=new JSONObject(data);
        obj=obj.getJSONObject("js");
        JSONArray ob=obj.getJSONArray("data");
        for(int i=0;i<ob.length();i++){
            obj=ob.optJSONObject(i);
            Movie movie=new Movie();
            movie.setVideoUrl(obj.getString("cmd"));
            movie.setTitle(obj.getString("name"));
            if (obj.has("description")){
                movie.setDescription(obj.getString("description"));
            }
            movie.setId(Long.valueOf(obj.getString("id")));
            list.add(movie);
        }
        return list;
    }

    public List<Movie> getTvCatagory() throws Exception {
        List<Movie> list=new ArrayList<>();
        String data=GET("http://s3.starone.pw:80/stalker_portal/server/load.php?type=itv&action=get_genres&JsHttpRequest=1-xml");
        JSONObject obj=new JSONObject(data);
        JSONArray ob=obj.getJSONArray("js");


        for(int i=0;i<ob.length();i++){
            obj=ob.optJSONObject(i);
            if (obj.getString("id").equals("*"))continue;
            Movie movie=new Movie();
//            movie.setVideoUrl(obj.getString("cmd"));
            movie.setTitle(obj.getString("title"));
            movie.setType("itv");
            movie.setId(Long.valueOf(obj.getString("id")));
            list.add(movie);
        }

        return list;
    }

    public List<Movie> getRadio() throws Exception {
        List<Movie>list=new ArrayList<>();
        String data=GET("http://s3.starone.pw:80/stalker_portal/server/load.php?type=radio&action=get_ordered_list&all=1&JsHttpRequest=1-xml");
        JSONObject obj=new JSONObject(data);
        obj=obj.getJSONObject("js");
        JSONArray ja=obj.getJSONArray("data");
        for(int i=0;i<ja.length();i++){
            obj=ja.getJSONObject(i);
            Movie movie=new Movie();
            movie.setId(Long.valueOf(obj.getString("id")));
            movie.setTitle(obj.getString("name"));
            movie.setVideoUrl(obj.getString("cmd"));
            movie.setType("radio");
            list.add(movie);
        }
        return list;
    }
    public List<Movie> getVideoCatagory() throws Exception {
        List<Movie>list=new ArrayList<>();
        String data=GET("http://s3.starone.pw:80/stalker_portal/server/load.php?type=vod&action=get_categories&JsHttpRequest=1-xml");
        JSONObject obj=new JSONObject(data);

        JSONArray ob=obj.getJSONArray("js");

        for(int l=0;l<ob.length();l++) {
                obj = ob.getJSONObject(l);
                if (obj.getString("id").equals("*"))continue;
                Movie movie = new Movie();
                movie.setTitle(obj.getString("title"));
//                movie.setVideoUrl(obj.getString("cmd"));
                movie.setType("vod");
//                movie.setDescription(obj.getString("description"));
                movie.setId( Long.valueOf(obj.getString("id")));
                list.add(movie);
        }
        return list;
    }
    private String filterBack(String link){
        String result="";
        for (int i =0;i<link.length();i++){
            if (link.charAt(i)=='\\')continue;
            result+=link.charAt(i);
        }
        return result;
    }


    public String getVideoLink(String link,String type) throws Exception {
        if(type==null||type.equals("radio")){
            return link;
        }
        String li="http://s3.starone.pw:80/stalker_portal/server/load.php?type="+type +"&action=create_link&JsHttpRequest=1-xml";
        if (type.equals("vod")){
            li+="&cmd="+link;
        }else{
            li+="&cmd="+link.replaceAll(" ","%20");
        }
        String data=GET(li);
        JSONObject obj=new JSONObject(data);
        obj=obj.getJSONObject("js");
        String l=filterBack(obj.getString("cmd"));
        if (!l.startsWith("http")){
            l=l.substring(l.indexOf("http"));
        }
//        return getM3u8(l);
        return l;
    }

    private String get(String link) throws Exception {
        OkHttpClient ok=new OkHttpClient();
        Request req=new Request.Builder().url(link).addHeader("Accept-Encoding","identity").build();
        return ok.newCall(req).execute().body().string();
    }





    private String encode(String encode) throws UnsupportedEncodingException {
        return URLEncoder.encode(encode, StandardCharsets.UTF_8.toString());
    }

public String getDeviceSerialNumber() throws Exception{
        SharedPreferences sp=context.getSharedPreferences(storage_name,Context.MODE_PRIVATE);
        MessageDigest md=MessageDigest.getInstance("MD5");
        String number=""+System.currentTimeMillis();
        md.update(number.getBytes(),0,number.length());
        number=new BigInteger(1,md.digest()).toString(16);
        number=number.length()>16?number.substring(0,16):number;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                number = Build.getSerial();
            }
        }catch (Exception e){}

    String snumber=sp.getString(serial_key,number);
    SharedPreferences.Editor she=sp.edit();
    she.putString(serial_key,snumber);
    she.commit();
    return snumber;
}


    private void afterHandshake(String random) throws Exception {
        long time=System.currentTimeMillis();
        String link="http://s3.starone.pw:80/stalker_portal/server/load.php?type=stb&action=get_profile&hd=1&ver=" +
                encode("ImageDescription: " + "0.2.18-r14-pub-250" + "; ImageDate: " + (new Date()).toString() + "; PORTAL version: "
                + "5.2.0" + "; API Version: " + "134"+"; Player Engine version: 0x566" ).replaceAll("\\+","%20")+
                "&" +
                "num_banks=2&" +
                "sn="+getDeviceSerialNumber()+
                "&stb_type=MAG250&" +
                "client_type=STB&" +
                "image_version=218&" +
                "video_out=hdmi&" +
                "device_id=&" +
                "device_id2=&" +
                "signature=&" +
                "auth_second_step=1" +
                "&hw_version=1.7-BD-00&" +
                "not_valid_token=0&" +
                "metrics=" +
                encode("{\"mac\":\""+mac_address+"\"," +
                "\"sn\":\""+getDeviceSerialNumber()+
                "\",\"model\":\"MAG250\"," +
                "\"type\":\"STB\"," +
                "\"uid\":\"\"," +
                "\"random\":\""+random+"\"}").replaceAll("\\+","%20")+
                "&hw_version_2="+random+System.currentTimeMillis()+"&" +
                "timestamp="+((long)(time/1000))
                +"&api_signature=262&" +
                "prehash=&JsHttpRequest=1-xml";

        String data=GET(link);
        JSONObject obj=new JSONObject(data);
        obj=obj.getJSONObject("js");
        final JSONObject j=obj;
        if (obj.has("block_msg")){
            h.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(context, j.getString("block_msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                    }
                }
            });
        }
        if (obj.has("msg")&&obj.getString("msg").equals("Not valid MAC")){
            h.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        SharedPreferences sp=context.getSharedPreferences(storage_name,Context.MODE_PRIVATE);
                        String name=sp.getString(username_key,"");
                        if (name.isEmpty()) {
                            TriggerLogin();
                        }else{
                            Thread th=new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        Login();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            th.start();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String GET(String link) throws Exception {
        OkHttpClient ok=new OkHttpClient();
        Request r=new Request.Builder().url(link)
                .addHeader("Connection","keep-alive")
                .addHeader("Accept","*/*")
                .addHeader("Authorization",token)
                .addHeader("X-User-Agent","Model: MAG250; Link: WiFi")
                .addHeader("Accept-Encoding","identity")
                .addHeader("User-Agent","Mozilla/5.0 (QtEmbedded; U; Linux; C) AppleWebKit/533.3 (KHTML, like Gecko) MAG200 stbapp ver: 2 rev: 250 Safari/533.3")
                .addHeader("Cookie",getCookie()).build();

        Response res=ok.newCall(r).execute();
        if (!res.isSuccessful()){
            throw new Exception("Failed to connect");
        }


        String response=res.body().string();
        if(response.equals("Authorization failed.")||response.equals("Unauthorized request.")){
            handShake();
            return GET(link);
        }
        return response;
    }
public String getSystemMac(){
    Random r=new Random();
    String mac="00:"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16))+":"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16))+":"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16))+":"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16))+":"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16))+":"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16))+":"+
            Integer.toHexString(r.nextInt(16))+Integer.toHexString(r.nextInt(16));
    mac=mac.toUpperCase();
    SharedPreferences sp=context
            .getSharedPreferences(storage_name,Context.MODE_PRIVATE);
    mac=sp.getString(mac_key,mac);

    SharedPreferences.Editor she=sp.edit();
    she.putString(mac_key,mac);
    she.commit();
    return mac;
}

    private String getCookie() throws Exception {
        MessageDigest m=MessageDigest.getInstance("MD5");

        SharedPreferences sp=context
                .getSharedPreferences(storage_name,Context.MODE_PRIVATE);
        String address =getSystemMac();
        String data=address+System.currentTimeMillis();
        m.update(data.getBytes(),0,data.length());
        String hash=new BigInteger(1,m.digest()).toString(16);
        String aid=sp
                .getString(aid_key,hash);
        SharedPreferences.Editor she=sp.edit();
        she.putString(aid_key,aid);
        she.commit();
        TimeZone tz = TimeZone.getDefault();
        mac_address=address;
        return "" +
                "mac=" +encode(address).replaceAll("\\+","%20")+
                "; " +
                "stb_lang=en;" +
                " timezone=" +encode(tz.getDisplayName(false, TimeZone.SHORT)).replaceAll("\\+","%20")+
                "; " +
                "adid="+ aid;
    }

    private void TriggerLogin() throws Exception {
        if(!isshowing) {
            isshowing = true;
            final Dialog d = new Dialog(context);
            d.setContentView(R.layout.loging);
            d.setCancelable(false);
            final EditText mac = d.findViewById(R.id.login_mac_address), serial_number = d.findViewById(R.id.login_serial_number), user = d.findViewById(R.id.login_username), pass = d.findViewById(R.id.login_password);
            getCookie();
            mac.setText(mac_address);
            serial_number.setText(getDeviceSerialNumber());
            d.findViewById(R.id.login_dialog_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mac.getText().toString().isEmpty()) {
                        mac.setError("Fill it");
                        return;
                    }
                    if (serial_number.getText().toString().isEmpty()) {
                        serial_number.setError("Fill it");
                        return;
                    }
                    if (user.getText().toString().isEmpty()) {
                        user.setError("Fill it");
                        return;
                    }
                    if (pass.getText().toString().isEmpty()) {
                        pass.setError("Fill it");
                        return;
                    }
                    mac_address = mac.getText().toString();
                    SharedPreferences.Editor she = context.getSharedPreferences(storage_name, Context.MODE_PRIVATE).edit();
                    she.putString(mac_key, mac_address);
                    she.putString(serial_key, serial_number.getText().toString());
                    she.putString(username_key, user.getText().toString());
                    she.putString(password_key, pass.getText().toString());
                    she.commit();
                    isshowing=false;
                    Thread th=new Thread(){
                        @Override
                        public void run() {
                            try {
                                Check();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    th.start();
                    d.dismiss();
                }
            });
            d.show();

        }
    }
}
