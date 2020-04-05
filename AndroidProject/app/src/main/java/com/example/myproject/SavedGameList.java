package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//gets information on user's saved games and puts them into listview

public class SavedGameList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView savedGamesList1;
    MyDatabaseHelper dbHelper;
    SimpleCursorAdapter adapter;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_game_list);

        savedGamesList1 = (ListView) findViewById(R.id.SavedGamesThe);
        dbHelper =  new MyDatabaseHelper(this);


        getSupportLoaderManager().initLoader(1, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.adapter_view_layout, cursor, new String[]{dbHelper.KEY_NAME, dbHelper.KEY_RELEASEDATE, dbHelper.KEY_PLAYTIME, dbHelper.KEY_ROWID, dbHelper.KEY_BACKGROUNDIMG}, new int[]{R.id.gameName, R.id.releaseDate, R.id.rating, R.id.invisibleView, R.id.gamePicture} ,0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            /** Binds the Cursor column defined by the specified index to the specified view */
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() == R.id.gamePicture) {

                ImageView iconImageView = (ImageView) view;



                Picasso.get().load(Uri.parse(cursor.getString(columnIndex))).resize(450, 235).into(iconImageView);




                return true;
            } else {  // Process the rest of the adapter with default settings.
                return false;
            }
        }
        });
        savedGamesList1.setAdapter(adapter);

        savedGamesList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d("myapp", "onItemClick(" + i + ")");
                Intent nextActivity = new Intent(getApplicationContext(), SavedGameViewer.class);
                TextView pos = (TextView) view.findViewById(R.id.invisibleView);
                String it = pos.getText().toString();
                nextActivity.putExtra("Index", it);
                nextActivity.putExtra("Index2", Long.toString(l));

                startActivity(nextActivity);
            }
        });

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("myapp", "onCreateLoader");

        return new CursorLoader(this, MyContentProvider.CONTENT_URI, new String[]{"_id", "name", "releaseDate", "playtime", "backgroundimg"},null,null,null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("myapp", "swap adapter data");

        adapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
