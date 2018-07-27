package com.gpstrack.syftrack;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.Constants;
import com.gpstrack.syftrack.Utils.Online;
import com.gpstrack.syftrack.Utils.Tracker;
import com.gpstrack.syftrack.WebService.TravelSummaryAdapterVolley;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class NearstVehicle extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveStartedListener
{
    private GoogleMap mMap;
    Context mContext = this;

    SeekBar seekBar;
    int progressChangedValue = 500;
    TextView radius;
    Circle circle;
    LinearLayout heading;
    int server_code;
    ImageView search;

    Tracker gps;
    String newlat,newlong;
    private Handler mHandler = new Handler();
    Double lat=0.0,lon=0.0;


    Marker marker;

    final Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearst_vehicle);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        seekBar=(SeekBar)findViewById(R.id.simpleSeekBar);
        radius=findViewById(R.id.raduis);
        heading=findViewById(R.id.heading);
        search=findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setClickable(false);
                Toast.makeText(NearstVehicle.this, "Please wait...", Toast.LENGTH_SHORT).show();
                callapi();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);


        TimerTask doAsynchronousTask = new TimerTask()
        {
            @Override
            public void run()
            {
                mHandler.post(new Runnable()
                {
                    public void run()
                    {
                        if(Online.isOnline(NearstVehicle.this))
                        {
                            gps = new Tracker(NearstVehicle.this);
                            if(lat.intValue()==0) {
                                if (gps.canGetLocation()) {
                                    gps = new Tracker(NearstVehicle.this);
                                    lat = gps.getLatitude();
                                    lon = gps.getLongitude();

                                    timer.cancel();
                                    mHandler.removeMessages(0);
                                    intilizemap();
                                } else {
                                    gps.showSettingsAlert();
                                }
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    public void intilizemap()
    {


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = 500;
                progressChangedValue = progressChangedValue + progress;
                newdata();
                radius.setText(String.valueOf(progressChangedValue));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        LatLng current_location = new LatLng(lat, lon);
        newlat= String.valueOf(current_location.latitude);
        newlong= String.valueOf(current_location.longitude);

        mMap.clear();
        Marker marker = mMap.addMarker(new MarkerOptions().position(current_location).title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));
        marker.showInfoWindow();
        circle = mMap.addCircle(new CircleOptions()
                .center(current_location)
                .radius(progressChangedValue)
                .strokeWidth(4)
                .strokeColor(Color.RED));

        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current_location) // Sets the center of the map to Mountain
                .zoom(15) // Sets the zoom// Sets the orientation of the camera to east
                .tilt(30) // Sets the tilt of the camera to 30 degrees
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        mMap.setOnCameraMoveStartedListener(this);
        // }
//                }


//        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition)
//            {
//                mMap.clear();
//
//                  Marker marker = mMap.addMarker(new MarkerOptions().position(cameraPosition.target).title("Marker")
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
//                    marker.showInfoWindow();
//
//                CameraPosition cameraPosition1 = new CameraPosition.Builder()
//                        .target(cameraPosition.target) // Sets the center of the map to Mountain
//                        .zoom(15) // Sets the zoom// Sets the orientation of the camera to east
//                        .tilt(30) // Sets the tilt of the camera to 30 degrees
//                        .build();
//                mMap.animateCamera(CameraUpdateFactory
//                        .newCameraPosition(cameraPosition1));
//
//
//                circle = mMap.addCircle(new CircleOptions()
//                        .center(cameraPosition.target)
//                        .radius(progressChangedValue)
//                        .strokeColor(Color.RED));
//

//        }
    }


    @Override
    public void onCameraMoveStarted(int i)
    {
        newdata();
    }


    public void newdata()
    {
        LatLng mPosition = mMap.getCameraPosition().target;
        newlat =String.valueOf(mPosition.latitude);
        newlong=String.valueOf(mPosition.longitude);
        mMap.clear();

        Marker marker = mMap.addMarker(new MarkerOptions().position(mPosition).title("New Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));
        marker.showInfoWindow();

        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                .target(mPosition) // Sets the center of the map to Mountain
                .zoom(15) // Sets the zoom// Sets the orientation of the camera to east
                .tilt(30) // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition1));


        circle = mMap.addCircle(new CircleOptions()
                .center(mPosition)
                .radius(progressChangedValue)
                .strokeWidth(4)
                .strokeColor(Color.RED));

    }


    public void callapi()
    {
        double diatnce;
        diatnce   = progressChangedValue / 1000.0;

        String userid=  new AppUtils(NearstVehicle.this).getUserName();
        String partnerid= new AppUtils(NearstVehicle.this).getPartnerID();
        String url="http://coreapi.gpsapphub.com/api/Util/"+partnerid+"/"+userid+"/"+newlat+"/"+newlong+"/"+diatnce;
        new TravelSummaryAdapterVolley(NearstVehicle.this).getjsonarray(url,
                new VolleyJsonReq.MyListener() {
                    @Override
                    public void onServercode(int code) {
                        server_code = code;
                    }

                    @Override
                    public void onServerResponse(String response)
                    {
                        if (server_code == 200)
                        {
                            try
                            {
                                iniliseMapapi(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(heading, response, Snackbar.LENGTH_LONG);
                            snackbar1.show();
                        }
                    }

                });

    }



    public void iniliseMapapi(String jsonArray)
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
                LatLng current_location = new LatLng(Double.parseDouble(newlat), Double.parseDouble(newlong));
                circle = mMap.addCircle(new CircleOptions()
                        .center(current_location)
                        .radius(progressChangedValue)
                        .strokeWidth(4)
                        .strokeColor(Color.RED));
                try {
                    JSONArray jsonArray1=new JSONArray(jsonArray);
                    for(int q=0;q<jsonArray1.length();q++)
                    {
                        JSONObject jsonObject=jsonArray1.getJSONObject(q);

                        String vehicle_nub=jsonObject.getString("veh");
                        Double latiitde=jsonObject.getDouble("lat");
                        Double longtitude=jsonObject.getDouble("lng");
                        int intentStatus=jsonObject.getInt("status");

                        int mapResource=0;

                        if (intentStatus == Constants.IN_MOTION) {
                            mapResource=R.drawable.green_0;
                        } else if (intentStatus == Constants.IDLING) {
                            mapResource=R.drawable.yellow_0;
                        } else if (intentStatus == Constants.STOP) {

                            mapResource=R.drawable.red;
                        } else if (intentStatus == Constants.NOTWORKING) {
                            mapResource=R.drawable.blue_0;
                        }

                        LatLng markerLoc = new LatLng(latiitde, longtitude);

//                        final CameraPosition cameraPosition = new CameraPosition.Builder()
//                                .target(markerLoc) // Sets the center of the map to Mountain
//                                .zoom(13) // Sets the zoom// Sets the orientation of the camera to east
//                                .tilt(30) // Sets the tilt of the camera to 30 degrees
//                                .build();

//                        TextView text = new TextView(NearstVehicle.this);
//                        text.setText("Your text here");
//                        IconGenerator generator = new IconGenerator(this);
//                        generator.setBackground(getDrawable(R.drawable.man));
//                        generator.setContentView(text);
//                        Bitmap icon = generator.makeIcon();
//
//                        MarkerOptions tp = new MarkerOptions().position(markerLoc).icon(BitmapDescriptorFactory.fromBitmap(icon));
//                        mMap.addMarker(tp);

                       Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(
                                latiitde, longtitude))
                                .title(vehicle_nub)
                                .icon(BitmapDescriptorFactory.fromResource(mapResource)));
                        marker.showInfoWindow();
//                     //   marker.setVisible(true);
                        if(q==jsonArray1.length()-1)
                        {
                            search.setClickable(true);
                        }
//                        mMap.animateCamera(CameraUpdateFactory
//                                .newCameraPosition(cameraPosition));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        mHandler.removeMessages(0);
    }
}
