package com.example.android.bakingapp;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.StepsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FragmetsActivityTest {


    @Rule
    public ActivityTestRule<StepsActivity> mStepsRuleTest = new ActivityTestRule<>(StepsActivity.class);

    @Test
    public void ingredientsFragmentsDisplaydTest(){
        onView(withId(R.id.ingredient_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void stepsFragmentsDisplaydTest(){
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));
    }
    @Test
    public void instructionFragmentDisplayedTest(){
        //check if the instruction fragment is displayed with smallest screen width device > 600
        if(getResources().getConfiguration().screenWidthDp > 600)
        onView(withId(R.id.instruction_fragment)).check(matches(isDisplayed()));
    }


    private Resources getResources(){
        return mStepsRuleTest.getActivity().getResources();
    }
}
