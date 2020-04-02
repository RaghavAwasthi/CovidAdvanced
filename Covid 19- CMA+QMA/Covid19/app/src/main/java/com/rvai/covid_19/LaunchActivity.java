package com.rvai.covid_19;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class LaunchActivity extends AppCompatActivity {
    private boolean wasRunBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ImageView logo = findViewById(R.id.app_logo);
        TextView title = findViewById(R.id.app_title);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        logo.startAnimation(slide_up);
        title.startAnimation(slide_up);

        new Handler().postDelayed(() -> {
            SharedPreferences preferences = getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
            wasRunBefore = preferences.getBoolean(Constants.WASRUNBEFORE, false);
            startupTasks();
            //Show info slider on first run
            if (wasRunBefore) {
                final Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, InfoSliderActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void startupTasks() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        AndroidThreeTen.init(this);
    }
}
