package com.temperoni.english.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.otto.Bus;
import com.temperoni.english.R;
import com.temperoni.english.database.model.Translation;
import com.temperoni.english.utils.text_to_speech.TextToSpeechHelper;
import com.temperoni.english.utils.text_to_speech.TextToSpeechListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by COCO on 10/10/2015.
 */
public class TranslationsAdapter extends RecyclerView.Adapter<TranslationsAdapter.ViewHolder>
        implements TextToSpeechListener {

    private Context context;
    private Bus bus;
    private List<Translation> translations;
    private TextToSpeechHelper textToSpeechHelper;
    private int speakingItem;
    private int expandedItem;

    public TranslationsAdapter(Context context, Bus bus, List<Translation> translations){

        this.context = context;
        this.translations = translations;
        this.bus = bus;
        speakingItem = -1;
        expandedItem = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item,
                parent, false);

        ViewHolder holder = new ViewHolder(view, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onContainerClick(int position) {
                expandView(position);
            }

            @Override
            public void onVolumeClick(int position) {
                callTextToSpeach(position);
            }

            @Override
            public void onOptionsClick(View caller, int position) {
                createPopUpMenu(caller, position);
            }
        }, context);

        return holder;

    }

    private void expandView(int position){

        boolean expanded = (position == expandedItem);

        int oldExpanded = -1;

        if(expanded){
            expandedItem = -1;
        }
        else {
            oldExpanded = expandedItem;
            expandedItem = position;
        }

        if(oldExpanded != -1)
            notifyItemChanged(oldExpanded);
        notifyItemChanged(position);
    }

    private void callTextToSpeach(int position){

        String text = translations.get(position).getEntry();

        if(textToSpeechHelper == null) {
            textToSpeechHelper = new TextToSpeechHelper(context, this);
            updateViewBeforePlaying(text, position);
        }
        else {
            if(!textToSpeechHelper.isSpeaking()) {
                updateViewBeforePlaying(text, position);
                textToSpeechHelper.speak();
            }
        }
    }

    private void updateViewBeforePlaying(String text, int position){

        textToSpeechHelper.setText(text);
        speakingItem = position;
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        boolean expanded = (position == expandedItem);
        boolean speaking = (position == speakingItem);

        Translation translation = translations.get(position);

        holder.entry.setText(translation.getEntry());
        holder.type.setText(translation.getType());

        if(expanded){
            holder.mAdapter.setMeanings(translation.getMeanings());
            holder.mAdapter.notifyDataSetChanged();
            holder.meanings.setVisibility(View.VISIBLE);
        }
        else {
            holder.meanings.setVisibility(View.GONE);
        }

        if(speaking){
            holder.volumeProgressBar.setVisibility(View.VISIBLE);
            holder.volume.setVisibility(View.GONE);
        }
        else {
            holder.volumeProgressBar.setVisibility(View.GONE);
            holder.volume.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return translations.size();
    }

    public void setTranslations(ArrayList<Translation> translations){ this.translations = translations; }

    @Override
    public void onInitSucceeded() {
        textToSpeechHelper.speak();
    }

    @Override
    public void onInitFailed() {
        Toast.makeText(context, "Ocurrió un problema, inténtalo más tarde",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted() {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateViewAfterOnComplete(speakingItem);
            }
        });
    }

    public void updateViewAfterOnComplete(int position){
        if(speakingItem != -1) {
            speakingItem = -1;
            notifyItemChanged(position);
        }
    }

    private void createPopUpMenu(View view, final int position){

        final PopupMenu popupMenu = new PopupMenu(context, view);

        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_translation_item,
                popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.translation_edit) {
                    bus.post(new EditTranslationEvent(translations.get(position).getId()));
                    return false;
                }

                if (item.getItemId() == R.id.translation_delete) {
                    bus.post(new DeleteTranslationEvent(translations.get(position).getId()));
                    return false;
                }

                return true;
            }
        });

        popupMenu.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.entry) TextView entry;
        @Bind(R.id.type) TextView type;
        @Bind(R.id.card_view) CardView container;
        @Bind(R.id.volume) ImageView volume;
        @Bind(R.id.volumeProgressBar) CircularProgressView volumeProgressBar;
        @Bind(R.id.moreOptions) ImageView moreOptions;
        @Bind(R.id.meanings) RecyclerView meanings;

        MeaningsAdapter mAdapter;

        public IMyViewHolderClicks mListener;

        public interface IMyViewHolderClicks {
            void onContainerClick(int position);
            void onVolumeClick(int position);
            void onOptionsClick(View caller, int position);
        }

        public ViewHolder(View v, final IMyViewHolderClicks listener, Context context) {

            super(v);

            ButterKnife.bind(this, v);

            mListener = listener;

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            meanings.setLayoutManager(mLayoutManager);
            meanings.setItemAnimator(new DefaultItemAnimator());
            meanings.setHasFixedSize(true);
            mAdapter = new MeaningsAdapter(context);
            meanings.setNestedScrollingEnabled(false);
            meanings.setAdapter(mAdapter);
        }

        @OnClick(R.id.card_view)
        protected void onContainerClick(){
            mListener.onContainerClick(getAdapterPosition());
        }

        @OnClick(R.id.volume)
        protected void onVolumeClick(){
            mListener.onVolumeClick(getAdapterPosition());
        }

        @OnClick(R.id.moreOptions)
        protected void onOptionsClick(){
            mListener.onOptionsClick(moreOptions, getAdapterPosition());
        }
    }

    public TextToSpeechHelper getTextToSpeechHelper(){ return textToSpeechHelper; }

    public class EditTranslationEvent {

        private int id;

        public EditTranslationEvent(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public class DeleteTranslationEvent {

        private int id;

        public DeleteTranslationEvent(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
