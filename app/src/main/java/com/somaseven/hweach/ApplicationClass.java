package com.somaseven.hweach;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.tsengvn.typekit.Typekit;

/**
 * Created by eminentstar on 2016. 7. 13..
 */
public class ApplicationClass extends Application {
    private static Context context;
    public void onCreate(){
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/BMDOHYEON_otf.otf"));


    }

    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    public static Context getContext() {return context;}
}
