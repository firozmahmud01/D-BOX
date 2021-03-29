package com.firoz.mahmud.d_box;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Fragment {
    //static variablas
    public static String splash_key="Splash_Key";




    private Context context;
    private Api api;
    public Settings(Context context){
        this.context=context;
        api=new Api(context,null);
    }

    //interface
    EditText mac,user,pass,snumber,portal;
    CheckBox splash;





    //variables
    SharedPreferences sp;
    SharedPreferences.Editor spe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        //assing
        mac=view.findViewById(R.id.settings_mac_edittext);
        snumber=view.findViewById(R.id.setting_serial_number_edittext);
        pass=view.findViewById(R.id.settings_password_edittext);
        user=view.findViewById(R.id.settings_User_name_Edittext);
        splash=view.findViewById(R.id.settings_checkBox);
        portal=view.findViewById(R.id.settings_portal_edittext);




        sp=view.getContext().getSharedPreferences(Api.storage_name,Context.MODE_PRIVATE);
        spe=sp.edit();
        mac.setText(api.getSystemMac());
        try {
            snumber.setText(api.getDeviceSerialNumber());
        }catch (Exception e){}
        portal.setText(sp.getString(Api.portal_key,""));
        user.setText(sp.getString(Api.username_key,""));
        pass.setText(sp.getString(Api.password_key,""));
        splash.setChecked(sp.getBoolean(splash_key,true));
        splash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spe.putBoolean(splash_key,isChecked);
                spe.commit();
            }
        });


        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spe.putString(Api.username_key,s.toString());
                spe.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        portal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spe.putString(Api.portal_key,s.toString());
                spe.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spe.putString(Api.password_key,s.toString());
                spe.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spe.putString(Api.mac_key,s.toString());
                spe.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        snumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spe.putString(Api.serial_key,s.toString());
                spe.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
}