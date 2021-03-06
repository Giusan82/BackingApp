package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.BuildConfig;
import com.example.android.bakingapp.IdlingResources.IdlingManager;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utilities.ApiRequest;
import com.example.android.bakingapp.utilities.ListAdapter;
import com.example.android.bakingapp.utilities.RecipesData;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ListAdapter.ItemOnClickHandler{

    private ArrayList<RecipesData> mItems;
    private ListAdapter adapter;

    @BindView(R.id.rv_list) RecyclerView mList;

    @Nullable
    private IdlingManager mIdlingManager;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingManager == null) {
            mIdlingManager = new IdlingManager();
        }
        return mIdlingManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the views
        ButterKnife.bind(this);

        // Get the IdlingResource instance
        getIdlingResource();

        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree(){
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + "|" + element.getMethodName() + "|" + element.getLineNumber();
                }
            });
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, RecipesData.numberOfColumns(this));

        mList.setLayoutManager(layoutManager);
        mList.setHasFixedSize(true);

        //the ArrayList is initialized
        mItems = new ArrayList<>();
        adapter = new ListAdapter(this, mItems, this,false);
        mList.setAdapter(adapter);
        ApiRequest apiRequest = new ApiRequest(this, mIdlingManager);
        apiRequest.get(new ApiRequest.Callback() {
            @Override
            public void onSuccess(String result) {
                RecipesData.setJsonResponse(getApplicationContext(),result);
                Gson gson = new Gson();
                RecipesData[] data = gson.fromJson(result, RecipesData[].class);

                clear();
                for(int i = 0; i < data.length; i++){
                    mItems.add(data[i]);
                }
            }
        }, true);
    }

    private void clear() {
        //clear the arraylist
        this.mItems.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        RecipesData current = (RecipesData) mItems.get(position);

        //this open the StepsActivity
        Intent intent = new Intent(this, StepsActivity.class);

        intent.putExtra(RecipesData.EXTRA_STEPS, current.getSteps());
        intent.putExtra(RecipesData.EXTRA_INGREDIENTS, current.getIngredients());
        intent.putExtra(RecipesData.EXTRA_RECIPE_NAME, current.getName());
        intent.putExtra(RecipesData.EXTRA_RECIPE_POSITION, position);

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
        }
    }
}
