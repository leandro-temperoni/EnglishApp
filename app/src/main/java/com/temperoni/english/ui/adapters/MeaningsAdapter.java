package com.temperoni.english.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.temperoni.english.R;
import com.temperoni.english.database.model.Meaning;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by COCO on 10/10/2015.
 */
public class MeaningsAdapter extends RecyclerView.Adapter<MeaningsAdapter.ViewHolder> {

    private Context context;
    private List<Meaning> meanings;

    public MeaningsAdapter(Context context){

        this.context = context;

    }

    public MeaningsAdapter(Context context, ArrayList<Meaning> meanings){

        this.context = context;
        this.meanings = meanings;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meaning_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Meaning meaning = meanings.get(position);

        holder.number.setText(String.valueOf(position + 1));
        holder.meaning.setText(meaning.getDefinition());
        holder.example.setText(context.getString(R.string.example, meaning.getExample()));
    }

    @Override
    public int getItemCount() {
        return meanings.size();
    }

    public void setMeanings(List<Meaning> meanings){ this.meanings = meanings; }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.number) TextView number;
        @Bind(R.id.meaning) TextView meaning;
        @Bind(R.id.example) TextView example;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
