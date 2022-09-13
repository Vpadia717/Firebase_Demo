package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button signIn_btn, signUp_btn;
    TextInputEditText temail, tpass;
    TextInputLayout em_label, pas_label;
    private FirebaseAuth mAuth;
    ProgressBar prg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //hooks
        prg1 = findViewById(R.id.progress_login_circular);
        signIn_btn = findViewById(R.id.login_btn);
        signUp_btn = findViewById(R.id.signup_btn);
        temail = findViewById(R.id.username1);
        tpass = findViewById(R.id.password1);
        em_label = findViewById(R.id.uname_label);
        pas_label = findViewById(R.id.pass_label);

        prg1.setVisibility(View.GONE);

        //white action bar
        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        aBar.setBackgroundDrawable(cd);

        mAuth = FirebaseAuth.getInstance();

        //login button code
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUserNameData() | !validatePasswordData()) {
                    return;
                } else {
                    signIn();
                }
            }
        });

        //login page -> registration button code
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //check if uer is already logged in then send the user to Dashboard
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, DashBoardActivity.class));
            finish();
        }
    }

    //sign in method using firebase
    private void signIn() {
        String em1 = temail.getText().toString();
        String pass1 = tpass.getText().toString();
        mAuth.signInWithEmailAndPassword(em1, pass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    prg1.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Hello, " + em1, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, DashBoardActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateUserNameData() {
        String val = temail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            em_label.setError("Email cannot be empty");
            return false;
        } else if (val.matches(emailPattern)) {
            em_label.setError(null);
            em_label.setErrorEnabled(false);
            return true;
        } else {
            em_label.setError("Not a valid email");
            return false;
        }
    }

    private boolean validatePasswordData() {
        String val = tpass.getText().toString().trim();
        if (val.isEmpty()) {
            pas_label.setError("Password cannot be empty");
            return false;
        } else {
            pas_label.setError(null);
            pas_label.setErrorEnabled(false);
            return true;
        }
    }
}