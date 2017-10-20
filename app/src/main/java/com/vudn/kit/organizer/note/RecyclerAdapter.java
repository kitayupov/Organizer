package com.vudn.kit.organizer.note;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vudn.kit.organizer.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final ArrayList<Note> arrayList;
    private SparseBooleanArray selectedItems;

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;
    private OnCompletedStateChangeListener stateChangeListener;

    public RecyclerAdapter() {
        this.arrayList = new ArrayList<>();
        selectedItems = new SparseBooleanArray();
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOnCompletedStateChangeListener(OnCompletedStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View inflate = layoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Note item = arrayList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.nameTextView.setTextColor(item.isCompleted() ? Color.LTGRAY : Color.DKGRAY);
        holder.cardView.setOnClickListener(clickListener);
        holder.cardView.setOnLongClickListener(longClickListener);
        holder.mainLayout.setActivated(selectedItems.get(position));
        holder.completedCheckBox.setChecked(item.isCompleted());
        holder.completedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateChangeListener != null) {
                    stateChangeListener.onCompletedStateChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public Note getItem(int position) {
        return arrayList.get(position);
    }

    public void toggleSelected(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemsCount() {
        return selectedItems.size();
    }

    public ArrayList<Integer> getSelectedPositions() {
        final ArrayList<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public ArrayList<Note> getSelectedItems() {
        final ArrayList<Note> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(arrayList.get(selectedItems.keyAt(i)));
        }
        return items;
    }

    public void addNote(Note note) {
        arrayList.add(note);
    }

    public void addNote(int position, Note note) {
        arrayList.remove(position);
        arrayList.add(position, note);
    }

    public void removeNote(Note note) {
        arrayList.remove(note);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View mainLayout;
        private CardView cardView;
        private TextView nameTextView;
        private CheckBox completedCheckBox;

        ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            cardView = view.findViewById(R.id.cardView);
            mainLayout = view.findViewById(R.id.cardContent);
            completedCheckBox = view.findViewById(R.id.completedCheckbox);
        }
    }

    public interface OnCompletedStateChangeListener {
        void onCompletedStateChanged(int position);
    }
}