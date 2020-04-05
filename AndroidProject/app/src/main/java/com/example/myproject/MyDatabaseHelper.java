package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


//makes user's saved games database

public class MyDatabaseHelper extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "theSavedGames.db";
    private static final int DATABASE_VERSION = 1;


    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_RELEASEDATE = "releaseDate";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PLAYTIME = "playtime";
    public static final String KEY_BACKGROUNDIMG = "backgroundimg";

    private static final String TAG = "myapp";
    public static final String SQLITE_TABLE = "Game";

    private static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_NAME + " text," +
                    KEY_RELEASEDATE + " text," +
                    KEY_DESCRIPTION + " text," +
                    KEY_BACKGROUNDIMG + " text," +
                    KEY_PLAYTIME +" text" +
                    ");";




    MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
        Log.d(TAG, "creatingDatabase");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from Version " +oldVersion+ " to " +newVersion+ " , which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }

    public boolean insertData(String name, String release, String desc, String playtime, String bgimg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_RELEASEDATE, release);
        values.put(KEY_DESCRIPTION, desc);
        values.put(KEY_PLAYTIME, playtime);
        values.put(KEY_BACKGROUNDIMG, bgimg);
        long result = db.insert(SQLITE_TABLE, null, values);
        Log.d(TAG, Integer.toString((int) result));
        if(result == -1){
            Log.d(TAG, "insert data not working");

            return false;
        }
        else {
            Log.d(TAG, "insert IS working");
            return true;
        }

    }




}
