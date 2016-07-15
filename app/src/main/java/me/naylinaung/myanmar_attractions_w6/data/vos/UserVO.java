package me.naylinaung.myanmar_attractions_w6.data.vos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract;

/**
 * Created by NayLinAung on 7/15/2016.
 */
public class UserVO {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    @SerializedName("country_of_origin")
    private String countryOfOrigin;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public static void saveUser(UserVO user) {
        Context context = MyanmarAttractionsApp.getContext();
        ContentValues cv = user.parseToContentValues();

        Uri insertedRow = context.getContentResolver().insert(AttractionsContract.UserEntry.CONTENT_URI, cv);

        Log.d(MyanmarAttractionsApp.TAG, "Login user inserted into User Table.");
    }

    public static void deleteUser(String userEmail) {
        Context context = MyanmarAttractionsApp.getContext();
        int rowDeleted = context.getContentResolver().delete(AttractionsContract.UserEntry.buildUserUriWithEmail(userEmail), null, null);

        Log.d(MyanmarAttractionsApp.TAG, "Login user deleted.");
    }

    private ContentValues parseToContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(AttractionsContract.UserEntry.COLUMN_NAME, this.getName());
        cv.put(AttractionsContract.UserEntry.COLUMN_EMAIL, this.getEmail());
        cv.put(AttractionsContract.UserEntry.COLUMN_ACCESS_TOKEN, this.getAccessToken());
        cv.put(AttractionsContract.UserEntry.COLUMN_DATE_OF_BIRTH, this.getDateOfBirth());
        cv.put(AttractionsContract.UserEntry.COLUMN_COUNTRY_OF_ORIGIN, this.getCountryOfOrigin());
        return cv;
    }

    public static UserVO parseFromCursor(Cursor data) {
        UserVO user = new UserVO();
        user.name = data.getString(data.getColumnIndex(AttractionsContract.UserEntry.COLUMN_NAME));
        user.email = data.getString(data.getColumnIndex(AttractionsContract.UserEntry.COLUMN_EMAIL));
        user.accessToken = data.getString(data.getColumnIndex(AttractionsContract.UserEntry.COLUMN_ACCESS_TOKEN));
        user.dateOfBirth = data.getString(data.getColumnIndex(AttractionsContract.UserEntry.COLUMN_DATE_OF_BIRTH));
        user.countryOfOrigin = data.getString(data.getColumnIndex(AttractionsContract.UserEntry.COLUMN_COUNTRY_OF_ORIGIN));

        return user;
    }
}
