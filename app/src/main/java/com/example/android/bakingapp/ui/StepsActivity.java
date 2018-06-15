package com.example.android.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Services.BackingServices;
import com.example.android.bakingapp.utilities.RecipesData;

import java.util.ArrayList;

import timber.log.Timber;

public class StepsActivity extends AppCompatActivity {
    private static final String KEY_STATE = "state";

    private RecipesData.Steps[] mSteps;
    private int mRecipePosition;
    private String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);


        Intent intent = getIntent();
        if (intent.hasExtra(RecipesData.EXTRA_RECIPE_NAME)) {
            mRecipeName = intent.getStringExtra(RecipesData.EXTRA_RECIPE_NAME);
            setTitle(mRecipeName);
        }
        if (intent.hasExtra(RecipesData.EXTRA_STEPS)) {

            mSteps = (RecipesData.Steps[]) intent.getSerializableExtra(RecipesData.EXTRA_STEPS);
        }
        if (intent.hasExtra(RecipesData.EXTRA_RECIPE_POSITION)) {
            mRecipePosition = intent.getIntExtra(RecipesData.EXTRA_RECIPE_POSITION, 0);
        }
        if (savedInstanceState == null) {
            if (findViewById(R.id.instruction_fragment) != null) {
                int position = 0;

                FragmentManager fragmentManager = getSupportFragmentManager();

                InstructionsFragment instructions = new InstructionsFragment();

                instructions.setPosition(position);
                instructions.setStepsList(mSteps);

                fragmentManager.beginTransaction().add(R.id.instruction_fragment, instructions).commit();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_STATE, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menu_switch = menu.findItem(R.id.sw_favorite);
        SwitchCompat mSwitch = (SwitchCompat) menu_switch.getActionView();
        if (RecipesData.getPosition(getApplicationContext()) == mRecipePosition) {
            mSwitch.setChecked(true);
        } else {
            mSwitch.setChecked(false);
        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timber.d("Switch: " + isChecked + "| Position: " + mRecipePosition);
                if (isChecked) {
                    RecipesData.setPosition(getApplicationContext(), mRecipePosition);
                    RecipesData.setRecipeName(getApplicationContext(), mRecipeName);
//
//                    Intent initialUpdateIntent = new Intent(
//                            AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//                    initialUpdateIntent
//                            .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//                    sendBroadcast(initialUpdateIntent);
                    BackingServices.updateWidgets(getApplicationContext());
                }
            }
        });

        return true;
    }
}