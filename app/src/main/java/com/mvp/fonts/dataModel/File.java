package com.mvp.fonts.dataModel;

import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("100")
    private String m100;

    @SerializedName("300")
    private String m300;

    @SerializedName("500")
    private String m500;

    @SerializedName("600")
    private String m600;

    @SerializedName("700")
    private String m700;

    @SerializedName("800")
    private String m800;

    @SerializedName("900")
    private String m900;

    @SerializedName("100italic")
    private String m100italic;

    @SerializedName("300italic")
    private String m300italic;

    @SerializedName("500italic")
    private String m500italic;

    @SerializedName("600italic")
    private String m600italic;

    @SerializedName("700italic")
    private String m700italic;

    @SerializedName("800italic")
    private String m800italic;

    @SerializedName("900italic")
    private String m900italic;

    @SerializedName("regular")
    private String mRegular;

    @SerializedName("italic")
    private String mItalic;


    public String getM100() {
        return m100;
    }

    public void setM100(String m100) {
        this.m100 = m100;
    }

    public String getM300() {
        return m300;
    }

    public void setM300(String m300) {
        this.m300 = m300;
    }

    public String getM500() {
        return m500;
    }

    public void setM500(String m500) {
        this.m500 = m500;
    }

    public String getM700() {
        return m700;
    }

    public void setM700(String m700) {
        this.m700 = m700;
    }

    public String getM900() {
        return m900;
    }

    public void setM900(String m900) {
        this.m900 = m900;
    }

    public String getM100italic() {
        return m100italic;
    }

    public void setM100italic(String m100italic) {
        this.m100italic = m100italic;
    }

    public String getM300italic() {
        return m300italic;
    }

    public void setM300italic(String m300italic) {
        this.m300italic = m300italic;
    }

    public String getM500italic() {
        return m500italic;
    }

    public void setM500italic(String m500italic) {
        this.m500italic = m500italic;
    }

    public String getM700italic() {
        return m700italic;
    }

    public void setM700italic(String m700italic) {
        this.m700italic = m700italic;
    }

    public String getM900italic() {
        return m900italic;
    }

    public void setM900italic(String m900italic) {
        this.m900italic = m900italic;
    }

    public String getRegular() {
        return mRegular;
    }

    public void setRegular(String regular) {
        mRegular = regular;
    }

    public String getItalic() {
        return mItalic;
    }

    public void setItalic(String italic) {
        mItalic = italic;
    }
}
