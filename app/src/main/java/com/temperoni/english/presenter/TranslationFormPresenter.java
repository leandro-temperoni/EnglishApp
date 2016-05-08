package com.temperoni.english.presenter;

import com.squareup.otto.Subscribe;
import com.temperoni.english.model.TranslationFormModel;
import com.temperoni.english.model.TranslationFormModel.ErrorEvent;
import com.temperoni.english.model.TranslationFormModel.ResponseDoneAddTranslationEvent;
import com.temperoni.english.ui.activities.TranslationFormActivity.TranslationFormType;
import com.temperoni.english.ui.adapters.MeaningsFormAdapter.DeleteMeaningEvent;
import com.temperoni.english.ui.adapters.MeaningsFormAdapter.EditMeaningEvent;
import com.temperoni.english.ui.dialogs.AddMeaningDialog.MeaningAddedEvent;
import com.temperoni.english.ui.dialogs.AddMeaningDialog.MeaningEditedEvent;
import com.temperoni.english.view.TranslationFormView;
import com.temperoni.english.view.TranslationFormView.AddMeaningEvent;
import com.temperoni.english.view.TranslationFormView.AddTranslationEvent;

import static com.temperoni.english.ui.activities.TranslationFormActivity.TranslationFormType.ADD_MODE;
import static com.temperoni.english.ui.activities.TranslationFormActivity.TranslationFormType.EDIT_MODE;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class TranslationFormPresenter {

    private TranslationFormModel model;
    private TranslationFormView view;

    private TranslationFormType type;
    private int id;

    public TranslationFormPresenter(TranslationFormModel model, TranslationFormView view,
                                    TranslationFormType type, int id){
        this.model = model;
        this.view = view;
        this.type = type;

        if(type == EDIT_MODE){
            this.id = id;
            getTranslation();
        }
    }

    private void getTranslation() {
        model.getTranslationData(id);
        view.updateView(model.getTranslation());
    }

    @Subscribe
    public void onResponseDoneAddTranslation(ResponseDoneAddTranslationEvent event){
        if(type == ADD_MODE) {
            view.showAddedTranslationSuccessMessage();
        }
        else if(type == EDIT_MODE){
            view.showEditedTranslationSuccessMessage();
        }
    }

    @Subscribe
    public void onAddMeaning(AddMeaningEvent event){
        view.showDialog();
    }

    @Subscribe
    public void onEditMeaning(EditMeaningEvent event){
        view.showDialog(event.getDefinition(), event.getExample());
        model.setMeaningToEdit(event.getPosition());
    }

    @Subscribe
    public void onMeaningAdded(MeaningAddedEvent event){
        model.addMeaning(event.getMeaning(), event.getExample());
        view.updateMeanings(model.getMeanings());
    }

    @Subscribe
    public void onMeaningEdited(MeaningEditedEvent event){
        model.editMeaning(event.getMeaning(), event.getExample());
        view.updateMeanings(model.getMeanings());
    }

    @Subscribe
    public void onDeleteMeaning(DeleteMeaningEvent event){
        model.deleteMeaning(event.getPosition());
        view.updateMeanings(model.getMeanings());
    }

    @Subscribe
    public void onAddTranslation(AddTranslationEvent event){
        model.setTranslationData(event.getWord(), event.getType());
        model.addTranslation();
    }

    @Subscribe
    public void onError(ErrorEvent event) {
        view.showErrorMessage(event.getError());
    }
}
