package com.temperoni.english.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.temperoni.english.R;
import com.temperoni.english.database.model.Meaning;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by COCO on 10/10/2015.
 */
public class MeaningsFormAdapter extends RecyclerView.Adapter<MeaningsFormAdapter.ViewHolder> {

    private Context context;
    private Bus bus;
    private List<Meaning> meanings;

    public MeaningsFormAdapter(Context context, Bus bus){
        this.context = context;
        this.bus = bus;
        meanings = new ArrayList<>();
    }

    public MeaningsFormAdapter(Context context, ArrayList<Meaning> meanings){
        this.context = context;
        this.meanings = meanings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning_item_form, parent, false);

        return new ViewHolder(view, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onContainerClick(View caller, int position) {
                bus.post(new EditMeaningEvent(meanings.get(position).getDefinition(),
                        meanings.get(position).getExample(), position));
            }

            @Override
            public void onDeleteMeaningClick(View caller, int position) {
                bus.post(new DeleteMeaningEvent(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Meaning meaning = meanings.get(position);

        holder.meaning.setText(meaning.getDefinition());
        holder.example.setText(context.getString(R.string.example, meaning.getExample()));
    }

    @Override
    public int getItemCount() {
        return meanings.size();
    }

    public void setMeanings(List<Meaning> meanings){
        this.meanings = meanings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.meaning) TextView meaning;
        @Bind(R.id.example) TextView example;
        @Bind(R.id.deleteMeaning) ImageView deleteMeaning;
        @Bind(R.id.card_view) CardView container;

        public IMyViewHolderClicks mListener;

        public interface IMyViewHolderClicks {
            void onContainerClick(View caller, int position);
            void onDeleteMeaningClick(View caller, int position);
        }

        public ViewHolder(View v, IMyViewHolderClicks mListener) {
            super(v);

            this.mListener = mListener;

            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.deleteMeaning)
        protected void onDeleteMeaningClick(){
            mListener.onDeleteMeaningClick(deleteMeaning, getAdapterPosition());
        }

        @OnClick(R.id.card_view)
        protected void onContainerClick(){
            mListener.onContainerClick(container, getAdapterPosition());
        }
    }

    public static class DeleteMeaningEvent{

        private int position;

        public DeleteMeaningEvent(int position){
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public static class EditMeaningEvent {

        private String definition;
        private String example;
        private int position;

        public EditMeaningEvent(String definition, String example, int position) {
            this.definition = definition;
            this.example = example;
            this.position = position;
        }

        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }

        public int getPosition() {
            return position;
        }
    }
}
