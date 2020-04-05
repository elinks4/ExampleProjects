package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import android.os.Bundle;


//This class runs thread to get game lists from RAWG Database and makes adapter to put the information to listview

public class MainGameListActivity extends AppCompatActivity {

    public static String next = "";
    public static  String previous = "";
    public static  ArrayList<Result> games;

    public Button prevPage;
    public Button nextPage;
    public static ListView listView1;
    public static MyAdapter adapter = null;

    public static Context cont;

    public boolean ifDissappeared = false;
    String uri;
    FetchData fetchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game_list);
        prevPage = (Button) findViewById(R.id.previousButton);
        nextPage = (Button) findViewById(R.id.nextButton);
        listView1 = (ListView) findViewById(R.id.listView1);

        cont = this;


        if(ifDissappeared){
            listView1.setVisibility(View.VISIBLE);
        }


        uri = "https://api.rawg.io/api/games";

         fetchData = new FetchData(cont);
        fetchData.runthread(uri);

        next = fetchData.getNext();
        previous = fetchData.getPrevious();
        games = fetchData.getmObjects();

      //  fetchData.putIfFetchableToFalse();


      //  adapter = new MyAdapter(this, R.layout.adapter_view_layout, games);
      //  listView1.setAdapter(adapter);



        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d("myapp", "gametoOnclickItemListener");

                listView1.setVisibility(View.GONE);
                prevPage.setVisibility(View.GONE);
                nextPage.setVisibility(View.GONE);
               int id = fetchData.mObjects.get(i)._id;

                GameInfo newfrag = new GameInfo();


                Bundle bundle = new Bundle();
                String s = Integer.toString(id);
                bundle.putString("game_number", s);
                newfrag.setArguments(bundle);
                openFragment(newfrag);

            }
        });


        setOnNext();
        setOnPrev();
    }

    public void setOnPrev(){

        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("myapp", "clicked on prev ");
                synchronized (previous){
                    if(previous != "null" && previous != "" && previous != null) {
                        Log.d("myapp", "prev is not null it is " + previous);

                        fetchData.clearmObjects();
                        fetchData.runthread(previous);
                    }
                    else{

                        Log.d("myapp", "prev is NULL");

                    }
                }

            }
        });
    }

    public void setOnNext(){

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("myapp", "clicked on next ");
                synchronized (next) {


                    if (next != "null" && next != "" && next != null) {

                        Log.d("myapp", "next is not null it is " + next);

                        fetchData.clearmObjects();
                        fetchData.runthread(next);
                    } else {
                        Log.d("myapp", "next is NULL");

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listView1.setVisibility(View.VISIBLE);
        prevPage.setVisibility(View.VISIBLE);
        nextPage.setVisibility(View.VISIBLE);
    }

    private void openFragment(GameInfo fragment)   {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.theRepLayout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }


    public static void setAdapter(ArrayList<Result> res){

        adapter = new MyAdapter(cont, R.layout.adapter_view_layout, res);
        listView1.setAdapter(adapter);


    }




    public static void updateAdapter(){


        adapter.notifyDataSetChanged();


    }
}

