package com.mvp.fonts.Comparator;

import com.mvp.fonts.dataModel.Font;

import java.util.Comparator;

public class LastModifiedComparator<RecyclerBaseItem> implements Comparator<Font> {
    @Override
    public int compare(Font font, Font t1) {
        return font.getLastModified().compareTo(t1.getLastModified());
    }
}
