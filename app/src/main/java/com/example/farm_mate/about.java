package com.example.farm_mate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class about extends AppCompatActivity {
    FloatingActionButton NewMail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.mateblack)); //status bar or the time bar at the top
        }


        NewMail=findViewById(R.id.fabEmail);


        NewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "smartpoulty01@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Question About FarmMate");
                intent.putExtra(Intent.EXTRA_TEXT, " ");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}