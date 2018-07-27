package com.gpstrack.syftrack;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoppageMap extends FragmentActivity implements OnMapReadyCallback,AdapterView.OnItemClickListener {

    private GoogleMap mMap;
    ListView lv;
    ArrayList<JSONObject> jsonObjects=new ArrayList<>();

    String JSon,vehicle_number;

    TextView vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoppage_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lv=findViewById(R.id.lv);
        vehicle=findViewById(R.id.vehicle_number);

        Intent i=getIntent();
        JSon=i.getStringExtra("json");
        vehicle_number=i.getStringExtra("vehicle_number");
        vehicle.setText(vehicle_number);

        try{
            JSONArray jsonArray=new JSONArray(JSon);
            for(int q=0;q<jsonArray.length();q++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(q);
                jsonObjects.add(jsonObject);
            }
        }
        catch (Exception E)
        {

        }

        lv.setOnItemClickListener(this);

        ListAdapter adapter = new ListAdapter(StoppageMap.this, jsonObjects);
        lv.setAdapter(adapter);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);

        try{
            JSONObject jsonObject=new JSONObject(jsonObjects.get(0).toString());
            double latitude=jsonObject.getDouble("latitude");
            double longtitude=jsonObject.getDouble("longitude");
            iniliseMap(latitude,longtitude);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        try{
            JSONObject jsonObject=new JSONObject(jsonObjects.get(position).toString());
            double latitude=jsonObject.getDouble("latitude");
            double longtitude=jsonObject.getDouble("longitude");
            iniliseMap(latitude,longtitude);
        }catch (Exception e)
        {

        }
    }

    private void iniliseMap(double laitde,double longtde)
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
                LatLng markerLoc = new LatLng(laitde, longtde);
                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(markerLoc) // Sets the center of the map to Mountain
                        .zoom(13) // Sets the zoom// Sets the orientation of the camera to east
                        .tilt(30) // Sets the tilt of the camera to 30 degrees
                        .build();

                mMap.addMarker(new MarkerOptions().position(new LatLng(
                        laitde, longtde)).icon(BitmapDescriptorFactory.fromResource(R.drawable.red)));
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

            }
        }
    }


    public class ListAdapter extends BaseAdapter
    {
        Context mContext;
        ArrayList<JSONObject> jsonObjects=new ArrayList<>();
        public ListAdapter(Context c, ArrayList<JSONObject> items)
        {
            mContext = c;
            this.jsonObjects=items;
        }

        @Override
        public int getCount() {
            return jsonObjects.size();
        }

        @Override
        public Object getItem(int position) {
            return jsonObjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=LayoutInflater.from(mContext);
            convertView=inflater.inflate(R.layout.stoppagemap_adapter,null);

            TextView stopfrom=convertView.findViewById(R.id.starttime);
            TextView stopto=convertView.findViewById(R.id.endtime);
            TextView duration=convertView.findViewById(R.id.duration);

            try{
                JSONObject jsonObject=new JSONObject(jsonObjects.get(position).toString());
                String time = jsonObject.getString("startTime").replace("T", " ");
                stopfrom.setText(time);
                String time1 = jsonObject.getString("endTime").replace("T", " ");
                stopto.setText(time1);
                duration.setText(jsonObject.getString("duration"));

            }catch(Exception e)
            {

            }
            return convertView;

        }

    }

}
