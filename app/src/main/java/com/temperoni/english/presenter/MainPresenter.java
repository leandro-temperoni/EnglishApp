package com.temperoni.english.presenter;

import android.os.Bundle;

import com.squareup.otto.Subscribe;
import com.temperoni.english.ui.activities.TranslationFormActivity.TranslationFormType;
import com.temperoni.english.utils.IntentFactory;
import com.temperoni.english.view.MainView;

/**
 * Created by leandro.temperoni on 3/16/2016.
 */
public class MainPresenter {

    private MainView view;

    public MainPresenter(MainView view){
        this.view = view;
    }

    @Subscribe
    public void onFabClick(MainView.OnFabClickEvent event) {
        view.getActivity().startActivity(IntentFactory.openTranslationFormActivity(
                view.getActivity(), TranslationFormType.ADD_MODE));
    }

    public void onSaveInstanceState(Bundle outState) {
        view.onSaveInstanceState(outState);
    }
}
