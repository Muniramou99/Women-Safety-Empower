package com.mou.freedom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CallActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    private EditText number1;

    private EditText number2;

    private EditText number3;

    private Button callBtn, callBtn2, callBtn3;

    private DatabaseReference CallUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        CallUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);


        number1=(EditText)findViewById(R.id.callTxt);
        number2=(EditText)findViewById(R.id.callTxt2);
        number3=(EditText)findViewById(R.id.callTxt3);

        callBtn = findViewById(R.id.callButton);
        callBtn2 = findViewById(R.id.callButton2);
        callBtn3 = findViewById(R.id.callButton3);


        CallUserRef.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    String myNumber1=dataSnapshot.child("number1").getValue().toString();

                    String myNumber2=dataSnapshot.child("number2").getValue().toString();

                    String myNumber3=dataSnapshot.child("number3").getValue().toString();



                    number1.setText (myNumber1);

                    number2.setText (myNumber2);

                    number3.setText (myNumber3);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallButton2();
            }
        });
        callBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallButton3();
            }
        });
        callBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallButton();
            }
        });



        mToolbar = (Toolbar) findViewById(R.id.phone_call_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Phone Call");
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
        Intent mainIntent = new Intent(CallActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void CallButton() {
        String number = number3.getText().toString();
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }
        }
    }
    private void CallButton2() {
        String number = number1.getText().toString();
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }
        }
    }
    private void CallButton3() {
        String number = number2.getText().toString();
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            }
        }
    }





    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissios, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CallButton();
                CallButton2();
                CallButton3();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
