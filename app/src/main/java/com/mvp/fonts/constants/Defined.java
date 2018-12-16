package com.mvp.fonts.constants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public class Defined {
    public static final int VOLLEY_MULTI_TIMEOUT = 30000;
    public static final int VOLLEY_TIMEOUT = 20000;
    public static final String API_KEY = "AIzaSyAyZaIFq-Psm_aqZrVJbFLAiSaulztu3YQ";


    @StringDef({CATEGORY, FAMILY, KIND, VERSION, LAST_MODIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SORT {
    }

    public static final String CATEGORY = "category";
    public static final String FAMILY = "family";
    public static final String KIND = "kind";
    public static final String VERSION = "version";
    public static final String LAST_MODIFIED = "last_modified";

}
