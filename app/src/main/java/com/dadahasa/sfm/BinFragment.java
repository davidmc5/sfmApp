package com.dadahasa.sfm;

/**
 * Created by David on 2/20/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BinFragment extends Fragment {

    //TODO changing the position variable to static works better because it stays constant to each fragment instance
    //but still, something is not working right.
    static int position;

    private TextView textView;
    private DatabaseReference mDatabase;

    //controller id hardcoded! Read it instead from the app's shared preferences.
    //TODO add a settings menu to enter the controller's ID.
    final String THIS_CONTROLLER = "c_1";
    String thisBin;
    String ROOT_CHILD = "controllers/" + THIS_CONTROLLER + "/";
    DataSnapshot rootData;

    public static BinFragment getInstance(int position) {
        BinFragment binFragment = new BinFragment();





        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);

        Log.d("BUNDLE", "\n\n############# POSITION BUNDLE: " + position + "\n");
        binFragment.setArguments(bundle);
        return binFragment;
    }

    //TODO 2: the fragment class is called twice since the adaptor is pre fetching the next view
    // so the position variable is updated twice with two consequtive numbers
    //need to either get the position from the tab listener into the fragment,
    // or make methods static so that it only gets updated once for each tab's fragment instance

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        //Log.d("POSITION", "\n\n############# POSITION AFTER: " + position + "\n");

        //ViewPager mViewPager = getView().findViewById(R.id.viewPager);
        //position = mViewPager.getCurrentItem();
       // thisBin = "bin_" + String.valueOf(position+1) + '_';



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bin_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





////
        //ViewPager mViewPager = getActivity().findViewById(R.id.viewPager);


        //position = mViewPager.getCurrentItem();

        //position = getArguments().getInt("pos");

////

        thisBin = "bin_" + String.valueOf(position+1) + '_';




        textView = view.findViewById(R.id.binId);

        //TODO get bin label from firebase
        Log.d("POSITION", "\n############# TRALARA: " + position + "\n");
        textView.setText("Bin " + String.valueOf(position + 1));
        //Log.d("POSITION", "\n############# POSITION: " + position + "\n");



        //Initialize all switches of the view with labels, state (on/off) and change listener (SwitchStatus)

        //Collect all the switches (touchables) of the view in an array
        //https://stackoverflow.com/questions/22639218/how-to-get-all-buttons-ids-in-one-time-on-android
        final ArrayList<View> allSwitches;
        allSwitches = (getView().findViewById(R.id.switch_container)).getTouchables();
        //Log.d("SMF", "The number of switches is " + allSwitches.size());

        //Look to set the SwitchState listener to all switches
        //the initial call may reset all the switches' values. Make sure we don't change the state of the database!!!
        for (int i = 0; i<allSwitches.size(); i++){
            int swId = allSwitches.get(i).getId();
            Switch sw = view.findViewById(swId);
            sw.setOnCheckedChangeListener(new SwitchState());
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child("goodby").setValue(null); //this deletes the entry

        //retrieve here the switch status from the database and set the UI switches
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once when first attaching the listener and again
                // whenever data at this location is updated.

                Boolean value;

                rootData = dataSnapshot.child("controllers").child(THIS_CONTROLLER);
                Switch sw;
                for (int i = 0; i<allSwitches.size(); i++) {
                    int swId = allSwitches.get(i).getId();
                    sw = getActivity().findViewById(swId);

                    try {
                        //get switch state from database (if any)
                        value = dataSnapshot.child(ROOT_CHILD + thisBin + "sw_" + String.valueOf(i+1)).getValue(Boolean.class);
                        //and set the state of the UI switch to match the one on the database
                        sw.setChecked(value);
                        //Log.d("HELLO", "\n############# THIS BIN: " + thisBin + "\n\");

                    } catch (Exception e) {
                        //Log.d("database", "EXCEPTION -------- >>>>>: " + value);
                        //TODO set default switch value false in the database if the switch does not exist.
                        //maybe this is done automatically by the listener on first attach (SwitchState)?
                        //No... the listener currently is only set for changes, not additions.
                        mDatabase.child(ROOT_CHILD + thisBin + "sw_" + String.valueOf(i+1)).setValue(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("database", "Failed to read value.", error.toException());
            }
        });
    }


    //this listener class needs to be attached to each switch
    // It will be called when a switch in one of the bin tabs has been clicked
    // it determines which switch has been clicked by the returned id.
    //It then sets the switch state on the database.
    class SwitchState implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //Log.d("database", "SW ID: " + buttonView.getId());

            switch (buttonView.getId()) {

                case R.id.bin_sw_1:
                    //TODO For some unknown reason, this toast causes a crash after a few  toggles of the switch
                    //Toast.makeText(getContext(), "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_1").setValue(isChecked);
                    break;

                case R.id.bin_sw_2:
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_2").setValue(isChecked);
                    break;

                case R.id.bin_sw_3:
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_3").setValue(isChecked);
                    break;

                case R.id.bin_sw_4:
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_4").setValue(isChecked);
                    break;

                case R.id.bin_sw_5:
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_5").setValue(isChecked);
                    break;

                case R.id.bin_sw_6:
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_6").setValue(isChecked);
                    break;

                case R.id.bin_sw_7:
                    mDatabase.child(ROOT_CHILD + thisBin + "sw_7").setValue(isChecked);
                    break;

            }

        }

    }


    public void binPosition(int binPosition) {
        //position = binPosition;
    }


    }