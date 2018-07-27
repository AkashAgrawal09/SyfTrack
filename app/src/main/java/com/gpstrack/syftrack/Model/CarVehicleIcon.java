package com.gpstrack.syftrack.Model;

import com.gpstrack.syftrack.R;

public class CarVehicleIcon {
    static int resource;

    public static int getgreen(int heading)
    {
        String ss = "green_" + heading;

        if (ss.equals("green_0")) {
            resource = R.drawable.car_0;
        } else if (ss.equals("green_1")) {
            resource = R.drawable.car_1;
        } else if (ss.equals("green_2")) {
            resource = R.drawable.car_2;
        } else if (ss.equals("green_3")) {
            resource = R.drawable.car_3;
        } else if (ss.equals("green_4")) {
            resource = R.drawable.car_4;
        } else if (ss.equals("green_5")) {
            resource = R.drawable.car_5;
        } else if (ss.equals("green_6")) {
            resource = R.drawable.car_6;
        } else if (ss.equals("green_7")) {
            resource = R.drawable.car_7;
        } else {
            resource = R.drawable.car_0;
        }
        return resource;
    }

    public static int getyellow(int heading) {

        String ss = "yellow_" + heading;

        if (ss.equals("yellow_0")) {
            resource = R.drawable.cary_0;
        } else if (ss.equals("yellow_1")) {
            resource = R.drawable.cary_1;
        } else if (ss.equals("yellow_2")) {
            resource = R.drawable.cary_2;
        } else if (ss.equals("yellow_3")) {
            resource = R.drawable.cary_3;
        } else if (ss.equals("yellow_4")) {
            resource = R.drawable.cary_4;
        } else if (ss.equals("yellow_5")) {
            resource = R.drawable.cary_5;
        } else if (ss.equals("yellow_6")) {
            resource = R.drawable.cary_6;
        } else if (ss.equals("yellow_7")) {
            resource = R.drawable.cary_7;
        } else {
            resource = R.drawable.cary_0;
        }
        return resource;
    }

    public static int getred(int heading) {
        String ss = "red_" + heading;

        if (ss.equals("red_0")) {
            resource = R.drawable.carr_0;
        } else if (ss.equals("red_1")) {
            resource = R.drawable.carr_1;
        } else if (ss.equals("red_2")) {
            resource = R.drawable.carr_2;
        } else if (ss.equals("red_3")) {
            resource = R.drawable.carr_3;
        } else if (ss.equals("red_4")) {
            resource = R.drawable.carr_4;
        } else if (ss.equals("red_5")) {
            resource = R.drawable.carr_5;
        } else if (ss.equals("red_6")) {
            resource = R.drawable.carr_6;
        } else if (ss.equals("red_7")) {
            resource = R.drawable.carr_7;
        } else {
            resource = R.drawable.carr_0;
        }
        return resource;
    }
    public static int getnotworking(int heading) {
        String ss = "nw_" + heading;

        if (ss.equals("nw_0")) {
            resource = R.drawable.carnw_0;
        } else if (ss.equals("nw_1")) {
            resource = R.drawable.carnw_1;
        } else if (ss.equals("nw_2")) {
            resource = R.drawable.carnw_2;
        } else if (ss.equals("nw_3")) {
            resource = R.drawable.carnw_3;
        } else if (ss.equals("nw_4")) {
            resource = R.drawable.carnw_4;
        } else if (ss.equals("nw_5")) {
            resource = R.drawable.carnw_5;
        } else if (ss.equals("nw_6")) {
            resource = R.drawable.carnw_6;
        } else if (ss.equals("nw_7")) {
            resource = R.drawable.carnw_7;
        } else {
            resource = R.drawable.carnw_0;
        }
        return resource;
    }
}
