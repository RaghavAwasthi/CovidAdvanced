package com.rvai.covid_19;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position)
        {
            case 0:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/covid-19-server.appspot.com/o/image1.jpg?alt=media&token=c5e75499-1006-4f37-ba03-8597c0ca3f0f");
                break;
            case 1:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/covid-19-server.appspot.com/o/image2.jpg?alt=media&token=415c838e-21a0-4d7b-81a7-e3b7d23ab541");
                break;
            case 2:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/covid-19-server.appspot.com/o/image3.jpg?alt=media&token=27a5cbd3-3e26-43f8-9e0f-71ceac32595e");
                break;
        }

    }
}
