package com.firoz.mahmud.d_box;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyLog","working");
        PackageManager pm=context.getPackageManager();
        context.startActivity(pm.getLaunchIntentForPackage("com.firoz.mahmud.d_box"));
    }

}