package com.dadahasa.sfm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;

    private Button mBinButton;

   //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.tabs_view);
        setContentView(R.layout.activity_main);
        mBinButton = (Button) findViewById(R.id.button1);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("users").child("hello").setValue("1234");
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