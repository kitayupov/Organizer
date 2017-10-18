package com.vudn.kit.organizer.note;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.kit.organizer.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Note> arrayList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.textView);
        }
    }

    public RecyclerAdapter(ArrayList<Note> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Note note = arrayList.get(position);
        holder.mTextView.setText(note.getBody());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}