package com.gpstrack.syftrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gpstrack.syftrack.Model.TravelSummaryModel;
import com.gpstrack.syftrack.R;

import java.util.ArrayList;


public class Reportmap_adapter extends RecyclerView.Adapter<Reportmap_adapter.ViewHolder >
{
    ArrayList<TravelSummaryModel> server_response;
    Context context;
    public Reportmap_adapter(Context context, ArrayList<TravelSummaryModel> server_response)
    {
        this.context=context;
        this.server_response=server_response;
    }
    @Override
    public Reportmap_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.reportmap_adapter,parent,false);
        return new Reportmap_adapter.ViewHolder(v,context,server_response);
    }

    @Override
    public void onBindViewHolder(Reportmap_adapter.ViewHolder holder, int position)
    {
        try{

            holder.idletime.setText(server_response.get(position).getIdle());
            holder.distance.setText(server_response.get(position).getDistnace());
            holder.avgspeed.setText(server_response.get(position).getAvgspeed());
            holder.topspeed.setText(server_response.get(position).getMaxspeed());


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
        TextView idletime,topspeed,avgspeed,distance;
        ArrayList<TravelSummaryModel> recyclegetsets=new ArrayList<>();

        public ViewHolder(View v, final Context context, final ArrayList<TravelSummaryModel> recyclegetsets)
        {
            super(v);
            this.context=context;
            this.recyclegetsets=recyclegetsets;

            idletime=v.findViewById(R.id.idletime);
            topspeed=v.findViewById(R.id.topspeed);
            avgspeed=v.findViewById(R.id.avgspeed);
            distance=v.findViewById(R.id.distnace);


        }

    }
}
