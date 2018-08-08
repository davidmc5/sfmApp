package com.dadahasa.sfm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;

    private Button mBinButton;


    ////used for authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //// Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;

    //Firebase Instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private String mUsername;
    public static final String ANONYMOUS = "anonymous";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.tabs_view);
        setContentView(R.layout.activity_main);
        mBinButton = (Button) findViewById(R.id.button1);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child("hello").setValue("1234");

        //// Initialize Firebase Components
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){

                // firebaseAuth has current login status for this user.
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    // user is signed in
                    onSignedInInitialize(user.getDisplayName());
                /*
                    Toast.makeText(MainActivity.this,
                                    "You're now signed in!",
                                    Toast.LENGTH_SHORT).show();
                */
                } else {
                    //user is signed out
                    //paste here the desired api call for the sign in flow
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //The returning activity is our sign in flow.
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in Canceled", Toast.LENGTH_SHORT).show();
                //finish this activity
                finish();
            }

        }
    }


    //add the auth listener onResume, and remove it onPause
    @Override
    protected void onPause(){
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //clean up when the activity is destroyed
        detachDatabaseReadListener();
        //mMessageAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitialize(String username){
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup(){
        //if the user is not signed in it should not be able to see the messages.
        //used in onAuthStateChanged
        mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        detachDatabaseReadListener();
    }


    private void attachDatabaseReadListener(){
        //used in onSignedInInitialize
        //only create and attache the listener if it has not yet been yet created
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    //mMessageAdapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            //mDatabaseReference.addChildEventListener(mChildEventListener);
        }
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }




    private void detachDatabaseReadListener(){
        //detach listener only at one time
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    public void openBinIntent(View view){
        //Toast.makeText(getApplicationContext(),"BINS!", Toast.LENGTH_SHORT).show();
        Intent binViewIntent = new Intent(getApplicationContext(),BinActivity.class);
        //inIntent.putExtra("Button", "Bins");
        startActivity(binViewIntent);
    }
    public void notImplemented(View view){
        Toast.makeText(getApplicationContext(),"Not Yet Implemented!", Toast.LENGTH_SHORT).show();
    }
}