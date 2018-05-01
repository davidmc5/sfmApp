package com.dadahasa.sfm;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by David on 2/21/2018.
 */

public class BinActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //private TextView text_view;
    //private Button button_A;
    //private Button button_B;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_view);

        //firebase

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child("goodby").setValue("8888");
        //mDatabase.child("users").child("goodby").setValue(null); //this deletes the entry
        mDatabase.child("users").child("goodby").setValue("asterix");
       // mDatabase.child("users").child("hello").setValue(null);
        mDatabase.child("users").child("hello").setValue("filomena");




        //put this in a new class and start with an intent from BIN button

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));

        // Associate the ViewPager with a new instance of our adapter
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onClick(View view) {

    }
}
