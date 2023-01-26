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

public class Exh_Fan extends AppCompatActivity {

    TextView gas_value,suggestion_value_gas;
    ProgressBar progressBar3;
    int processValue3=0;
    String c;
    SwitchCompat gassw;


    int G , i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exh_fan);




        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500)); //status bar or the time bar at the top
        }
        suggestion_value_gas=(TextView)findViewById(R.id.suggestionExh);

        gassw = findViewById(R.id.switchex);
        DatabaseReference databaseReferenceEx = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Exhausttest");

        SharedPreferences sharedPreferencesEx = getSharedPreferences("save", MODE_PRIVATE);
        gassw.setChecked(sharedPreferencesEx.getBoolean("value",true));

        databaseReferenceEx.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ex=String.valueOf(snapshot.getValue());
                if(ex.equals("0")){
                    gassw.setChecked(false);
                }else{
                    gassw.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gassw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gassw.isChecked()){

                    databaseReferenceEx.setValue("1");

                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    gassw.setChecked(true);


                }
                else {
                    databaseReferenceEx.setValue("0");
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    gassw.setChecked(false);

                }

            }
        });




        //database
        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Data");
        Query querys=databaseReferences.orderByKey().limitToLast(1);
        querys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    c=String.valueOf(snapshot1.child("Gas_Value").getValue());
                    System.out.println(c);
                    i = Integer.parseInt(c);
                    if(i>100){

                        databaseReferenceEx.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String gas=String.valueOf(snapshot.getValue());
                                if(gas.equals("0")){
                                    suggestion_value_gas.setText("Please Turn On The Exhaust Fan !!! Gas Level High");;
                                }else{
                                    suggestion_value_gas.setText("Exhaust Fan Is Running . Gas Level Will Go Down");;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // Toast.makeText(Humidity.this ,"Please turn on fan ",Toast.LENGTH_SHORT).show();
                    }else if(i<99){

                        databaseReferenceEx.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String gas=String.valueOf(snapshot.getValue());
                                if(gas.equals("1")){
                                    suggestion_value_gas.setText("Please Turn Off The Exhaust Fan !!! Gas Level Is At Optimal");
                                }else{
                                    suggestion_value_gas.setText("Gas Is At Optimal Level . No Need To Turn The Exhaust Fan On");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        suggestion_value_gas.setText("Gas Is At Optimal Level . Turn Off The Exhaust Fan");


                    }
                    else {
                        suggestion_value_gas.setText("Gas Is At Optimal Level . No Need To Turn The Exhaust Fan On");
                    }
                    //  i=.parseInt(a);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressBar3=(ProgressBar) findViewById(R.id.exprocessbar);
//        textfield for fillup data
        gas_value=(TextView)findViewById(R.id.gasss_value);


//        sattings up processvalue
        progressBar3.setProgress(processValue3);


        Timer timer1 = new Timer();

        Activity activity1= new Activity();

        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                processValue3=processValue3+1;
                progressBar3.setProgress(processValue3);
                activity1.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gas_value.setText(String.valueOf(processValue3));

                    }
                });
                if (progressBar3.getProgress()>=i){
                    timer1.cancel();
                }
            }
        },1000,20);

    }
}