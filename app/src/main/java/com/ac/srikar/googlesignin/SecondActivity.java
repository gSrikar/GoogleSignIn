package com.ac.srikar.googlesignin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener,
        GooglePlayServices.PerformSignOutRevokeAccess {

    /**
     * Variable has reference to the Utility class
     */
    private GooglePlayServices googlePlayServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // SignOut Button listener
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        // Initialize UI elements
        TextView userName = (TextView) findViewById(R.id.userName_textView);
        TextView userEmail = (TextView) findViewById(R.id.userEmail_textView);
        SimpleDraweeView userImage = (SimpleDraweeView) findViewById(R.id.user_imageView);

        //Initialize Google Play Services class
        googlePlayServices = new GooglePlayServices(this, this);

        // Get the account information from the bundle
        GoogleSignInAccount acct = getIntent().getParcelableExtra("ACCOUNT_INFO");
        if (acct != null) {
            // Retrieve the user Display name
            String personName = acct.getDisplayName();
            // Retrieve the user Email
            String personEmail = acct.getEmail();
            // Retrieve the user Profile picture link
            Uri personPhoto = acct.getPhotoUrl();
            // Set the User name
            userName.setText(personName);
            // Set the User email
            userEmail.setText(personEmail);
            // Set the User picture
            userImage.setImageURI(personPhoto);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    /**
     * Sign out the user and revoke access
     */
    private void signOut() {
        finish();
        googlePlayServices.signOutAndRevoke();
    }

    @Override
    public void signOutRevokeAccess() {
        // Create an intent to open the Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        // Start opening the activity
        startActivity(intent);
        // Finish this activity
        finish();
    }
}
