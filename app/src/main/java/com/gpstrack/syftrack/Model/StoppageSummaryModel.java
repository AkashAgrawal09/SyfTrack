package com.gpstrack.syftrack.Model;

public class StoppageSummaryModel
{
    public String vehicleNo;
    public String totalstops;
    public String jsonarray;
    public int value;
    public int lastvalue;
    public int vehicletype;

    public int getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(int vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getTotalstops() {
        return totalstops;
    }

    public void setTotalstops(String totalstops) {
        this.totalstops = totalstops;
    }

    public String getJsonarray() {
        return jsonarray;
    }

    public void setJsonarray(String jsonarray) {
        this.jsonarray = jsonarray;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLastvalue() {
        return lastvalue;
    }

    public void setLastvalue(int lastvalue) {
        this.lastvalue = lastvalue;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }



}
