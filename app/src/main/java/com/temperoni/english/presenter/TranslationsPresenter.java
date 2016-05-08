package com.temperoni.english.presenter;

import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;
import com.temperoni.english.model.TranslationsModel;
import com.temperoni.english.model.TranslationsModel.ErrorEvent;
import com.temperoni.english.model.TranslationsModel.ResponseDoneEvent;
import com.temperoni.english.ui.activities.TranslationFormActivity;
import com.temperoni.english.ui.adapters.TranslationsAdapter.DeleteTranslationEvent;
import com.temperoni.english.ui.adapters.TranslationsAdapter.EditTranslationEvent;
import com.temperoni.english.utils.IntentFactory;
import com.temperoni.english.view.TranslationsView;
import com.temperoni.english.view.TranslationsView.OnAttachSearchViewEvent;
import com.temperoni.english.view.TranslationsView.OnQueryTextChangeEvent;
import com.temperoni.english.view.TranslationsView.ReloadDataEvent;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class TranslationsPresenter {

    private TranslationsModel model;
    private TranslationsView view;

    private boolean searching = false;

    public TranslationsPresenter(TranslationsModel model, TranslationsView view){
        this.model = model;
        this.view = view;
    }

    private void getTranslations(){
        model.getTranslations();
    }

    public void setUpMenu(Fragment fragment){
        view.setUpMenu(fragment);
    }

    @Subscribe
    public void onReloadData(ReloadDataEvent event){
        view.showLoading();
        getTranslations();
    }

    @Subscribe
    public void onResponseDone(ResponseDoneEvent event) {
        view.setTranslations(event.getTranslations());
    }

    @Subscribe
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.getError());
    }

    @Subscribe
    public void onEditTranslationEvent(EditTranslationEvent event){
        view.getActivity().startActivity(IntentFactory.openTranslationFormActivity(
                view.getActivity(), TranslationFormActivity.TranslationFormType.EDIT_MODE,
                event.getId()));
    }

    @Subscribe
    public void onDeleteTranslationEvent(DeleteTranslationEvent event){
        model.deleteTranslation(event.getId());
        getTranslations();
    }

    @Subscribe
    public void onQueryTextChangeEvent(OnQueryTextChangeEvent event){
        model.getTranslations(event.getText());
    }

    @Subscribe
    public void onAttachSearchViewEvent(OnAttachSearchViewEvent event){
        searching = event.isAttached();
        if(!searching) {
            view.showFab();
            getTranslations();
        }
        else view.hideFab();
    }

    public void shutDownSpeech() {
        view.shutDownSpeech();
    }

    public void onResume() {
        getTranslations();
    }
}
