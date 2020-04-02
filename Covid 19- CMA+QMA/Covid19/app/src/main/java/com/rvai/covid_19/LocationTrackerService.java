package com.rvai.covid_19;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rvai.covid_19.models.LocationModel;
import com.rvai.covid_19.utils.Utils;

public class LocationTrackerService extends Service {
    SharedPreferences preferences ;
    public LocationTrackerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildNotification();
        requestLocationUpdates();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void buildNotification() {

        Notification notification = new NotificationCompat.Builder(this, Constants.LOCATIONCHANNEL)
                .setContentTitle("Tracking Locaion")
                .setSmallIcon(R.drawable.ic_location)
                .build();

        startForeground(25, notification);


    }


//Initiate the request to track the device's location//

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
preferences=getSharedPreferences(Constants.APP_PREFERENCE_NAME,MODE_PRIVATE);
//Specify how often your app should request the device’s location//

        request.setInterval(10000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//
                    if (FirebaseAuth.getInstance().getCurrentUser() != null&&preferences.getString(Constants.MOID,null)!=null) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("locationupdates").child(
                                preferences.getString(Constants.MOID,null)).child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            LocationModel model = new LocationModel();
                            model.setLat(location.getLatitude());
                            model.setLon(location.getLongitude());
                            model.setTimestamp(Utils.getCurrentDateTime());
//Save the location data to the database//


                            ref.setValue(model);
                        }
                    }

                }
            }, null);
        }
    }


}

