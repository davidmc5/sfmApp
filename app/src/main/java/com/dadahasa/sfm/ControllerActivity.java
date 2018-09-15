package com.dadahasa.sfm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class ControllerActivity extends AppCompatActivity {


    private Toolbar toolbar;
    String thisController;
    public EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.controller_activity);

        toolbar = findViewById(R.id.controller_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        //display the current controller id
        editText = findViewById(R.id.controller_id);

        final SharedPreferences  pref = this.getApplication().getSharedPreferences("myPrefFile", Context.MODE_PRIVATE);
        //final SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        thisController = pref.getString("CONTROLLER_ID", "demo");
        editText.setText(thisController);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //boolean handled = false;
                //davidToast.makeText(getApplicationContext(),"ON EDIT ACTION" + String.valueOf(actionId), Toast.LENGTH_LONG).show();
                if (actionId == 6) {
                    ////////////////////
                    //Toast.makeText(getApplicationContext(),"PRESSED DONE", Toast.LENGTH_LONG).show();
                    //"enter" key was pressed
                    //store value and return to previous activity
                    thisController = v.getText().toString();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("CONTROLLER_ID", thisController);
                    editor.commit();
                    finish();
                }return true;
            }
        });





    }


}
