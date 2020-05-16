package com.pratapkumar.bakingapplication.utilities;

import android.content.Context;
import android.util.TypedValue;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.pratapkumar.bakingapplication.utilities.Constant.GRID_COLUMN_WIDTH_DEFAULT;
import static com.pratapkumar.bakingapplication.utilities.Constant.GRID_SPAN_COUNT;

public class GridAutofitLayoutManager extends GridLayoutManager {
    private int mColumnWidth;
    private boolean mColumnWidthChanged = true;

    public GridAutofitLayoutManager(Context context, int columnWidth) {
        super(context, GRID_SPAN_COUNT);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    public GridAutofitLayoutManager(Context context, int columnWidth,
                                    int orientation, boolean reverseLayout) {
        super(context, GRID_SPAN_COUNT, orientation, reverseLayout);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    private int checkedColumnWidth(Context context, int columnWidth) {
        if (columnWidth <= 0) {
                       columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    GRID_COLUMN_WIDTH_DEFAULT, context.getResources().getDisplayMetrics());
        }
        return columnWidth;
    }

    public void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth;
            mColumnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mColumnWidthChanged && mColumnWidth > 0) {
            int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / mColumnWidth);
            setSpanCount(spanCount);
            mColumnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}
