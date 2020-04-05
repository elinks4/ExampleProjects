package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//has buttons to open the two list activities

public class MainActivity extends AppCompatActivity {


   public static Button gamelistB;
    public static Button savedListButton;
    public String uri;

    public static Context context;

    public boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // context = getApplicationContext();

        gamelistB = (Button) findViewById(R.id.gameListButton);
        savedListButton = (Button) findViewById(R.id.savedGamesButton);

        context = this;



        MainActivity.gamelistB.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                  Intent intent = new Intent(getApplicationContext(), MainGameListActivity.class);
                  startActivity(intent);

            }
        });

        MainActivity.savedListButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SavedGameList.class);
                startActivity(intent);

            }
        });

    }

    public static void makeToast(){

        Toast.makeText(context, "You can now use buttons", Toast.LENGTH_SHORT).show();
    }



}
