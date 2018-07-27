package com.gpstrack.syftrack.Model;


import com.gpstrack.syftrack.R;

public class BikeVehicleIcon
{
    static int resource;

    public static int getgreen(int heading)
    {
        String ss = "green_" + heading;

        if (ss.equals("green_0")) {
            resource = R.drawable.bike_0;
        } else if (ss.equals("green_1")) {
            resource = R.drawable.bike_1;
        } else if (ss.equals("green_2")) {
            resource = R.drawable.bike_2;
        } else if (ss.equals("green_3")) {
            resource = R.drawable.bike_3;
        } else if (ss.equals("green_4")) {
            resource = R.drawable.bike_4;
        } else if (ss.equals("green_5")) {
            resource = R.drawable.bike_5;
        } else if (ss.equals("green_6")) {
            resource = R.drawable.bike_6;
        } else if (ss.equals("green_7")) {
            resource = R.drawable.bike_7;
        } else {
            resource = R.drawable.bike_0;
        }
        return resource;
    }

    public static int getyellow(int heading) {

        String ss = "yellow_" + heading;

        if (ss.equals("yellow_0")) {
            resource = R.drawable.bikey_0;
        } else if (ss.equals("yellow_1")) {
            resource = R.drawable.bikey_1;
        } else if (ss.equals("yellow_2")) {
            resource = R.drawable.bikey_2;
        } else if (ss.equals("yellow_3")) {
            resource = R.drawable.bikey_3;
        } else if (ss.equals("yellow_4")) {
            resource = R.drawable.bikey_4;
        } else if (ss.equals("yellow_5")) {
            resource = R.drawable.bikey_5;
        } else if (ss.equals("yellow_6")) {
            resource = R.drawable.bikey_6;
        } else if (ss.equals("yellow_7")) {
            resource = R.drawable.bikey_7;
        } else {
            resource = R.drawable.bikey_0;
        }
        return resource;
    }

    public static int getred(int heading) {
        String ss = "red_" + heading;

        if (ss.equals("red_0")) {
            resource = R.drawable.biker_0;
        } else if (ss.equals("red_1")) {
            resource = R.drawable.biker_1;
        } else if (ss.equals("red_2")) {
            resource = R.drawable.biker_2;
        } else if (ss.equals("red_3")) {
            resource = R.drawable.biker_3;
        } else if (ss.equals("red_4")) {
            resource = R.drawable.biker_4;
        } else if (ss.equals("red_5")) {
            resource = R.drawable.biker_5;
        } else if (ss.equals("red_6")) {
            resource = R.drawable.biker_6;
        } else if (ss.equals("red_7")) {
            resource = R.drawable.biker_7;
        } else {
            resource = R.drawable.biker_0;
        }
        return resource;
    }
    public static int getnotworking(int heading) {
        String ss = "nw_" + heading;

        if (ss.equals("nw_0")) {
            resource = R.drawable.bikenw_0;
        } else if (ss.equals("nw_1")) {
            resource = R.drawable.bikenw_1;
        } else if (ss.equals("nw_2")) {
            resource = R.drawable.bikenw_2;
        } else if (ss.equals("nw_3")) {
            resource = R.drawable.bikenw_3;
        } else if (ss.equals("nw_4")) {
            resource = R.drawable.bikenw_4;
        } else if (ss.equals("nw_5")) {
            resource = R.drawable.bikenw_5;
        } else if (ss.equals("nw_6")) {
            resource = R.drawable.bikenw_6;
        } else if (ss.equals("nw_7")) {
            resource = R.drawable.bikenw_7;
        } else {
            resource = R.drawable.bikenw_0;
        }
        return resource;
    }
}
