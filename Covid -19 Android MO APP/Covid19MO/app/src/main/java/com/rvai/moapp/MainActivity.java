package com.rvai.moapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    int RC_SIGN_IN = 1254;
    FirebaseAuth mAuth;
    private MainActivityViewModel mViewModel;
    private BottomNavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Menu mOptionsMenu;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidThreeTen.init(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            signIn();
        }


        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.getSelectedNavItem().observe(this, this::displayNavigationItem);
        navigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            selectInitialNavigationItem();
        }

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                    signIn();
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final MainActivityViewModel.MainActivityNavigationItem employeeNavigationItem;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                employeeNavigationItem = MainActivityViewModel.MainActivityNavigationItem.HOME;
                break;
            case R.id.nav_patient:
                employeeNavigationItem = MainActivityViewModel.MainActivityNavigationItem.PATIENT;
                break;
            case R.id.nav_profile:
                employeeNavigationItem = MainActivityViewModel.MainActivityNavigationItem.PROFILE;
                break;

            case R.id.nav_chat:
                employeeNavigationItem = MainActivityViewModel.MainActivityNavigationItem.CHAT;
                break;
            default:
                Log.e(TAG, "onNavigationItemSelected: Navigation item not resolved", new IllegalArgumentException());
                throw new IllegalArgumentException();
        }
        mViewModel.setmSelectedNavItem(employeeNavigationItem);

        return true;
    }

    public void selectInitialNavigationItem() {
        int initialItem = R.id.nav_home;
        onNavigationItemSelected(navigationView.getMenu().findItem(initialItem));
        navigationView.setSelectedItemId(initialItem);
    }

    private void displayNavigationItem(MainActivityViewModel.MainActivityNavigationItem employeeNavigationItem) {
        MainFragment newFragment;
        switch (employeeNavigationItem) {

            case HOME:

                newFragment = new HomeFragment();
                break;
            case PATIENT:
                newFragment = new PatientFragment();
                break;

            case PROFILE:
                newFragment = new LocationAlertsFragment();
                break;

            case CHAT:
                newFragment = new ChatListFragment();
                break;
            default:
                Log.e(TAG, "displayNavigationItem: name not resolved", new IllegalArgumentException());
                throw new IllegalArgumentException();
        }
        if (newFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, newFragment)
                    .commit();
            setTitle(newFragment.getTitle());

        }
    }

    public void signIn() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            // already signed in
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder().setLogo(R.mipmap.ic_launcher)
                            .setIsSmartLockEnabled(false, true)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.PhoneBuilder().build()
                            ))
                            .build(),
                    RC_SIGN_IN);
            // not signed in
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
                if (Math.abs(metadata.getCreationTimestamp() - metadata.getLastSignInTimestamp()) < 5000L) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("MOID", mAuth.getCurrentUser().getPhoneNumber());


                } else {
                    // This is an existing user, show them a welcome back screen.
                }


            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign in was not successfull", Toast.LENGTH_SHORT).show();
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

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Network Found", Toast.LENGTH_SHORT).show();
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
                Log.e("MainActivity", "Sign-in error: ", response.getError());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                signout();
                break;


        }
        return true;
    }

    public void signout() {
        mAuth.signOut();
    }


}
