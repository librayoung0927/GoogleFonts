package com.mvp.fonts.comparator;

import com.mvp.fonts.dataModel.Font;

import java.util.Comparator;

public class FamilyComparator<RecyclerBaseItem> implements Comparator<Font> {

    @Override
    public int compare(Font font, Font t1) {
        return font.getFamily().compareTo(t1.getFamily());
    }
}
