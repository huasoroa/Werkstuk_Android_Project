package be.ehb.dig_x.ricardo.werkstuk_android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.ehb.dig_x.ricardo.werkstuk_android.Adapters.PictureAdapter;
import be.ehb.dig_x.ricardo.werkstuk_android.EditProfileActivity;
import be.ehb.dig_x.ricardo.werkstuk_android.Model.Post;
import be.ehb.dig_x.ricardo.werkstuk_android.Model.User;
import be.ehb.dig_x.ricardo.werkstuk_android.R;

public class ProfileFragment extends Fragment {

    ImageView image_profile, options;
    TextView posts, followers, following, fullname, bio, username;
    Button edit_profile;

    private List<String> mySaves;

    RecyclerView recyclerView_saves;
    PictureAdapter pictureAdapter_saves;
    List<Post> postList_saves;

    RecyclerView recyclerView;
    PictureAdapter pictureAdapter;
    List<Post> postList;

    FirebaseUser firebaseUser;
    String profileid;

    ImageButton my_pics, saved_pics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        profileid = preferences.getString("profileid","none");

        image_profile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        edit_profile = view.findViewById(R.id.edit_profile);
        my_pics = view.findViewById(R.id.my_pics);
        saved_pics = view.findViewById(R.id.saved_pics);

        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        pictureAdapter= new PictureAdapter(getContext(),postList);
        recyclerView.setAdapter(pictureAdapter);

        recyclerView_saves= view.findViewById(R.id.recycler_view_save);
        recyclerView_saves.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new GridLayoutManager(getContext(),3);
        recyclerView_saves.setLayoutManager(linearLayoutManager1);
        postList_saves = new ArrayList<>();
        pictureAdapter_saves= new PictureAdapter(getContext(),postList_saves);
        recyclerView_saves.setAdapter(pictureAdapter_saves);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_saves.setVisibility(View.GONE);

        userInfo();
        getFollowers();
        getAmountPosts();
        myPics();
        mySavedPics();

        if (profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Edit Profile");
        }else{
            checkFollow();
            saved_pics.setVisibility(View.GONE);
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if (btn.equals("Edit Profile")){
                    startActivity(new Intent(getContext(),EditProfileActivity.class));
                }else if (btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                }else if (btn.equals("following")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        my_pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_saves.setVisibility(View.GONE);
            }
        });

        saved_pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerView_saves.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }


    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext()==null){
                    return;
                }

                User user= dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()){
                    edit_profile.setText("following");
                }else {
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getFollowers(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getAmountPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void myPics(){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                pictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mySavedPics(){
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mySaves.add(snapshot.getKey());
                }

                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readSaves(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList_saves.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    for (String id : mySaves){
                        if (post.getPostid().equals(id)){
                            postList_saves.add(post);
                        }
                    }
                }

                pictureAdapter_saves.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
