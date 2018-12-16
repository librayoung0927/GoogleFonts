package com.mvp.fonts.utils;

import java.util.Iterator;
import java.util.Map;

public class DebugUtils {
    public static String debugMapData(Map<String, String> data){
        String ret = null;
        if(data != null){
            Iterator<String> keyIterator = data.keySet().iterator();
            while(keyIterator.hasNext()){
                String key = keyIterator.next();
                String value = data.get(key);
                ret = (ret == null)? String.format("(%s, %s)", key, value) : String.format("%s\n(%s, %s)", ret, key, value);
            }
        }
        return ret;
    }
}
