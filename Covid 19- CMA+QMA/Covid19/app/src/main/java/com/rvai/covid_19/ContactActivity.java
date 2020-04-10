package com.rvai.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rvai.covid_19.models.ContactModel;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        RecyclerView recyclerView = findViewById(R.id.contact_list);
        ContactAdapter adapter = new ContactAdapter();

        ArrayList<ContactModel> contact = new ArrayList<>();
        contact.add(new ContactModel("Andhra Pradesh", "0866-2410978"));
        contact.add(new ContactModel("Arunachal Pradesh ", "9436055743"));


        contact.add(new ContactModel("Assam ", "6913347770"));
        contact.add(new ContactModel("Bihar ", "104"));
        contact.add(new ContactModel("Chhattisgarh ", "104"));
        contact.add(new ContactModel("Goa", "104"));
        contact.add(new ContactModel("Gujarat", "104"));
        contact.add(new ContactModel("Haryana ", "8558893911"));
        contact.add(new ContactModel("Himachal Pradesh ", "104"));
        contact.add(new ContactModel("Jharkhand", "104"));
        contact.add(new ContactModel("Karnataka ", "104"));
        contact.add(new ContactModel("Kerala ", "04712552056"));
        contact.add(new ContactModel("Madhya Pradesh", "104"));
        contact.add(new ContactModel("Maharashtra ", "020-26127394"));
        contact.add(new ContactModel("Manipur", "3852411668"));
        contact.add(new ContactModel("Meghalaya ", "108"));
        contact.add(new ContactModel("Mizoram ", "102"));
        contact.add(new ContactModel("Nagaland", "7005539653"));
        contact.add(new ContactModel("Odisha ", "9439994859"));
        contact.add(new ContactModel("Punjab ", "104"));
        contact.add(new ContactModel("Rajasthan ", "0141-2225624"));
        contact.add(new ContactModel("Sikkim", "104"));
        contact.add(new ContactModel("Tamil Nadu ", "044-29510500"));
        contact.add(new ContactModel("Telangana ", "104"));
        contact.add(new ContactModel("Tripura ", "03812315879"));
        contact.add(new ContactModel("Uttarakhand ", "104"));
        contact.add(new ContactModel("Uttar Pradesh", "18001805145"));
        contact.add(new ContactModel("West Bengal", "1800313444222"));
        contact.add(new ContactModel("Andaman and Nicobar" +
                "Islands", "03192232102"));
        contact.add(new ContactModel("Chandigarh ", "9779558282"));
        contact.add(new ContactModel("Dadra and Nagar Haveli and Daman & Diu ", "104"));
        contact.add(new ContactModel("Delhi ", "01122307145"));
        contact.add(new ContactModel("Jammu & Kashmir", "01912520982"));
        contact.add(new ContactModel("Ladakh", "01982256462"));
        contact.add(new ContactModel("Lakshadweep", "104"));
        contact.add(new ContactModel("Puducherry", "104"));
        contact.add(new ContactModel("", ""));


        adapter.setList(contact);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
