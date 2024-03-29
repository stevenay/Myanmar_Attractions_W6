package me.naylinaung.myanmar_attractions_w6.data.persistence;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by NayLinAung on 7/13/2016.
 */
public class AttractionProvider extends ContentProvider {

    //region Variable Creation
    public static final int ATTRACTION = 100;
    public static final int ATTRACTION_IMAGE = 200;
    public static final int USER = 300;

    private static final String sAttractionTitleSelection = AttractionsContract.AttractionEntry.COLUMN_TITLE + " = ?";
    private static final String sAttractionImageSelectionWithTitle = AttractionsContract.AttractionImageEntry.COLUMN_ATTRACTION_TITLE + " = ?";
    private static final String sUserSelectionWithUserEmail = AttractionsContract.UserEntry.COLUMN_EMAIL + " = ?";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private AttractionDBHelper mAttractionDBHelper;
    //endregion

    @Override
    public boolean onCreate() {
        mAttractionDBHelper = new AttractionDBHelper(getContext());
        return true;
    }

    // Logic between Content Provider and Sqlite
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor queryCursor;

        int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case ATTRACTION:
                String attractionTitle = AttractionsContract.AttractionEntry.getTitleFromParam(uri);
                if (!TextUtils.isEmpty(attractionTitle)) {
                    selection = sAttractionTitleSelection;
                    selectionArgs = new String[]{attractionTitle};
                }
                queryCursor = mAttractionDBHelper.getReadableDatabase().query(AttractionsContract.AttractionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, //group_by
                        null, //having
                        sortOrder);
                break;
            case ATTRACTION_IMAGE:
                String title = AttractionsContract.AttractionImageEntry.getAttractionTitleFromParam(uri);
                if (title != null) {
                    selection = sAttractionImageSelectionWithTitle;
                    selectionArgs = new String[]{title};
                }
                queryCursor = mAttractionDBHelper.getReadableDatabase().query(AttractionsContract.AttractionImageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case USER:
                String userEmail = AttractionsContract.UserEntry.getUserEmailFromParam(uri);
                if (userEmail != null) {
                    selection = sUserSelectionWithUserEmail;
                    selectionArgs = new String[]{userEmail};
                }
                queryCursor = mAttractionDBHelper.getReadableDatabase().query(AttractionsContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        Context context = getContext();
        if (context != null) {
            queryCursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return queryCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mAttractionDBHelper.getWritableDatabase();
        final int matchUri = sUriMatcher.match(uri);
        Uri insertedUri;

        switch (matchUri) {
            case ATTRACTION: {
                long _id = db.insert(AttractionsContract.AttractionEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    insertedUri = AttractionsContract.AttractionEntry.buildAttractionUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case ATTRACTION_IMAGE: {
                long _id = db.insert(AttractionsContract.AttractionImageEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    insertedUri = AttractionsContract.AttractionImageEntry.buildAttractionImageUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case USER: {
                long _id = db.insert(AttractionsContract.UserEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    insertedUri = AttractionsContract.UserEntry.buildUserUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return insertedUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mAttractionDBHelper.getWritableDatabase();
        String tableName = getTableName(uri);
        int insertedCount = 0;

        try {
            db.beginTransaction();
            for (ContentValues cv : values) {
                long _id = db.insert(tableName, null, cv);
                if (_id > 0) {
                    insertedCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return insertedCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mAttractionDBHelper.getWritableDatabase();
        int rowDeleted;
        String tableName = getTableName(uri);
        String userEmail = AttractionsContract.UserEntry.getUserEmailFromParam(uri);
        if (userEmail != null) {
            selection = sUserSelectionWithUserEmail;
            selectionArgs = new String[]{userEmail};
        }

        rowDeleted = db.delete(tableName, selection, selectionArgs);
        Context context = getContext();
        if (context != null && rowDeleted > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mAttractionDBHelper.getWritableDatabase();
        int rowUpdated;
        String tableName = getTableName(uri);

        rowUpdated = db.update(tableName, contentValues, selection, selectionArgs);
        Context context = getContext();
        if (context != null && rowUpdated > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AttractionsContract.CONTENT_AUTHORITY, AttractionsContract.PATH_ATTRACTIONS, ATTRACTION);
        uriMatcher.addURI(AttractionsContract.CONTENT_AUTHORITY, AttractionsContract.PATH_ATTRACTION_IMAGES, ATTRACTION_IMAGE);
        uriMatcher.addURI(AttractionsContract.CONTENT_AUTHORITY, AttractionsContract.PATH_USERS, USER);

        return uriMatcher;
    }

    private String getTableName(Uri uri) {
        final int matchUri = sUriMatcher.match(uri);

        switch (matchUri) {
            case ATTRACTION:
                return AttractionsContract.AttractionEntry.TABLE_NAME;
            case ATTRACTION_IMAGE:
                return AttractionsContract.AttractionImageEntry.TABLE_NAME;
            case USER:
                return AttractionsContract.UserEntry.TABLE_NAME;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case ATTRACTION:
                return AttractionsContract.AttractionEntry.DIR_TYPE;
            case ATTRACTION_IMAGE:
                return AttractionsContract.AttractionImageEntry.DIR_TYPE;
            case USER:
                return AttractionsContract.AttractionImageEntry.ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
    }
}
