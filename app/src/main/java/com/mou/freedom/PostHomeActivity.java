package com.mou.freedom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostHomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton PostButton;
    private RecyclerView listPost;

    FirebaseAuth mAuth;
    private DatabaseReference PostsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_post_home);

        mAuth = FirebaseAuth.getInstance ();
        PostsRef = FirebaseDatabase.getInstance ().getReference ().child ("Posts");

        mToolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Posts");

        listPost = (RecyclerView) findViewById (R.id.all_post_list);
        listPost.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this);
        linearLayoutManager.setReverseLayout (true);
        linearLayoutManager.setStackFromEnd (true);
        listPost.setLayoutManager (linearLayoutManager);


        PostButton = (ImageButton)findViewById (R.id.post_button);


        PostButton.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                SendusertoPostActivity();
            }
        });

        DisplayAllUsersPosts();
    }

    private void DisplayAllUsersPosts() {

        FirebaseRecyclerAdapter<Posts, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostViewHolder>
                        (Posts.class,
                                R.layout.all_posts_layout,
                                PostViewHolder.class,
                                PostsRef

                                )
                {
                    @Override
                    protected void populateViewHolder(PostViewHolder postViewHolder, Posts posts, int i) {

                        postViewHolder.setFullname (posts.getFullname () );
                        postViewHolder.setTime (posts.getTime () );
                        postViewHolder.setDate (posts.getDate () );
                        postViewHolder.setDescription (posts.getDescription () );
                        postViewHolder.setProfileimage (getApplicationContext (),posts.getProfileimage ());


                    }
                };

        listPost.setAdapter (firebaseRecyclerAdapter);

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public PostViewHolder(@NonNull View itemView) {
            super (itemView);

            mView = itemView;
        }

        public void setFullname(String fullname)
        {
            TextView username = (TextView) mView.findViewById (R.id.post_user_name);
            username.setText (fullname);
        }
        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView image = (CircleImageView) mView.findViewById (R.id.post_profile_image);
            Picasso.with (ctx).load (profileimage).into (image);
        }
        public void setTime(String time)
        {
            TextView PostTime = (TextView) mView.findViewById (R.id.post_time);
            PostTime.setText ("   " + time);
        }

        public void setDate(String date)
        {
            TextView PostDate = (TextView) mView.findViewById (R.id.post_date);
            PostDate.setText ("   " + date);
        }

        public void setDescription(String description)
        {
            TextView PostDescription = (TextView) mView.findViewById (R.id.post_discription);
            PostDescription.setText (description);
        }


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
        Intent mainIntent = new Intent(PostHomeActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void SendusertoPostActivity() {

        Intent PostIntent = new Intent (PostHomeActivity.this,PostActivity.class);
        startActivity (PostIntent);
    }
}
