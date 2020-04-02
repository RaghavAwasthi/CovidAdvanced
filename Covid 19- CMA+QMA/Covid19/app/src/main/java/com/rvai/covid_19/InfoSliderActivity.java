package com.rvai.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class InfoSliderActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new Step.Builder().setTitle("Wash Hands")
                .setContent("Hand wash is a healthy habit to keep yourself safe")
                .setBackgroundColor(Color.parseColor("#04bcf4"))
                .setDrawable(R.drawable.handwash)
                .build());

        addFragment(new Step.Builder().setTitle("Use Sanitizer")
                .setContent("Sanatizer weakens the virus")
                .setBackgroundColor(Color.parseColor("#153f87"))
                .setDrawable(R.drawable.sanitizer)
                .build());

        addFragment(new Step.Builder().setTitle("Social Distance")
                .setContent("Social Distancing is the key to break the corona chain")
                .setBackgroundColor(Color.parseColor("#FF4081"))
                .setDrawable(R.drawable.ic_access_time)
                .build());
    }
    @Override
    public void finishTutorial() {
        super.finishTutorial();
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        SharedPreferences preferences = getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(Constants.WASRUNBEFORE, true);
        editor.apply();

        finish();
    }
}
