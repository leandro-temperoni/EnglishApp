package com.temperoni.english.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.temperoni.english.ui.activities.TranslationFormActivity;
import com.temperoni.english.ui.activities.TranslationFormActivity.TranslationFormType;

/**
 * Created by leandro.temperoni on 3/16/2016.
 */
public class IntentFactory {

    public static Intent openTranslationFormActivity(Context context, TranslationFormType type){
        return openTranslationFormActivity(context, type, -1);
    }

    public static Intent openTranslationFormActivity(Context context, TranslationFormType type,
                                                     int id){
        Intent intent = new Intent (context, TranslationFormActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("type", type);
        if(id != -1) {
            extras.putInt("id", id);
        }
        intent.putExtras(extras);
        return intent;
    }
}
