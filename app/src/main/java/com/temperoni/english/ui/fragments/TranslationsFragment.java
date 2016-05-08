package com.temperoni.english.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.temperoni.english.R;
import com.temperoni.english.model.TranslationsModel;
import com.temperoni.english.database.model.Type.TranslationType;
import com.temperoni.english.presenter.TranslationsPresenter;
import com.temperoni.english.utils.AppBus;
import com.temperoni.english.view.TranslationsView;

/**
 * Created by COCO on 10/10/2015.
 */
public class TranslationsFragment extends Fragment {

    private TranslationsPresenter presenter;

    public static TranslationsFragment newInstance(TranslationType type){
        TranslationsFragment fragment = new TranslationsFragment();
        Bundle args = new Bundle();
        args.putSerializable("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.translations_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        TranslationType type = (TranslationType) getArguments().getSerializable("type");

        presenter = new TranslationsPresenter(
                new TranslationsModel(AppBus.getInstance(), type),
                new TranslationsView(this, AppBus.getInstance()));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        presenter.setUpMenu(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_translations, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppBus.getInstance().register(presenter);
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        AppBus.getInstance().unregister(presenter);
        presenter.shutDownSpeech();
    }
}
