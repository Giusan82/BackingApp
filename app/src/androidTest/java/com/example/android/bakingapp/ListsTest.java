package com.example.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.MainActivity;
import com.example.android.bakingapp.utilities.RecipesData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ListsTest {

    @Rule
    public IntentsTestRule<MainActivity> mRecipesRuleTest = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mRecipesRuleTest.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void RecipesListClickTest(){
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.ingredient_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));

        intended(allOf(hasExtraWithKey(RecipesData.EXTRA_INGREDIENTS), hasExtraWithKey(RecipesData.EXTRA_STEPS)));
    }

    @Test
    public void StepsListClickTest(){
        ViewInteraction recipesList = onView(
                allOf(withId(R.id.rv_list)));
        recipesList.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction stepsList = onView(
                allOf(withId(R.id.step_fragment)));
        stepsList.perform(actionOnItemAtPosition(2, click()));

        if(getResources().getConfiguration().screenWidthDp > 600){
            onView(withId(R.id.instruction_fragment)).check(matches(isDisplayed()));
            onView(withId(R.id.instructions)).check(matches(withText(startsWith("2. Whisk"))));
        }else{
            intended(allOf(hasExtraWithKey(RecipesData.EXTRA_STEPS_LIST), hasExtra(RecipesData.EXTRA_STEPS_POSITION, 2)));
            onView(allOf(withId(R.id.instructions), isDisplayed())).check(matches(withText(startsWith("2. Whisk"))));
        }

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    private Resources getResources(){
        return mRecipesRuleTest.getActivity().getResources();
    }
}
