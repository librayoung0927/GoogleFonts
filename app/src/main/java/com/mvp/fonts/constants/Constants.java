package com.mvp.fonts.constants;

import com.mvp.fonts.BuildConfig;

public class Constants {

    //測試外網
    private static final String SERVER_URL_DEV =
            "https://www.googleapis.com/webfonts/v1/webfonts?" + "key=" + Defined.API_KEY + "&";

    //正式外網
    private static final String SERVER_URL =
            "https://www.googleapis.com/webfonts/v1/webfonts?" + "key=" + Defined.API_KEY + "&";

    public static String getServerUrl() {
        return BuildConfig.DEBUG ? SERVER_URL_DEV : SERVER_URL;
    }



}
