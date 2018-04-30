package com.dadahasa.sfm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dadahasa.sfm.MainAdapter.MainAdapterOnClickHandler;

public class MainActivity extends AppCompatActivity implements MainAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private MainAdapter mMainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_menu);

        /*
        * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
        * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
        * languages.
        */

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mMainAdapter = new MainAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMainAdapter);

        mRecyclerView.setVisibility(View.VISIBLE);

        String[] menuLabels = {"Bins", "Dryers", "Pits", "Lights", "Outlets", "Legs", "Misc" };
        mMainAdapter.setMenuLabels(menuLabels);

    }

    @Override
    public void onClick(String menuItem) {
        Context context = this;
        Class destinationClass = ItemsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        // COMPLETED (1) Pass the weather to the DetailActivity
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, menuItem);
        startActivity(intentToStartDetailActivity);

    }
}
