package com.touristguide.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.touristguide.R;
import com.touristguide.login.LoginActivity;
import com.touristguide.login.SignUpActivity;

public class UserSettingActivity extends AppCompatActivity {
    FirebaseUser user;
    private TextView mUserName;
    private TextView mUserEmail;
    private ImageView mUseruser;
    private EditText mNewEmail;
    private EditText mNewPassword;
    private EditText mPasswordResetEmail;
    private Button mBtnChangeEmail;
    private Button mBtnChangePassword;
    private Button mBtnSignout;
    private Button mBtnRemoveUser;
    private Button mBtnSendPasswordResetEmail;
    private ProgressBar mProgressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        inItView();
        setClickListener();
        setUserProfile();

    }

    private void inItView() {
        mNewEmail = findViewById(R.id.email);
        mProgressBar = findViewById(R.id.progressBar);
        mBtnChangeEmail = findViewById(R.id.btn_change_email);
        mBtnChangePassword = findViewById(R.id.btn_change_password);
        mBtnSignout = findViewById(R.id.btn_signout);
        mNewPassword = findViewById(R.id.password);
        mBtnRemoveUser = findViewById(R.id.btn_remove_user);
        mBtnSendPasswordResetEmail = findViewById(R.id.btn_send_password_reset_email);
        mPasswordResetEmail = findViewById(R.id.passwordreset_email);
        mUserName = findViewById(R.id.Text_View_user_name);
        mUserEmail = findViewById(R.id.Text_View_user_email);
        mUseruser = findViewById(R.id.Image_View_user_profie);
    }

    private void setUserProfile() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            if (emailVerified) {
                // Name, email address, and user photo Url
                String name = user.getDisplayName();
                String email = user.getEmail();
                Uri photoUrl = user.getPhotoUrl();

                String uid = user.getUid();
                CoordinatorLayout coordinatorLayout = findViewById(R.id.CL_user_setting);
                //Setting User Name
                mUserEmail = new TextView(UserSettingActivity.this);
                mUserEmail.setText(name);
                coordinatorLayout.addView(mUserEmail);

            }
        }
    }

    private void setClickListener() {

        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = firebaseAuth -> {
            FirebaseUser user1 = firebaseAuth.getCurrentUser();
            if (user1 == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(UserSettingActivity.this
                        , LoginActivity.class));
                finish();
            }
        };

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

        mBtnChangeEmail.setOnClickListener(v -> {
            mProgressBar.setVisibility(View.VISIBLE);
            if (user != null && !mNewEmail.getText().toString().trim().equals("")) {
                user.updateEmail(mNewEmail.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserSettingActivity.this
                                        , "Email address is updated. Please sign in with new email id!"
                                        , Toast.LENGTH_LONG).show();
                                signOut();
                                mProgressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(UserSettingActivity.this
                                        , "Failed to update email!", Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            } else if (mNewEmail.getText().toString().trim().equals("")) {
                mNewEmail.setError("Enter email");
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mBtnChangePassword.setOnClickListener(v -> {
            mProgressBar.setVisibility(View.VISIBLE);
            if (user != null && !mNewPassword.getText().toString().trim().isEmpty()) {
                if (mNewPassword.getText().toString().trim().length() < 6) {
                    mNewPassword.setError("Password too short, enter minimum 6 characters");
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(mNewPassword.getText().toString().trim())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserSettingActivity.this
                                            , "Password is updated, sign in with new password!"
                                            , Toast.LENGTH_SHORT).show();
                                    signOut();
                                    mProgressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(UserSettingActivity.this
                                            , "Failed to update password!"
                                            , Toast.LENGTH_SHORT).show();
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                }
            } else if (mNewPassword.getText().toString().trim().equals("")) {
                mNewPassword.setError("Enter password");
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mBtnSendPasswordResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (!mPasswordResetEmail.getText().toString().trim().equals("")) {
                    auth.sendPasswordResetEmail(mPasswordResetEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserSettingActivity.this
                                                , "Reset password email is sent!"
                                                , Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(UserSettingActivity.this
                                                , "Failed to send reset email!"
                                                , Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    mPasswordResetEmail.setError("Enter email");
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mBtnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserSettingActivity.this
                                                , "Your user is deleted:( Create a account now!"
                                                , Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UserSettingActivity.this
                                                , SignUpActivity.class));
                                        finish();
                                        mProgressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(UserSettingActivity.this
                                                , "Failed to delete your account!"
                                                , Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
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

