package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {
    Button logIn_btn, reg_signUp_btn;
    TextInputEditText reg_temail, reg_tpass, reg_uname, reg_phone;
    TextInputLayout reg_em_label, reg_pas_label, reg_uname_label, reg_phone_label;
    private FirebaseAuth mAuth;
    ProgressBar prg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //hooks
        prg1 = findViewById(R.id.progress_circular);
        reg_signUp_btn = findViewById(R.id.reg_btn);
        logIn_btn = findViewById(R.id.reg_login_btn);
        reg_temail = findViewById(R.id.reg_email);
        reg_tpass = findViewById(R.id.reg_password1);
        reg_uname = findViewById(R.id.reg_username1);
        reg_phone = findViewById(R.id.reg_phone);
        reg_em_label = findViewById(R.id.reg_email_label);
        reg_pas_label = findViewById(R.id.reg_pass_label);
        reg_uname_label = findViewById(R.id.reg_uname_label);
        reg_phone_label = findViewById(R.id.reg_phone_label);

        prg1.setVisibility(View.GONE);

        //white action bar
        ActionBar aBar;
        aBar = getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#FFFFFF"));
        aBar.setBackgroundDrawable(cd);

        //making instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //registration button flow
        reg_signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUserNameData() | !validatePasswordData() | !validateEmailData() | !validatePhoneData()) {
                    return;
                } else {
                    signIn();
                }
            }
        });

        //registration page -> login button flow
        logIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //check if uer is already logged in then send the user to Dashboard
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(RegActivity.this, DashBoardActivity.class));
            finish();
        }
    }

    private void signIn() {

        //taking data from the user
        String uname = reg_uname.getText().toString();
        String phone = reg_phone.getText().toString();
        String email = reg_temail.getText().toString();
        String password = reg_tpass.getText().toString();

        //creating user if user is new
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Users");
                    UserHelperClass userHelperClass = new UserHelperClass(uname, phone, email, password);
                    myRef.child(email).setValue(userHelperClass);
                    prg1.setVisibility(View.VISIBLE);
                    startActivity(new Intent(RegActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegActivity.this, "Try after some time.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //validation functions
    private boolean validateUserNameData() {
        String val = reg_uname.getText().toString().trim();
        String namePattern = "^[A-Za-z]+$";
        if (val.isEmpty()) {
            reg_uname_label.setError("Username cannot be empty");
            return false;
        } else if (val.trim().equals("")) {
            reg_uname_label.setError("White space not allowed");
            return false;
        } else if (val.matches(namePattern)) {
            reg_uname_label.setError(null);
            reg_uname_label.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhoneData() {
        String val = reg_phone.getText().toString().trim();
        if (val.isEmpty()) {
            reg_phone_label.setError("Phone cannot be empty");
            return false;
        } else if (val.trim().equals("")) {
            reg_phone_label.setError("White space not allowed");
            return false;
        } else {
            reg_phone_label.setError(null);
            reg_phone_label.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmailData() {
        String val = reg_temail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            reg_em_label.setError("Email cannot be empty");
            return false;
        } else if (val.matches(emailPattern)) {
            reg_em_label.setError(null);
            reg_em_label.setErrorEnabled(false);
            return true;
        } else {
            reg_em_label.setError("Not a valid email");
            return false;
        }
    }

    private boolean validatePasswordData() {
        String val = reg_tpass.getText().toString().trim();
        if (val.isEmpty()) {
            reg_pas_label.setError("Password cannot be empty");
            return false;
        } else if (val.length() < 10) {
            reg_pas_label.setError("Password should be 10 digit.");
            return false;
        } else {
            reg_pas_label.setError(null);
            reg_pas_label.setErrorEnabled(false);
            return true;
        }
    }
}