package com.gpstrack.syftrack.Model;

public class TravelSummaryModel
{
    public String vehicleNo;
    public String idle;
    public String Stop;
    public String distnace;
    public String maxspeed;
    public String avgspeed;
    public int vehciletype;
    public int value;

    public int getVehciletype() {
        return vehciletype;
    }

    public void setVehciletype(int vehciletype) {
        this.vehciletype = vehciletype;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public void setIdle(String idle) {
        this.idle = idle;
    }

    public void setStop(String stop) {
        Stop = stop;
    }

    public void setDistnace(String distnace) {
        this.distnace = distnace;
    }

    public void setMaxspeed(String maxspeed) {
        this.maxspeed = maxspeed;
    }

    public void setAvgspeed(String avgspeed) {
        this.avgspeed = avgspeed;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getIdle() {
        return idle;
    }

    public String getStop() {
        return Stop;
    }

    public String getDistnace() {
        return distnace;
    }

    public String getMaxspeed() {
        return maxspeed;
    }

    public String getAvgspeed() {
        return avgspeed;
    }


}
