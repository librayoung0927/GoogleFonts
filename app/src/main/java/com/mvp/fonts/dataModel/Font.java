package com.mvp.fonts.dataModel;

import com.google.gson.annotations.SerializedName;
import com.mvp.fonts.adapter.FontRecyclerAdapter;

import java.util.HashMap;
import java.util.List;

public class Font extends RecyclerBaseItem {

    public Font() {
        super(FontRecyclerAdapter.LAYOUT_TYPE_ITEM);
    }

    @SerializedName("kind")
    private String mKind;

    @SerializedName("family")
    private String mFamily;

    @SerializedName("category")
    private String mCategory;

    @SerializedName("subsets")
    private List<String> mSubsets;

    @SerializedName("version")
    private String mVersion;

    @SerializedName("lastModified")
    private String mLastModified;

    @SerializedName("variants")
    private List<String> mVariants;// variants = key 對應到 file 的key value

    private HashMap<String, String> mFiles;


    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public String getFamily() {
        return mFamily;
    }

    public void setFamily(String family) {
        mFamily = family;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public List<String> getSubsets() {
        return mSubsets;
    }

    public void setSubsets(List<String> subsets) {
        mSubsets = subsets;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public String getLastModified() {
        return mLastModified;
    }

    public void setLastModified(String lastModified) {
        mLastModified = lastModified;
    }

    public List<String> getVariants() {
        return mVariants;
    }

    public void setVariants(List<String> variants) {
        mVariants = variants;
    }

    public HashMap<String, String> getFiles() {
        return mFiles;
    }

    public void setFiles(HashMap<String, String> files) {
        mFiles = files;
    }


}
