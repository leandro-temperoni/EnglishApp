package com.temperoni.english.view;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class FragmentView {

    private WeakReference<Fragment> fragment;

    public FragmentView(Fragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    public Activity getActivity() {
        Fragment f = fragment.get();
        return (f == null) ? null : f.getActivity();
    }
}
