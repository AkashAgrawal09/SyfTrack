package com.gpstrack.syftrack.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gpstrack.syftrack.Adapter.VehicleStatusAdapter;
import com.gpstrack.syftrack.Model.GetSet;
import com.gpstrack.syftrack.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleStatus extends Fragment implements TextWatcher {
    public static int value;
    RecyclerView rc;
    String code,server_response;
    EditText searchview;
Context context;
    VehicleStatusAdapter vehicleStatusAdapter;
    View v;

ArrayList<JSONObject> jsonObjects=new ArrayList<JSONObject>();

    public VehicleStatus() {
        // Required empty public constructor
    }
    public VehicleStatus(String code) {
        this.code = code;
        value= Integer.parseInt(code);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        getActivity().setTitle("Vehicle Status");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         v=inflater.inflate(R.layout.fragment_vehicle_status, container, false);
        GetSet getSet=(GetSet)context;
        server_response=getSet.getJson_response();
        if(server_response!=null) {
            try {
                JSONArray jsonArray = new JSONArray(server_response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    jsonObjects.add(jsonObject);
                }

            } catch (Exception e) {
            }

            rc = v.findViewById(R.id.rc);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            rc.setLayoutManager(linearLayoutManager);
          vehicleStatusAdapter   = new VehicleStatusAdapter(context, jsonObjects, code);
            rc.setAdapter(vehicleStatusAdapter);
        }
        else{
            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        searchview=v.findViewById(R.id.searchview);

        searchview.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        ArrayList<JSONObject> jsonObjectnew=new ArrayList<JSONObject>();

        try {
            JSONArray jsonArray = new JSONArray(server_response);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int status=jsonObject.getInt("status");
                if(status==VehicleStatus.value)
                {
                    JSONObject inner=new JSONObject(jsonObject.getString("device"));
                    if(inner.getString("vehicleNumber").toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        jsonObjectnew.add(jsonObject);
                    }
                }
                else if(25==VehicleStatus.value)
                {
                    JSONObject inner=new JSONObject(jsonObject.getString("device"));
                    if(inner.getString("vehicleNumber").toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        jsonObjectnew.add(jsonObject);
                    }
                }

            }

            vehicleStatusAdapter.filteritem(jsonObjectnew,code);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }


}
