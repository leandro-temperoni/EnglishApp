package com.temperoni.english.model;

import com.squareup.otto.Bus;
import com.temperoni.english.EnglishApp;
import com.temperoni.english.database.model.Meaning;
import com.temperoni.english.database.model.Translation;

import java.util.List;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class TranslationFormModel {

    private Bus bus;
    private Translation translation;

    private int meaningBeingEdited = -1;

    public TranslationFormModel(Bus bus) {
        this.bus = bus;
        translation = new Translation();
    }

    public void addMeaning(String definition, String example) {
        translation.getMeanings().add(new Meaning(definition, example));
    }

    public void deleteMeaning(int position) {
        translation.getMeanings().remove(position);
    }

    public void setTranslationData(String word, String type){
        translation.setEntry(word);
        translation.setType(type);
    }

    public List<Meaning> getMeanings() {
        return translation.getMeanings();
    }

    public void addTranslation() {
        EnglishApp.getDataBase().addTranslation(translation);
        bus.post(new ResponseDoneAddTranslationEvent());
    }

    public void getTranslationData(int id) {
        translation = EnglishApp.getDataBase().getTranslation(id).copy();
    }

    public Translation getTranslation(){
        return translation;
    }

    public void setMeaningToEdit(int position) {
        meaningBeingEdited = position;
    }

    public void editMeaning(String definition, String example) {
        if(meaningBeingEdited != -1) {
            translation.getMeanings().get(meaningBeingEdited).setDefinition(definition);
            translation.getMeanings().get(meaningBeingEdited).setExample(example);
            meaningBeingEdited = -1;
        }
    }

    public static class ResponseDoneAddTranslationEvent {

        public ResponseDoneAddTranslationEvent() {}
    }

    public static class ErrorEvent {

        private String error;

        public ErrorEvent(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
