package com.vudn.kit.organizer.note;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vudn.kit.organizer.R;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {

    private ArrayList<Note> arrayList;

    public NoteAdapter(ArrayList<Note> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Note getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null);
        }
        return noteView(convertView, position);
    }

    private View noteView(View view, int position) {
        final Note item = getItem(position);
        final TextView textView = view.findViewById(R.id.textView);
        textView.setText(item.getBody());
        textView.setTextColor(item.isCompleted() ? Color.LTGRAY : Color.DKGRAY);
        return view;
    }
}
