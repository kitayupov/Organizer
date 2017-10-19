package com.vudn.kit.organizer.note;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vudn.kit.organizer.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final ArrayList<Note> arrayList;
    private SparseBooleanArray selectedItems;

    public RecyclerAdapter(ArrayList<Note> arrayList) {
        this.arrayList = arrayList;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View inflate = layoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTextView.setText(arrayList.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public Note getItem(int position) {
        return arrayList.get(position);
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.textView);
        }
    }
}