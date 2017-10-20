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

    public RecyclerAdapter(ArrayList<Note> arrayList) {
        this.arrayList = arrayList;
        selectedItems = new SparseBooleanArray();
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View inflate = layoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Note item = arrayList.get(position);
        holder.nameTextView.setText(item.getBody());
        holder.nameTextView.setTextColor(item.isCompleted() ? Color.LTGRAY : Color.DKGRAY);
        holder.cardView.setOnClickListener(clickListener);
        holder.cardView.setOnLongClickListener(longClickListener);
        holder.mainLayout.setActivated(selectedItems.get(position));
        holder.completedCheckBox.setChecked(item.isCompleted());
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

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public ArrayList<Integer> getSelectedItems() {
        final ArrayList<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
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
}