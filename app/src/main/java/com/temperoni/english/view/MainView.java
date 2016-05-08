package com.temperoni.english.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;
import com.squareup.otto.Bus;
import com.temperoni.english.R;
import com.temperoni.english.database.model.Type.TranslationType;
import com.temperoni.english.ui.fragments.TranslationsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leandro.temperoni on 3/16/2016.
 */
public class MainView extends ActivityView {

    private Bus bus;
    private BottomBar mBottomBar;
    private FragNavController mNavController;

    private final int INDEX_VOCABULARY = FragNavController.TAB1;
    private final int INDEX_EXPRESSIONS = FragNavController.TAB2;
    private final int INDEX_PHRASAL_VERBS = FragNavController.TAB3;

    public MainView(AppCompatActivity activity, Bus bus, Bundle savedInstanceState){
        super(activity);
        this.bus = bus;
        ButterKnife.bind(this, activity);
        setUpViews(savedInstanceState, activity);
    }

    public void setUpViews(Bundle savedInstanceState, AppCompatActivity activity) {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        loadFragments(activity);

        mBottomBar = BottomBar.attach(activity, savedInstanceState);
        mBottomBar.setItems(
                new BottomBarTab(R.drawable.ic_font_download_white_24dp, "Vocabulary"),
                new BottomBarTab(R.drawable.ic_record_voice_over_white_24dp, "Expressions"),
                new BottomBarTab(R.drawable.ic_textsms_white_24dp, "Phrasal verbs")
        );

        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                switchTab(position);
            }

            @Override
            public void onTabReSelected(int position) {
                mNavController.clearStack();
            }
        });

        mBottomBar.setActiveTabColor(ContextCompat.getColor(activity, R.color.accent));
    }

    private void loadFragments(AppCompatActivity activity) {

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TranslationsFragment.newInstance(TranslationType.VOCABULARY));
        fragments.add(TranslationsFragment.newInstance(TranslationType.EXPRESSIONS));
        fragments.add(TranslationsFragment.newInstance(TranslationType.PHRASAL_VERBS));

        mNavController = new FragNavController(activity.getSupportFragmentManager(),
                R.id.container, fragments);
    }

    @OnClick(R.id.fab)
    protected void onFabClick() {
        bus.post(new OnFabClickEvent());
    }

    public void onSaveInstanceState(Bundle outState) {
        mBottomBar.onSaveInstanceState(outState);
    }

    public static class OnFabClickEvent {

        public OnFabClickEvent() {
        }
    }

    private void switchTab(int position){
        switch (position){
            case 0: {
                mNavController.switchTab(INDEX_VOCABULARY);
                break;
            }
            case 1: {
                mNavController.switchTab(INDEX_EXPRESSIONS);
                break;
            }
            case 2: {
                mNavController.switchTab(INDEX_PHRASAL_VERBS);
                break;
            }
        }
    }
}
