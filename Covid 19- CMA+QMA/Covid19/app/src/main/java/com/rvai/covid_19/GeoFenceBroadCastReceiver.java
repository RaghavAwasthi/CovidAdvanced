package com.rvai.covid_19;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

public class GeoFenceBroadCastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Send notification and log the transition details.
            sendNotification(context);
        } else {

        }
    }

    public void sendNotification(Context context)
    {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, Constants.CHANNELID)
                .setSmallIcon(R.drawable.ic_notification_reminder)
                .setContentTitle("Location Alert")
                .setContentText("You have breached the security boundary")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(context);
        notificationmanager.notify(200, notification.build());
    }

}
