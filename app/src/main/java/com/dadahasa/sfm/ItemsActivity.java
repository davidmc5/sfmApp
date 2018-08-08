package com.dadahasa.sfm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by David on 2/14/2018.
 */

public class ItemsActivity extends AppCompatActivity{

    private String mDetail;
    private TextView mDetailDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin_items);

        mDetailDisplay = (TextView) findViewById(R.id.tv_bin_items);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mDetail = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mDetailDisplay.setText(mDetail);

                //Set the title of the activity to the item selected in the main activity
                setTitle(mDetail);
            }
        }
    }

}
