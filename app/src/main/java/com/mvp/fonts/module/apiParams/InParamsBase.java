package com.mvp.fonts.module.apiParams;

import com.android.volley.Request;
import com.mvp.fonts.module.volley.VolleyMultipartRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InParamsBase {
    private String mSubPath;
    protected String mOs = "android";
    protected String mToken;
    private int mMethod = Request.Method.POST;


    List<VolleyMultipartRequest.DataPart> dataPartList;

    public InParamsBase(String group, String name, String token) {
        this.mSubPath = group + "/" + name;
        this.mToken = token;
    }

    public InParamsBase(String subPath, String token) {
        this.mSubPath = subPath;
        this.mToken = token;
    }

    public int getMethod() {
        return mMethod;
    }

    public void setMethod(int method) {
        this.mMethod = method;
    }

    public void setSubPath(String subPath) {
        this.mSubPath = subPath;
    }

    public String getSubPath() {
        return mSubPath;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getOs() {
        return mOs;
    }

    public void setOs(String os) {
        this.mOs = os;
    }

    //-------------------------------------------------------------------
    public List<VolleyMultipartRequest.DataPart> getDataPartList() {
        return dataPartList;
    }

    public void setDataPartList(List<VolleyMultipartRequest.DataPart> dataPartList) {
        this.dataPartList = dataPartList;
    }

    public abstract Map<String, String> renderMap() throws Exception;

    private boolean hasByteData() {
        return dataPartList != null && dataPartList.size() > 0;
    }

    //public abstract Map<String, VolleyMultipartRequest.DataPart> renderDataPartMap() throws Exception;
    public Map<String, VolleyMultipartRequest.DataPart> renderDataPartMap() throws Exception {
        Map<String, VolleyMultipartRequest.DataPart> ret = null;
        if (hasByteData()) {
            ret = new HashMap<String, VolleyMultipartRequest.DataPart>();
            for (VolleyMultipartRequest.DataPart item : dataPartList)
                ret.put(item.getKey(), item);
        }
        return ret;
    }

    public void addDataPart(VolleyMultipartRequest.DataPart dataPart) {
        if (dataPartList == null) dataPartList = new ArrayList<VolleyMultipartRequest.DataPart>();
        dataPartList.add(dataPart);
    }

    public void removeDataPart(VolleyMultipartRequest.DataPart dataPart) {
        if (dataPartList != null) {
            dataPartList.remove(dataPart);
        }
    }
}
