package com.mou.freedom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity<OnC> extends AppCompatActivity {
    private ProgressDialog loadingBar;
    private Button UpdatePostButton;
    private EditText PostDiscription;
    private  String Description;
    private StorageReference Postsreferencr;
    private DatabaseReference UsersRef,PostsRef;
    private FirebaseAuth mAuth;


private String saveCurrentDate,saveCurrentTime,postRandomName, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance ();
        current_user_id = mAuth.getCurrentUser ().getUid ();

        UpdatePostButton = (Button) findViewById (R.id.share_button);
        PostDiscription = (EditText) findViewById (R.id.post_discription);
        loadingBar=new ProgressDialog(this);

        Postsreferencr= FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance ().getReference ().child ("Users");
        PostsRef = FirebaseDatabase.getInstance ().getReference ().child ("Posts");

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ValidatePostInfo();
            }
        });
    }
    private  void ValidatePostInfo()
    {
        Description=PostDiscription.getText().toString();
        if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"please say somthing about your story...",Toast.LENGTH_SHORT).show();
        }else
        {loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("please wait,while we are updating post");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            storingPostToFireBaseStorage();
        }
    }
    private void storingPostToFireBaseStorage()
    {

        Calendar calFordDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentDate.format(calFordDate.getTime());

        Calendar calFordTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        saveCurrentTime=currentTime.format(calFordDate.getTime());

        postRandomName=saveCurrentDate+saveCurrentTime;
       //torageReference filePath=Postsreferencr.child("Post Description").child(postRandomName+".txt");



        UsersRef.child (current_user_id).addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists ())
                {
                    String userFullName = dataSnapshot.child ("fullname").getValue ().toString ();
                    String userProfileImage = dataSnapshot.child ("profileimage").getValue ().toString ();
                    HashMap postsMap=new HashMap ();
                    postsMap.put("uid",current_user_id);
                    postsMap.put("date",saveCurrentDate);
                    postsMap.put("time",saveCurrentTime);
                    postsMap.put("description",Description);
                    postsMap.put("profileimage",userProfileImage);
                    postsMap.put("fullname",userFullName);

                    PostsRef.child (current_user_id + postRandomName).updateChildren (postsMap)
                            .addOnCompleteListener (new OnCompleteListener ( ) {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                  if(task.isSuccessful ())
                                  {
                                      SendUserToPostHomeActivity();
                                      Toast.makeText (PostActivity.this," New Post is updated successfully",Toast.LENGTH_SHORT).show ();
                                 loadingBar.dismiss ();}
                                  else
                                  {
                                      Toast.makeText (PostActivity.this," Error Occured while updating your post",Toast.LENGTH_SHORT).show ();
                                  loadingBar.dismiss ();}
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToPostHomeActivity() {
        Intent PostHomeIntent = new Intent (PostActivity.this,PostHomeActivity.class);
        startActivity (PostHomeIntent);
    }
}
