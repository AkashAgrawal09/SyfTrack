package com.gpstrack.syftrack.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpstrack.syftrack.DashBoard;
import com.gpstrack.syftrack.Fragment.VehicleStatus;
import com.gpstrack.syftrack.MapActivity;
import com.gpstrack.syftrack.MapActivytNew;
import com.gpstrack.syftrack.R;
import com.gpstrack.syftrack.Utils.Constants;
import com.gpstrack.syftrack.Utils.Online;

import org.json.JSONObject;

import java.util.ArrayList;

public class VehicleStatusAdapter extends RecyclerView.Adapter<VehicleStatusAdapter.ViewHolder>
{
    private Context context;
    ArrayList<JSONObject> server_response=new ArrayList<>();
    String code;

    public VehicleStatusAdapter(Context context, ArrayList<JSONObject> server_response, String code) {
        this.server_response.clear();
        for (int i = 0; i < server_response.size(); i++)
        {
                try {
                    JSONObject jsonObject=new JSONObject(String.valueOf(server_response.get(i)));
                    int status=jsonObject.getInt("status");
                    if(status== VehicleStatus.value)
                    {
                        this.server_response.add(server_response.get(i));
                    }
                    else if(25==VehicleStatus.value)
                    {
                        this.server_response.add(server_response.get(i));
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

        }
        this.code=code;
        this.context = context;
        notifyDataSetChanged();

    }
    @Override
    public VehicleStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.vehiclestatusadapter,parent,false);
        return new ViewHolder(v,context,server_response,code);
    }

    @Override
    public void onBindViewHolder(VehicleStatusAdapter.ViewHolder holder, int position)
    {
        try{
            JSONObject jsonObject=new JSONObject(String.valueOf(server_response.get(position)));
            JSONObject inner=new JSONObject(jsonObject.getString("device"));
            JSONObject lastlocation=new JSONObject(jsonObject.getString("lastLocation"));
            JSONObject vehicletype=new JSONObject(inner.getString("vehicleType"));
            JSONObject icon_staus=new JSONObject(jsonObject.getString("vehicleStatus"));

            String lastupdate;
            int m_vehicle_type=vehicletype.getInt("vehicleTypeID");
            int status=jsonObject.getInt("status");
            int power=icon_staus.getInt("power");
            int acenable=icon_staus.getInt("acEnable");
            int fuelenable=icon_staus.getInt("fuelEnable");
            int ignitionenable=icon_staus.getInt("ignitionEnable");
            String speed=lastlocation.getString("speed");
            lastupdate=lastlocation.getString("lastStatusDuration");
            String Current_time=lastlocation.getString("timestamp").replace("T"," ");

            holder.vehicle_number.setText(inner.getString("vehicleNumber"));
            holder.dateTime.setText(Current_time);
            holder.speed_text.setText(speed+" Km/Hr");

            if(power==1|| power==-1)
            {
                holder.battery.setImageResource(R.drawable.batterygreen);
            }
            else
            {
                holder.battery.setImageResource(R.drawable.batteryred);
            }


            if(acenable==0)
            {
                holder.ac.setImageResource(R.drawable.fan_grey);
            }
            else
            {
                if(icon_staus.getInt("ac")==1)
                {
                    holder.ac.setImageResource(R.drawable.fan_green);
                }
                else
                {
                  holder.ac.setImageResource(R.drawable.fan_red);
                }
            }

            if(fuelenable==0)
            {
                holder.fuel.setImageResource(R.drawable.fuel_grey);
            }
            else
            {
                if(icon_staus.getInt("fuel")==0)
                {
                    holder.fuel.setImageResource(R.drawable.fuel_green);
                }
//                else
//                {
//                    holder.fuel.setImageResource(R.drawable.fuel_red);
//                }
            }

            if(ignitionenable==0)
            {
                holder.ignition.setImageResource(R.drawable.key_grey);
            }
            else
            {
                if(icon_staus.getInt("ignition")==1)
                {
                    holder.ignition.setImageResource(R.drawable.key_green);
                }
                else
                {
                    holder.ignition.setImageResource(R.drawable.key_red);
                }
            }



            if(m_vehicle_type == 1){
                if (status== Constants.IN_MOTION) {
                    holder.laststring.setText("Running from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bus_running);
                } else if (status == Constants.IDLING) {
                    holder.laststring.setText("Idle from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bus_idle);
                } else if (status == Constants.STOP) {
                    holder.laststring.setText("Stop from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bus_stop);
                } else if (status == Constants.NOTWORKING) {
                    holder.laststring.setText("In Active from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bus_inactive);
                }
            }
            else if(m_vehicle_type == 2){
                if (status == Constants.IN_MOTION) {
                    holder.laststring.setText("Running from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.truck_running);
                } else if (status== Constants.IDLING) {
                    holder.laststring.setText("Idle from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.truck_idle);
                } else if (status == Constants.STOP) {
                    holder.laststring.setText("Stop from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.truck_stop);
                } else if (status == Constants.NOTWORKING) {
                    holder.laststring.setText("In Active from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.truck_inactive);
                }
            }
            else if(m_vehicle_type == 4){
                if (status == Constants.IN_MOTION) {
                    holder.laststring.setText("Running from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bike_running);
                } else if (status == Constants.IDLING) {
                    holder.laststring.setText("Idle from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bike_idle);
                } else if (status == Constants.STOP) {
                    holder.laststring.setText("Stop from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bike_stop);
                } else if (status == Constants.NOTWORKING) {
                    holder.laststring.setText("In Active from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.bike_inactive);
                }
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
                if (status == Constants.IN_MOTION) {
                    holder.laststring.setText("Running from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.poplane_running);
                } else if (status== Constants.IDLING) {
                    holder.laststring.setText("Idle from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.poplne_idle);
                } else if (status == Constants.STOP) {
                    holder.laststring.setText("Stop from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.poplane_stop);
                } else if (status == Constants.NOTWORKING) {
                    holder.laststring.setText("In Active from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.poplane_inactive);
                }
            }
            else if(m_vehicle_type == 9){
                if (status== Constants.IN_MOTION) {
                    holder.laststring.setText("Running from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.ambulance_running);
                } else if (status== Constants.IDLING) {
                    holder.laststring.setText("Idle from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.ambulance_idle);
                } else if (status == Constants.STOP) {
                    holder.laststring.setText("Stop from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.ambulance_stop);
                } else if (status == Constants.NOTWORKING) {
                    holder.laststring.setText("In Active from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.ambulance_inactive);
                }
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
                if (status == Constants.IN_MOTION) {
                    holder.laststring.setText("Running from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.car_running);
                } else if (status == Constants.IDLING) {
                    holder.laststring.setText("Idle from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.car_idle);
                } else if (status== Constants.STOP) {
                    holder.laststring.setText("Stop from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.car_stop);
                } else if (status== Constants.NOTWORKING) {
                    holder.laststring.setText("In Active from "+lastupdate);
                    holder.device_image.setImageResource(R.drawable.car_inactive);
                }
            }


        }catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return server_response.size();
    }

    public void filteritem(ArrayList<JSONObject> jsonObjectnew, String code)
    {
        server_response=jsonObjectnew;
        this.code=code;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        public LinearLayout rel;
        Context context;
        ImageView device_image,ac,battery,ignition,fuel;
        TextView vehicle_number,dateTime,laststring,speed_text;
        ArrayList<JSONObject> recyclegetsets=new ArrayList<JSONObject>();

        public ViewHolder(View v, final Context context, final ArrayList<JSONObject> recyclegetsets, String code)
        {
            super(v);
            this.context=context;
            this.recyclegetsets=recyclegetsets;

            rel=v.findViewById(R.id.ll);
            device_image=v.findViewById(R.id.vehicleimage);
            ac=v.findViewById(R.id.icon_ac);
            fuel=v.findViewById(R.id.icon_fuel);
            battery=v.findViewById(R.id.icon_battery);
            ignition=v.findViewById(R.id.icon_ignition);
            vehicle_number=v.findViewById(R.id.vehicle_number);
            dateTime=v.findViewById(R.id.dateView);
            laststring=v.findViewById(R.id.laststring);
            speed_text=v.findViewById(R.id.speed_text);

            rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Context context1 = v.getContext();
                    int position=getAdapterPosition();
                    if(Online.isOnline(context)) {
                        Intent intent = new Intent(context, MapActivytNew.class);
                        intent.putExtra("json", String.valueOf(recyclegetsets.get(position)));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
