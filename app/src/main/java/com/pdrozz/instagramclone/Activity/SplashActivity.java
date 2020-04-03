package com.pdrozz.instagramclone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pdrozz.instagramclone.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashAnimation();

    }

    private void splashAnimation(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartLoginActivity();
            }
        },1500);

    }

    private void StartLoginActivity(){
        startActivity(new Intent(this,LoginActivity.class));
    }
}
