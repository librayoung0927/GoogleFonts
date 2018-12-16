package com.mvp.fonts.module.apiParams;

import com.android.volley.Request;

import java.util.Map;

public class InParamsAllFonts extends InParamsBase {

    public InParamsAllFonts() {
        super("", null);
        setMethod(Request.Method.GET);
    }

    @Override
    public Map<String, String> renderMap() throws Exception {

        return null;
    }
}
