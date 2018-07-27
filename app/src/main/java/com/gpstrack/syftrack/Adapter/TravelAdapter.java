package com.gpstrack.syftrack.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gpstrack.syftrack.Model.TravelSummaryModel;
import com.gpstrack.syftrack.R;

import java.util.ArrayList;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.ViewHolder >
{
    private Context context;
    ArrayList<TravelSummaryModel> server_response=new ArrayList<>();

    public TravelAdapter(Context context, ArrayList<TravelSummaryModel> server_response)
    {
        this.context=context;
        this.server_response=server_response;
    }
    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.traveladapter,parent,false);
        return new TravelAdapter.ViewHolder(v,context,server_response);
    }

    @Override
    public void onBindViewHolder(TravelAdapter.ViewHolder holder, int position)
    {
        try{

            holder.vehicle_number.setText(server_response.get(position).getVehicleNo());

            int m_vehicle_type=server_response.get(position).getVehciletype();
            if(m_vehicle_type == 1){
               holder.device_image.setImageResource(R.drawable.bus_stop);
            }
            else if(m_vehicle_type == 2){
                    holder.device_image.setImageResource(R.drawable.truck_stop);
            }
            else if(m_vehicle_type == 4){
                    holder.device_image.setImageResource(R.drawable.bike_stop);

            }
//            else if(m_vehicle_type == 5){
//                if (status == Constants.IN_MOTION) {
//                    holder.device_image.setImageResource(R.drawable.greenbigman);
//                } else if (status == Constants.IDLING) {
//                    holder.device_image.setImageResource(R.drawable.yellowbigman);
//                } else if (status == Constants.STOP) {
//                    holder.device_image.setImageResource(R.drawable.redbigman);
//                } else if (status == Constants.NOTWORKING) {
//                    holder.device_image.setImageResource(R.drawable.bluebigman);
//                }
//            }
//            else if(m_vehicle_type == 6){
//                if (status == Constants.IN_MOTION) {
//                    holder.device_image.setImageResource(R.drawable.green_erish);
//                } else if (status == Constants.IDLING) {
//                    holder.device_image.setImageResource(R.drawable.yellow_erish);
//                } else if (status== Constants.STOP) {
//                    holder.device_image.setImageResource(R.drawable.red_erish);
//                } else if (status == Constants.NOTWORKING) {
//                    holder.device_image.setImageResource(R.drawable.blue_erish);
//                }
//            }
//            else if(m_vehicle_type == 7){
//                if (status == Constants.IN_MOTION) {
//                    holder.device_image.setImageResource(R.drawable.green_arrow);
//                } else if (status== Constants.IDLING) {
//                    holder.device_image.setImageResource(R.drawable.yellow_arrow);
//                } else if (status== Constants.STOP) {
//                    holder.device_image.setImageResource(R.drawable.red_arrow);
//                } else if (status == Constants.NOTWORKING) {
//                    holder.device_image.setImageResource(R.drawable.blue_arrow);
//                }
//            }
            else if(m_vehicle_type == 8){
                    holder.device_image.setImageResource(R.drawable.poplane_stop);
            }
            else if(m_vehicle_type == 9){
                    holder.device_image.setImageResource(R.drawable.ambulance_stop);

            }
//            else if(m_vehicle_type == 10){
//                if (status == Constants.IN_MOTION) {
//                    holder.device_image.setImageResource(R.drawable.green_tractor);
//                } else if (status == Constants.IDLING) {
//                    holder.device_image.setImageResource(R.drawable.yellow_tractor);
//                } else if (status == Constants.STOP) {
//                    holder.device_image.setImageResource(R.drawable.red_tractor);
//                } else if (status== Constants.NOTWORKING) {
//                    holder.device_image.setImageResource(R.drawable.blue_tractor);
//                }
//            }
//            else if(m_vehicle_type == 11){
//                if (status == Constants.IN_MOTION) {
//                    holder.device_image.setImageResource(R.drawable.green_jcb);
//                } else if (status == Constants.IDLING) {
//                    holder.device_image.setImageResource(R.drawable.yellow_jcb);
//                } else if (status== Constants.STOP) {
//                    holder.device_image.setImageResource(R.drawable.red_jcb);
//                } else if (status == Constants.NOTWORKING) {
//                    holder.device_image.setImageResource(R.drawable.blue_jcb);
//                }
//            }
            else{
                    holder.device_image.setImageResource(R.drawable.car_stop);
            }

            if(server_response.get(position).getValue()==1)
            {
               holder.progress_idle.setVisibility(View.GONE);
                holder.idle.setVisibility(View.VISIBLE);
                holder.idle.setText(server_response.get(position).getIdle());

                holder.progres_stop.setVisibility(View.GONE);
                holder.stop.setVisibility(View.VISIBLE);
                holder.stop.setText(server_response.get(position).getStop());

                holder.progress_dist.setVisibility(View.GONE);
                holder.km.setVisibility(View.VISIBLE);
                holder.km.setText(server_response.get(position).getDistnace());


                holder.progress_as.setVisibility(View.GONE);
                holder.avgspeed.setVisibility(View.VISIBLE);
                holder.avgspeed.setText(server_response.get(position).getAvgspeed());

                holder.progress_maxs.setVisibility(View.GONE);
                holder.maxspeed.setVisibility(View.VISIBLE);
                holder.maxspeed.setText(server_response.get(position).getMaxspeed());
            }

        }catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return server_response.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        Context context;
        ImageView device_image;
        TextView vehicle_number,km,avgspeed,maxspeed,idle,stop;
        ProgressBar progress_idle,progres_stop,progress_dist,progress_maxs,progress_as;
        ArrayList<TravelSummaryModel> recyclegetsets=new ArrayList<>();

        public ViewHolder(View v, final Context context, final ArrayList<TravelSummaryModel> recyclegetsets)
        {
            super(v);
            this.context=context;
            this.recyclegetsets=recyclegetsets;

               device_image=v.findViewById(R.id.image);
            vehicle_number=v.findViewById(R.id.vehicle_number);
            progress_idle=v.findViewById(R.id.progressidle);
            progres_stop=v.findViewById(R.id.progressstop);
            progress_dist=v.findViewById(R.id.progresskm);
            progress_maxs=v.findViewById(R.id.progressas);
            progress_as=v.findViewById(R.id.progressms);

            km=v.findViewById(R.id.km);
            avgspeed=v.findViewById(R.id.avgspeed);
            maxspeed=v.findViewById(R.id.maxspeed);
            idle=v.findViewById(R.id.idle);
            stop=v.findViewById(R.id.stop);

        }

    }
}
