package com.gpstrack.syftrack;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gpstrack.syftrack.Model.ArrowVehicleIcon;
import com.gpstrack.syftrack.Model.GetSet;
import com.gpstrack.syftrack.Utils.Constants;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HistoryPlay extends FragmentActivity implements OnMapReadyCallback,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private GoogleMap mMap;
    TextView fromdate,fromtime,todate,totime,vehicle_number;
    String fromdate_text=null,todate_text=null,fromtime_text=null,totime_text=null;
    String deviceso;
    int a=0,t=0;
    int speed=2000;
    LinearLayout play,x_textset;
    private Handler mHandler = new Handler();
    Timer timer = new Timer();

    ArrayList<LatLng> arrayList=new ArrayList<>();

    ArrayList<JSONObject> array_jsonObjects=new ArrayList<>();
    Polyline polyline=null;

    ImageView image_play;
    int passvalue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_play);

        Intent i=getIntent();
        deviceso=i.getStringExtra("deviceso");

        fromdate=findViewById(R.id.fromdate);
        fromtime=findViewById(R.id.fromtime);
        totime=findViewById(R.id.totime);
        todate=findViewById(R.id.todate);
        vehicle_number=findViewById(R.id.vehicle_number);
        play=findViewById(R.id.play);
        x_textset=findViewById(R.id.x_textset);
        image_play=findViewById(R.id.play_iamge);
        image_play.setVisibility(View.GONE);

        vehicle_number.setText(i.getStringExtra("vehicle"));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        String b1=s+"-"+e+"-"+year;
        String b=year+"-"+e+"-"+s;
        fromdate.setText(b1);
        todate.setText(b1);
        fromtime.setText("00:00:00");
        totime.setText("23:59:59");

        fromdate_text=b;
        todate_text=b;
        fromtime_text=fromtime.getText().toString();
        totime_text=totime.getText().toString();

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=1;
                setdata();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=2;
                setdata();
            }
        });
        totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=3;
                settime();
            }
        });
        fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=4;
                settime();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromdate_text==null|| fromtime_text==null||totime_text==null|| todate_text==null)
                {
                    Toast.makeText(HistoryPlay.this, "Please select all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-mm-dd");
                    String inputString1 =fromdate_text;
                    String inputString2 = todate_text;

                    try {
                        Date date1 = myFormat.parse(inputString1);
                        Date date2 = myFormat.parse(inputString2);
                        long diff = date1.getTime() - date2.getTime();
                        diff= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        if(diff>3)
                        {
                            Toast.makeText(HistoryPlay.this, "Difference between two dates not more than 2", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            servicecall();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        image_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (passvalue) {
                    case 1: {
                        passvalue = 0;
                        image_play.setImageResource(R.drawable.pause); //play condition

                        break;
                    }
                    case 0: {
                        image_play.setImageResource(R.drawable.play);   //pause condition
                        passvalue=1;
                        break;
                    }
                    default :{
                        // pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
    }

    public  void settime()
    {
        TimePickerDialog timePickerDialog=TimePickerDialog.newInstance(HistoryPlay.this,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.setAccentColor(Color.parseColor("#9591bf"));
        timePickerDialog.show(getFragmentManager(),"");
    }

    public void setdata()
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                HistoryPlay.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle("SELECT DATE");
        dpd.setAccentColor(Color.parseColor("#9591bf"));
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "SELECT DATE");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
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
        }
        String b1=s+"-"+e+"-"+year;
        String b=year+"-"+e+"-"+s;
        if(a==1)
        {
            fromdate_text=b;
            fromdate.setText(b1);
        }
        else
        {
            todate.setText(b1);
            todate_text=b;
        }

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second)
    {
        String k=Integer.toString(hourOfDay);
        if (hourOfDay<10){
            k=0+""+k;
        }

        String l=Integer.toString(minute);
        if(minute<=9){
            l=0+""+l;
        }
        String a1= k+":"+l;

        if(a==3)
        {
            totime.setText(a1);
            totime_text=a1;
        }
        else
        {
            fromtime.setText(a1);
            fromtime_text=a1;
        }

    }

    public void servicecall()
    {
        GetSet getSet=(GetSet)getApplicationContext();
        String partnerid=getSet.getPartnerid();
        String timezone=getSet.getTimezone();
        String submitfromdate= StringFormatter.convertToUTF8(fromdate_text+" "+fromtime_text);
        String submittodate=StringFormatter.convertToUTF8(todate_text+" "+totime_text);
        partnerid= StringFormatter.convertToUTF8(partnerid);
        deviceso= StringFormatter.convertToUTF8(deviceso);
        timezone= StringFormatter.convertToUTF8(timezone);
        new VolleyJsonReq(HistoryPlay.this).getjsonObject("http://coreapi.gpsapphub.com/api/HistoryReplay/"+partnerid+"/"+deviceso+"/"+submitfromdate+"/"+submittodate+"/"+timezone+"/2",
                new VolleyJsonReq.MyListener()
                {
                    @Override
                    public void onServercode(int code) {

                    }

                    @Override
                    public void onServerResponse(String response)
                    {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray gpsdata=new JSONArray(jsonObject.getString("gpsData"));
                            if(array_jsonObjects.size()>0)
                            {
                                array_jsonObjects.clear();
                                timer = new Timer(); //T
                                arrayList.clear();

                            }

                            for(int a=0;a<gpsdata.length();a++)
                            {
                                JSONObject jsonObject1 = gpsdata.getJSONObject(a);
                                array_jsonObjects.add(jsonObject1);

                            }
                            t=0;
                            mMap.clear();
                            setmap();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setmap()
    {
        image_play.setVisibility(View.VISIBLE);
        image_play.setImageResource(R.drawable.pause);

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run()
                    {
                        if (t < array_jsonObjects.size()) {

                            if(passvalue==0) {
                                try {
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    t++;
                                    JSONObject jsonObject1 = new JSONObject(array_jsonObjects.get(t).toString());
                                    JSONObject locatio = new JSONObject(jsonObject1.getString("location"));

                                    int m_status = jsonObject1.getInt("vehicleStatus");
                                    double m_heading = locatio.getDouble("heading");
                                    int mapResource = 0;

                                    int heading = Integer.parseInt(String.valueOf((Math.round(m_heading / 45.0)) % 8));
                                    if (m_status == Constants.IN_MOTION) {

                                        mapResource = ArrowVehicleIcon.getgreen(heading);
                                    } else if (m_status == Constants.IDLING) {

                                        mapResource = ArrowVehicleIcon.getyellow(heading);
                                    } else if (m_status == Constants.STOP) {

                                        mapResource = ArrowVehicleIcon.getred(heading);
                                    } else if (m_status == Constants.NOTWORKING) {

                                        mapResource = ArrowVehicleIcon.getred(heading);
                                    } else {
                                    }

                                    double latiitde = locatio.getDouble("latitude");
                                    ;
                                    double longid = locatio.getDouble("longitude");
                                    ;
                                    arrayList.add(new LatLng(latiitde, longid));
                                    if(arrayList.size()==1) {
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(latiitde, longid))
                                                .icon(BitmapDescriptorFactory.fromResource(mapResource)));
                                    }

                                    for (int j = 0; j < arrayList.size(); j++) {
                                        builder.include(arrayList.get(j));
                                    }

                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
                                    mMap.animateCamera(cu);

//                                    PolylineOptions polylineOptions2 = new PolylineOptions();
//                                    polylineOptions2.color(Color.RED);
//                                    polylineOptions2.width(4);
//                                    polylineOptions2.addAll(arrayList);
//                                    polyline = mMap.addPolyline(polylineOptions2);

                                    if(t==array_jsonObjects.size()-1)
                                    {
                                        Toast.makeText(HistoryPlay.this,"Data Loaded",Toast.LENGTH_LONG).show();
                                    }
                                    if(arrayList.size()>0) {
                                        int size=arrayList.size();
                                        double lati=arrayList.get(size-2).latitude;
                                        double longi=arrayList.get(size-2).longitude;
                                        Marker marker1 = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(lati, longi))
                                                .icon(BitmapDescriptorFactory.fromResource(mapResource)));
                                        animateMarker(new LatLng(latiitde, longid), marker1);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else
                        { timer.cancel();
                            mHandler.removeMessages(0);}

                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, speed);

    }
    public  void animateMarker(final LatLng destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            //  final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());
            final LatLng endPosition= destination;
            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(speed); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);

                        marker.setPosition(newPosition);

                        updatePolyLine(newPosition);
                        float bearing =0;
                        marker.setRotation(computeRotation(v, startRotation, bearing));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            valueAnimator.start();
        }
    }
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }
    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }
    private PolylineOptions rectOptions = new PolylineOptions();

    private void updatePolyLine(LatLng latLng)
    {
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(latLng);
        rectOptions.color(Color.RED);
        rectOptions.width(3);
        rectOptions.addAll(points);

        mMap.addPolyline(rectOptions);
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to Mountain
                .zoom(14) // Sets the zoom// Sets the orientation of the camera to east
                .tilt(30) // Sets the tilt of the camera to 30 degrees
                .build();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        mHandler.removeMessages(0);
        super.onPause();
    }

    @Override
    protected void onStop() {
        timer.cancel();
        mHandler.removeMessages(0);
        super.onStop();
    }
}
