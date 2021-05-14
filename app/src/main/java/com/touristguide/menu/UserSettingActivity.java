package com.touristguide.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.touristguide.R;
import com.touristguide.login.ForgotPasswordActivity;
import com.touristguide.login.LoginActivity;

public class UserSettingActivity extends AppCompatActivity {
    private EditText mNewEmail;
    private Button mBtnChangeEmail;
    private Button mBtnForgotPassword;
    private Button mBtnSignout;
    private ProgressBar mProgressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        inItView();
        setClickListener();
        signOut();
    }


    private void inItView() {
        mNewEmail = findViewById(R.id.txt_input_change_email);
        mProgressBar = findViewById(R.id.progressBar);
        mBtnChangeEmail = findViewById(R.id.btn_change_email);
        mBtnForgotPassword = findViewById(R.id.btn_forgot_password);
        mBtnSignout = findViewById(R.id.btn_signout);

    }


    private void setClickListener() {

        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(UserSettingActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }


        mBtnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (user != null && !mNewEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(mNewEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserSettingActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                        mProgressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(UserSettingActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (mNewEmail.getText().toString().trim().equals("")) {
                    mNewEmail.setError("Enter email");
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mBtnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        mBtnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

}

