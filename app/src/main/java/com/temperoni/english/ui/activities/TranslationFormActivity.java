package com.temperoni.english.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.temperoni.english.R;
import com.temperoni.english.model.TranslationFormModel;
import com.temperoni.english.presenter.TranslationFormPresenter;
import com.temperoni.english.utils.AppBus;
import com.temperoni.english.view.TranslationFormView;


public class TranslationFormActivity extends AppCompatActivity {

    private TranslationFormPresenter presenter;
    private TranslationFormType type;

    public enum TranslationFormType{
        ADD_MODE, EDIT_MODE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_form);

        Bundle extras = getIntent().getExtras();
        TranslationFormType type = (TranslationFormType) extras.getSerializable("type");

        this.type = type;
        int id = extras.getInt("id");

        presenter = new TranslationFormPresenter(new TranslationFormModel(AppBus.getInstance()),
                new TranslationFormView(this, AppBus.getInstance()), type, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (type){
            case ADD_MODE: {
                getMenuInflater().inflate(R.menu.menu_translation_form, menu);
                break;
            }
            case EDIT_MODE: {
                getMenuInflater().inflate(R.menu.menu_translation_form_edit, menu);
                break;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppBus.getInstance().register(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppBus.getInstance().unregister(presenter);
    }
}
