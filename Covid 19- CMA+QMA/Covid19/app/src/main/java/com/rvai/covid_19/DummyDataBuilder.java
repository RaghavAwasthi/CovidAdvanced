package com.rvai.covid_19;

import com.google.firebase.database.FirebaseDatabase;
import com.rvai.covid_19.models.AdvisoryModel;
import com.rvai.covid_19.utils.Utils;

import java.util.UUID;

public class DummyDataBuilder {
    static FirebaseDatabase db = FirebaseDatabase.getInstance();

    public static void buildDummydata() {
        AdvisoryModel am = new AdvisoryModel();
        am.setId(UUID.randomUUID().toString());
        am.setTitle("Some Title");
        am.setTimestamp(Utils.getCurrentDateTime());
        am.setDesc("Some Description which you might be interested in reading");
        am.setUrl("gs://covid-19-server.appspot.com/pexels-photo-462118.jpeg");
        db.getReference().child("advisory").child(am.getId()).setValue(am);

    }
}
