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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class Humidity extends AppCompatActivity {
    TextView humidity_value,suggestion_value;
    ProgressBar progressBar1;
    int processValue1=0;
    String a;
    SwitchCompat fansw;


    float i ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500)); //status bar or the time bar at the top
        }
        suggestion_value=(TextView)findViewById(R.id.suggestion);

        fansw = findViewById(R.id.switchHum);
        DatabaseReference databaseReferenceFan = FirebaseDatabase.getInstance().getReference().child("SPMS").child("fantest");

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
                    a=String.valueOf(snapshot1.child("Humidity").getValue());
                    System.out.println(a);
                  i = Float.parseFloat(a);
                    if(i>70.00){

                        databaseReferenceFan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fan=String.valueOf(snapshot.getValue());
                                if(fan.equals("0")){
                                    suggestion_value.setText("Please Turn On The Fan !!! Humidity Level High");;
                                }else{
                                    suggestion_value.setText("Fan Is Running . Humidity Level Will Go Down");;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // Toast.makeText(Humidity.this ,"Please turn on fan ",Toast.LENGTH_SHORT).show();
                    }else if(i<70.00){

                        databaseReferenceFan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fan=String.valueOf(snapshot.getValue());
                                if(fan.equals("1")){
                                    suggestion_value.setText("Please Turn Off The Fan !!! Humidity Level Is At Optimal");
                                }else{
                                    suggestion_value.setText("Humidity Is At Optimal Level . No Need To Turn The Fan On");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        suggestion_value.setText("Humidity Is At Optimal Level . Turn Off The Fan");


                    }
                    else {
                        suggestion_value.setText("Humidity Is At Optimal Level . No Need To Turn The Fan On");
                    }
                    //  i=.parseInt(a);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressBar1=(ProgressBar) findViewById(R.id.nprocessbar);
//        textfield for fillup data
        humidity_value=(TextView)findViewById(R.id.humidity_value);


//        sattings up processvalue
        progressBar1.setProgress(processValue1);


        Timer timer1 = new Timer();

        Activity activity1= new Activity();

        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                processValue1=processValue1+1;
                progressBar1.setProgress(processValue1);
                activity1.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity_value.setText(String.valueOf(processValue1));

                    }
                });
                if (progressBar1.getProgress()>=i){
                    timer1.cancel();
                }
            }
        },1000,20);

    }
}