package in.imast.snowcemdealer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.play.core.appupdate.AppUpdateManager;

import in.imast.snowcemdealer.R;

import in.imast.snowcemdealer.helper.StaticSharedpreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {

    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;
    String newFcmToken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_spash);

        checkLoginAvailability();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putInt("splash", 1);
        mFirebaseAnalytics.logEvent("splash_view", bundle);

    }




    private void checkLoginAvailability(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!StaticSharedpreference.getInfo("AccessToken",SplashActivity.this).equals("")){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class)
                            .putExtra("status",""));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this,WelcomeScreen.class));
                    finish();
                }

            }
        },2000);

    }





}
