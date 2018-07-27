package com.gpstrack.syftrack.Model;

import com.gpstrack.syftrack.R;

public class ArrowVehicleIcon
{
    static int resource;

    public static int getgreen(int heading)
    {
        String ss = "green_" + heading;

        if (ss.equals("green_0")) {
            resource = R.drawable.green_0;
        } else if (ss.equals("green_1")) {
            resource = R.drawable.green_1;
        } else if (ss.equals("green_2")) {
            resource = R.drawable.green_2;
        } else if (ss.equals("green_3")) {
            resource = R.drawable.green_3;
        } else if (ss.equals("green_4")) {
            resource = R.drawable.green_4;
        } else if (ss.equals("green_5")) {
            resource = R.drawable.green_5;
        } else if (ss.equals("green_6")) {
            resource = R.drawable.green_6;
        } else if (ss.equals("green_7")) {
            resource = R.drawable.green_7;
        } else {
            resource = R.drawable.green_0;
        }
        return resource;
    }

    public static int getyellow(int heading)
    {
        String ss = "yellow_" + heading;

        if (ss.equals("yellow_0")) {
            resource = R.drawable.yellow_0;
        } else if (ss.equals("yellow_1")) {
            resource = R.drawable.yellow_1;
        } else if (ss.equals("yellow_2")) {
            resource = R.drawable.yellow_2;
        } else if (ss.equals("yellow_3")) {
            resource = R.drawable.yellow_3;
        } else if (ss.equals("yellow_4")) {
            resource = R.drawable.yellow_4;
        } else if (ss.equals("yellow_5")) {
            resource = R.drawable.yellow_5;
        } else if (ss.equals("yellow_6")) {
            resource = R.drawable.yellow_6;
        } else if (ss.equals("yellow_7")) {
            resource = R.drawable.yellow_7;
        } else {
            resource = R.drawable.yellow_0;
        }
        return resource;
    }

    public static int getred(int heading)
    {
        String ss = "red_" + heading;

        if (ss.equals("red_0")) {
            resource = R.drawable.red_0;
        } else if (ss.equals("red_1")) {
            resource = R.drawable.red_1;
        } else if (ss.equals("red_2")) {
            resource = R.drawable.red_2;
        } else if (ss.equals("red_3")) {
            resource = R.drawable.red_3;
        } else if (ss.equals("red_4")) {
            resource = R.drawable.red_4;
        } else if (ss.equals("red_5")) {
            resource = R.drawable.red_5;
        } else if (ss.equals("red_6")) {
            resource = R.drawable.red_6;
        } else if (ss.equals("red_7")) {
            resource = R.drawable.red_7;
        } else {
            resource = R.drawable.red_0;
        }
        return resource;
    }
    public static int getnotworking(int heading)
    {
        String ss = "nw_" + heading;

        if (ss.equals("nw_0")) {
            resource = R.drawable.blue_0;
        } else if (ss.equals("nw_1")) {
            resource = R.drawable.blue_1;
        } else if (ss.equals("nw_2")) {
            resource = R.drawable.blue_2;
        } else if (ss.equals("nw_3")) {
            resource = R.drawable.blue_3;
        } else if (ss.equals("nw_4")) {
            resource = R.drawable.blue_4;
        } else if (ss.equals("nw_5")) {
            resource = R.drawable.blue_5;
        } else if (ss.equals("nw_6")) {
            resource = R.drawable.blue_6;
        } else if (ss.equals("nw_7")) {
            resource = R.drawable.blue_7;
        } else {
            resource = R.drawable.blue_0;
        }
        return resource;
    }
}
