package com.temperoni.english.database.model;

import io.realm.RealmObject;

/**
 * Created by COCO on 06/03/2016.
 */
public class Meaning extends RealmObject implements Cloneable {

    private String definition;
    private String example;

    public Meaning(){}

    public Meaning(String definition, String example){
        this.definition = definition;
        this.example = example;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Meaning copy(){

        Meaning meaning = new Meaning();
        meaning.setDefinition(definition);
        meaning.setExample(example);

        return meaning;
    }
}
