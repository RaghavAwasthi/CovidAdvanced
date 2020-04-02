package com.rvai.covid_19.quarantine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rvai.covid_19.Constants;
import com.rvai.covid_19.DataStore;
import com.rvai.covid_19.R;
import com.rvai.covid_19.models.AssesmentModel;
import com.rvai.covid_19.utils.Utils;

import java.util.UUID;

public class AssesmentActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialButton cough_yes, cough_no, breathing_yes, breathing_no, button_submit;
    boolean c, b;
    TextInputEditText bodytemp;
    Spinner units;
    AssesmentModel assesmentModel;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    static FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assesmentModel = new AssesmentModel();
        setContentView(R.layout.activity_assesment);
        cough_no = findViewById(R.id.cough_no);
        cough_yes = findViewById(R.id.cough_yes);
        breathing_no = findViewById(R.id.breathing_no);
        breathing_yes = findViewById(R.id.breathing_yes);
        button_submit = findViewById(R.id.button_submit);
        cough_yes.setOnClickListener(this);
        cough_no.setOnClickListener(this);
        button_submit.setOnClickListener(this);
        breathing_yes.setOnClickListener(this);
        breathing_no.setOnClickListener(this);
        bodytemp = findViewById(R.id.body_temp);
        preferences = getSharedPreferences(Constants.APP_PREFERENCE_NAME, MODE_PRIVATE);
        editor = preferences.edit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cough_no:
                cough_no.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.selectionColor));
                cough_yes.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.white));
                cough_yes.setTextColor(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.black));
                assesmentModel.setCough(0);
                c = true;
                break;

            case R.id.cough_yes:
                cough_yes.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.selectionColor));
                cough_no.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.white));
                cough_no.setTextColor(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.black));
                assesmentModel.setCough(1);
                c = true;
                break;


            case R.id.breathing_yes:
                breathing_yes.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.selectionColor));
                breathing_no.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.white));
                breathing_no.setTextColor(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.black));
                assesmentModel.setBreathingdifficulty(1);
                b = true;
                break;

            case R.id.breathing_no:
                breathing_no.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.selectionColor));
                breathing_yes.setBackgroundTintList(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.white));
                breathing_yes.setTextColor(ContextCompat.getColorStateList(AssesmentActivity.this, R.color.black));
                assesmentModel.setBreathingdifficulty(0);

                b = true;
                break;

            case R.id.button_submit:
                submit();
                break;


        }

    }

    private void submit() {

        if (validate()) {
            assesmentModel.setId(UUID.randomUUID().toString());
            assesmentModel.setTimestamp(Utils.getCurrentDateTime());
            assesmentModel.setBodytemp(Integer.parseInt(bodytemp.getText().toString()));
            if (assesmentModel.getBodytemp() > 98) {
                assesmentModel.setFever(1);
            } else {
                assesmentModel.setFever(0);
            }

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);



        }
    }

    int CAMERA_REQUEST = 1;

    private boolean validate() {
        boolean flag = true;
        if (b == false) {
            Toast.makeText(this, "Please Select if you have breathing problem or not", Toast.LENGTH_SHORT).show();
            return false;
        } else if (c == false) {
            Toast.makeText(this, "Please Select if you have coughing problem or not", Toast.LENGTH_SHORT).show();
            return false;
        }
        return flag;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            DataStore ds = new DataStore(getApplicationContext());
            ds.processImage(photo);

            Toast.makeText(this, "Image Verified Successfully", Toast.LENGTH_SHORT).show();
            db.getReference().child("assesment").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).push().setValue(assesmentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AssesmentActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                    editor.putLong(Constants.LASTASSESMENT, Utils.getCurrentDateTime());
                    finish();
                }
            });

        }
    }
}
