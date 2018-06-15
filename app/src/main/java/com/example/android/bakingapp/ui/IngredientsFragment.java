package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class IngredientsFragment extends Fragment implements ListAdapter.ItemOnClickHandler{

    @BindView(R.id.rv_list_ingredients)
    RecyclerView mListIngredients;

    private ArrayList<RecipesData.Ingredients> mIngredients;
    private ListAdapter mIngredientsAdapter;


    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManagerIngredients = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mListIngredients.setLayoutManager(layoutManagerIngredients);
        mListIngredients.setHasFixedSize(true);

        mIngredients = new ArrayList<>();
        mIngredientsAdapter = new ListAdapter(getContext(), mIngredients,this, true);
        mListIngredients.setAdapter(mIngredientsAdapter);

        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(RecipesData.EXTRA_INGREDIENTS)){
            RecipesData.Ingredients[] ingredients = (RecipesData.Ingredients[]) intent.getSerializableExtra(RecipesData.EXTRA_INGREDIENTS);

            for(int i = 0; i < ingredients.length; i++){
                mIngredients.add(ingredients[i]);
            }
        }
        return rootView;
    }


    @Override
    public void onClick(int position) {
        Timber.d("Position: " + position);
    }
}
