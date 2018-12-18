package com.mvp.fonts.dataModel;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;
import com.mvp.fonts.adapter.FontRecyclerAdapter;

import java.util.HashMap;
import java.util.List;

public class Font extends RecyclerBaseItem {

    // for db use
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String URL = "url";
    public final static String PATH = "path";

    public final static String KIND = "kind";
    public final static String FAMILY = "family";
    public final static String CATEGORY = "category";
    public final static String VERSION = "version";
    public final static String LAST_MODIFIED = "lastModified";
    public final static String SUBSETS = "subsets";

    private int mID;
    private String mName;
    private String mUrl;
    private String mPath;
    private String mSubsetsString;


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

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    // for gson use
    public Font() {
        super(FontRecyclerAdapter.LAYOUT_TYPE_ITEM);
    }

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

    public String getSubsetsString() {
        return mSubsetsString;
    }

    public void setSubsetsString(String subsetsString) {
        mSubsetsString = subsetsString;
    }

    public void setFiles(HashMap<String, String> files) {
        mFiles = files;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(ID, mID);
        cv.put(NAME, mName);
        cv.put(URL, mUrl);
        cv.put(PATH, mPath);
        cv.put(KIND, mKind);
        cv.put(CATEGORY, mCategory);
        cv.put(FAMILY, mFamily);
        cv.put(VERSION, mVersion);
        cv.put(LAST_MODIFIED, mLastModified);
        cv.put(SUBSETS, mSubsetsString);
        return cv;
    }
}
