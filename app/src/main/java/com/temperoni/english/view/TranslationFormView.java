package com.temperoni.english.view;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.temperoni.english.R;
import com.temperoni.english.database.model.Meaning;
import com.temperoni.english.database.model.Translation;
import com.temperoni.english.ui.adapters.MeaningsFormAdapter;
import com.temperoni.english.ui.decorations.VerticalSpaceItemDecoration;
import com.temperoni.english.ui.dialogs.AddMeaningDialog;
import com.temperoni.english.utils.ToolbarUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leandro.temperoni on 3/16/2016.
 */
public class TranslationFormView extends ActivityView implements OnMenuItemClickListener {

    private Bus bus;
    private MeaningsFormAdapter mAdapter;

    @Bind(R.id.input_word) protected EditText wordEditText;
    @Bind(R.id.input_layout_word) protected TextInputLayout wordTtextInputLayout;
    @Bind(R.id.typeSpinner) protected Spinner typeSpinner;
    @Bind(R.id.meaningsView) protected RecyclerView meanings;
    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.appbar) protected AppBarLayout appBar;

    public TranslationFormView(Activity activity, Bus bus){
        super(activity);
        this.bus = bus;
        ButterKnife.bind(this, getActivity());
        setUpViews();
    }

    public void setUpViews() {

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        appBar.setPadding(0, ToolbarUtils.getStatusBarHeight(getActivity()), 0, 0);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(this);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, getActivity().getResources().getTextArray(R.array.types));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        meanings.setLayoutManager(mLayoutManager);
        meanings.setItemAnimator(new DefaultItemAnimator());
        meanings.setHasFixedSize(true);
        mAdapter = new MeaningsFormAdapter(activity, bus);
        meanings.addItemDecoration(new VerticalSpaceItemDecoration(20));
        meanings.setAdapter(mAdapter);
    }

    @OnClick(R.id.addMeaning)
    protected void onAddMeaningButtonClick(){
        bus.post(new AddMeaningEvent());
    }

    protected void onSaveTranslationButtonClick(){

        if(!validateWord())
            return;

        if(!validateMeanings()) {
            Toast.makeText(getActivity(), "No has agregado ningún significado todavía",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String word = wordEditText.getText().toString();
        String type = typeSpinner.getSelectedItem().toString();

        bus.post(new AddTranslationEvent(word, type));
    }

    private boolean validateWord() {
        if (wordEditText.getText().toString().trim().isEmpty()) {
            wordTtextInputLayout.setError("Este campo es obligatorio");
            return false;
        } else {
            wordTtextInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMeanings() {
        return meanings.getAdapter().getItemCount() > 0;
    }

    public void showDialog(){
        showDialog(null, null);
    }

    public void showDialog(String definition, String example){
        AddMeaningDialog dialog = new AddMeaningDialog();
        dialog.setBus(bus);
        if(definition != null && example != null) {
            dialog.setData(definition, example);
        }
        dialog.show(getActivity().getFragmentManager(), "MeaningDialog");
    }

    public void updateMeanings(List<Meaning> meanings) {
        mAdapter.setMeanings(meanings);
        mAdapter.notifyDataSetChanged();
    }

    public void showAddedTranslationSuccessMessage() {
        Toast.makeText(getActivity(), "Traducción añadida", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    public void showEditedTranslationSuccessMessage() {
        Toast.makeText(getActivity(), "Traducción editada", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    public void showErrorMessage(String error) {
        Snackbar.make(meanings, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if(item.getItemId() == R.id.action_done || item.getItemId() == R.id.action_save){
            onSaveTranslationButtonClick();
            return true;
        }

        if(item.getItemId() == R.id.action_discard){
            getActivity().finish();
            return true;
        }

        return false;
    }

    public void updateView(Translation translation) {
        wordEditText.setText(translation.getEntry());

        String[] types = getActivity().getResources().getStringArray(R.array.types);
        int index = 0;
        for (int i = 0; i < types.length; i++) {
            if(types[i].equals(translation.getType()))
                index = i;
        }
        typeSpinner.setSelection(index, true);

        updateMeanings(translation.getMeanings());
    }

    public static class AddMeaningEvent {

        public AddMeaningEvent(){}
    }

    public static class AddTranslationEvent {

        private String word;
        private String type;

        public AddTranslationEvent(String word, String type){
            this.word = word;
            this.type = type;
        }

        public String getWord() {
            return word;
        }

        public String getType() {
            return type;
        }
    }
}
