package com.touristguide.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.touristguide.R;
import com.touristguide.login.LoginActivity;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(this::run);
        thread.start();
    }

    private void run() {
        try {
            Thread.sleep(400);
            SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            SplashScreen.this.finish();
        } catch (Exception e) {
            Toast.makeText(SplashScreen.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}