package com.example.android.bakingapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.ListAdapter;
import com.example.android.bakingapp.utilities.RecipesData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class StepsFragment extends Fragment implements ListAdapter.ItemOnClickHandler {

    //Constructor
    public StepsFragment() {
    }

    @BindView(R.id.rv_list_steps)
    RecyclerView mListStep;

    private ArrayList<RecipesData.Steps> mStepsList;
    private RecipesData.Steps[] mSteps;

    private ListAdapter mStepsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManagerSteps = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mListStep.setLayoutManager(layoutManagerSteps);
        mListStep.setHasFixedSize(true);

        //the ArrayList of steps list is initialized
        mStepsList = new ArrayList<>();
        mStepsAdapter = new ListAdapter(getContext(), mStepsList, this, false);
        mListStep.setAdapter(mStepsAdapter);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(RecipesData.EXTRA_STEPS)) {

            mSteps = (RecipesData.Steps[]) intent.getSerializableExtra(RecipesData.EXTRA_STEPS);

            for (int i = 0; i < mSteps.length; i++) {
                mStepsList.add(mSteps[i]);
            }
        }
        return rootView;
    }

    @Override
    public void onClick(int position) {
        if (getActivity().findViewById(R.id.instruction_fragment) != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            InstructionsFragment instructions = new InstructionsFragment();
            instructions.setPosition(position);
            instructions.setStepsList(mSteps);
            fragmentManager.beginTransaction().replace(R.id.instruction_fragment, instructions).commit();
        } else {
            Timber.d("Position: " + position + " No have two pane");
            //this open the InstructionsActivity
            Intent intent = new Intent(getContext(), InstructionsActivity.class);
            intent.putExtra(RecipesData.EXTRA_STEPS_LIST, mSteps);
            intent.putExtra(RecipesData.EXTRA_STEPS_POSITION, position);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                this.startActivity(intent);
            }
        }
    }
}
