package com.gpstrack.syftrack.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gpstrack.syftrack.Model.GetSet;
import com.gpstrack.syftrack.R;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.FragUtilis;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashbOard extends Fragment implements View.OnClickListener
{
    View v;
    FrameLayout dash_frame;
    TextView running,stop,not_working,idle,total;
    RelativeLayout run_rl,s_rl,nw_rl,i_rl,t_rl;

    String partnerid,userid,timezone;
    Context context;
    Runnable runnable;

    int server_code;
    private Handler mHandler=new Handler();

    public DashbOard()
    { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        context=getActivity().getApplicationContext();
        getActivity().setTitle("DashBorad");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_dashb_oard, container, false);
      userid=  new AppUtils(context).getUserName();
       partnerid= new AppUtils(context).getPartnerID();
       timezone= new AppUtils(context).getTimeZone();

        GetSet getSet=(GetSet)getActivity().getApplicationContext();
        getSet.setPartnerid(partnerid);
        getSet.setUserid(userid);
        getSet.setTimezone(timezone);

        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        dash_frame=v.findViewById(R.id.dashboard_frame);

        running=v.findViewById(R.id.running);
        stop=v.findViewById(R.id.stop);
        not_working=v.findViewById(R.id.inactive);
        idle=v.findViewById(R.id.idle);
        total=v.findViewById(R.id.total);

        run_rl=v.findViewById(R.id.running_rel);
        s_rl=v.findViewById(R.id.stop_rel);
        nw_rl=v.findViewById(R.id.inactive_rel);
        i_rl=v.findViewById(R.id.idle_rel);
        t_rl=v.findViewById(R.id.all_rel);

        run_rl.setOnClickListener(this);
        s_rl.setOnClickListener(this);
        nw_rl.setOnClickListener(this);
        i_rl.setOnClickListener(this);
        t_rl.setOnClickListener(this);
        callAPI();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        final String interval = sharedPrefs.getString("refreshtiming", "0");
        if (!(interval.equals("0"))) {
            mHandler.postDelayed( runnable = new Runnable() {
                public void run() {
                    callAPI();

                    mHandler.postDelayed(runnable, Integer.parseInt(interval));
                }
            }, Integer.parseInt(interval));

        }

    }

    @Override
    public void onPause() {
        mHandler.removeMessages(0);
        mHandler.removeCallbacks(runnable);
        Log.e("TAG","OnPause(");
        super.onPause();

    }

    @Override
    public void onStop() {

        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.running_rel)
        {
            if(!running.getText().toString().equalsIgnoreCase("0")) {
                VehicleStatus vehicleStatus = new VehicleStatus();
                FragUtilis.openFrag((AppCompatActivity) getActivity(), R.id.fm, new VehicleStatus("23"), null);
            }
            else{
                Snackbar snackbar1 = Snackbar.make(dash_frame, "No Vehoicle Found", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }
        else if(v.getId()==R.id.stop_rel)
        {
            if(!stop.getText().toString().equalsIgnoreCase("0")) {
                FragUtilis.openFrag((AppCompatActivity) getActivity(), R.id.fm, new VehicleStatus("21"), null);
            }
            else{
                Snackbar snackbar1 = Snackbar.make(dash_frame, "No Vehoicle Found", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }
        else if(v.getId()==R.id.inactive_rel)
        {
            if(!not_working.getText().toString().equalsIgnoreCase("0")) {
                FragUtilis.openFrag((AppCompatActivity) getActivity(), R.id.fm, new VehicleStatus("24"), null);
            }
            else{
                Snackbar snackbar1 = Snackbar.make(dash_frame, "No Vehoicle Found", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }
        else  if(v.getId()==R.id.idle_rel)
        {
            if(!idle.getText().toString().equalsIgnoreCase("0")) {
                FragUtilis.openFrag((AppCompatActivity) getActivity(), R.id.fm, new VehicleStatus("22"), null);
            }
            else{
                Snackbar snackbar1 = Snackbar.make(dash_frame, "No Vehoicle Found", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }
        else if(v.getId()==R.id.all_rel)
        {
            if(!total.getText().toString().equalsIgnoreCase("0")) {
                FragUtilis.openFrag((AppCompatActivity) getActivity(), R.id.fm, new VehicleStatus("25"), null);
            }
            else{
                Snackbar snackbar1 = Snackbar.make(dash_frame, "No Vehoicle Found", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }

    }

    private void callAPI()
    {
        partnerid= StringFormatter.convertToUTF8(partnerid);
        userid= StringFormatter.convertToUTF8(userid);
        timezone= StringFormatter.convertToUTF8(timezone);
        String url="http://coreapi.gpsapphub.com/api/Dashboard/"+partnerid+"/"+userid+"/"+timezone;
        new VolleyJsonReq(getActivity()).getjson(url,
                new VolleyJsonReq.MyListener() {
                    @Override
                    public void onServercode(int code) {
                        server_code = code;
                    }

                    @Override
                    public void onServerResponse(String response)
                    {
                        if (server_code == 200) {
                            int r_count = 0, i_count = 0, nt_count = 0, s_count = 0;
                            GetSet getSet = (GetSet)getActivity().getApplicationContext();
                            getSet.setJson_response(response);
                            //  server_response=response;
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject js = jsonArray.getJSONObject(i);
                                    int status = js.getInt("status");
                                    if (status == 21) {
                                        s_count = s_count + 1;
                                    } else if (status == 22) {
                                        i_count = i_count + 1;
                                    } else if (status == 23) {
                                        r_count = r_count + 1;
                                    } else if (status == 24) {
                                        nt_count = nt_count + 1;
                                    }


                                }
                                running.setText(String.valueOf(r_count));
                                idle.setText(String.valueOf(i_count));
                                stop.setText(String.valueOf(s_count));
                                not_working.setText(String.valueOf(nt_count));
                                total.setText(String.valueOf(s_count + i_count + r_count + nt_count));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(dash_frame, response, Snackbar.LENGTH_LONG);
                            snackbar1.show();
                            GetSet getSet = (GetSet)context;
                            getSet.setJson_response(response);
                        }
                    }

                });
    }
}
