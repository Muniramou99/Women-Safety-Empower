package com.mou.freedom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SMSActivity extends AppCompatActivity {

    //private Button send;
    private String number1,number2,number3;

    private DatabaseReference DUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        DUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);




        DUserRef.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    String myNumber1=dataSnapshot.child("number1").getValue().toString();

                    String myNumber2=dataSnapshot.child("number2").getValue().toString();

                    String myNumber3=dataSnapshot.child("number3").getValue().toString();



                    number1 = myNumber1;
                    number2 = myNumber2;
                    number3 = myNumber3;

                    dmessage();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mToolbar = (Toolbar) findViewById(R.id.dstress_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("D-Stress");


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SMSActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }


    private void dmessage() {
        String message = "Iam Safe now";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number1,null,message,null,null);
        smsManager.sendTextMessage(number2,null,message,null,null);
        smsManager.sendTextMessage(number3,null,message,null,null);
    }

}
