package com.example.myproject;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    // database
    private MyDatabaseHelper database;
    Context context;

    // used for the UriMacher
    private static final int GAMES = 10;
    private static final int GAME_ID = 20;

    private static final String AUTHORITY = "com.example.myproject.contentprovider";

    private static final String BASE_PATH = "theGames";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/theGames";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/theGame";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, GAMES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", GAME_ID);
    }

    @Override
    public boolean onCreate() {
        database = new MyDatabaseHelper(getContext());
        context = getContext();
        Log.d("myapp", "ONCREATE CONTENT PROVIDER");
        if(database != null) { Log.d("myapp", "Database not null oncreate");}

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        //  checkColumns(projection);

        // Set the table
        queryBuilder.setTables(database.SQLITE_TABLE);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case GAMES:
                break;
            case GAME_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(database.KEY_ROWID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d("myapp", "QURSORQUERY");
        if(database != null) { Log.d("myapp", "Database not null cursor query");}

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case GAMES:
                id = sqlDB.insert(database.SQLITE_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

      if(database != null) { Log.d("myapp", "Database not null Insert");}

        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        if(database != null) { Log.d("myapp", "Database not null before delete database getting");}

        SQLiteDatabase sqlDB = database.getWritableDatabase();

        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case GAMES:
                rowsDeleted = sqlDB.delete(database.SQLITE_TABLE, selection,
                        selectionArgs);
                break;
            case GAME_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            database.SQLITE_TABLE,
                            database.KEY_ROWID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            database.SQLITE_TABLE,
                            database.KEY_ROWID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    public int tryingDel(Uri uri, String selection, String[] selectionArgs, MyDatabaseHelper helper, Context contexttry){

        if(database != null) { Log.d("myapp", "Database not null before delete database getting");}

        SQLiteDatabase sqlDB = helper.getWritableDatabase();

        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case GAMES:
                rowsDeleted = sqlDB.delete(helper.SQLITE_TABLE, selection,
                        selectionArgs);
                break;
            case GAME_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            helper.SQLITE_TABLE,
                            helper.KEY_ROWID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            helper.SQLITE_TABLE,
                            helper.KEY_ROWID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        contexttry.getContentResolver().notifyChange(uri, null);

        Log.d("myapp", "context");
        return rowsDeleted;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case GAMES:
                rowsUpdated = sqlDB.update(database.SQLITE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case GAME_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(database.SQLITE_TABLE,
                            values,
                            database.KEY_ROWID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(database.SQLITE_TABLE,
                            values,
                            database.KEY_ROWID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        if(database != null) { Log.d("myapp", "Database not null update");}

        return rowsUpdated;
    }
}
