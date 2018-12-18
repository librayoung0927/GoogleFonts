package com.mvp.fonts.comparator;

import com.mvp.fonts.dataModel.Font;

import java.util.Comparator;

public class VersionComparator <RecyclerBaseItem>implements Comparator<Font> {
    @Override
    public int compare(Font font, Font t1) {
        return font.getVersion().compareTo(t1.getVersion());
    }
}
