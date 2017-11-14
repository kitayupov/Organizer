package com.vudn.kit.organizer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vudn.kit.organizer.R;
import com.vudn.kit.organizer.note.Note;
import com.vudn.kit.organizer.util.DateTimeUtil;

public class DateTimeTextView extends LinearLayout {

    public static final int dateTextViewId = R.id.dateTargetTextView;
    public static final int timeTextViewId = R.id.timeTargetTextView;

    private static final String TAG = DateTimeTextView.class.getSimpleName();

    private TextView dateTextView;
    private TextView timeTextView;

    public DateTimeTextView(Context context) {
        super(context);
        init(context);
    }

    public DateTimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateTimeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.date_time_layout, this);
        dateTextView = view.findViewById(R.id.dateTargetTextView);
        timeTextView = view.findViewById(R.id.timeTargetTextView);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        dateTextView.setOnClickListener(l);
        timeTextView.setOnClickListener(l);
    }

    public void setTarget(Note.TimeTarget target, long date) {
        if (date != Note.DEFAULT_DATE_TARGET) {
            dateTextView.setText(DateTimeUtil.getDateTextString(date));
            switch (target) {
                case NONE:
                    timeTextView.setText(null);
                    break;
                case SINGLE:
                    timeTextView.setText(DateTimeUtil.getTimeTextString(date));
                    break;
                default:
                    Log.e(TAG, "Not implemented yet: " + target);
            }
        }
    }

    public void setTextColor(int textColor) {
        dateTextView.setTextColor(textColor);
        timeTextView.setTextColor(textColor);
    }
}
