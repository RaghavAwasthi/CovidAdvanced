package com.rvai.covid_19;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rvai.covid_19.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataStore {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context ctx;
    static FirebaseDatabase db = FirebaseDatabase.getInstance();

    public DataStore(Context ctx) {
        this.ctx = ctx;
        preferences = ctx.getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void registerUser(UserModel user) {
        db.getReference().child("users").child(user.getId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public UserModel fetchUser(FirebaseAuth mAuth) {
        final UserModel[] user = new UserModel[1];
        DatabaseReference userRef = db.getReference().child("users").child(mAuth.getCurrentUser().getPhoneNumber());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user[0] = dataSnapshot.getValue(UserModel.class);
                if (user[0] != null) {
                    editor.putString(Constants.USERADDRESS, user[0].getAddress());
                    editor.putString(Constants.USERIMAGE, user[0].getImageurl());
                    editor.putFloat(Constants.USERLAT, (float) user[0].getLocation().getLat());
                    editor.putFloat(Constants.USERLON, (float) user[0].getLocation().getLon());
                    editor.putString(Constants.USERNAME, user[0].getName());
                    editor.putString(Constants.MOID,user[0].getMoId());
                    if (user[0].getIsQuarantined() == 1) {
                        editor.putBoolean(Constants.QUARANTINEMODE, true);
                    } else editor.putBoolean(Constants.QUARANTINEMODE, false);

                    editor.apply();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return user[0];
    }

    public void refresh() {
        new JsonTask().execute("https://ironinfo.herokuapp.com/info");
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            fetchUser(FirebaseAuth.getInstance());
        editor.apply();

    }


    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject jsonArr = null;
            try {
                jsonArr = new JSONObject(result);
                JSONObject jsonObj = jsonArr.getJSONObject("data");
                editor.putInt(Constants.DEATHCOUNT, (Integer) jsonObj.get("deaths"));
                editor.putInt(Constants.CUREDCOUNT, (Integer) jsonObj.get("recovered"));
                editor.putInt(Constants.TOTALCOUNT, (Integer) jsonObj.get("coronaCases"));
                editor.apply();

            } catch (JSONException e) {

                e.printStackTrace();
            }


        }
    }

    public void processImage(Bitmap bmp)
    {

    }


}
