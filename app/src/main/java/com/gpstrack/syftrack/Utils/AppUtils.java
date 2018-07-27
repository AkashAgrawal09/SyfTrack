package com.gpstrack.syftrack.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppUtils
{
    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;

    public AppUtils(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constants.APP_UTILS_KEY, 0);
        this.context = context;
        editor = sharedPreferences.edit();
    }

    public void setLogSkipStatus(boolean b){
        editor.putBoolean(Constants.Login_SKIP_KEY,b);
        editor.commit();

    }

    public boolean getLogSkipStatus(){
        boolean status = sharedPreferences.getBoolean(Constants.Login_SKIP_KEY,false);
        return status;
    }

    public void setUserName(String name){
        editor.putString(Constants.USER_ID,name);
        editor.commit();
    }

    public String getUserName(){
        return sharedPreferences.getString(Constants.USER_ID,null);
    }

    public void setPartnerID(String PartnerID){
        editor.putString(Constants.PARTNER_ID,PartnerID);
        editor.commit();
    }

    public String getPartnerID(){
        return sharedPreferences.getString(Constants.PARTNER_ID,null);
    }

    public void setTimeZone(String timeZone){
        editor.putString(Constants.TIME_ZONE,timeZone);
        editor.commit();
    }

    public String getTimeZone(){
        return sharedPreferences.getString(Constants.TIME_ZONE,null);
    }

    public void setlogintype(String type){
        editor.putString(Constants.TYPE,type);
        editor.commit();
    }

    public String getlogintype(){
        return sharedPreferences.getString(Constants.TYPE,null);
    }




    public void setnumberremeber(boolean b){
        editor.putBoolean(Constants.Login_NUMBER_KEY,b);
        editor.commit();

    }

    public boolean getnumberrember(){
        boolean status = sharedPreferences.getBoolean(Constants.Login_NUMBER_KEY,false);
        return status;
    }


}
