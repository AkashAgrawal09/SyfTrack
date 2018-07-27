package com.gpstrack.syftrack.Model;

import com.gpstrack.syftrack.R;

public class BusVehicleIcon {
	static int resource;

	public static int getgreen(int heading)
	{
		String ss = "green_" + heading;

		if (ss.equals("green_0")) {
			resource = R.drawable.busg_0;
		} else if (ss.equals("green_1")) {
			resource = R.drawable.busg_1;
		} else if (ss.equals("green_2")) {
			resource = R.drawable.busg_2;
		} else if (ss.equals("green_3")) {
			resource = R.drawable.busg_3;
		} else if (ss.equals("green_4")) {
			resource = R.drawable.busg_4;
		} else if (ss.equals("green_5")) {
			resource = R.drawable.busg_5;
		} else if (ss.equals("green_6")) {
			resource = R.drawable.busg_6;
		} else if (ss.equals("green_7")) {
			resource = R.drawable.busg_7;
		} else {
			resource = R.drawable.busg_0;
		}
		return resource;
	}

	public static int getyellow(int heading) {

		String ss = "yellow_" + heading;

		if (ss.equals("yellow_0")) {
			resource = R.drawable.busy_0;
		} else if (ss.equals("yellow_1")) {
			resource = R.drawable.busy_1;
		} else if (ss.equals("yellow_2")) {
			resource = R.drawable.busy_2;
		} else if (ss.equals("yellow_3")) {
			resource = R.drawable.busy_3;
		} else if (ss.equals("yellow_4")) {
			resource = R.drawable.busy_4;
		} else if (ss.equals("yellow_5")) {
			resource = R.drawable.busy_5;
		} else if (ss.equals("yellow_6")) {
			resource = R.drawable.busy_6;
		} else if (ss.equals("yellow_7")) {
			resource = R.drawable.busy_7;
		} else {
			resource = R.drawable.busy_0;
		}
		return resource;
	}

	public static int getred(int heading) {
		String ss = "red_" + heading;

		if (ss.equals("red_0")) {
			resource = R.drawable.busr_0;
		} else if (ss.equals("red_1")) {
			resource = R.drawable.busr_1;
		} else if (ss.equals("red_2")) {
			resource = R.drawable.busr_2;
		} else if (ss.equals("red_3")) {
			resource = R.drawable.busr_3;
		} else if (ss.equals("red_4")) {
			resource = R.drawable.busr_4;
		} else if (ss.equals("red_5")) {
			resource = R.drawable.busr_5;
		} else if (ss.equals("red_6")) {
			resource = R.drawable.busr_6;
		} else if (ss.equals("red_7")) {
			resource = R.drawable.busr_7;
		} else {
			resource = R.drawable.busr_0;
		}
		return resource;
	}
	public static int getnotworking(int heading) {
		String ss = "nw_" + heading;

		if (ss.equals("nw_0")) {
			resource = R.drawable.busnw_0;
		} else if (ss.equals("nw_1")) {
			resource = R.drawable.busnw_1;
		} else if (ss.equals("nw_2")) {
			resource = R.drawable.busnw_2;
		} else if (ss.equals("nw_3")) {
			resource = R.drawable.busnw_3;
		} else if (ss.equals("nw_4")) {
			resource = R.drawable.busnw_4;
		} else if (ss.equals("nw_5")) {
			resource = R.drawable.busnw_5;
		} else if (ss.equals("nw_6")) {
			resource = R.drawable.busnw_6;
		} else if (ss.equals("nw_7")) {
			resource = R.drawable.busnw_7;
		} else {
			resource = R.drawable.busnw_0;
		}
		return resource;
	}
}
