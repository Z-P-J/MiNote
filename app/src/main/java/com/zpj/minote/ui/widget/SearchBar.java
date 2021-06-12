package com.zpj.minote.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.zpj.widget.toolbar.ZSearchBar;

public class SearchBar extends ZSearchBar {
    public SearchBar(Context context) {
        super(context);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        if (editable) {
            getEditor().setOnClickListener(null);
            getEditor().setFocusable(true);
            getEditor().setCursorVisible(true);
            getEditor().setFocusableInTouchMode(true);
            getEditor().requestFocus();
            getEditor().invalidate();
        }
    }
}
