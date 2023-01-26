package com.example.farm_mate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class Temperature extends AppCompatActivity {
    TextView temp_value,suggestion_value_Fan ,suggestion_value_light ;
    ProgressBar progressBar2;
    int processValue2=0;
    String b , c ;
    SwitchCompat fansw , lightsw;


    float i , T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500)); //status bar or the time bar at the top
        }

        suggestion_value_Fan=(TextView)findViewById(R.id.Fansuggestion);
        suggestion_value_light=(TextView)findViewById(R.id.Lightsuggestion);

        fansw = findViewById(R.id.switchfan);
        lightsw = findViewById(R.id.switchlight);
        DatabaseReference databaseReferenceFan = FirebaseDatabase.getInstance().getReference().child("SPMS").child("fantest");
        DatabaseReference databaseReferenceLight = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Lighttest");

        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        fansw.setChecked(sharedPreferences.getBoolean("value",true));

        databaseReferenceFan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fan=String.valueOf(snapshot.getValue());
                if(fan.equals("0")){
                    fansw.setChecked(false);
                }else{
                    fansw.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fansw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fansw.isChecked()){

                    databaseReferenceFan.setValue("1");

                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    fansw.setChecked(true);


                }
                else {
                    databaseReferenceFan.setValue("0");
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    fansw.setChecked(false);

                }

            }
        });




        //database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Data");
        Query query=databaseReference.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    b=String.valueOf(snapshot1.child("Temp").getValue());
                    System.out.println(b);
                    i = Float.parseFloat(b);
                    if(i>24.00){

                        databaseReferenceFan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fan=String.valueOf(snapshot.getValue());
                                if(fan.equals("0")){
                                    suggestion_value_Fan.setText("Please Turn On The Fan !!! Temperature Level High");;
                                }else{
                                    suggestion_value_Fan.setText("Fan Is Running . Temperature Level Will Go Down");;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // Toast.makeText(Humidity.this ,"Please turn on fan ",Toast.LENGTH_SHORT).show();
                    }else if(i<24.00){

                        databaseReferenceFan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fan=String.valueOf(snapshot.getValue());
                                if(fan.equals("1")){
                                    suggestion_value_Fan.setText("Please Turn Off The Fan !!! Temperature Level Is At Optimal");
                                }else{
                                    suggestion_value_Fan.setText("Temperature Is At Optimal Level . No Need To Turn The Fan On");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        suggestion_value_Fan.setText("Temperature Is At Optimal Level . Turn Off The Fan");


                    }
                    else {
                        suggestion_value_Fan.setText("Temperature Is At Optimal Level . No Need To Turn The Fan On");
                    }
                    //  i=.parseInt(a);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        SharedPreferences sharedPreferencesTemp = getSharedPreferences("save", MODE_PRIVATE);
        lightsw.setChecked(sharedPreferencesTemp.getBoolean("value",true));

        databaseReferenceLight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String light=String.valueOf(snapshot.getValue());
                if(light.equals("0")){
                    lightsw.setChecked(false);
                }else{
                    lightsw.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lightsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lightsw.isChecked()){

                    databaseReferenceLight.setValue("1");

                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    lightsw.setChecked(true);


                }
                else {
                    databaseReferenceLight.setValue("0");
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    lightsw.setChecked(false);

                }

            }
        });




        //database
        DatabaseReference databaseReferenceW = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Data");
        Query queryW=databaseReferenceW.orderByKey().limitToLast(1);
        queryW.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    c=String.valueOf(snapshot1.child("Temp").getValue());
                    System.out.println(c);
                    T = Float.parseFloat(c);
                    if(T>24.00){

                        databaseReferenceLight.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String light=String.valueOf(snapshot.getValue());
                                if(light.equals("0")){
                                    suggestion_value_light.setText("Light Is Off . Temperature Level Will Go Down");;
                                }else{
                                    suggestion_value_light.setText("Please Turn Off The Light !!! Temperature Is High ");;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // Toast.makeText(Humidity.this ,"Please turn on fan ",Toast.LENGTH_SHORT).show();
                    }else if(T<24.00){

                        databaseReferenceLight.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String light=String.valueOf(snapshot.getValue());
                                if(light.equals("1")){
                                    suggestion_value_light.setText("Light Is On . Temperature Level Will Go Up");
                                }else{
                                    suggestion_value_light.setText("Please Turn On The Light !!! Temperature Level Low");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        suggestion_value_light.setText("Temperature Is At Optimal Level . Turn Off The Light");


                    }
                    else {
                        suggestion_value_light.setText("Temperature Is At Optimal Level . No Need To Turn The Light On");
                    }
                    //  i=.parseInt(a);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        progressBar2=(ProgressBar) findViewById(R.id.Tprocessbar);
//        textfield for fillup data
        temp_value=(TextView)findViewById(R.id.temp_value);


//        sattings up processvalue
        progressBar2.setProgress(processValue2);


        Timer timer1 = new Timer();

        Activity activity1= new Activity();

        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                processValue2=processValue2+1;
                progressBar2.setProgress(processValue2);
                activity1.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        temp_value.setText(String.valueOf(processValue2));

                    }
                });
                if (progressBar2.getProgress()>=i){
                    timer1.cancel();
                }
            }
        },1000,20);

    }
}