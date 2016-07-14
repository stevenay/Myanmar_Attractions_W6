package me.naylinaung.myanmar_attractions_w6;

import android.app.Application;
import android.content.Context;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class MyanmarAttractionsApp extends Application {

    public static final String TAG = "MyanmarAttractionsApp";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        context = null;
    }

    public static Context getContext() {
        return context;
    }
}
