package com.example.farm_mate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class code extends AppCompatActivity {

    private EditText Name;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    DatabaseReference mDatabase;
    String idvalue;
    //sharedPreference
    SharedPreferences sharedPreferences;
    String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.mateblack ));

        sharedPreferences=getSharedPreferences(code, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(code)){
            Intent intent = new Intent(code.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("SPMS").child("Device").child("device_code");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                idvalue = value;
                //System.out.println(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Name = (EditText) findViewById(R.id.etName);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);

        Info.setText("No of attempts remaining: 5");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString());
            }
        });
    }

    private void validate(String userName) {
        if ((userName.equals(idvalue))) {
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString(code,idvalue);
            editor.commit();
            Intent intent = new Intent(code.this, HomeScreen.class);
            startActivity(intent);
            finish();
        } else {
            counter--;

            Info.setText("No of attempts remaining: " + String.valueOf(counter));

            if (counter == 0) {
                Login.setEnabled(false);
                Toast.makeText(getApplicationContext(), "You can't log in.Please,go back and come again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}