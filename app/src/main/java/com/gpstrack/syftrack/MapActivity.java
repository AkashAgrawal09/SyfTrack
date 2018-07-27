package com.gpstrack.syftrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gpstrack.syftrack.Model.ArrowVehicleIcon;
import com.gpstrack.syftrack.Model.BikeVehicleIcon;
import com.gpstrack.syftrack.Model.BusVehicleIcon;
import com.gpstrack.syftrack.Model.CarVehicleIcon;
import com.gpstrack.syftrack.Model.GetSet;
import com.gpstrack.syftrack.Model.TruckVehicleIcon;
import com.gpstrack.syftrack.Utils.Constants;
import com.gpstrack.syftrack.Utils.Online;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.LivingTrackingVolley;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;
import com.gpstrack.syftrack.WebService.VolleyPostReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView ac,battery,ignition,fuel,down;
    TextView vehicle_number,dateTime,address,lst,speed,lastloc,flsd,totald,flsp,totalp,flsu,totaldu,refresh_time;
    double laitde,longtde;
    String deviceSno,Current_time;
    String drivername,drivernumber;
    Polyline polyline=null;
    String lastliti,longi,subs,vehcle_name;

    LinearLayout driver,laststoppedmap,playback,immobilize,innerLay;

    RelativeLayout heading_error;

    int intentStatus=0,vehicletype=0;
    int mapResource=0,heading=0;

    String strAddress;
    private Handler mHandler = new Handler();
    ArrayList<LatLng> arrayList=new ArrayList<>();

    int server_code;

    private int[] items1 = new int[] {
            R.string.i_on,
            R.string.i_off
    };

    private int[] icons = new int[] {
            R.drawable.down,
            R.drawable.ignition_off
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ac=findViewById(R.id.fan);
        battery=findViewById(R.id.battery);
        ignition=findViewById(R.id.ignition_off);
        fuel=findViewById(R.id.fuel);
        vehicle_number=findViewById(R.id.vehicle_number);
        dateTime=findViewById(R.id.date);
        address=findViewById(R.id.address);
        lst=findViewById(R.id.lst);
        speed=findViewById(R.id.speed);
        lastloc=findViewById(R.id.lastloc);
        flsd=findViewById(R.id.flsd);
        totald=findViewById(R.id.totald);
        flsp=findViewById(R.id.flsp);
        totalp=findViewById(R.id.totalp);
        flsu=findViewById(R.id.flsdu);
        totaldu=findViewById(R.id.totaldu);

        refresh_time=findViewById(R.id.refresh_time);

        driver=findViewById(R.id.driver);
        playback=findViewById(R.id.playback);
        laststoppedmap=findViewById(R.id.laststoppedmap);
        immobilize=findViewById(R.id.immobilize);
        innerLay=findViewById(R.id.innerLay);
        down=findViewById(R.id.down);

        heading_error =findViewById(R.id.heading);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i=getIntent();
        try {
            JSONObject jsonObject=new JSONObject(i.getStringExtra("json"));
            JSONObject inner=new JSONObject(jsonObject.getString("device"));
            JSONObject lastlocation=new JSONObject(jsonObject.getString("lastLocation"));
            JSONObject vehicletype1=new JSONObject(inner.getString("vehicleType"));
            JSONObject icon_staus=new JSONObject(jsonObject.getString("vehicleStatus"));
            JSONObject driver=new JSONObject(jsonObject.getString("driver"));

            vehicle_number.setText(inner.getString("vehicleNumber"));
            vehcle_name=inner.getString("vehicleNumber");
            deviceSno=inner.getString("deviceSno");
            vehicletype=vehicletype1.getInt("vehicleTypeID");
            intentStatus=jsonObject.getInt("status");
            int power=icon_staus.getInt("power");
            int acenable=icon_staus.getInt("acEnable");
            int fuelenable=icon_staus.getInt("fuelEnable");
            int ignitionenable=icon_staus.getInt("ignitionEnable");
            String speed1=lastlocation.getString("speed");
            laitde= Double.parseDouble(lastlocation.getString("latitude"));
            longtde= Double.parseDouble(lastlocation.getString("longitude"));
            drivername=driver.getString("driverName");
            drivernumber=driver.getString("mobileNumber");

            heading= Integer.parseInt(String.valueOf((Math.round(lastlocation.getInt("heading")/45.0)) % 8));

            speed.setText(speed1+" Km/Hr");

            Current_time=lastlocation.getString("timestamp").replace("T"," ");

            dateTime.setText(Current_time);
            refresh_time.setText(Current_time);

            if(power==1 || power==-1)
            {
                battery.setImageResource(R.drawable.batterygreen);
            }
            else
            {
                battery.setImageResource(R.drawable.batteryred);
            }

            if(acenable==0)
            {
                ac.setImageResource(R.drawable.fan_grey);
            }
            else
            {
                if(icon_staus.getInt("ac")==1)
                {
                    ac.setImageResource(R.drawable.fan_green);
                }
                else
                {
                    ac.setImageResource(R.drawable.fan_red);
                }
            }

            if(fuelenable==0)
            {
                fuel.setImageResource(R.drawable.fuel_grey);
            }
            else
            {
                if(icon_staus.getInt("fuel")==0)
                {
                    fuel.setImageResource(R.drawable.fuel_green);
                }

            }

            if(ignitionenable==0)
            {
                ignition.setImageResource(R.drawable.key_grey);
            }
            else
            {
                if(icon_staus.getInt("ignition")==1)
                {
                    ignition.setImageResource(R.drawable.key_green);
                }
                else
                {
                    ignition.setImageResource(R.drawable.key_red);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        callAPI();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        GetSet getSet=(GetSet)getApplicationContext();
                        String partnerid= getSet.getPartnerid();
                        String time=getSet.getTimezone();
                        String userid=getSet.getUserid();

                        partnerid= StringFormatter.convertToUTF8(partnerid);
                        deviceSno= StringFormatter.convertToUTF8(deviceSno);
                        time= StringFormatter.convertToUTF8(time);
                        new LivingTrackingVolley(MapActivity.this).getjsonObject("http://coreapi.gpsapphub.com/api/Dashboard/"+partnerid+"/"+userid+"/"+deviceSno+"/"+time,
                                new VolleyJsonReq.MyListener() {
                                    @Override
                                    public void onServercode(int code)
                                    {
                                        server_code=code;
                                    }

                                    @Override
                                    public void onServerResponse(String response)
                                    {
                                        if(server_code==200) {
                                            setnewdata(response);
                                        }
                                        else
                                        {
                                            Snackbar snackbar1 = Snackbar.make(heading_error, response, Snackbar.LENGTH_LONG);
                                            snackbar1.show();
                                        }
                                    }
                                });
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000);

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drivernumber.equalsIgnoreCase("N/A")) {
                    phonenumber();
                }
                else
                {
                    Toast.makeText(MapActivity.this, "No Driver allocated to this vehicle", Toast.LENGTH_LONG).show();
                }
            }
        });
        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MapActivity.this,HistoryPlay.class);
                i.putExtra("deviceso",deviceSno);
                i.putExtra("vehicle",vehicle_number.getText().toString());
                startActivity(i);
            }
        });
        laststoppedmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twomarker();
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(innerLay.getVisibility()==View.VISIBLE)
                {
                    // down.setImageResource(0);
                    innerLay.setVisibility(View.GONE);
                    // down.setImageResource(R.drawable.up);
                }
                else
                {
                    //  down.setImageResource(0);
                    innerLay.setVisibility(View.VISIBLE);
                    //      down.setImageResource(R.drawable.down);
                }
            }
        });

        immobilize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                BottomSheet.Builder builder = new BottomSheet.Builder(MapActivity.this);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(Online.isOnline(MapActivity.this)) {
                            if (which == 0) {
                                powerapi(Constants.IGNITION_ON);
                            } else if (which == 1) {
                                powerapi(Constants.IGNITION_OFF);
                            }
                        }
                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(heading_error,"No Internet Connection", Snackbar.LENGTH_LONG);
                            snackbar1.show();
                        }
                    }
                });
                builder.setItemTextColor(Color.BLACK);
                builder.show();
            }
        });

    }


    private void twomarker()
    {
        if(!lastliti.equalsIgnoreCase("N/A")  )
        {
            if(Online.isOnline(MapActivity.this)) {
                callAPiforpolyline();
            }
            else
            {
                Snackbar snackbar1 = Snackbar.make(heading_error,"No Internet Connection", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        iniliseMap();
    }

    public String address(double latitude, double longitude){
        Geocoder gc = new Geocoder(MapActivity.this);

        strAddress = null;
        if(Geocoder.isPresent()){
            List<Address> list = null;

            Double lat = latitude;
            Double longi = longitude;
            try {
                list = gc.getFromLocation(lat, longi,1);

                Address address = list.get(0);
                StringBuffer str = new StringBuffer();
                str.append( address.getAddressLine(0) + ", ");
                str.append(address.getAddressLine(1)+ ", ");
                str.append(address.getAddressLine(2)+ ", ");
                str.append(address.getCountryName() + ".");
                str.append("Country Code: " + address.getCountryCode());
                strAddress  = str.toString();
                if(strAddress.contains("null,")){
                    strAddress = strAddress.replace("null,", "");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IndexOutOfBoundsException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return strAddress;
    }

    public void callAPI()
    {
        GetSet getSet=(GetSet)getApplicationContext();
        String partnerid=getSet.getPartnerid();
        String timezone=getSet.getTimezone();
        partnerid= StringFormatter.convertToUTF8(partnerid);
        deviceSno= StringFormatter.convertToUTF8(deviceSno);
        timezone= StringFormatter.convertToUTF8(timezone);
        new VolleyJsonReq(MapActivity.this).getjsonObject("http://coreapi.gpsapphub.com/api/VehicleStatus/"+partnerid+"/"+deviceSno+"/"+timezone,
                new VolleyJsonReq.MyListener() {
                    @Override
                    public void onServercode(int code) {
                        server_code=code;
                    }

                    @Override
                    public void onServerResponse(String response)
                    {
                        if(server_code==200) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                totald.setText(jsonObject.getString("fullDayDistance") + " Km");   //4
                                flsd.setText(jsonObject.getString("lastStopDistance") + " Km");    //3
                                totaldu.setText(jsonObject.getString("totalDayDormant"));       //8
                                flsu.setText(jsonObject.getString("lastStopDormant"));          //7
                                totalp.setText(jsonObject.getString("totalDayStoppage"));   //6
                                flsp.setText(jsonObject.getString("lastStopStoppage"));   //5
                                subs = jsonObject.getString("laststopTimeR");
                                if (subs.contains("1970")) {
                                    lst.setText("N/A");
                                    lastloc.setText("N/A");
                                    lastliti = "N/A";
                                    longi = "N/A";
                                } else {
                                    String Current_time = jsonObject.getString("laststopTime").replace("T", " ");
                                    lst.setText(Current_time);
                                    lastliti = jsonObject.getString("lastStopLatitude");
                                    longi = jsonObject.getString("lastStopLongitude");
                                    lastloc.setTextSize(10);
                                    lastloc.setText(lastliti + "\n" + longi);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(heading_error, response, Snackbar.LENGTH_LONG);
                            snackbar1.show();
                        }

                    }
                });

    }
    public void iniliseMap()
    {
        mMap.clear();
        int statusa = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(statusa!= ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusa, this, requestCode);
            dialog.show();

        }else {
            if (mMap == null) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
                Toast.makeText(this, "Creating map again", Toast.LENGTH_SHORT).show();
            }
            else {

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

                final String map_type = sharedPrefs.getString("mapdisplay", "Normal");

                if(map_type==null||map_type.equals(""))
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else if(map_type.equals("Satelite"))
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                else if(map_type.equals("Normal"))
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else if(map_type.equals("Tarran"))
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                else
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                if (intentStatus == Constants.IN_MOTION) {
                    if (vehicletype == 1) {
                        mapResource = BusVehicleIcon.getgreen(heading);
                    } else if (vehicletype == 2) {
                        mapResource = TruckVehicleIcon.getgreen(heading);
                    } else if (vehicletype == 3) {
                        //  mapResource=R.drawable.cargreen_0;
                        mapResource = CarVehicleIcon.getgreen(heading);
                    } else if (vehicletype == 4) {
                        mapResource = BikeVehicleIcon.getgreen(heading);
                    }
                    else {
                        mapResource = ArrowVehicleIcon.getgreen(heading);
                    }
                }

                else if (intentStatus == Constants.IDLING) {
                    if (vehicletype == 1) {
                        mapResource = BusVehicleIcon.getyellow(heading);
                    } else if (vehicletype == 2) {
                        mapResource = TruckVehicleIcon.getyellow(heading);
                    } else if (vehicletype == 3) {
                        //     mapResource=R.drawable.caryellow_0;
                        mapResource = CarVehicleIcon.getyellow(heading);
                    }else if (vehicletype == 4) {
                        mapResource = BikeVehicleIcon.getyellow(heading);
                    }
                    else {
                        mapResource=ArrowVehicleIcon.getyellow(heading);
                    }
                }

                else if (intentStatus == Constants.STOP) {

                    if (vehicletype == 1) {
                        mapResource = BusVehicleIcon.getred(heading);
                    } else if (vehicletype == 2) {
                        mapResource = TruckVehicleIcon.getred(heading);
                    } else if (vehicletype == 3) {
                        //  mapResource=R.drawable.carr__0;
                        mapResource = CarVehicleIcon.getred(heading);
                    }else if (vehicletype == 4) {
                        mapResource = BikeVehicleIcon.getred(heading);
                    }
                    else{
                        mapResource=ArrowVehicleIcon.getred(heading);
                    }
                }

                else if (intentStatus == Constants.NOTWORKING) {
                    if (vehicletype == 1) {
                        mapResource = BusVehicleIcon.getnotworking(heading);
                    } else if (vehicletype == 2) {
                        mapResource = TruckVehicleIcon.getnotworking(heading);
                    } else if (vehicletype == 3) {
                        //   mapResource=R.drawable.carblue_0;
                        mapResource = CarVehicleIcon.getnotworking(heading);
                    } else if (vehicletype == 4) {
                        mapResource = BikeVehicleIcon.getnotworking(heading);
                    }
                    else
                    {
                        mapResource=ArrowVehicleIcon.getnotworking(heading);
                    }

                }

                LatLng markerLoc = new LatLng(laitde, longtde);
                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(markerLoc) // Sets the center of the map to Mountain
                        .zoom(13) // Sets the zoom// Sets the orientation of the camera to east
                        .tilt(30) // Sets the tilt of the camera to 30 degrees
                        .build();

                mMap.addMarker(new MarkerOptions().position(new LatLng(
                        laitde, longtde)).icon(BitmapDescriptorFactory.fromResource(mapResource)));
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        }
        address.setText(address(laitde, longtde));

    }

    public void phonenumber()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("Call to "+drivername);
        builder.setMessage("Do you want to call on "+drivernumber);
        builder.setPositiveButton("Call",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Intent in=new Intent(Intent.ACTION_DIAL , Uri.parse("tel:" + drivernumber));
                        startActivity(in);
                    }
                });

        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        builder.show();
    }
    public void callAPiforpolyline()
    {
        String currentDate="";
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        String e=Integer.toString(monthOfYear);
        if (monthOfYear<10){
            int a=1+monthOfYear;
            e="0"+a;
        }
        else {
            e=Integer.toString(monthOfYear+1);
        }

        String s=Integer.toString(dayOfMonth);
        if (dayOfMonth<10){
            s="0"+s;
        };
        String b=year+"-"+e+"-"+s;
        currentDate=b+" 00:00:00";

        GetSet getSet=(GetSet)getApplicationContext();
        String partnerid= getSet.getPartnerid();
        String time=getSet.getTimezone();

        partnerid= StringFormatter.convertToUTF8(partnerid);
        deviceSno= StringFormatter.convertToUTF8(deviceSno);
        currentDate= StringFormatter.convertToUTF8(currentDate);
        Current_time= StringFormatter.convertToUTF8(Current_time);
        time= StringFormatter.convertToUTF8(time);

        new VolleyJsonReq(MapActivity.this).getjson("http://coreapi.gpsapphub.com/api/HistoryReplay/"+partnerid+"/"+deviceSno+"/"+currentDate+"/"+Current_time+"/"+time+"/1",
                new VolleyJsonReq.MyListener() {
                    @Override
                    public void onServercode(int code) {
                        server_code=code;
                    }

                    @Override
                    public void onServerResponse(String response) {
                        if (server_code == 200)
                        {
                            try {
                                if (polyline != null)
                                    polyline.remove();
                                arrayList.clear();
                                JSONArray jsonArray = new JSONArray(response);
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                double firstLat = 0, firstLng = 0;

                                for (int z = 0; z < jsonArray.length(); z++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(z);
                                    Double mlatitude = jsonObject.getDouble("lat");
                                    Double mlongitude = jsonObject.getDouble("lng");
                                    arrayList.add(new LatLng(mlatitude, mlongitude));
                                    builder.include(new LatLng(mlatitude, mlongitude));
                                    if (z == 0) {
                                        firstLat = mlatitude;
                                        firstLng = mlongitude;
                                    }
                                }

                                PolylineOptions polylineOptions2 = new PolylineOptions();
                                polylineOptions2.color(Color.RED);
                                polylineOptions2.width(4);
                                polylineOptions2.addAll(arrayList);
                                polyline = mMap.addPolyline(polylineOptions2);


                                mMap.addMarker(new MarkerOptions().position(new LatLng(
                                        firstLat, firstLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));

                                LatLngBounds bounds = builder.build();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));

                            } catch (Exception e) {
                            }
                        }
                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(heading_error, response, Snackbar.LENGTH_LONG);
                            snackbar1.show();
                        }
                    }

                });
    }

    public void setnewdata(String response)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String strDate = mdformat.format(calendar.getTime());
            refresh_time.setText(strDate);
            JSONObject jsonObjectouter=new JSONObject(response);
            JSONObject jsonObject=new JSONObject(jsonObjectouter.getString("location"));
            JSONObject lastlocation=new JSONObject(jsonObject.getString("lastLocation"));
            JSONObject icon_staus=new JSONObject(jsonObject.getString("vehicleStatus"));
            JSONObject driver=new JSONObject(jsonObject.getString("driver"));

            int new_heading=lastlocation.getInt("heading");

            heading=Integer.parseInt(String.valueOf((Math.round(new_heading/45.0)) % 8));

            intentStatus=jsonObject.getInt("status");
            int power=icon_staus.getInt("power");
            int acenable=icon_staus.getInt("acEnable");
            int fuelenable=icon_staus.getInt("fuelEnable");
            int ignitionenable=icon_staus.getInt("ignitionEnable");
            String speed1=lastlocation.getString("speed");
            laitde= Double.parseDouble(lastlocation.getString("latitude"));
            longtde= Double.parseDouble(lastlocation.getString("longitude"));
            drivername=driver.getString("driverName");
            drivernumber=driver.getString("mobileNumber");

            speed.setText(speed1+" Km/Hr");

            Current_time=lastlocation.getString("timestamp").replace("T"," ");

            dateTime.setText(Current_time);

            if(power==1 || power==-1)
            {
                battery.setImageResource(R.drawable.batterygreen);
            }
            else
            {
                battery.setImageResource(R.drawable.batteryred);
            }


            if(acenable==0)
            {
                ac.setImageResource(R.drawable.fan_grey);
            }
            else
            {
                if(icon_staus.getInt("ac")==1)
                {
                    ac.setImageResource(R.drawable.fan_green);
                }
                else
                {
                    ac.setImageResource(R.drawable.fan_red);
                }
            }

            if(fuelenable==0)
            {
                fuel.setImageResource(R.drawable.fuel_grey);
            }
            else
            {
                if(icon_staus.getInt("fuel")==0)
                {
                    fuel.setImageResource(R.drawable.fuel_green);
                }

            }

            if(ignitionenable==0)
            {
                ignition.setImageResource(R.drawable.key_grey);
            }
            else
            {
                if(icon_staus.getInt("ignition")==1)
                {
                    ignition.setImageResource(R.drawable.key_green);
                }
                else
                {
                    ignition.setImageResource(R.drawable.key_red);
                }
            }
            JSONObject details=new JSONObject(jsonObjectouter.getString("details"));
            totald.setText(details.getString("fullDayDistance")+" Km");
            flsd.setText(details.getString("lastStopDistance")+" Km");
            totaldu.setText(details.getString("totalDayDormant"));
            flsu.setText(details.getString("lastStopDormant"));
            totalp.setText(details.getString("totalDayStoppage"));
            flsp.setText(details.getString("lastStopStoppage"));
            subs=details.getString("laststopTimeR");
            if(subs.contains("1970"))
            {
                lst.setText("N/A");
                lastloc.setText("N/A");
                lastliti="N/A";
                longi="N/A";
            }
            else
            {
                String Current_time=details.getString("laststopTime").replace("T"," ");
                lst.setText(Current_time);
                lastliti=details.getString("lastStopLatitude");
                longi=details.getString("lastStopLongitude");
                lastloc.setTextSize(10);
                lastloc.setText(lastliti+"\n"+longi);
            }

            iniliseMap();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void powerapi(final int i)
    {
        GetSet getSet=(GetSet)getApplicationContext();
        String partnerid= getSet.getPartnerid();
        String time=getSet.getTimezone();
        String userid=getSet.getUserid();
        JSONObject jsonObject=new JSONObject();
        try{


            JSONObject user=new JSONObject();
            user.put("PartnerID",partnerid);
            user.put("UserID",userid);

            JSONObject  device=new JSONObject();
            device.put("DeviceSno",deviceSno);
            device.put("VehicleNumber",vehcle_name);

            JSONObject notifi_detail=new JSONObject();
            notifi_detail.put("RegID", FirebaseInstanceId.getInstance().getToken());
            notifi_detail.put("ServerID","AIzaSyCTkMAeu3NDfVcLGCSUlagswrWHUINofXw");
            notifi_detail.put("ProjectID","890335683401");
            jsonObject.put("RequestFor",i);
            jsonObject.put("Timezone",time);
            jsonObject.put("User",user);
            jsonObject.put("Device",device);
            jsonObject.put("Notification",notifi_detail);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        new VolleyPostReq(MapActivity.this).getjsonObject("http://coreapi.gpsapphub.com/api/Request",jsonObject, new VolleyPostReq.MyListener() {
            @Override
            public void onServercode(int code) {
                server_code=code;
            }

            @Override
            public void onServerResponse(String response) {
                if (server_code == 200) {
                    try {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                        builder.setTitle("Power Alert");
                        if(i== Constants.IGNITION_ON)
                        {
                            builder.setMessage(vehcle_name +" Power resume success");
                        }
                        else
                        {
                            builder.setMessage(vehcle_name +" Power cut success");
                        }
                        builder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });

                        builder.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Snackbar snackbar1 = Snackbar.make(heading_error, response, Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }
            }
        });

    }
}
