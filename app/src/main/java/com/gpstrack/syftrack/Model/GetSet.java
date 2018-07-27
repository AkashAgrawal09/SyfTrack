package com.gpstrack.syftrack.Model;

import android.app.Application;

public class GetSet extends Application
{
    public String getJson_response() {
        return json_response;
    }

    public void setJson_response(String json_response) {
        this.json_response = json_response;
    }

    public String json_response;

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String partnerid;
    public String userid;
    public String timezone;

}
