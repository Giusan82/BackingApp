package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.RecipesData;
import com.example.android.bakingapp.utilities.TabsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class InstructionsActivity extends AppCompatActivity {
    private static final String KEY_POSITION_STATE = "position";
    private static final int DELAY_MILLIS = 500;
    private RecipesData.Steps[] mSteps;
    private int mPosition;

//    @BindView(R.id.previous)
//    Button mPrevious;
//
//    @BindView(R.id.next)
//    Button mNext;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        ButterKnife.bind(this);
        Intent intent = getIntent();


        if (intent.hasExtra(RecipesData.EXTRA_STEPS_POSITION)) {
            mPosition = intent.getIntExtra(RecipesData.EXTRA_STEPS_POSITION, 0);
        }
        if (intent.hasExtra(RecipesData.EXTRA_STEPS_LIST)) {
            mSteps = (RecipesData.Steps[]) intent.getSerializableExtra(RecipesData.EXTRA_STEPS_LIST);
//            InstructionsFragment instructionsFragment = new InstructionsFragment();
//            instructionsFragment.setStepsList(mSteps);
        }

        // Create an adapter that knows which fragment should be shown on each page
        TabsAdapter tabsAdapter = new TabsAdapter(this, getSupportFragmentManager(), mSteps);
        // Set the adapter onto the view pager
        viewPager.setAdapter(tabsAdapter);
        //viewPager.setCurrentItem(mPosition);

        tabLayout.setupWithViewPager(viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.setElevation(3);
        }
        if(savedInstanceState == null){
            tabLayout.setSmoothScrollingEnabled(true);
            tabLayout.setScrollPosition(mPosition, 0, true);

            //delay the setting of scroll position to allow the update the display
            //source: https://stackoverflow.com/a/31452777
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.getTabAt(mPosition).select();
                        }
                    }, DELAY_MILLIS);
        }else{
            final int position = savedInstanceState.getInt(KEY_POSITION_STATE);
            tabLayout.setSmoothScrollingEnabled(true);
            tabLayout.setScrollPosition(position, 0, true);

            //delay the setting of scroll position to allow the update the display
            //source: https://stackoverflow.com/a/31452777
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.getTabAt(position).select();
                        }
                    }, DELAY_MILLIS);
        }


//        final FragmentManager fragmentManager = getSupportFragmentManager();
//        final InstructionsFragment instructions = new InstructionsFragment();
//        instructions.setStepsList(mSteps);
//        instructions.setPosition(mPosition);
//        fragmentManager.beginTransaction().add(R.id.instruction_fragment, instructions).commit();
//
//        mPrevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mPosition >= 0 && mPosition < mSteps.length){
//                    InstructionsFragment instructions = new InstructionsFragment();
//                    instructions.setStepsList(mSteps);
//                    instructions.setPosition(mPosition--);
//                    Timber.d("Position: " + mPosition);
//                    fragmentManager.beginTransaction().replace(R.id.instruction_fragment, instructions).commit();
//                }
//
//            }
//        });
//
//        mNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mPosition >= 0 && mPosition < mSteps.length){
//                    InstructionsFragment instructions = new InstructionsFragment();
//                    instructions.setStepsList(mSteps);
//                    instructions.setPosition(mPosition++);
//                    Timber.d("Position: " + mPosition);
//                    fragmentManager.beginTransaction().replace(R.id.instruction_fragment, instructions).commit();
//                }
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION_STATE, tabLayout.getSelectedTabPosition());
    }
}
