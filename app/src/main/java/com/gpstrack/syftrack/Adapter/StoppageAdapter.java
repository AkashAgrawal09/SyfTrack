package com.gpstrack.syftrack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gpstrack.syftrack.Model.StoppageSummaryModel;
import com.gpstrack.syftrack.R;
import com.gpstrack.syftrack.StoppageMap;

import java.util.ArrayList;

public class StoppageAdapter extends RecyclerView.Adapter<StoppageAdapter.ViewHolder >
{

    private Context context;
    ArrayList<StoppageSummaryModel> server_response=new ArrayList<>();
    int value=0;

    public StoppageAdapter(Context context, ArrayList<StoppageSummaryModel> server_response)
    {
        this.context=context;
        this.server_response=server_response;
    }
    @Override
    public StoppageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.stoppageadapteer,parent,false);
        return new StoppageAdapter.ViewHolder(v,context,server_response,value);
    }

    @Override
    public void onBindViewHolder(StoppageAdapter.ViewHolder holder, int position)
    {
        try{

            holder.vehicle_number.setText(server_response.get(position).getVehicleNo());

            int m_vehicle_type=server_response.get(position).getVehicletype();
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
                value=server_response.get(position).getLastvalue();
                holder.progressastotalstop.setVisibility(View.GONE);
                holder.totalstop.setVisibility(View.VISIBLE);
                holder.totalstop.setText(server_response.get(position).getTotalstops());
            }

        }catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return server_response.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView device_image;
        TextView vehicle_number, totalstop;
        ProgressBar progressastotalstop;
        LinearLayout ll;
        int value=0;

        ArrayList<StoppageSummaryModel> recyclegetsets = new ArrayList<>();

        public ViewHolder(View v, final Context context, final ArrayList<StoppageSummaryModel> recyclegetsets, final int value) {
            super(v);
            this.context = context;
            this.value=value;
            this.recyclegetsets = recyclegetsets;

            device_image = v.findViewById(R.id.image);
            vehicle_number = v.findViewById(R.id.vehicle_number);
            progressastotalstop = v.findViewById(R.id.progressastotalstop);
            totalstop = v.findViewById(R.id.tts);
            ll = v.findViewById(R.id.ll);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    Context context1 = context;
                    int position = getAdapterPosition();
                    int sise=recyclegetsets.size()-1;
                        if (!(recyclegetsets.get(position).getJsonarray() =="0") && recyclegetsets.get(sise).getLastvalue()==1 ) {
                            Intent intent = new Intent(context, StoppageMap.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("vehicle_number",recyclegetsets.get(position).getVehicleNo());
                            intent.putExtra("json", recyclegetsets.get(position).getJsonarray());
                            context.startActivity(intent);


                        } else {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_LONG).show();
                        }

                }
            });

        }
    }
}
