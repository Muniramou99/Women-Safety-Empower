package com.mou.freedom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
//import android.support.v7.widget.Toolbar;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telecom.Call;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Button play;
    Button call;
    Button message;
    Button recording;
    Button d_stress;
    Button map;
/*recording*/
    /*recording*/
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postlist;
    private Toolbar mToolbar;

    private CircleImageView NaveProfileImage;
    private TextView NaveProfileUserName;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);


        play=(Button)findViewById(R.id.button_play);
          final MediaPlayer mp=MediaPlayer.create(MainActivity.this,R.raw.police_siren_sound);
          play.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(mp.isPlaying())
                  {
                      mp.pause();
                      play.setBackgroundResource(R.drawable.police_siren);
                  }else
                  {
                      mp.start();
                      play.setBackgroundResource(R.drawable.pause);
                  }
              }
          });

          call = (Button) findViewById(R.id.button_call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCall();
            }
        });
        message = (Button) findViewById(R.id.button_message);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonemessage();
            }
        });
        recording = (Button) findViewById(R.id.button_recording);

        recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonerecording();
            }
        });

        d_stress = (Button) findViewById(R.id.button_D);

        d_stress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dstressmessage();

            }
        });

        map = (Button) findViewById(R.id.button_Map);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location();
            }        });





        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");


        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout = (DrawerLayout)findViewById(R.id.drawable_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
         actionBarDrawerToggle.syncState();
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);

        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);
        NaveProfileImage = (CircleImageView) navView.findViewById(R.id.new_profile_image);
        NaveProfileUserName = (TextView) navView.findViewById(R.id.new_user_full_name);


        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        NaveProfileUserName.setText(fullname);
                    }
                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(NaveProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile Name Do not exists...", Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });


    }

    private void location() {
        Intent LocationIntent = new Intent(MainActivity.this, LocationActivity.class);
        startActivity(LocationIntent);
    }

    private void dstressmessage() {
        Intent DstressIntent = new Intent(MainActivity.this, SMSActivity.class);
        startActivity(DstressIntent);

    }

    private void phonemessage() {
        Intent SMSIntent = new Intent(MainActivity.this, LocationSMSActivity.class);
        startActivity(SMSIntent);
    }

    private void phoneCall() {
        Intent callIntent = new Intent(MainActivity.this, CallActivity.class);
        startActivity(callIntent);
    }
    private void phonerecording() {
        Intent callIntent = new Intent(MainActivity.this, RecordingActivity.class);
        startActivity(callIntent);
    }

    @Override
    protected void onStart()
    {

        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser==null)
        {
          SendUserToLoginActivity();
        }
        else
        {
            checkUserExistence();
        }

    }
    private void checkUserExistence()
    {
       final String current_user_id=mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void SendUserToSetupActivity()
    {
        Intent setupIntent=new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
    private void SendUserToLoginActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendUserToModifyContactActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this,ModifyContactActivity.class);
        startActivity(loginIntent);
    }
    private void SendUserToDevloperActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this,DevloperActivity.class);
        startActivity(loginIntent);
    }
    private void SendUserToPostActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this,PostHomeActivity.class);
        startActivity(loginIntent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item)
    {
switch (item.getItemId())
{
    case R.id.add_or_modify_contact:
        SendUserToModifyContactActivity();
        Toast.makeText(this,"ADD/Modify Contact",Toast.LENGTH_SHORT).show();

        break;
    case R.id.update_profile:
        Toast.makeText(this,"Update Profile",Toast.LENGTH_SHORT).show();
        break;
    case R.id.share_or_post_story:
        SendUserToPostActivity();
        Toast.makeText(this,"Share/Post Story",Toast.LENGTH_SHORT).show();
        break;
    case R.id.about_developer_and_app:
        SendUserToDevloperActivity();
        Toast.makeText(this,"About Developer And App",Toast.LENGTH_SHORT).show();
        break;
    case R.id.logout:
        mAuth.signOut();
        SendUserToLoginActivity();
        break;
}
    }
}
