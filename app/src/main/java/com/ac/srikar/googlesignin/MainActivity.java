package com.ac.srikar.googlesignin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GooglePlayServices.PerformSignAction {

    /**
     * Request code used in the startActivityForResult method
     */
    private static final int RC_SIGN_IN = 9001;

    /**
     * Variable holds reference to the Utility class
     */
    private GooglePlayServices googlePlayServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // SignIn Button listener
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        //Initialize Google Play Services class
        googlePlayServices = new GooglePlayServices(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                startActivityForResult(googlePlayServices.signIn(), RC_SIGN_IN);
                break;
        }
    }

    /**
     * Activity's onActivityResult method
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            googlePlayServices.handleResult(data);
        }
    }

    @Override
    public void signIntoAccount(GoogleSignInAccount acct) {
        // Create an intent to open the Second Activity
        Intent intent = new Intent(this, SecondActivity.class);
        // Send Account Info
        intent.putExtra("ACCOUNT_INFO", acct);
        // Start opening the activity
        startActivity(intent);
        // Finish this activity
        finish();
    }
}
