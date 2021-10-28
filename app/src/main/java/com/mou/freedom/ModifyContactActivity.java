package com.mou.freedom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.widget.Toolbar;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ModifyContactActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText relation1;
    private EditText number1;
    private EditText relation2;
    private EditText number2;
    private EditText relation3;
    private EditText number3;
    private Button UpdateButton;
    private DatabaseReference SettingsUserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contact);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        SettingsUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        mToolbar = (Toolbar) findViewById (R.id.modify_contact_toolbar);
        setSupportActionBar (mToolbar);
        getSupportActionBar ().setTitle ("Add or modify");



        relation1 =(EditText)findViewById(R.id.EditText1);
        number1=(EditText)findViewById(R.id.EditText2);
        relation2=(EditText)findViewById(R.id.EditText3);
        number2=(EditText)findViewById(R.id.EditText4);
        relation3=(EditText)findViewById(R.id.EditText5);
        number3=(EditText)findViewById(R.id.EditText6);

        UpdateButton=(Button)findViewById(R.id.Update_buttons);
        SettingsUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(dataSnapshot.exists())
             {
                 String myRelation1=dataSnapshot.child("relation1").getValue().toString();
                 String myNumber1=dataSnapshot.child("number1").getValue().toString();
                 String myRelation2=dataSnapshot.child("relation2").getValue().toString();
                 String myNumber2=dataSnapshot.child("number2").getValue().toString();
                 String myRelation3=dataSnapshot.child("relation3").getValue().toString();
                 String myNumber3=dataSnapshot.child("number3").getValue().toString();


                 relation1.setText (myRelation1);
                 number1.setText (myNumber1);
                 relation2.setText (myRelation2);
                 number2.setText (myNumber2);
                 relation3.setText (myRelation3);
                 number3.setText (myNumber3);

             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UpdateButton.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                ValidateAccountInfo();
            }
        });
    }
    private void ValidateAccountInfo()
    {
        String userrelation1=relation1.getText ().toString ();
        String usernumber1=number1.getText().toString ();
        String userrelation2=relation2.getText().toString ();
        String usernumber2=number2.getText().toString ();
        String userrelation3=relation3.getText().toString ();
        String usernumber3=number3.getText().toString ();
        if(TextUtils.isEmpty (userrelation1))
        {
            Toast.makeText (this,"please write your relation1...",Toast.LENGTH_SHORT).show ();
        }
        if(TextUtils.isEmpty (usernumber1))
        {
            Toast.makeText (this,"please write your number1...",Toast.LENGTH_SHORT).show ();
        }
        if(TextUtils.isEmpty (userrelation2))
        {
            Toast.makeText (this,"please write your relation2...",Toast.LENGTH_SHORT).show ();
        }
        if(TextUtils.isEmpty (usernumber2))
        {
            Toast.makeText (this,"please write your number2...",Toast.LENGTH_SHORT).show ();
        }
        if(TextUtils.isEmpty (userrelation3))
        {
            Toast.makeText (this,"please write your relation3..",Toast.LENGTH_SHORT).show ();
        }
        if(TextUtils.isEmpty (usernumber3))
        {
            Toast.makeText (this,"please write your number3...",Toast.LENGTH_SHORT).show ();
        }
        else
        {
            UpdateAccountInfo(userrelation1,usernumber1,userrelation2,usernumber2,userrelation3,usernumber3);
        }

    }
    private void UpdateAccountInfo(String relation1,String number1,String relation2,String number2,String relation3,String number3)
    {
        HashMap userMap=new HashMap ();
        userMap.put("relation1",relation1);
        userMap.put("number1",number1);
        userMap.put("relation2",relation2);
        userMap.put("number2",number2);
        userMap.put("relation3",relation3);
        userMap.put("number3",number3);

        SettingsUserRef.updateChildren (userMap).addOnCompleteListener (new OnCompleteListener ( ) {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful ())
                {
                    SendUserToMainActivity ();
                    Toast.makeText (ModifyContactActivity.this,"Account settings Updated Successfully....",Toast.LENGTH_SHORT).show ();
                }
                else{
                    Toast.makeText (ModifyContactActivity.this,"Error!",Toast.LENGTH_SHORT).show ();
                }
            }
        });
    }
    private void SendUserToMainActivity()
    {
        Intent mainIntent=new Intent(ModifyContactActivity.this,MainActivity.class) ;
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
