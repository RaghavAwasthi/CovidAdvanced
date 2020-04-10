package com.rvai.covid_19.info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pdfview.PDFView;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends MainFragment {

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        PDFView contact=view.findViewById(R.id.contactpdfview);
        contact.fromAsset("coronvavirushelplinenumber.pdf");
        return view;
    }

    @Override
    public int getTitle() {
        return R.string.info;
    }
}
