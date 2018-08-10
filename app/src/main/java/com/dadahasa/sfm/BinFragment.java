package com.dadahasa.sfm;

/**
 * Created by David on 2/20/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class BinFragment extends Fragment {

    int position;
    private TextView textView;
    private DatabaseReference mDatabase;

    //controller id hardcoded! Read it instead from the app's shared preferences.
    //TODO add a settings menu to enter the controller's ID.
    final String THIS_CONTROLLER = "c_1";
    String switchClicked;



    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        BinFragment binFragment = new BinFragment();
        binFragment.setArguments(bundle);
        return binFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bin_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.binId);

        //TODO get bin label from firebase
        textView.setText("Bin " + (position + 1));


        //set initial app switches' state from the database
        final Switch s1 = getView().findViewById(R.id.binAuger);
        s1.setOnCheckedChangeListener(new SwitchStatus());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child("goodby").setValue(null); //this deletes the entry
        //mDatabase.child("users").child("goodby").setValue("La MAR!!!");

             //retrieve here the switch status of the database and update the UI
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once when first attaching the listener and again
                // whenever data at this location is updated.
                Boolean value1, value2;
                DataSnapshot rootData = dataSnapshot.child("controllers").child(THIS_CONTROLLER);

                try {
                    value1 = rootData.child("bin_1_sw_1").getValue(Boolean.class);
                    value2 = rootData.child("bin_2_sw_1").getValue(Boolean.class);
                }catch(Exception e){
                    value1 = false;
                    value2 = false;
                    //Log.d("database", "EXCEPTION -------- >>>>>: " + value);

                    //TODO set default switch value in the database.
                }
                //Log.d("database", "Value is: " + value);
                //There is a problem here that, crashes with null pointer when clicking at BINs
                if(position ==0) {
                    s1.setChecked(value1);
                }else if (position == 1){
                    s1.setChecked(value2);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("database", "Failed to read value.", error.toException());
            }
        });
    }


    //this class updates the firebase database switch status
    // when a switch in one of the bin tabs has been clicked
    class SwitchStatus implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //Log.d("database", "SW ID: " + buttonView.getId());



            switch (buttonView.getId()) {

                case R.id.binAuger:

                    //TODO For some unknown reason, this toast causes a crash after a few  toggles of the switch
                   //Toast.makeText(getContext(), "The Switch is " + (isChecked ? "on" : "off"), Toast.LENGTH_SHORT).show();

                    if(position ==0) {
                        mDatabase.child("controllers").child("c_1").child("bin_1_sw_1").setValue(isChecked);
                    }else if(position ==1) {
                        mDatabase.child("controllers").child("c_1").child("bin_2_sw_1").setValue(isChecked);
                    }

                    break;
            }
        }

    }

}