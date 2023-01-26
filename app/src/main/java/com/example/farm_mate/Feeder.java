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

public class Feeder extends AppCompatActivity {

    TextView weight_value,suggestion_value_weight;
    ProgressBar progressBar4;
    int processValue4=0;
    String d;


    float i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500)); //status bar or the time bar at the top
        }
        suggestion_value_weight=(TextView)findViewById(R.id.weightsuggestion);





        //database
        DatabaseReference databaseReferenceweight = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Data");
        Query queryww=databaseReferenceweight.orderByKey().limitToLast(1);
        queryww.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    d=String.valueOf(snapshot1.child("Weight").getValue());
                    System.out.println(d);
                    i = Float.parseFloat(d);
                    if(i>50.00){

                        suggestion_value_weight.setText("Please Dispense Some Food . Feeder Is Almost Full ");

                        // Toast.makeText(Humidity.this ,"Please turn on fan ",Toast.LENGTH_SHORT).show();
                    }else if(i<10.00){

                        suggestion_value_weight.setText("Please Add Some Food . Feeder Is Almost Empty");


                    }
                    else {
                        suggestion_value_weight.setText("Feeder Is At Optimal Weight .");
                    }
                    //  i=.parseInt(a);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressBar4=(ProgressBar) findViewById(R.id.weightprocessbar);
//        textfield for fillup data
        weight_value=(TextView)findViewById(R.id.weight_value);


//        sattings up processvalue
        progressBar4.setProgress(processValue4);


        Timer timer1 = new Timer();

        Activity activity1= new Activity();

        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                processValue4=processValue4+1;
                progressBar4.setProgress(processValue4);
                activity1.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weight_value.setText(String.valueOf(processValue4));

                    }
                });
                if (progressBar4.getProgress()>=i){
                    timer1.cancel();
                }
            }
        },1000,20);

    }
}