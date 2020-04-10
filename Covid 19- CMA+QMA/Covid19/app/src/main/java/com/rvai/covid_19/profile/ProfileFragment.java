package com.rvai.covid_19.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rvai.covid_19.ChatActivity;
import com.rvai.covid_19.Constants;
import com.rvai.covid_19.DataStore;
import com.rvai.covid_19.MainActivity;
import com.rvai.covid_19.MainFragment;
import com.rvai.covid_19.R;
import com.rvai.covid_19.RegisterUserActivity;
import com.rvai.covid_19.models.AlertsModel;
import com.rvai.covid_19.models.UserModel;
import com.rvai.covid_19.utils.Utils;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends MainFragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    MaterialButton login, signup, quarantine_button, raisealert,chatbutton;
    AppCompatImageView imageview;
    AppCompatTextView username, useraddress, userMobile, quarantineDuration;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseDatabase db;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        db = FirebaseDatabase.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            view = inflater.inflate(R.layout.profile_not_logged_in, container, false);
            login = view.findViewById(R.id.button_login);
            signup = view.findViewById(R.id.button_signup);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).signIn();
                    ((MainActivity) Objects.requireNonNull(getActivity())).selectInitialNavigationItem();
                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).signIn();
                    ((MainActivity) Objects.requireNonNull(getActivity())).selectInitialNavigationItem();

                }
            });


        } else {

            view = inflater.inflate(R.layout.fragment_profile, container, false);
            LinearLayoutCompat quarantineholder = view.findViewById(R.id.quarantine_holder);
            quarantineDuration = view.findViewById(R.id.quarantineDuration);
            imageview = view.findViewById(R.id.employee_image);
            username = view.findViewById(R.id.user_name);
            useraddress = view.findViewById(R.id.userAddress);
            userMobile = view.findViewById(R.id.userMobile);
            useraddress.setText(preferences.getString(Constants.USERADDRESS, " "));
            userMobile.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
            username.setText(preferences.getString(Constants.USERNAME, " "));
            quarantine_button = view.findViewById(R.id.button_quarantine);
            if (preferences.getBoolean(Constants.QUARANTINEMODE, false)) {
                quarantine_button.setVisibility(View.GONE);
                quarantineholder.setVisibility(View.VISIBLE);
                quarantineDuration.setText(Integer.toString(preferences.getInt(Constants.QUARANTINEDAYS, 0)));
            }
            raisealert = view.findViewById(R.id.button_alert);
            Glide.with(imageview).load(preferences.getString(Constants.USERIMAGE, "")).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(imageview);
            quarantine_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialog();


                }
            });

            raisealert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertsModel model = new AlertsModel();
                    model.setPatientID(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    model.setMoid(preferences.getString(Constants.MOID, null));
                    model.setPatientName(preferences.getString(Constants.USERNAME, null));
                    model.setTimetamp(Utils.getCurrentDateTime());
                    model.setPatientAddress(preferences.getString(Constants.USERADDRESS, null));
                    db.getReference("alerts").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Alert Sent Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


            chatbutton=view.findViewById(R.id.button_chat);
            chatbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getContext(), ChatActivity.class);
                    startActivity(i);
                }
            });

        }
        return view;
    }

    public void showdialog() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.setListener(new MyDialogFragment.ExampleDialogListener() {
            @Override
            public void applyTexts(String moID, String password) {
                applyText(moID, password);
            }
        });
        dialogFragment.show(getFragmentManager(), "dialog");

    }

    @Override
    public int getTitle() {
        return R.string.title_profile;
    }


    public void applyText(String moID, String password) {

        if (password.equals("AZERTY")) {
            editor.putBoolean(Constants.QUARANTINEMODE, true);
            editor.putString(Constants.MOID, moID);
            quarantine_button.setVisibility(View.GONE);
            editor.putInt(Constants.QUARANTINEDAYS, 14);
            editor.apply();


            DatabaseReference userRef = db.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    user.setIsQuarantined(1);
                    user.setDayStarted(14);
                    user.setMoId(moID);
                    userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Information Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
