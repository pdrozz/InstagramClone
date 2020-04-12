package com.pdrozz.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import com.pdrozz.instagramclone.FirebaseHelperManager.FirebaseAuthManager;
import com.pdrozz.instagramclone.R;
import com.pdrozz.instagramclone.helper.HelperStorageManager;

public class SplashActivity extends AppCompatActivity {

    private Handler handler=new Handler();
    private FirebaseAuthManager auth=new FirebaseAuthManager(this);
    private Bitmap image_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashAnimation();
    }

    private void splashAnimation(){
        //loadFiles();
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
        Intent main=new Intent(this,MainActivity.class);
        if (image_profile!=null)main.putExtra("bitmap",image_profile);
        startActivity(main);
        finish();
    }
    private void startLoginActivity(){
        Intent login=new Intent(this,LoginActivity.class);
        if (image_profile!=null)login.putExtra("bitmap",image_profile);
        startActivity(login);
        finish();
    }

    private void loadFiles(){
        String profile_pic="image_profile.jpeg";
        image_profile= HelperStorageManager.getBitmapFromInternal(this,profile_pic);
    }
}
