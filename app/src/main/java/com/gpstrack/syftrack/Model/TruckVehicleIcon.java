package com.gpstrack.syftrack.Model;

import com.gpstrack.syftrack.R;

public class TruckVehicleIcon
{
    static int resource;

    public static int getgreen(int heading)
    {
        String ss = "green_" + heading;

        if (ss.equals("green_0")) {
            resource = R.drawable.truck_0;
        } else if (ss.equals("green_1")) {
            resource = R.drawable.truck_1;
        } else if (ss.equals("green_2")) {
            resource = R.drawable.truck_2;
        } else if (ss.equals("green_3")) {
            resource = R.drawable.truck_3;
        } else if (ss.equals("green_4")) {
            resource = R.drawable.truck_4;
        } else if (ss.equals("green_5")) {
            resource = R.drawable.truck_5;
        } else if (ss.equals("green_6")) {
            resource = R.drawable.truck_6;
        } else if (ss.equals("green_7")) {
            resource = R.drawable.truck_7;
        } else {
            resource = R.drawable.truck_0;
        }
        return resource;
    }

    public static int getyellow(int heading) {

        String ss = "yellow_" + heading;

        if (ss.equals("yellow_0")) {
            resource = R.drawable.trucky_0;
        } else if (ss.equals("yellow_1")) {
            resource = R.drawable.trucky_1;
        } else if (ss.equals("yellow_2")) {
            resource = R.drawable.trucky_2;
        } else if (ss.equals("yellow_3")) {
            resource = R.drawable.trucky_3;
        } else if (ss.equals("yellow_4")) {
            resource = R.drawable.trucky_4;
        } else if (ss.equals("yellow_5")) {
            resource = R.drawable.trucky_5;
        } else if (ss.equals("yellow_6")) {
            resource = R.drawable.trucky_6;
        } else if (ss.equals("yellow_7")) {
            resource = R.drawable.trucky_7;
        } else {
            resource = R.drawable.trucky_0;
        }
        return resource;
    }

    public static int getred(int heading) {
        String ss = "red_" + heading;

        if (ss.equals("red_0")) {
            resource = R.drawable.truckr_0;
        } else if (ss.equals("red_1")) {
            resource = R.drawable.truckr_1;
        } else if (ss.equals("red_2")) {
            resource = R.drawable.truckr_2;
        } else if (ss.equals("red_3")) {
            resource = R.drawable.truckr_3;
        } else if (ss.equals("red_4")) {
            resource = R.drawable.truckr_4;
        } else if (ss.equals("red_5")) {
            resource = R.drawable.truckr_5;
        } else if (ss.equals("red_6")) {
            resource = R.drawable.truckr_6;
        } else if (ss.equals("red_7")) {
            resource = R.drawable.truckr_7;
        } else {
            resource = R.drawable.truckr_0;
        }
        return resource;
    }
    public static int getnotworking(int heading) {
        String ss = "nw_" + heading;

        if (ss.equals("nw_0")) {
            resource = R.drawable.trucknw_0;
        } else if (ss.equals("nw_1")) {
            resource = R.drawable.trucknw_1;
        } else if (ss.equals("nw_2")) {
            resource = R.drawable.trucknw_2;
        } else if (ss.equals("nw_3")) {
            resource = R.drawable.trucknw_3;
        } else if (ss.equals("nw_4")) {
            resource = R.drawable.trucknw_4;
        } else if (ss.equals("nw_5")) {
            resource = R.drawable.trucknw_5;
        } else if (ss.equals("nw_6")) {
            resource = R.drawable.trucknw_6;
        } else if (ss.equals("nw_7")) {
            resource = R.drawable.trucknw_7;
        } else {
            resource = R.drawable.trucknw_0;
        }
        return resource;
    }
}