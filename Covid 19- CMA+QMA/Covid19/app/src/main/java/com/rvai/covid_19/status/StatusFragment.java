package com.rvai.covid_19.status;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.pdfview.PDFView;
import com.rvai.covid_19.Constants;
import com.rvai.covid_19.HeatMapActivity;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends MainFragment {
    ProgressDialog pd;
    AppCompatTextView cured, total, dead;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        Button b = view.findViewById(R.id.heatmap_button);
        cured = view.findViewById(R.id.case_cured);
        total = view.findViewById(R.id.case_total);
        dead = view.findViewById(R.id.case_dead);;
        preferences = getContext().getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        dead.setText(Integer.toString(preferences.getInt(Constants.DEATHCOUNT, 0)));
        cured.setText(Integer.toString(preferences.getInt(Constants.CUREDCOUNT, 0)));
        total.setText(Integer.toString(preferences.getInt(Constants.TOTALCOUNT, 0)));

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), HeatMapActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public int getTitle() {
        return R.string.title_status;
    }



}

