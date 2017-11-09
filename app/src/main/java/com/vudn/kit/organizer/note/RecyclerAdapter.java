package com.vudn.kit.organizer.note;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.util.DateUtil;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final ArrayList<Note> arrayList;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray expandedItems;

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;
    private OnCompletedStateChangeListener stateChangeListener;
    private OnEditButtonClickListener editButtonClickListener;

    public RecyclerAdapter() {
        this.arrayList = new ArrayList<>();
        selectedItems = new SparseBooleanArray();
        expandedItems = new SparseBooleanArray();
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

    public void setOnEditButtonClickListener(OnEditButtonClickListener editButtonClickListener) {
        this.editButtonClickListener = editButtonClickListener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View inflate = layoutInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Note item = arrayList.get(position);
        setViewHolderContent(holder, item);
        setCompletedState(holder, item.isCompleted());
        holder.cardView.setOnClickListener(clickListener);
        holder.cardView.setOnLongClickListener(longClickListener);
        holder.mainLayout.setActivated(selectedItems.get(position));
        holder.completedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateChangeListener != null) {
                    stateChangeListener.onCompletedStateChanged(holder.getAdapterPosition());
                }
            }
        });
        holder.expandedContent.setVisibility(expandedItems.get(position) ? View.VISIBLE : View.GONE);
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClicked(holder.getAdapterPosition());
                }
            }
        });
    }

    private void setViewHolderContent(ViewHolder holder, Note item) {
        holder.nameTextView.setText(item.getName());
        holder.bodyTextView.setText(item.getBody());
        setDateTarget(holder, item.getDateTarget());
    }

    private void setDateTarget(ViewHolder holder, long dateTarget) {
        if (dateTarget != -1) {
            holder.dateTargetTextView.setText(DateUtil.getDateString(dateTarget));
        } else {
            holder.dateTargetTextView.setVisibility(View.GONE);
        }
    }

    private void setCompletedState(ViewHolder holder, boolean completed) {
        holder.nameTextView.setTextColor(getCompletedStateColor(completed));
        holder.bodyTextView.setTextColor(getCompletedStateColor(completed));
        holder.dateTargetTextView.setTextColor(getCompletedStateColor(completed));
        holder.completedCheckBox.setChecked(completed);
    }

    private int getCompletedStateColor(boolean completed) {
        return completed ? Color.LTGRAY : Color.DKGRAY;
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

    public void toggleExpanded(int position) {
        if (expandedItems.get(position, false)) {
            expandedItems.delete(position);
        } else {
            expandedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mainLayout;
        private final CardView cardView;
        private final TextView nameTextView;
        private final TextView bodyTextView;
        private final TextView dateTargetTextView;
        private final CompoundButton completedCheckBox;
        private final View expandedContent;
        private final View editImageView;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            mainLayout = view.findViewById(R.id.cardContent);
            nameTextView = view.findViewById(R.id.nameTextView);
            bodyTextView = view.findViewById(R.id.bodyTextView);
            dateTargetTextView = view.findViewById(R.id.dateTargetTextView);
            completedCheckBox = view.findViewById(R.id.completedCheckbox);
            expandedContent = view.findViewById(R.id.expandedContentLayout);
            editImageView = view.findViewById(R.id.editImageView);
        }
    }

    public interface OnCompletedStateChangeListener {
        void onCompletedStateChanged(int position);
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClicked(int position);
    }
}