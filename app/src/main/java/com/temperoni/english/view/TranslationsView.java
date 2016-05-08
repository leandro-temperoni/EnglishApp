package com.temperoni.english.view;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.widget.LinearLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.otto.Bus;
import com.temperoni.english.R;
import com.temperoni.english.database.model.Translation;
import com.temperoni.english.ui.adapters.TranslationsAdapter;
import com.temperoni.english.ui.decorations.VerticalSpaceItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.v7.widget.SearchView.OnQueryTextListener;

/**
 * Created by leandro.temperoni on 3/15/2016.
 */
public class TranslationsView extends FragmentView implements OnQueryTextListener,
        OnAttachStateChangeListener {

    @Bind(R.id.cardsView) RecyclerView recyclerView;
    @Bind(R.id.no_translations) LinearLayout noTranslations;
    @Bind(R.id.progressBar) CircularProgressView progressBar;

    private Toolbar toolbar;
    private SearchView searchView;

    private TranslationsAdapter mAdapter;
    private Bus bus;

    public TranslationsView(Fragment fragment, Bus bus) {
        super(fragment);
        this.bus = bus;
        ButterKnife.bind(this, fragment.getView());
        setUpViews(fragment);
    }

    public void setUpViews(Fragment fragment) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(30));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
    }

    public void setUpMenu(Fragment fragment){

        toolbar = (Toolbar) fragment.getActivity().findViewById(R.id.toolbar);

        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        searchView.addOnAttachStateChangeListener(this);
    }

    public void setTranslations(List<Translation> translations) {

        progressBar.setVisibility(View.GONE);

        if(translations.size() > 0) {
            mAdapter = new TranslationsAdapter(getActivity(), bus, translations);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            noTranslations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            noTranslations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void showErrorMessage(String error){
        progressBar.setVisibility(View.GONE);

        Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
        .setAction("RECARGAR", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bus.post(new ReloadDataEvent());
            }
        })
        .show();
    }

    public void shutDownSpeech() {
        if(mAdapter != null && mAdapter.getTextToSpeechHelper() != null) {
            mAdapter.getTextToSpeechHelper().shutdown();
        }
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void updateView() {
        mAdapter.notifyDataSetChanged();
        checkNoDataAfterUpdate();
    }

    private void checkNoDataAfterUpdate(){
        if(mAdapter.getItemCount() == 0){
            noTranslations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        bus.post(new OnQueryTextChangeEvent(query));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        bus.post(new OnQueryTextChangeEvent(newText));
        return false;
    }

    public void showFab() {
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).show();
    }

    public void hideFab() {
        ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
    }

    @Override
    public void onViewAttachedToWindow(View view) {
        bus.post(new OnAttachSearchViewEvent(true));
    }

    @Override
    public void onViewDetachedFromWindow(View view) {
        bus.post(new OnAttachSearchViewEvent(false));
    }

    public static class ReloadDataEvent {

        public ReloadDataEvent(){}
    }

    public static class OnQueryTextChangeEvent {

        private String text;

        public OnQueryTextChangeEvent(String text){
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public static class OnAttachSearchViewEvent {

        private boolean attached;

        public OnAttachSearchViewEvent(boolean attached){
            this.attached = attached;
        }

        public boolean isAttached() {
            return attached;
        }
    }
}
