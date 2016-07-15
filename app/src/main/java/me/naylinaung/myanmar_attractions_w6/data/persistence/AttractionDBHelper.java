package me.naylinaung.myanmar_attractions_w6.data.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract.AttractionEntry;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract.AttractionImageEntry;
import me.naylinaung.myanmar_attractions_w6.data.persistence.AttractionsContract.UserEntry;
/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "attractions.db";

    private static final String SQL_CREATE_ATTRACTION_TABLE = "CREATE TABLE " + AttractionEntry.TABLE_NAME + " (" +
            AttractionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AttractionEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            AttractionEntry.COLUMN_DESC + " TEXT NOT NULL, " +

            " UNIQUE (" + AttractionEntry.COLUMN_TITLE + ") ON CONFLICT IGNORE" +
            " );";

    private static final String SQL_CREATE_ATTRACTION_IMAGE_TABLE = "CREATE TABLE " + AttractionImageEntry.TABLE_NAME + " (" +
            AttractionImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AttractionImageEntry.COLUMN_ATTRACTION_TITLE + " TEXT NOT NULL, "+
            AttractionImageEntry.COLUMN_IMAGE + " TEXT NOT NULL, "+

            " UNIQUE (" + AttractionImageEntry.COLUMN_ATTRACTION_TITLE + ", "+
            AttractionImageEntry.COLUMN_IMAGE + ") ON CONFLICT IGNORE" +
            " );";

    private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
            UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UserEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
            UserEntry.COLUMN_ACCESS_TOKEN + " TEXT NOT NULL, " +
            UserEntry.COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
            UserEntry.COLUMN_COUNTRY_OF_ORIGIN + " TEXT NOT NULL, " +

            " UNIQUE (" + UserEntry.COLUMN_EMAIL + ") ON CONFLICT IGNORE" +
            " );";

    public AttractionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ATTRACTION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ATTRACTION_IMAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AttractionImageEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AttractionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);

        this.onCreate(sqLiteDatabase);
    }
}
