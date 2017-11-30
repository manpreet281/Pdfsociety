package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        bounce();

        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            handler.sendEmptyMessageDelayed(102,2000);
        }
        else{
            handler.sendEmptyMessageDelayed(101,2000);
        }
    }

    public void bounce(){
        ImageView image = (ImageView)findViewById(R.id.imageView2);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        image.startAnimation(animation);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101){
                startActivity(new Intent(SplashActivity.this, SignIn.class));
                finish();
            }
            else if(msg.what == 102){
                Log.d("Pdfsociety", "splash worked");
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }
    };
}
