package com.rvai.covid_19.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.rvai.covid_19.ContactActivity;
import com.rvai.covid_19.FundsActivity;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.MainSliderAdapter;
import com.rvai.covid_19.PicassoImageLoadingService;
import com.rvai.covid_19.R;
import com.rvai.covid_19.TouchyWebView;

import ss.com.bannerslider.ImageLoadingService;
import ss.com.bannerslider.Slider;

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
        PicassoImageLoadingService service=new PicassoImageLoadingService(getContext());
        Slider.init(service);
        Slider slider=view.findViewById(R.id.banner_slider1);
        MainSliderAdapter adapter=new MainSliderAdapter();
        slider.setAdapter(adapter);
        MaterialButton donatebutton = view.findViewById(R.id.donate_button);
        MaterialButton helplineButton=view.findViewById(R.id.helpline_button);

        helplineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), ContactActivity.class);
                startActivity(i);
            }
        });
        donatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FundsActivity.class);
                startActivity(i);
            }
        });
        String webscript_who = "<a class=\"twitter-timeline\" href=\"https://twitter.com/WHO?ref_src=twsrc%5Etfw\">Tweets by WHO</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        String webscript_moh = "<a class=\"twitter-timeline\" data-theme=\"dark\" href=\"https://twitter.com/MoHFW_INDIA?ref_src=twsrc%5Etfw\">Tweets by MoHFW_INDIA</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        String webscript_gov = "<a class=\"twitter-timeline\" data-theme=\"dark\" href=\"https://twitter.com/mygovindia?ref_src=twsrc%5Etfw\">Tweets by mygovindia</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        TouchyWebView wv_who = view.findViewById(R.id.webview_who);
        TouchyWebView wv_moh = view.findViewById(R.id.webview_moh);
        TouchyWebView wv_gov = view.findViewById(R.id.webview_gov);

//***** Perform webview settings *****
        wv_who.getSettings().setJavaScriptEnabled(true);
        wv_gov.getSettings().setJavaScriptEnabled(true);
        wv_moh.getSettings().setJavaScriptEnabled(true);
        wv_who.getSettings().setLoadsImagesAutomatically(true);
        wv_gov.getSettings().setLoadsImagesAutomatically(true);
        wv_moh.getSettings().setLoadsImagesAutomatically(true);
        wv_who.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wv_moh.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wv_gov.getSettings().setAllowUniversalAccessFromFileURLs(true);

        /* !!!!! Dirty fix, but it's working !!!!! */
        wv_who.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        wv_gov.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        wv_moh.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");

/***** Let's load data with base URL pointing to https://twitter.com *****/
        wv_who.loadDataWithBaseURL("https://twitter.com", webscript_who, "text/html", "UTF-8", null);
        wv_gov.loadDataWithBaseURL("https://twitter.com", webscript_gov, "text/html", "UTF-8", null);
        wv_moh.loadDataWithBaseURL("https://twitter.com", webscript_moh, "text/html", "UTF-8", null);
        return view;

    }

    @Override
    public int getTitle() {
        return R.string.title_home;
    }
}
