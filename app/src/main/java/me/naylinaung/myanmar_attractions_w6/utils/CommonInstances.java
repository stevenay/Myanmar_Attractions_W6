package me.naylinaung.myanmar_attractions_w6.utils;

import com.google.gson.Gson;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class CommonInstances {

    private static Gson gson = new Gson();
    public static Gson getGsonInstance() {
        return gson;
    }
}
