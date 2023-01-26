package com.example.farm_mate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class clean_case extends AppCompatActivity {

    SwitchCompat cbsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_case);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500)); //status bar or the time bar at the top
        }

        cbsw = findViewById(R.id.switchCB);

        DatabaseReference databaseReferenceBelt = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Belt");

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        cbsw.setChecked(sharedPreferences.getBoolean("value",true));

        databaseReferenceBelt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String belt=String.valueOf(snapshot.getValue());
                if(belt.equals("0")){
                    cbsw.setChecked(false);
                }else{
                    cbsw.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cbsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cbsw.isChecked()){

                    databaseReferenceBelt.setValue("1");

                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    cbsw.setChecked(true);


                }
                else {
                    databaseReferenceBelt.setValue("0");
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    cbsw.setChecked(false);

                }

            }
        });

    }
}