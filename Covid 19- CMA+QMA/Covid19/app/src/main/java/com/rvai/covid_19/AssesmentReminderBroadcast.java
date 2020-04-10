package com.rvai.covid_19;

import android.Manifest;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import org.threeten.bp.LocalDateTime;

public class AssesmentReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        requestLocationUpdates(context);

        Intent serviceIntent = new Intent(context, LocationTrackerService.class);

        ContextCompat.startForegroundService(context, serviceIntent);


        SharedPreferences preferences = context.getSharedPreferences(Constants.APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        LocalDateTime obj = Utils.getLocalDateTime(Utils.getCurrentDateTime());
        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(context, Constants.CHANNELID)
                .setSmallIcon(R.drawable.ic_notification_reminder)
                .setContentTitle("Water Reminder")
                .setContentText("Please Drink a glass of Water")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationmanager5 = NotificationManagerCompat.from(context);
        notificationmanager5.notify(200, notification2.build());
        SharedPreferences.Editor editor = preferences.edit();
        {
            switch (obj.getHour()) {
                case 10:
                    if (preferences.getBoolean(Constants.P, false) == false) {
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, Constants.CHANNELID)
                                .setSmallIcon(R.drawable.ic_notification_reminder)
                                .setContentTitle("ASSESMENT")
                                .setContentText("Your ASSesment is Pending")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(context);
                        notificationmanager.notify(200, notification.build());
                    }
                case 12:
                    if (preferences.getBoolean(Constants.P, false) == false) {
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, Constants.CHANNELID)
                                .setSmallIcon(R.drawable.ic_notification_reminder)
                                .setContentTitle("ASSESMENT")
                                .setContentText("Your ASSesment is Pending")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(context);
                        notificationmanager.notify(200, notification.build());
                    }
                case 14:
                    if (preferences.getBoolean(Constants.P, false) == false) {
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, Constants.CHANNELID)
                                .setSmallIcon(R.drawable.ic_notification_reminder)
                                .setContentTitle("ASSESMENT")
                                .setContentText("Your ASSesment is Pending")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(context);
                        notificationmanager.notify(200, notification.build());
                    }

                case 16:
                    if (preferences.getBoolean(Constants.P, false) == false) {
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, Constants.CHANNELID)
                                .setSmallIcon(R.drawable.ic_notification_reminder)
                                .setContentTitle("ASSESMENT")
                                .setContentText("Your ASSesment is Pending")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(context);
                        notificationmanager.notify(200, notification.build());
                    }

                case 23:
                    editor.putBoolean(Constants.P, false);
                    editor.putBoolean(Constants.Q, false);
                    editor.putBoolean(Constants.R, false);
                    editor.putBoolean(Constants.S, false);
                    editor.putInt(Constants.QUARANTINEDAYS, preferences.getInt(Constants.QUARANTINEDAYS, 1) - 1);
                    if(preferences.getInt(Constants.QUARANTINEDAYS,0)==0)
                    {
                        editor.putBoolean(Constants.QUARANTINEMODE,false);

                    }


            }

        }
        editor.apply();


    }

    private void requestLocationUpdates(Context context) {
        buildNotification(context);
        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(10000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        int permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("locationupdates").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
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
            }, null);
        }
    }

    private void buildNotification(Context context) {

        Notification notification = new NotificationCompat.Builder(context, Constants.LOCATIONCHANNEL)
                .setContentTitle("Tracking Locaion")
                .setSmallIcon(R.drawable.ic_location)
                .build();


    }

}
