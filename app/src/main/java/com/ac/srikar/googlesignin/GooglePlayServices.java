package com.ac.srikar.googlesignin;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Class implements Google Sign In logic.
 */
class GooglePlayServices implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * Log cat Tag
     */
    private static final String LOG_TAG = GooglePlayServices.class.getSimpleName();

    /**
     * Variable can have reference to either FragmentActivity or AppCompatActivity
     */
    private final FragmentActivity activity;

    /**
     * Variable has reference to an Activity's Context
     */
    private final Context context;

    /**
     * Variable has reference to Google SignIn Options
     */
    private GoogleSignInOptions gso;

    /**
     * Variable has reference to Google Api Client
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Interface used to notify Main activity to perform sign in actions
     */
    private PerformSignAction performSignAction;

    /**
     * Interface used to notify Second activity to perform sign out actions
     */
    private PerformSignOutRevokeAccess performSignOutRevokeAccess;

    /**
     * Default Constructor.
     *
     * @param activity has reference to the activity
     * @param context  has reference to the context
     */
    GooglePlayServices(FragmentActivity activity, Context context) {
        this.activity = activity;
        this.context = context;

        configureSignIn();
        buildGoogleApiClient();

        // Initialize PerformSignAction Interface
        try {
            performSignAction = (PerformSignAction) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        // Initialize PerformSignOutRevokeAccess Interface
        try {
            performSignOutRevokeAccess = (PerformSignOutRevokeAccess) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure sign-in to request the user's ID, email address, and basic
     * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
     */
    private void configureSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
    }

    /**
     * Build Google Api Client
     */
    private void buildGoogleApiClient() {
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Google Api Connection Failed: " + connectionResult.getErrorMessage());

    }

    /**
     * Sign out and Revoke access
     */
    void signOutAndRevoke() {
        // Sign out the user
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
        // Revoke the access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
        performSignOutRevokeAccess.signOutRevokeAccess();
    }

    /**
     * Handle sign-in button tap by creating a sign-in intent with the getSignInIntent method,
     * and starting the intent with startActivityForResult.
     */
    Intent signIn() {
        // Clears the account selected by the user and reconnects the client asking the
        // user to pick an account again.
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    /**
     * Handle the result returned from launching the Intent from GoogleSignInApi#getSignInIntent(...)
     */
    void handleResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.d(LOG_TAG, "Handle SignIn Result:" + result.isSuccess());
        handleSignInResult(result);
    }

    /**
     * Handle SignIn Result.
     */
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // If sign-in succeeded, you can call the getSignInAccount method to get a
            // GoogleSignInAccount object that contains information about the signed-in user.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.i(LOG_TAG, "Account Information: " + acct);
            if (acct != null) {
                performSignAction.signIntoAccount(acct);
            }
        }
    }

    /**
     * Interface that notifies the MainActivity when the users signs in.
     */
    interface PerformSignAction {
        void signIntoAccount(GoogleSignInAccount acct);
    }

    /**
     * Interface that notifies the SecindActivity when the user signs out.
     */
    interface PerformSignOutRevokeAccess {
        void signOutRevokeAccess();
    }
}
