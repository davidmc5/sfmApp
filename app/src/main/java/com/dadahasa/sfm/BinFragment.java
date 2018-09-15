package com.dadahasa.sfm;

/**
 * Created by David on 2/20/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BinFragment extends Fragment {

    //TODO changing the position variable to static works better because it stays constant to each fragment instance
    //but still, something is not working right.
    static int position;

    private TextView textView;
    //private TextView sensor1;

    private DatabaseReference mDatabase;
    private ValueEventListener listener;

    //controller id hardcoded! Read it instead from the app's shared preferences.
    //TODO add a settings menu to enter the controller's ID.
    String THIS_CONTROLLER = "demo";
    String thisBin;
    String ROOT_CHILD;
    String SWITCHES;
    String LABELS;
    String SENSORS;



    boolean dbUpdated = true;
    SharedPreferences mSharedPreference;
    public static String preference = "myPrefFile";

    public static BinFragment getInstance(int position) {
        BinFragment binFragment = new BinFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);

        //Log.d("BUNDLE", "\n\n############# POSITION BUNDLE: " + position + "\n");
        binFragment.setArguments(bundle);
        return binFragment;
    }

    //TODO 2: the fragment class is called twice since the adaptor is pre fetching the next view
    // so the position variable is updated twice with two consecutive numbers
    //need to either get the position from the tab listener into the fragment,
    // or make methods static so that it only gets updated once for each tab's fragment instance

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        Log.d("POSITION", "\n\n############# POSITION AFTER: " + position + "\n");

        //ViewPager mViewPager = getView().findViewById(R.id.viewPager);
        //position = mViewPager.getCurrentItem();
       // thisBin = "bin_" + String.valueOf(position+1) + '_';


        //GET PREFERENCE FOR CONTROLLER ID

        ///LOGS ARE NOT WORKING HERE!!!! ????
        //Log.d("CONTROLLER ID= ", "\n\n\n\n\n\n\n\n\n\n\n TEST LINE!!!!!!!!!! \n\n\n\n\n\n\n\n\n\n\n");


        //try with a specific preference file:
        mSharedPreference = getContext().getSharedPreferences(preference, Context.MODE_PRIVATE);

        //since this is a fragment, we need to get the context of the activity
         //mSharedPreference =  PreferenceManager.getDefaultSharedPreferences(getContext());


        if (mSharedPreference.contains("CONTROLLER_ID")) {
            THIS_CONTROLLER = mSharedPreference.getString("CONTROLLER_ID", "demo");
            //Toast.makeText(getContext(), THIS_CONTROLLER, Toast.LENGTH_LONG).show();
        }else{
            //Store default value
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putString("CONTROLLER_ID", "demo");
            editor.apply();
        }
        //set the database references for this controller
        ROOT_CHILD = "controllers/" + THIS_CONTROLLER + "/";
        SWITCHES = ROOT_CHILD + "switches/";
        LABELS = ROOT_CHILD + "labels/";
        SENSORS = ROOT_CHILD + "sensors/";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO changed for single activity
        //return inflater.inflate(R.layout.bin_fragment, container, false);
        return inflater.inflate(R.layout.single_main_activity, container, false);
    }

    @Override
    //this runs immediately after the view has been created
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
        //Log.d("POSITION", "\n############# TRALARA: " + position + "\n");
        //TODO removed for single activity
        //textView.setText("Bin " + String.valueOf(position + 1));
        //Log.d("POSITION", "\n############# POSITION: " + position + "\n");


        //// get the view for the sensors
        final TextView sensor1 = view.findViewById(R.id.sensor1);
        final TextView sensor2 = view.findViewById(R.id.sensor2);
        final TextView sensor3 = view.findViewById(R.id.sensor3);
        final TextView sensor4 = view.findViewById(R.id.sensor4);
        final TextView sensor5 = view.findViewById(R.id.sensor5);
        final TextView sensor6 = view.findViewById(R.id.sensor6);
        final TextView sensor7 = view.findViewById(R.id.sensor7);
        final TextView sensor8 = view.findViewById(R.id.sensor8);
        final TextView sensor9 = view.findViewById(R.id.sensor9);
        final TextView sensor10 = view.findViewById(R.id.sensor10);
        final List<TextView> sensors = Arrays.asList(
                sensor1, sensor2, sensor3, sensor4, sensor5,
                sensor6, sensor7, sensor8, sensor9, sensor10);


        //Initialize all switches of the view with labels, db state (on/off) and add change listener (SwitchStatus)

        //Collect all the switches (touchables) of the view in an array
        //https://stackoverflow.com/questions/22639218/how-to-get-all-buttons-ids-in-one-time-on-android
        final ArrayList<View> allSwitches;

        //TODO replaced for single activity
        //allSwitches = (getView().findViewById(R.id.switch_container)).getTouchables();
        allSwitches = (getView().findViewById(R.id.single_activity)).getTouchables();
        //The switches invisible will not be counted! Make sure to turn the visibility off after this count!!!
        //Log.d("SMF", "The number of switches is " + allSwitches.size());

        //Set the SwitchState listener to all switches
        //the initial call may reset all the switches' values. Make sure we don't change the state of the database!!!
        for (int i = 0; i<allSwitches.size(); i++){
            int swId = allSwitches.get(i).getId();
            Switch sw = view.findViewById(swId);

            //Note: setOnClickListener triggers events only on user action
            // setOnCheckedChangeListener triggers events on any status changes, even if done by code (.setChecked)
            sw.setOnClickListener(new SwitchState());
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child("goodbye").setValue(null); //this deletes the entry


        //retrieve here the switch status from the database to set the UI switches
        //**mDatabase.addValueEventListener(new ValueEventListener() {
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once when first attaching the listener and again
                // whenever data at this database location is updated.
                //Toast.makeText(getContext(), "DATA CHANGE CALLED", Toast.LENGTH_LONG).show();

                Boolean value;
                String label;

                //String sensorValue;

                //rootData = dataSnapshot.child("controller_activity").child(THIS_CONTROLLER);
                //Log.d("HELLO", "\n############# THIS SWITCH1: " );
                Switch sw;
                for (int i = 0; i < allSwitches.size(); i++) {
                    int swId = allSwitches.get(i).getId();
                    ///////////////////////
                    ////for debuging multiple clicks instability
                    //Log.d("HELLO", "\n############# THIS SWITCH2: " + String.valueOf(i + 1));

                    try {
                        sw = getActivity().findViewById(swId);
                        //get switch state from database (if any)
                        value = dataSnapshot.child(SWITCHES + thisBin + "sw_" + String.valueOf(i + 1)).getValue(Boolean.class);
                        //and set the state of the UI switch to match the one on the database
                        sw.setChecked(value);
                        //Log.d("HELLO", "\n############# THIS SWITCH3: " + String.valueOf(i + 1));


                        try {

                            //TODO: do the following only once, maybe onCreate?
                            //otherwise we are updating all labels every time a switch changes state!

                            label = dataSnapshot.child(LABELS + thisBin + "sw_" + String.valueOf(i + 1)).getValue(String.class);
                            //and set the label of the UI switch to match the one on the database
                            //Toast.makeText(getContext(), "Label" + label, Toast.LENGTH_LONG).show();
                            //Log.d("LABELS", "\n############# THIS LABEL: " + label);

                            if (!label.equals("")) {
                                sw.setText(label);
                                sw.setVisibility(View.VISIBLE);
                            }
                            else{
                                sw.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            //Since label is missing, add a generic (BLANK) label stub to db to edit later
                            String newLabel = "sw_" + String.valueOf(i + 1);
                            //mDatabase.child(LABELS + thisBin + newLabel).setValue(newLabel);
                            mDatabase.child(LABELS + thisBin + newLabel).setValue("");
                            //Do not show a blank switch until it has a label
                            sw.setVisibility(View.INVISIBLE);
                        }

                    } catch (Exception e) {
                        Log.d("database", "EXCEPTION -------- >>>>>: " + e);
                        //set default switch value false in the database if the switch does not exist.
                        //the listener currently is only set for changes, not additions.
                        mDatabase.child(SWITCHES + thisBin + "sw_" + String.valueOf(i + 1)).setValue(false);
                        Log.d("BIN FRAGMENT", "Switch ID " + swId);
                    }
                }

                //TODO: display two sensors
                //Retrieve and display sensor data changes
                //get new sensor readings from database (if any)
                displaySensorValue(dataSnapshot, sensors);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("database", "Failed to read value.", error.toException());
            }
        };
    }

    void displaySensorValue(DataSnapshot dataSnapshot,  List<TextView> sensors){
        List<String> labels = Arrays.asList(
                "c_1", "c_2", "c_3", "c_4", "c_5",
                "c_6", "c_7", "c_8", "c_9", "c_10");

        for (int n = 0; n < labels.size(); n++){

            String sensorValue = dataSnapshot.child(SENSORS + labels.get(n)).getValue(String.class);
            if (parseInteger(sensorValue) != 0) {
                sensors.get(n).setText(sensorValue + " A");
                sensors.get(n).setVisibility(View.VISIBLE);
            }else{
                sensors.get(n).setVisibility(View.INVISIBLE);
            }
        }
    }

    //this listener class needs to be attached to each switch
    // It will be called when a switch in one of the bin tabs has been clicked
    // it determines which switch has been clicked by the returned id.
    //It then sets the switch state on the database.

    class SwitchState implements CompoundButton.OnClickListener {
        //String changedSwitch;

        @Override
        public void onClick(View view) {

            //Log.d("SWITCH", "SWITCH CHANGED STATES");

            Switch sw = (Switch) view;
            Boolean swState = sw.isChecked();
            String changedSwitch;

            switch (view.getId()) {

                case R.id.bin_sw_1:
                    changedSwitch = SWITCHES + thisBin + "sw_1";
                    break;

                case R.id.bin_sw_2:
                    changedSwitch = SWITCHES + thisBin + "sw_2";
                    break;

                case R.id.bin_sw_3:
                    changedSwitch = SWITCHES + thisBin + "sw_3";
                    break;

                case R.id.bin_sw_4:
                    changedSwitch = SWITCHES + thisBin + "sw_4";
                    break;

                case R.id.bin_sw_5:
                    changedSwitch = SWITCHES + thisBin + "sw_5";
                    break;

                case R.id.bin_sw_6:
                    changedSwitch = SWITCHES + thisBin + "sw_6";
                    break;

                case R.id.bin_sw_7:
                    changedSwitch = SWITCHES + thisBin + "sw_7";
                    break;

                case R.id.bin_sw_8:
                    changedSwitch = SWITCHES + thisBin + "sw_8";
                    break;

                case R.id.bin_sw_9:
                    changedSwitch = SWITCHES + thisBin + "sw_9";
                    break;

                case R.id.bin_sw_10:
                    changedSwitch = SWITCHES + thisBin + "sw_10";
                    break;

                default:
                    changedSwitch="X";
                    break;
            }
            mDatabase.child(changedSwitch).setValue(swState);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mDatabase.addValueEventListener(listener);
        //Optionally, use use instead addListenerForSingleValueEvent(),
        // that automatically detaches listener onPause()
        //so it wouldn't need to be removed on onPause
    }


    @Override
    public void onPause(){
        super.onPause();
        //When the device goes to sleep, the following code will remove the listener.
        //need to add the listener back on onResume()!
        mDatabase.removeEventListener(listener);
    }

    public int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException nfe) {
            // Log exception.
            return 0;
        }
    }

/*
    public void binPosition(int binPosition) {
        //position = binPosition;
    }
*/
}