package com.rvai.covid_19.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.rvai.covid_19.Constants;
import com.rvai.covid_19.MainActivity;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends MainFragment {
    Chip chip;
    AppCompatTextView cured, total, dead;
    SharedPreferences preferences;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        chip = view.findViewById(R.id.view_more);
        cured = view.findViewById(R.id.case_cured);
        total = view.findViewById(R.id.case_total);
        dead = view.findViewById(R.id.case_dead);
        preferences = getContext().getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).selectNavItem(R.id.nav_status);
            }
        });

        dead.setText(Integer.toString(preferences.getInt(Constants.DEATHCOUNT, 0)));
        cured.setText(Integer.toString(preferences.getInt(Constants.CUREDCOUNT, 0)));
        total.setText(Integer.toString(preferences.getInt(Constants.TOTALCOUNT, 0)));

        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                dead.setText(Integer.toString(preferences.getInt(Constants.DEATHCOUNT, 0)));
                cured.setText(Integer.toString(preferences.getInt(Constants.CUREDCOUNT, 0)));
                total.setText(Integer.toString(preferences.getInt(Constants.TOTALCOUNT, 0)));

            }
        });
        return view;

    }

    @Override
    public int getTitle() {
        return R.string.title_home;
    }
}
