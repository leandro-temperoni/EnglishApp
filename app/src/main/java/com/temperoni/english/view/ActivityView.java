package com.temperoni.english.view;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class ActivityView {

    private WeakReference<Activity> activity;

    public ActivityView(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        return activity.get();
    }
}
