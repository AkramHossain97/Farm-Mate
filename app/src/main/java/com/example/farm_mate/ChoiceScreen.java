package com.example.farm_mate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ChoiceScreen extends AppCompatActivity {

    DatabaseReference mDatabase;
    String idvalue;
    SharedPreferences sharedPreferences;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_screen);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.purple_500 ));

        sharedPreferences=getSharedPreferences(code, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(code)){
            Intent intent = new Intent(ChoiceScreen.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }

        LinearLayout scanlayout=(LinearLayout) findViewById(R.id.scanlayout);
        LinearLayout codelayout=(LinearLayout) findViewById(R.id.codelayout);

        //database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Device").child("device_name");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                idvalue = value;
                System.out.println("Database Value"+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        scanlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
        codelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChoiceScreen.this, code.class);
                startActivity(intent);
            }
        });
    }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        {
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoiceScreen.this);
                // builder.setTitle("Logged In Successfully!");
                String a = result.getContents();
                System.out.println("Scan Value"+a);
                System.out.println(idvalue);
                if (result.getContents().equals(idvalue)) {
                    builder.setTitle("Logged In Successfully!");
                    builder.setMessage(result.getContents());

                    System.out.println("Yes");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dialogInterface.dismiss();
                            Intent intent = new Intent(ChoiceScreen.this, HomeScreen.class);
                            startActivity(intent);
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putString(code,idvalue);
                            editor.commit();
                            finish();
                        }
                    }).show();


                } else {
                    builder.setTitle("Sorry It is not your device!");
                    builder.setMessage(idvalue);

                    System.out.println("No");
                    builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }


            }
        }
    });

}