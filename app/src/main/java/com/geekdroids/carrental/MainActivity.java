package com.geekdroids.carrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //variable

    Animation topAnimation, bottomAnimation;
    ImageView carimage;
    TextView name, slogan;


   private static int timer = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.animation_top);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.animation_bottom);

        //Hooks

        carimage = findViewById(R.id.car);
        name = findViewById(R.id.carRent);
        slogan = findViewById(R.id.slogan);

        carimage.setAnimation(topAnimation);
        name.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);

        //splash screen
    //animation screen transition handler

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, login_signup.class);
                Pair [] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(carimage, "logoImage");
                pairs[1] = new Pair<View, String>(name, "logoText");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, options.toBundle());
                finish();
            }
        }, timer);

    }
}