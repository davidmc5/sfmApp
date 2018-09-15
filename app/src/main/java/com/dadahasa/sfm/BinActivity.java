package com.dadahasa.sfm;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


/**
 * Created by David on 2/21/2018.
 */

public class BinActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin_tabs);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        // Associate the ViewPager with a new instance of our adapter
        viewPager = findViewById(R.id.viewPager);
        BinAdapter adapter = new BinAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);



    ////////// TODO 1: THIS IS NOT WORKING WELL
        //https://stackoverflow.com/questions/11860820/android-simpleonpagechangelistener-determine-swipe-direction/28933530
        //https://stackoverflow.com/questions/8538035/android-pageradapter-get-current-position/13831450#13831450
        //https://stackoverflow.com/questions/11293300/determine-when-a-viewpager-changes-pages
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                //pass this to the fragment
                //the position returned by this listener's call back is reliable
                // but it needs to be passed to the tab's fragment
                //g.d("POSITION", "\n############# REAL POSITION: " + position + "\n");

                //pass position to fragment ----> This is not working well
                //Capture the fragment instance to call one of its methods
                getSupportFragmentManager().executePendingTransactions();
                BinFragment binFragment = (BinFragment) getSupportFragmentManager().findFragmentById(R.id.viewPager);
                //binFragment.binPosition(position);
            }
        });

        ///////////




        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public void onClick(View view) {
    }


}
