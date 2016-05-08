package com.temperoni.english.model;

import com.squareup.otto.Bus;
import com.temperoni.english.EnglishApp;
import com.temperoni.english.database.model.Translation;
import com.temperoni.english.database.model.Type.TranslationType;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class TranslationsModel {

    private Bus bus;
    private TranslationType type;

    public TranslationsModel(Bus bus, TranslationType type) {
        this.bus = bus;
        this.type = type;
    }

    public void getTranslations() {
        getTranslations(null);
    }

    public void getTranslations(String text) {
        RealmResults<Translation> translations = EnglishApp.getDataBase()
                .getTranslations(type, text);
        bus.post(new ResponseDoneEvent(translations));
    }

    public void deleteTranslation(int id){
        EnglishApp.getDataBase().deleteTranslation(id);
    }

    public static class ResponseDoneEvent {

        private List<Translation> translations;

        public ResponseDoneEvent(RealmResults<Translation> translations) {
            this.translations = translations;
        }

        public List<Translation> getTranslations() {
            return translations;
        }
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
