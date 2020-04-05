package com.example.myproject;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//this class connects to RAWG database and gets data on games. Also sets adapter.

public class FetchData extends AppCompatActivity{

    MainGameListActivity mainGameListActivity;
    Context context;

    String next = "";
    String previous = "";
    public boolean ifFetched = false;

    ArrayList<Result> mObjects = new ArrayList<Result>();
   RequestQueue mRequestQueue;

   protected void onCreate(Bundle savedInstanceData) {
       super.onCreate(savedInstanceData);




   }

   FetchData(Context context){

       this.context = context;
   }


    String [] res;
    public void runthread(final String uri){

        mRequestQueue = Volley.newRequestQueue(context);

        new Thread() {
            public void run() {

                try {

                    Log.d("myapp", "the COMPLETE START");

                     URL url = new URL(uri);
                //    String url = "https://api.rawg.io/api/games";
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    JSONObject jsonObject = null;

                    String line;
                    while ((line = bufferedReader.readLine()) != null){

                        stringBuilder.append(line);
                    }

                    JSONTokener jsonTokener = new JSONTokener(stringBuilder.toString());
                    JSONObject response = new JSONObject(jsonTokener);

                    next = response.getString("next");
                    previous = response.getString("previous");


                    Log.d("myapp", "Got Response");



                            try {
                                JSONArray jsonArray = response.getJSONArray("results");
                                Log.d("myapp", "jsonarray list size " + Integer.toString(jsonArray.length()));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject result = jsonArray.getJSONObject(i);

                                    int id = result.getInt("id");
                                    String slug = result.getString("slug");
                                    String name = result.getString("name");
                                    String released = result.getString("released");
                                    boolean tba = result.getBoolean("tba");
                                    String backgroundImg = result.getString("background_image");
                                    Double rating = result.getDouble("rating");
                                    Double rating_top = result.getDouble("rating_top");
                                    mObjects.add(new Result(id, slug, name, released, tba, backgroundImg, rating, rating_top));
                                }



                                //add to sqlite
                                Log.d("myapp", "object list size " + Integer.toString(mObjects.size()));
                                ifFetched = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("myapp", "JSONEXCEPTION");

                            }
                        } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("myapp", "intheEnd");


                try {
                    if (ifFetched) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                synchronized (MainGameListActivity.next) {
                                    MainGameListActivity.next = next;

                                }

                                synchronized (MainGameListActivity.previous) {
                                    MainGameListActivity.previous = previous;
                                }

                                if(MainGameListActivity.adapter != null) {

                                    Log.d("myapp", "trying to update adapter");
                                 //   MainGameListActivity.updateAdapter();
                                    MainGameListActivity.setAdapter(mObjects);

                                    putIfFetchableToFalse();

                                }
                                else {
                                    Log.d("myapp", "trying to set adapter the first time");

                                    MainGameListActivity.setAdapter(mObjects);
                                    putIfFetchableToFalse();

                                }
                            }
                        });
                    }
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }.start();
    }


    public void clearmObjects(){

        mObjects.clear();

    }

    public boolean checkIfCanStartIntent(){

        return ifFetched;
    }

    public void putIfFetchableToFalse(){

        ifFetched = false;
    }

    public String getPrevious(){

        return previous;
    }

    public String getNext(){
        return next;
    }

    public ArrayList<Result> getmObjects(){
        return mObjects;
    }
}

