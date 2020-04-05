package com.example.myproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//runs a thread to get description and other information on one game from RAWG database and saves the game into SQLite database if wanted

public class GameInfo extends Fragment {

    public static TextView text1;
    public static TextView text2;
    public static TextView text3;
    public static TextView text4;
    public static ImageView fimage;
    public Button saveButton;
    GameDescFetch gameDescFetch;

    String urlbegin = "https://api.rawg.io/api/games/";


    public Context context;
    Toast toast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_info, container, false);

        context = getContext();

        text1 = (TextView) v.findViewById(R.id.FgameName);
        text2 = (TextView) v.findViewById(R.id.FgameRelease);
        text3 = (TextView) v.findViewById(R.id.FotherInfo);
        text4 = (TextView) v.findViewById(R.id.FgameTime);
        fimage = (ImageView) v.findViewById(R.id.FimageView);
        saveButton = (Button) v.findViewById(R.id.Fsave);

        setOnClick();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString("game_number");
            String fullURL = urlbegin + id;
            gameDescFetch = new GameDescFetch(context);
            gameDescFetch.runthread(fullURL);


         //   text1.setText("TestComplete");


        }

    }

    public void setOnClick() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             MyDatabaseHelper dbHelper =  new MyDatabaseHelper(context);

             insertToDatabase(dbHelper);
                Log.d("myapp", "got the click");
            }
        });

    }

    public void insertToDatabase(MyDatabaseHelper databaseHelper){

            String [] alli = new String[5];
            alli = gameDescFetch.getAllInfo();

            Log.d("myapp", "in inserting data void");

          boolean alreadyexists = Exists(databaseHelper, alli[0]);
          if(alreadyexists == false) {
              Log.d("myapp", "inserting data in database");

              databaseHelper.insertData(alli[0], alli[1], alli[2], alli[3], alli[4]);
              toast = Toast.makeText(context, "You have saved to Database", Toast.LENGTH_SHORT);
              toast.show();

          }
          else {

              Log.d("myapp", "NOT inserting data database");
              toast = Toast.makeText(context, "Already saved", Toast.LENGTH_SHORT);
              toast.show();

          }


        }


    public boolean Exists(MyDatabaseHelper dbhelper, String searchItem) {

        Log.d("myapp", "checking database before inserting");


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
