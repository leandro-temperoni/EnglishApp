package com.temperoni.english.database;

import android.content.Context;

import com.temperoni.english.database.model.Translation;
import com.temperoni.english.database.model.Type.TranslationType;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DataBase {

    private static final String DATABASE_NAME = "com.temperoni.english.db";
    private static final int DATABASE_VERSION = 1;

    private Realm mRealm;

    public DataBase(Context context) {

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(context)
                .schemaVersion(DataBase.DATABASE_VERSION)
                .name(DataBase.DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build());

        mRealm = Realm.getDefaultInstance();
    }

    public void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    public RealmResults<Translation> getTranslations(TranslationType type, String filter) {

        RealmQuery<Translation> query = null;

        switch (type) {

            case EXPRESSIONS: {
                query = mRealm.where(Translation.class)
                        .equalTo("type", "expression");
                break;
            }

            case PHRASAL_VERBS: {
                query = mRealm.where(Translation.class)
                        .equalTo("type", "phrasal verb");
                break;
            }

            case VOCABULARY: {

                query = mRealm.where(Translation.class)
                        .beginGroup()
                        .equalTo("type", "noun")
                        .or()
                        .equalTo("type", "verb")
                        .or()
                        .equalTo("type", "adverb")
                        .or()
                        .equalTo("type", "adjective")
                        .endGroup();
                break;
            }
        }

        if (filter != null && !filter.isEmpty()) {
            query = query.beginsWith("entry", filter);
        }

        RealmResults<Translation> translations = query.findAll().sort("entry");

        return translations;
    }

    public void addTranslation(Translation translation) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(translation);
        mRealm.commitTransaction();
    }

    public void deleteTranslation(int id) {
        mRealm.beginTransaction();
        Translation translation = mRealm.where(Translation.class).equalTo("id", id)
                .findAll().first();
        translation.deleteFromRealm();
        mRealm.commitTransaction();
    }

    public Translation getTranslation(int id) {
        RealmQuery<Translation> query = mRealm.where(Translation.class)
                .equalTo("id", id);
        return query.findAll().first();
    }

    public int getNextKeyForTranslation() {
        try {
            return mRealm.where(Translation.class).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return 1;
        } catch (NullPointerException ex) {
            return 1;
        }
    }
}
