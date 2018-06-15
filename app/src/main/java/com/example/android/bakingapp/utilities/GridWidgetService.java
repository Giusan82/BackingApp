package com.example.android.bakingapp.utilities;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridWidgedAdapter(this.getApplicationContext());
    }
}
