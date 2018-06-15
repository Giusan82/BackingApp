package com.example.android.bakingapp.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.IdlingResources.IdlingManager;
import com.example.android.bakingapp.R;

/**
 * This Api request was made using these tutorials:
 * https://www.androidhive.info/2014/05/android-working-with-volley-library-1/
 * https://www.sitepoint.com/volley-a-networking-library-for-android/
 * https://stackoverflow.com/questions/28120029/how-can-i-return-value-from-function-onresponse-of-volley
 */

public class ApiRequest {
    private static final String LOG_TAG = ApiRequest.class.getSimpleName();
    private static final int DELAY_MILLIS = 3000;
    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String TAG_REQUEST = "api_request";
    private Context mContext;
    private RequestQueue requestQueue;

    @Nullable
    private IdlingManager mIdlingManager;

    //Constructor
    public ApiRequest(Context context, IdlingManager idlingManager) {
        mContext = context;
        mIdlingManager = idlingManager;
    }

    public interface Callback {
        void onSuccess(String result);
    }

    public void get(final Callback callback, boolean hasLoading) {

        final ProgressDialog loader;
        if (hasLoading) {
            loader = new ProgressDialog(mContext, R.style.alertDialog);
            loader.setTitle(mContext.getString(R.string.loading_title));
            loader.setMessage(mContext.getString(R.string.loading_message));
            loader.show();
        } else {
            loader = null;
        }

        if (mIdlingManager != null) {
            mIdlingManager.setIdleState(false);
        }
        requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (callback != null) {
                    callback.onSuccess(response);
                    if (loader != null) {
                        loader.dismiss();
                    }
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (mIdlingManager != null) {
                                        mIdlingManager.setIdleState(true);
                                    }
                                }
                            }, DELAY_MILLIS);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());
                Toast.makeText(mContext, mContext.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                if (loader != null) {
                    loader.dismiss();
                }
            }
        });
        stringRequest.setTag(TAG_REQUEST);
        requestQueue.add(stringRequest);
    }
}
