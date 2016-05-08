package com.temperoni.english.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Bus;
import com.temperoni.english.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by COCO on 20/03/2016.
 */
public class AddMeaningDialog extends DialogFragment {

    private Bus bus;
    @Bind(R.id.input_meaning) protected EditText meaningEditText;
    @Bind(R.id.input_layout_meaning) protected TextInputLayout meaningTextInputLayout;
    @Bind(R.id.input_example) protected EditText exampleEditText;
    @Bind(R.id.input_layout_example) protected TextInputLayout exampleTextInputLayout;

    private boolean editing = false;
    private String definition;
    private String example;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.add_meaning_dialog, null);

        ButterKnife.bind(this, dialogView);

        builder.setView(dialogView)
        .setTitle("Agregar significado")
        .setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
        .setPositiveButton("Aceptar", null)
        .setCancelable(false);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) getDialog())
                        .getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onAcceptButtonClick()){
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        meaningEditText.setText(definition);
        exampleEditText.setText(example);

        return dialog;
    }

    private boolean onAcceptButtonClick(){

        if(!validateMeaning())
            return false;

        if(!validateExample())
            return false;

        String meaning = meaningEditText.getText().toString();
        String example = exampleEditText.getText().toString();

        if(editing){
            editing = false;
            bus.post(new MeaningEditedEvent(meaning, example));
        }
        else {
            bus.post(new MeaningAddedEvent(meaning, example));
        }

        return true;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public void setData(String definition, String example){
        editing = true;
        this.definition = definition;
        this.example = example;
    }

    private boolean validateMeaning() {
        if (meaningEditText.getText().toString().trim().isEmpty()) {
            meaningTextInputLayout.setError("Este campo es obligatorio");
            return false;
        } else {
            meaningTextInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateExample() {
        if (exampleEditText.getText().toString().trim().isEmpty()) {
            exampleTextInputLayout.setError("Este campo es obligatorio");
            return false;
        } else {
            exampleTextInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public static class MeaningAddedEvent{

        private String meaning;
        private String example;

        public MeaningAddedEvent(String meaning, String example){
            this.meaning = meaning;
            this.example = example;
        }

        public String getMeaning() {
            return meaning;
        }

        public String getExample() {
            return example;
        }
    }

    public static class MeaningEditedEvent{

        private String meaning;
        private String example;

        public MeaningEditedEvent(String meaning, String example){
            this.meaning = meaning;
            this.example = example;
        }

        public String getMeaning() {
            return meaning;
        }

        public String getExample() {
            return example;
        }
    }
}
