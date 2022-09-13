package com.example.firebasedemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoardActivity extends AppCompatActivity {
    Button logOut_btn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        aBar.setBackgroundDrawable(cd);

        mAuth = FirebaseAuth.getInstance();
        logOut_btn = findViewById(R.id.logOut_btn);

        logOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(DashBoardActivity.this, "Logout SuccessFUlly !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashBoardActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}