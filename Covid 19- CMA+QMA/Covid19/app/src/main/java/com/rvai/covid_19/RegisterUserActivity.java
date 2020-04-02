package com.rvai.covid_19;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rvai.covid_19.models.LocationModel;
import com.rvai.covid_19.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    boolean mLocationPermissionGranted = false;
    Location mLastKnownLocation;
    final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 569;
    final int MY_CAMERA_PERMISSION_CODE = 584;
    final int CAMERA_REQUEST = 546;
    FusedLocationProviderClient mFusedLocationProviderClient;
    int DEFAULT_ZOOM = 15;
    TextInputEditText Addresstextfield, nameTextField;
    Marker myLoc;
    MaterialButton next, selectImage;
    AppCompatImageView user_image;
    UserModel user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mUploadReference;
    Uri imageUri;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setTitle("Register");
        preferences = getSharedPreferences(Constants.APP_PREFERENCE_NAME, MODE_PRIVATE);
        editor = preferences.edit();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // myLoc.getPosition();
        nameTextField = findViewById(R.id.name_field);
        Addresstextfield = findViewById(R.id.address_field);
        selectImage = findViewById(R.id.button_image_click);
        user_image = findViewById(R.id.user_image);
        next = findViewById(R.id.button_next);
        user = new UserModel();


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TextInputEditText> fields = new ArrayList<>();
                fields.add(nameTextField);
                fields.add(Addresstextfield);
                if (validate(fields)) {
                    user.setName(nameTextField.getText().toString());
                    user.setAddress(Addresstextfield.getText().toString());
                    user.setDayStarted(-1);
                    user.setId(auth.getCurrentUser().getPhoneNumber());
                    user.setIsQuarantined(0);
                    user.setLocation(new LocationModel(myLoc.getPosition().latitude, myLoc.getPosition().longitude));

                    mUploadReference = storage.getReference().child("user_images").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    mUploadReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    user.setImageurl(uri.toString());
                                    db.getReference().child("users").child(user.getId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            editor.putString(Constants.USERADDRESS, user.getAddress());
                                            editor.putString(Constants.USERIMAGE, user.getImageurl());
                                            editor.apply();
                                            Toast.makeText(RegisterUserActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Thread.sleep(Toast.LENGTH_LONG);
                                                        finish();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }).start();
                                        }
                                    });

                                }
                            });
                        }
                    });
                }


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        Toast.makeText(this, "Location Set", Toast.LENGTH_SHORT).show();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

        updateLocationUI();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);

                            myLoc = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                                    .title("My House")
                                    .draggable(true));
                        } else {
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }

                    }

                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            user_image.setPadding(0, 0, 0, 0);
            user_image.setImageBitmap(photo);
        }
    }

    public boolean validate(List<TextInputEditText> edittextlist) {
        boolean flag = true;
        for (EditText appCompatEditText : edittextlist) {
            if (appCompatEditText == null || appCompatEditText.getText().toString().length() < 1) {
                appCompatEditText.setError("Field is required");
                flag = false;
            }
            if (imageUri == null) {
                Toast.makeText(this, "Please Set Profile Image", Toast.LENGTH_SHORT).show();
                flag = false;
            }


        }
        return flag;
    }
}
