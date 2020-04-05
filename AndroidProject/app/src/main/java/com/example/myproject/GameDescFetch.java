package com.example.myproject;

import android.content.Context;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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

//fetches data on one game from RAWG database

public class GameDescFetch extends AppCompatActivity {

    Context context;

    RequestQueue mRequestQueue;
    public boolean ifFetched = false;

    int id;
    String slug;
    String name;
    String released;
    boolean tba;
    String backgroundImg;
    Double rating;
    Double rating_top;
    String description;
    String pureDesc;
    Double playtime;

    String [] allInfo = new String[5];

    GameDescFetch(Context context){

        this.context = context;
    }


    public void runthread(final String uri){



        new Thread() {
            public void run() {

                try {

                    Log.d("myapp", "the COMPLETE START");

                    URL url = new URL(uri);
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




                    Log.d("myapp", "Got Response");



                    try {





                             id = response.getInt("id");
                             slug = response.getString("slug");
                             name = response.getString("name");
                             released = response.getString("released");
                             tba = response.getBoolean("tba");
                             backgroundImg = response.getString("background_image");
                             rating = response.getDouble("rating");
                             rating_top = response.getDouble("rating_top");
                             description = response.getString("description");
                        //     pureDesc = description.replaceAll("\\<.*?\\>", "");
                             pureDesc = android.text.Html.fromHtml(description).toString();
                             playtime = response.getDouble("playtime");




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

                                GameInfo.text1.setText(name);
                                GameInfo.text2.setText(released);
                                GameInfo.text3.setText(pureDesc);
                                GameInfo.text3.setMovementMethod(new ScrollingMovementMethod());
                                GameInfo.text4.setText("playtime " + playtime.toString());
                                Picasso.get().load(Uri.parse(backgroundImg)).resize(128, 128).into(GameInfo.fimage);



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


    public String[] getAllInfo(){

           allInfo[0] = name;
           allInfo[1] = released;
           allInfo[2] = pureDesc;
           allInfo[3] = "playtime " + playtime.toString();
           allInfo[4] = backgroundImg;
        return allInfo;
    }

}
