package com.leondroid.fluber.application;

import android.content.Context;
import android.content.pm.PackageManager;

public class MetaData {
    public static final String TAG = MetaData.class.getName();

    public static String getFlickRApiKey(Context context) throws Exception {
        return context
                .getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                .metaData
                .getString("FLICKR_API_KEY");
    }
}
