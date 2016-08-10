package com.digipay.digipay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.SessionManager;

public class SplashActivity extends AppCompatActivity {
    Animation anim;
    SessionManager mSession;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        mSession = new SessionManager(getApplicationContext());
        user_id = mSession.getUserId();
        Log.d("userId_SplashScren", user_id);
        if (!user_id.equals("")) {
            Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
            startActivity(i);
            finish();
        } else {
            StartAnimations();
            int SPLASH_TIME_OUT = 4000;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void StartAnimations() {
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout linearCover = (LinearLayout) findViewById(R.id.linearCover);
        linearCover.clearAnimation();
        linearCover.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView imgIconSplash = (ImageView) findViewById(R.id.imgLogo);
        imgIconSplash.clearAnimation();
        imgIconSplash.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        LinearLayout linearCover1 = (LinearLayout) findViewById(R.id.linearCover1);
        linearCover1.setVisibility(View.VISIBLE);
        linearCover1.clearAnimation();
        linearCover1.startAnimation(anim);

    }

}
