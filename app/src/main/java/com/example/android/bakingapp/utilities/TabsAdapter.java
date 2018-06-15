package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.InstructionsFragment;


public class TabsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private RecipesData.Steps[] mSteps;

    public TabsAdapter(Context context, FragmentManager fm, RecipesData.Steps[] steps) {
        super(fm);
        mSteps = steps;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        InstructionsFragment instructionsFragment = null;
        //creates new fragments views as many as steps found
        for (int i = 0; i < mSteps.length; i++) {
            instructionsFragment = new InstructionsFragment();
            instructionsFragment.setPosition(position);
            instructionsFragment.setStepsList(mSteps);
        }
        return instructionsFragment;
    }

    @Override
    public int getCount() {
        return mSteps.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String label = "";
        for (int i = 0; i < mSteps.length; i++) {
            if (position == 0) {
                label = mContext.getString(R.string.first_tab_name);
            } else {
                label = String.valueOf(mSteps[position].getStepID());
            }
        }
        return label;
    }
}
