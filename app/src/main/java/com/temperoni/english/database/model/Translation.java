package com.temperoni.english.database.model;

import com.temperoni.english.EnglishApp;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by COCO on 06/03/2016.
 */
public class Translation extends RealmObject {

    @PrimaryKey
    private int id;
    private String entry;
    private String type;
    private RealmList<Meaning> meanings;

    public Translation(){
        meanings = new RealmList<>();
        id = EnglishApp.getDataBase().getNextKeyForTranslation();
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = (RealmList<Meaning>) meanings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Translation copy(){

        Translation translation = new Translation();
        translation.setId(id);
        translation.setEntry(entry);
        translation.setType(type);

        for (int i = 0; i < meanings.size(); i++) {
            translation.getMeanings().add(meanings.get(i).copy());
        }

        return translation;
    }
}
