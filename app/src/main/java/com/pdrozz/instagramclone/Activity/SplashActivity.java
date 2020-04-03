package com.pdrozz.instagramclone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pdrozz.instagramclone.FirebaseAuth.FirebaseAuthManager;
import com.pdrozz.instagramclone.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler=new Handler();
    private FirebaseAuthManager auth=new FirebaseAuthManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashAnimation();
    }

    private void splashAnimation(){
        if(auth.getCurrentUser()==null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startLoginActivity();
                }
            },1500);
        }else {
                handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            },1500);
        }
    }
    private void startMainActivity(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private void startLoginActivity(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
