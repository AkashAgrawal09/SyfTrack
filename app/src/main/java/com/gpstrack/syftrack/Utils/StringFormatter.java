package com.gpstrack.syftrack.Utils;

public class StringFormatter {

    // convert UTF-8 to internal Java String format
    public static String convertUTF8ToString(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    // convert internal Java String format to UTF-8
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    public static String convertToUTF8(String s){
        s= s.replace(" ","%20");
     //   s=s.replace("-","%2d");
     //   s=s.replace(":","%3a");
        return s;

    }

}