package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// source for implementing Serializable: https://stackoverflow.com/a/2736612
public class RecipesData implements Serializable{
    public static final String LOG_TAG = RecipesData.class.getName();

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_INGREDIENT = "ingredient";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_STEPS_ID = "id";
    private static final String KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_VIDEO_URL = "videoURL";
    private static final String KEY_THUMBNAIL_URl = "thumbnailURL";
    private static final String KEY_IMAGE = "image";

    public static final String EXTRA_INGREDIENTS = "extra_ingredients";
    public static final String EXTRA_STEPS = "extra_steps";
    public static final String EXTRA_STEPS_LIST = "extra_steps_list";
    public static final String EXTRA_RECIPE_POSITION = "extra_recipe_position";
    public static final String EXTRA_STEPS_POSITION = "extra_steps_position";
    public static final String EXTRA_RECIPE_NAME = "extra_recipe_name";

    private static final String PREF_KEY_RECIPE_POSITION = "pref_key_recipe_position";
    private static final String PREF_KEY_RECIPE_NAME = "pref_key_recipe_name";
    private static final String PREF_KEY_JSON_RESPONSE = "pref_key_json_response";
    public static final int PREF_KEY_DEFAULT_POSITION = -1;


    @SerializedName(KEY_ID)
    private int mID;
    @SerializedName(KEY_NAME)
    private String mName;
    @SerializedName(KEY_SERVINGS)
    private int mServings;
    @SerializedName(KEY_IMAGE)
    private String mImage;

    @SerializedName(KEY_INGREDIENTS)
    private Ingredients[] mIngredients;
    @SerializedName(KEY_STEPS)
    private Steps[] mSteps;

    public RecipesData(){}

    public int getID(){return mID;}
    public String getName(){return mName;}
    public int getServings(){return mServings;}

    public Ingredients[] getIngredients(){return mIngredients;}
    public Steps[] getSteps(){return mSteps;}


    public class Ingredients implements Serializable{

        @SerializedName(KEY_INGREDIENT)
        private String mIngredient;
        @SerializedName(KEY_QUANTITY)
        private float mQuantity;
        @SerializedName(KEY_MEASURE)
        private String mMeasure;

        public String getIngredient(){return mIngredient;}
        public float getQuantity(){return mQuantity;}
        public String getMeasure(){return mMeasure;}
    }

    public class Steps implements Serializable{

        @SerializedName(KEY_STEPS_ID)
        private int mStepID;
        @SerializedName(KEY_SHORT_DESCRIPTION)
        private String mShortDescription;
        @SerializedName(KEY_DESCRIPTION)
        private String mDescription;
        @SerializedName(KEY_VIDEO_URL)
        private String mVideoUrl;
        @SerializedName(KEY_THUMBNAIL_URl)
        private String mImageUrl;

        public int getStepID(){return mStepID;}
        public String getShortDescription(){return mShortDescription;}
        public String getStepDescription(){return mDescription;}
        public String getVideoUrl(){return mVideoUrl;}
        public String getImageUrl(){return mImageUrl;}
    }

    public static void setPosition(Context context, int position) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(PREF_KEY_RECIPE_POSITION, position);
        editor.apply();
    }
    public static int getPosition(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int position = sharedPrefs.getInt(PREF_KEY_RECIPE_POSITION, PREF_KEY_DEFAULT_POSITION);
        return position;
    }

    public static void setRecipeName(Context context, String name) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PREF_KEY_RECIPE_NAME, name);
        editor.apply();
    }

    public static String getRecipeName(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPrefs.getString(PREF_KEY_RECIPE_NAME, "");
        return name;
    }

    public static void setJsonResponse(Context context, String jsonResponse) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(PREF_KEY_JSON_RESPONSE, jsonResponse);
        editor.apply();
    }

    public static String getJsonResponse(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonResponse = sharedPrefs.getString(PREF_KEY_JSON_RESPONSE, "");
        return jsonResponse;
    }
}
