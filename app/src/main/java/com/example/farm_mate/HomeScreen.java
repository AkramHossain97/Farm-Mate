package com.example.farm_mate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500)); //status bar or the time bar at the top
        }
        Button button=(Button)findViewById(R.id.about);
        Button button1=(Button)findViewById(R.id.logout);
        CardView humidity = (CardView)findViewById(R.id.humidity);
        CardView temperature = (CardView)findViewById(R.id.temp);
        CardView clean_case_p = (CardView)findViewById(R.id.cleancase);
        CardView exh_FAN_gas = (CardView)findViewById(R.id.exhfan);
        CardView fooddp = (CardView)findViewById(R.id.feeder);

        fooddp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeScreen.this,Feeder.class));

            }
        });

        exh_FAN_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeScreen.this,Exh_Fan.class));

            }
        });


        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this,Temperature.class));
            }
        });

        humidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this,Humidity.class));
            }
        });

        clean_case_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this,clean_case.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this,about.class));
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HomeScreen.this, ChoiceScreen.class);
                HomeScreen.this.startActivity(myIntent);
                sharedPreferences=getSharedPreferences(code, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();

            }

        });
    }
}