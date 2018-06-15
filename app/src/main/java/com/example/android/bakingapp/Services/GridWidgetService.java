package com.example.android.bakingapp.Services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.utilities.GridWidgedAdapter;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridWidgedAdapter(this.getApplicationContext());
    }
}
