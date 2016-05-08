package com.temperoni.english.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.temperoni.english.R;
import com.temperoni.english.presenter.MainPresenter;
import com.temperoni.english.utils.AppBus;
import com.temperoni.english.view.MainView;

public class MainActivity extends AppCompatActivity {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(new MainView(this, AppBus.getInstance(), savedInstanceState));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }
}
