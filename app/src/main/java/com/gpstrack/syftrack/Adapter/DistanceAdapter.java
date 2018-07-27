package com.gpstrack.syftrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.gpstrack.syftrack.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.ViewHolder >
{
    private Context context;
    ArrayList<JSONObject> server_response=new ArrayList<>();
    String code;

    public DistanceAdapter(Context context, ArrayList<JSONObject> server_response)
    {
        this.context=context;
        this.server_response=server_response;
    }
    @Override
    public DistanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.distanceapater,parent,false);
        return new DistanceAdapter.ViewHolder(v,context,server_response);
    }

    @Override
    public void onBindViewHolder(DistanceAdapter.ViewHolder holder, int position)
    {
        try{
            JSONObject jsonObject=new JSONObject(String.valueOf(server_response.get(position)));
            holder.vehicle_number.setText(jsonObject.getString("vehicleNo"));
            holder.km.setText(jsonObject.getString("distance")+" Km");


        }catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return server_response.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {


        Context context;
       // ImageView device_image;
        TextView vehicle_number,km;
        ArrayList<JSONObject> recyclegetsets=new ArrayList<JSONObject>();

        public ViewHolder(View v, final Context context, final ArrayList<JSONObject> recyclegetsets)
        {
            super(v);
            this.context=context;
            this.recyclegetsets=recyclegetsets;

         //   device_image=v.findViewById(R.id.device_image);
            vehicle_number=v.findViewById(R.id.device_number);
            km=v.findViewById(R.id.km);



        }

    }
}
