package me.naylinaung.myanmar_attractions_w6.data.vos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

import me.naylinaung.myanmar_attractions_w6.MyanmarAttractionsApp;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionVO {

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String desc;

    @SerializedName("images")
    private String[] images;

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public static void saveAttractions(List<AttractionVO> attractionList) {
        Context context = MyanmarAttractionsApp.getContext();
        ContentValues[] attractionCVs = new ContentValues[attractionList.size()];
        for (int index = 0; index < attractionList.size(); index++) {
            AttractionVO vo = attractionList.get(index);
            attractionCVs[index] = vo.parseToContentValues();

            // Bulk insert
            AttractionVO.saveAttractionImages(vo.getTitle(), vo.getImages());
        }

        // Bulk insert into Attractions.
        int insertedCount = context.getContentResolver().bulkInsert(AttractionsContract.AttractionEntry.CONTENT_URI, attractionCVs);

        Log.d(MyanmarAttractionsApp.TAG, "Bulk inserted into Attractions Table.");
    }

    private static void saveAttractionImages(String title, String[] images) {
        ContentValues[] attractionImagesCVs = new ContentValues[images.length];
        for (int index = 0; index < images.length; index++) {
            String image = images[index];

            ContentValues cv = new ContentValues();
            cv.put(AttractionsContract.AttractionImageEntry.COLUMN_ATTRACTION_TITLE, title);
            cv.put(AttractionsContract.AttractionImageEntry.COLUMN_IMAGE, image);

            attractionImagesCVs[index] = cv;
        }

        Context context = MyanmarAttractionsApp.getContext();
        context.getContentResolver().bulkInsert(AttractionsContract.AttractionImageEntry.CONTENT_URI, attractionImagesCVs);

        Log.d(MyanmarAttractionsApp.TAG, "Bulk inserted into Attraction Images Table");
    }

    private ContentValues parseToContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(AttractionsContract.AttractionEntry.COLUMN_TITLE, this.getTitle());
        cv.put(AttractionsContract.AttractionEntry.COLUMN_DESC, this.getDesc());
        return cv;
    }

    public static AttractionVO parseFromCursor(Cursor data) {
        AttractionVO attraction = new AttractionVO();
        attraction.title = data.getString(data.getColumnIndex(AttractionsContract.AttractionEntry.COLUMN_TITLE));
        attraction.desc = data.getString(data.getColumnIndex(AttractionsContract.AttractionEntry.COLUMN_DESC));
        return attraction;
    }

    public static String[] loadAttractionImagesByTitle(String title) {
        Context context = MyanmarAttractionsApp.getContext();
        ArrayList<String> images = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(AttractionsContract.AttractionImageEntry.buildAttractionImageUriWithTitle(title),
                null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            do {
                images.add(cursor.getString(cursor.getColumnIndex(AttractionsContract.AttractionImageEntry.COLUMN_IMAGE)));
            } while (cursor.moveToNext());
        }

        String[] imageArray = new String[images.size()];
        images.toArray(imageArray);
        return imageArray;
    }
}
