package com.kevin.android.cardgamev1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.kevin.android.cardgamev1.battle.Battle;
import com.kevin.android.cardgamev1.creationtools.CreatorMain;
import com.kevin.android.cardgamev1.decks.DeckSelector;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    Button battleButton;
    Button libraryButton;
    Button creationButton;
    SignInButton signInButton;
   // private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleApiClient mGoogleApiClient;
    private int mCurrentScreen;
    private static int RC_SIGN_IN = 9001;
    public static int RC_SELECT_PLAYERS = 9005;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;
    private boolean initialLogin;

    final private static int[] SCREENS = {
            R.id.sign_in_screen, R.id.main_screen
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();

      //  mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        creationButton = (Button)findViewById(R.id.creation_activity_button);
        battleButton = (Button)findViewById(R.id.battle_button);
        libraryButton = (Button)findViewById(R.id.library_button);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                onStartMatchClicked(view);
                Intent battleActivity = new Intent(MainActivity.this, Battle.class);
                startActivity(battleActivity);
            }
        });
        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deckSelectorActivity = new Intent(MainActivity.this, DeckSelector.class);
                startActivity(deckSelectorActivity);
            }
        });
        creationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cardCreationActivity = new Intent(MainActivity.this, CreatorMain.class);
                startActivity(cardCreationActivity);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInClicked();
            }
        });
            mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
            // add other apis needed here
        .build();

    }



    public void onStartMatchClicked(View view) {
        Intent intent =
                Games.TurnBasedMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 7, true);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }



    @Override
    protected void onStart() {
        if (mGoogleApiClient == null){
            if (!initialLogin){
                switchToScreen(R.id.sign_in_screen);
            }

        } else if (!mGoogleApiClient.isConnected()){

            mGoogleApiClient.connect();
        } else if (mGoogleApiClient.isConnected()){

            switchToMainScreen();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        stopKeepingScreenOn();
        initialLogin = false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    // hide sign in button here and allow player to continue
    switchToMainScreen();
        initialLogin = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    if (mResolvingConnectionFailure){
        // already doing resolve process
        return;
    }
    // sign in button clicked or auto sign in - launches signin
    if (mSignInClicked || mAutoStartSignInflow) {
        mAutoStartSignInflow = false;
        mSignInClicked = false;
        mResolvingConnectionFailure = true;

        if (!BaseGameUtils.resolveConnectionFailure(this,
                mGoogleApiClient, connectionResult,
                RC_SIGN_IN, R.string.signin_other_error)) {
            mResolvingConnectionFailure = false;
            Log.d("PROBLEM", "HERE IS THE PROBLEM");
            Log.d("PROBLEM", "HERE IS THE PROBLEM");
            Log.d("PROBLEM", "HERE IS THE PROBLEM");
            Log.d("PROBLEM", "HERE IS THE PROBLEM");
            Log.d("PROBLEM", "HERE IS THE PROBLEM");
        }
    }
// sign in button code goes here
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == RC_SIGN_IN){
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK){
                mGoogleApiClient.connect();
            } else {
                // bring up error dialogue here
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_failure);
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
                Log.d("LOGIN RESULT", Integer.toString(resultCode));
            }
        }
    }

    // call when sign in button is clicked
    private void signInClicked(){
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }
    // Call when the sign-out button is clicked
    private void signOutclicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        switchToScreen(R.id.sign_in_screen);
    }
    
    // sample to guard google API calls
   //    final static String MY_ACHIEVEMEMENT_ID = "...."; // your achievement ID here
    
//   if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
    // Call a Play Games services API method, for example:
  //  Achievements.unlock(mGoogleApiClient, MY_ACHIEVEMENT_ID);
//} else {
    // Alternative implementation (or warn user that they must
    // sign in to use this feature)
//  }

    // Sets the flag to keep this screen on.
   private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
   private void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void switchToScreen(int screenId) {
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurrentScreen = screenId;
        // can add notifications here based on connectedness
    }
    private void switchToMainScreen() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.main_screen);
        }
        else {
            switchToScreen(R.id.sign_in_screen);
        }
    }

}
