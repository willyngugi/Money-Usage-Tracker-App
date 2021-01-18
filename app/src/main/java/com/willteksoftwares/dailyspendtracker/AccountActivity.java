package com.willteksoftwares.dailyspendtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView logedInOn, name, email;
    private Button logout;
    private CircleImageView profileImage;

    private FirebaseAuth mAuth;
    private String onlinUserId = "";
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Information");
        logedInOn = findViewById(R.id.logedInOn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        logout = findViewById(R.id.logoutBtn);
        profileImage  = findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        onlinUserId = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(onlinUserId);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logedInOn.setText(snapshot.child("logedinon").getValue().toString());
                name.setText(snapshot.child("name").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());

                Glide.with(AccountActivity.this).load(snapshot.child("profilepictureurl").getValue().toString()).into(profileImage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Daily Spend Tracker")
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No", null)
                        .show();
            }
        });
    }
}