package com.mvp.fonts.dataModel;

import java.io.Serializable;

public class RecyclerBaseItem implements Serializable {
    private int mLayoutType;
    private boolean mClickable = true;
    private String mTitle;
    private Object mTag;

    public RecyclerBaseItem(int layoutType) {
        this.mLayoutType = layoutType;
    }

    public int getLayoutType() {
        return mLayoutType;
    }

    public void setLayoutType(int layoutType) {
        this.mLayoutType = layoutType;
    }

    public boolean isClickable() {
        return mClickable;
    }

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }
}