package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


//activity to show information on one game from user's database

public class SavedGameViewer extends AppCompatActivity{


    MyDatabaseHelper dbHelper;
  // SimpleCursorAdapter adapter;
    MyContentProvider provider;
   // Cursor cursor;
    int theIndex;
 //   long index2;
    SQLiteDatabase database;

    TextView firstView;
    TextView secondView;
    TextView thirdView;
    TextView fourthView;
    ImageView img;
    Button DeleteB;

    String mname;
    String mdesc;
    String mreleaseD;
    String mplayT;
    String imgurl;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_game_viewer);

        dbHelper =  new MyDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();
        provider = new MyContentProvider();

        Bundle bundle = getIntent().getExtras();
       String sentIndex = bundle.getString("Index");
       theIndex = Integer.parseInt(sentIndex);

       firstView = (TextView) findViewById(R.id.testView);
        secondView = (TextView) findViewById(R.id.DescInfo);
        thirdView = (TextView) findViewById(R.id.SRelease);
        fourthView = (TextView) findViewById(R.id.SPlayT);
        img = (ImageView) findViewById(R.id.SimageView);
        DeleteB = (Button) findViewById(R.id.Delbutton);


        context = getApplicationContext();


        mname = getName(Integer.toString(theIndex));
        mdesc = getDesc(Integer.toString(theIndex));
        mreleaseD = getReleaseD(Integer.toString(theIndex));
        mplayT = getPlayT(Integer.toString(theIndex));
        imgurl = getImage(Integer.toString(theIndex));




        firstView.setText(mname);
        secondView.setText(mdesc);
        secondView.setMovementMethod(new ScrollingMovementMethod());
        thirdView.setText(mreleaseD);
        fourthView.setText(mplayT);
        Picasso.get().load(Uri.parse(imgurl)).resize(146, 104).into(img);

        setOnDel();

    }



    public void setOnDel(){

    DeleteB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           boolean exists = Exists(dbHelper, mname);

           if(exists) {
               deleteRow(Integer.toString(theIndex));
           }
           else {

               Toast toast = Toast.makeText(context, "Already Deleted", Toast.LENGTH_SHORT);
               toast.show();
           }
        }
     });
    }



    public String getName(String s){

        Log.d("myapp", "the number is " +s);

        String[] columns = new String[]{MyDatabaseHelper.KEY_ROWID,MyDatabaseHelper.KEY_NAME,MyDatabaseHelper.KEY_DESCRIPTION,MyDatabaseHelper.KEY_RELEASEDATE, MyDatabaseHelper.KEY_PLAYTIME, MyDatabaseHelper.KEY_BACKGROUNDIMG};
        Cursor c = database.query(MyDatabaseHelper.SQLITE_TABLE, columns, MyDatabaseHelper.KEY_ROWID + "=" + s, null, null, null, null);// change this line in this method
        if(c!=null)
        {
            c.moveToFirst();
            String name = c.getString(c.getColumnIndex("name"));
            return name;
        }
        return null;

    }

    public String getDesc(String s){

        Log.d("myapp", s);

        String[] columns = new String[]{MyDatabaseHelper.KEY_ROWID,MyDatabaseHelper.KEY_NAME,MyDatabaseHelper.KEY_DESCRIPTION,MyDatabaseHelper.KEY_RELEASEDATE, MyDatabaseHelper.KEY_PLAYTIME, MyDatabaseHelper.KEY_BACKGROUNDIMG};
        Cursor c = database.query(MyDatabaseHelper.SQLITE_TABLE, columns, MyDatabaseHelper.KEY_ROWID + "=" + s, null, null, null, null);// change this line in this method
        if(c!=null)
        {
            c.moveToFirst();
            String desc = c.getString(c.getColumnIndex("description"));
            return desc;
        }
        return null;

    }

    public String getReleaseD(String s){

        Log.d("myapp", s);

        String[] columns = new String[]{MyDatabaseHelper.KEY_ROWID,MyDatabaseHelper.KEY_NAME,MyDatabaseHelper.KEY_DESCRIPTION,MyDatabaseHelper.KEY_RELEASEDATE, MyDatabaseHelper.KEY_PLAYTIME, MyDatabaseHelper.KEY_BACKGROUNDIMG};
        Cursor c = database.query(MyDatabaseHelper.SQLITE_TABLE, columns, MyDatabaseHelper.KEY_ROWID + "=" + s, null, null, null, null);// change this line in this method
        if(c!=null)
        {
            c.moveToFirst();
            String r = c.getString(c.getColumnIndex("releaseDate"));
            return r;
        }
        return null;

    }

    public String getPlayT(String s){

        Log.d("myapp", s);

        String[] columns = new String[]{MyDatabaseHelper.KEY_ROWID,MyDatabaseHelper.KEY_NAME,MyDatabaseHelper.KEY_DESCRIPTION,MyDatabaseHelper.KEY_RELEASEDATE, MyDatabaseHelper.KEY_PLAYTIME, MyDatabaseHelper.KEY_BACKGROUNDIMG};
        Cursor c = database.query(MyDatabaseHelper.SQLITE_TABLE, columns, MyDatabaseHelper.KEY_ROWID + "=" + s, null, null, null, null);// change this line in this method
        if(c!=null)
        {
            c.moveToFirst();
            String playtime = c.getString(c.getColumnIndex("playtime"));
            Log.d("myapp", "this is the playtime: " + playtime);
            return playtime;
        }
        return null;

    }


    public String getImage(String s){

        Log.d("myapp", s);

        String[] columns = new String[]{MyDatabaseHelper.KEY_ROWID,MyDatabaseHelper.KEY_NAME,MyDatabaseHelper.KEY_DESCRIPTION,MyDatabaseHelper.KEY_RELEASEDATE, MyDatabaseHelper.KEY_PLAYTIME, MyDatabaseHelper.KEY_BACKGROUNDIMG};
        Cursor c = database.query(MyDatabaseHelper.SQLITE_TABLE, columns, MyDatabaseHelper.KEY_ROWID + "=" + s, null, null, null, null);// change this line in this method
        if(c!=null)
        {
            c.moveToFirst();
            String bgimgurl = c.getString(c.getColumnIndex("backgroundimg"));
            return bgimgurl;
        }
        return null;

    }


    public void deleteRow(String id){

        int deletedRows = provider.tryingDel(Uri.parse("content://" + "com.example.myproject.contentprovider"
                        + "/" + "theGames"), MyDatabaseHelper.KEY_ROWID + "=" + id,
              null, dbHelper, context);

        Toast toast = Toast.makeText(context, "Deleted from database", Toast.LENGTH_SHORT);
        toast.show();
        //  new String[]{String.valueOf(id)}

    }


    public boolean Exists(MyDatabaseHelper dbhelper, String searchItem) {

        Log.d("myapp", "checking database before deleting");


        String[] columns = { dbhelper.KEY_NAME };
        String selection = dbhelper.KEY_NAME  + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(dbhelper.SQLITE_TABLE, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
