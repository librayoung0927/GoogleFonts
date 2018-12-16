package com.mvp.fonts.module.apiParams;

import com.android.volley.Request;
import com.mvp.fonts.constants.Defined;

import java.util.Map;

public class InParamFontsBySort extends InParamsBase {


    public InParamFontsBySort(@Defined.SORT String sort) {
        super("sort=" + sort, null);
        setMethod(Request.Method.GET);

    }

    @Override
    public Map<String, String> renderMap() throws Exception {

        return null;
    }
}
