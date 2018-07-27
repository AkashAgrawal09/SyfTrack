package com.gpstrack.syftrack.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpstrack.syftrack.Adapter.TravelAdapter;
import com.gpstrack.syftrack.DashBoard;
import com.gpstrack.syftrack.Model.TravelSummaryModel;
import com.gpstrack.syftrack.R;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.Online;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.TravelSummaryAdapterVolley;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelSummary extends Fragment implements View.OnClickListener
{
    View v;
    Context context;
    LinearLayout fromll,toll;
    RecyclerView recyclerView;

    TextView fromdate,fromtime,todate,totime;
    Button apply;

    String partnerid,userid,timezone;
    FrameLayout heading;
    int server_code;
    ArrayList<TravelSummaryModel> tsmaaray=new ArrayList<>();
    ArrayList<String> vehicle_number=new ArrayList<>();
    ArrayList<Integer> vehicle_type=new ArrayList<>();

    ArrayList<Integer> deviceSno=new ArrayList<>();

    TravelAdapter Adapter;

    int i=0;
    int a=0,apply_disable=0;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String submit_fromdate,submit_todate;

    public TravelSummary()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context=getActivity().getApplicationContext();
        getActivity().setTitle("Travel Summary");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        fromll=v.findViewById(R.id.fromll);
        toll=v.findViewById(R.id.toll);
       apply=v.findViewById(R.id.apply);
        recyclerView=v.findViewById(R.id.rc);
        fromdate=v.findViewById(R.id.fromdate);
        todate=v.findViewById(R.id.todate);

        heading=v.findViewById(R.id.heading);

        toll.setOnClickListener(this);
        fromll.setOnClickListener(this);
        apply.setOnClickListener(this);

        userid=  new AppUtils(context).getUserName();
        partnerid= new AppUtils(context).getPartnerID();
        timezone= new AppUtils(context).getTimeZone();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        String e= Integer.toString(monthOfYear);
        if (monthOfYear<10){
            int a=1+monthOfYear;
            e="0"+a;
        }
        else {
            e= Integer.toString(monthOfYear+1);
        }

        String s= Integer.toString(dayOfMonth);
        if (dayOfMonth<10){
            s="0"+s;
        }
        String b1=s+"-"+e+"-"+year;
        String b=year+"-"+e+"-"+s;
        fromdate.setText(b1);
        todate.setText(b1);
        submit_fromdate=b+" 00:00:00";
        submit_todate=b+" 00:00:00";

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v= inflater.inflate(R.layout.fragment_travel_summary, container, false);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                String e= Integer.toString(monthOfYear);
                if (monthOfYear<10){
                    int a=1+monthOfYear;
                    e="0"+a;
                }
                else {
                    e= Integer.toString(monthOfYear+1);
                }

                String s= Integer.toString(dayOfMonth);
                if (dayOfMonth<10){
                    s="0"+s;
                }
                String b1=s+"-"+e+"-"+year;
                String b=year+"-"+e+"-"+s;
                if(a==0)
                {
                    fromdate.setText(b1);
                    submit_fromdate=b+" 00:00:00";
                }
                else
                {
                    todate.setText(b1);

                    submit_todate=b+" 23:59:59";
                }
            }

        };


        return v;
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.fromll)
        {
            a=0;
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.setCanceledOnTouchOutside(false);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.getDatePicker().setSpinnersShown(true);
            dialog.getDatePicker().setCalendarViewShown(false);
            dialog.show();
        }
        if(v.getId()==R.id.toll)
        {
            a=1;
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.setCanceledOnTouchOutside(false);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getDatePicker().setSpinnersShown(true);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show();
        }
        else if(v.getId()==R.id.apply)
        {
            if(Online.isOnline(context))
            {
                if(apply_disable==0)
                {


                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String inputString1 =submit_fromdate;
                    String inputString2 = submit_todate;

                    try {
                        Date date1 = myFormat.parse(inputString1);
                        Date date2 = myFormat.parse(inputString2);
                        long diff = date1.getTime() - date2.getTime();
                        diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        String str= String.valueOf(diff).replace("-","");
                        int diiff= Integer.parseInt(str);
                        if (diiff > 3) {
                            Toast.makeText(context, "Difference between two dates not more than 2", Toast.LENGTH_LONG).show();
                        } else {
                            apply_disable=1;
                            callAPI();
                        }
                    }
                        catch(Exception e){}


                }
                else
                {
                    Snackbar snackbar1 = Snackbar.make(heading, "Please wait data is loading", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }

            }
            else
            {
                Snackbar snackbar1 = Snackbar.make(heading, "No Internet Connection", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
        }
    }


    private void callAPI()
    {

        partnerid= StringFormatter.convertToUTF8(partnerid);
        userid= StringFormatter.convertToUTF8(userid);
        timezone= StringFormatter.convertToUTF8(timezone);
        String url="http://coreapi.gpsapphub.com/api/Device/"+partnerid+"/"+userid;
        new VolleyJsonReq(getContext()).getjson(url,
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
                                tsmaaray.clear();
                                deviceSno.clear();
                                vehicle_type.clear();
                                vehicle_number.clear();

                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    TravelSummaryModel travelSummaryModel=new TravelSummaryModel();
                                    JSONObject js = jsonArray.getJSONObject(i);

                                    travelSummaryModel.setVehicleNo(js.getString("vehicleNumber"));
                                    JSONObject jsonObject=new JSONObject(js.getString("vehicleType"));
                                    vehicle_number.add(js.getString("vehicleNumber"));

                                    travelSummaryModel.setVehciletype(jsonObject.getInt("vehicleTypeID"));
                                    vehicle_type.add(jsonObject.getInt("vehicleTypeID"));
                                    travelSummaryModel.setValue(0);

                                    deviceSno.add(js.getInt("deviceSno"));

                                    tsmaaray.add(travelSummaryModel);
                                }
                                i=0;
                                setdat();
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

    public void setdat() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

         Adapter = new TravelAdapter(context, tsmaaray);
        recyclerView.setAdapter(Adapter);

        setnewdata();

    }

    @Override
    public void onStop() {
        startActivity(new Intent(context, DashBoard.class));
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach()
    {

        super.onDetach();
    }

    public void setnewdata()
    {
      String submit_fromdatenew=StringFormatter.convertToUTF8(submit_fromdate);
       String submit_todatenew=StringFormatter.convertToUTF8(submit_todate);
        String device= String.valueOf(deviceSno.get(i));
        String url="http://coreapi.gpsapphub.com/api/TravelSummaryReport/"+partnerid+"/"+device+"/"+submit_fromdatenew+"/"+submit_todatenew+"/"+timezone;
        new TravelSummaryAdapterVolley(getContext()).getjsonObject(url,
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
                                TravelSummaryModel travelSummaryModel=new TravelSummaryModel();
                                JSONObject jsonObject=new JSONObject(response);

                                travelSummaryModel.setVehicleNo(vehicle_number.get(i));
                                travelSummaryModel.setVehciletype(vehicle_type.get(i));
                                travelSummaryModel.setIdle(jsonObject.getString("dormat"));
                                travelSummaryModel.setStop(jsonObject.getString("stoppage"));
                                travelSummaryModel.setDistnace(jsonObject.getString("distance")+" Km");
                                travelSummaryModel.setMaxspeed(jsonObject.getString("maxSpeedR")+" Km/Hr");
                                travelSummaryModel.setAvgspeed(jsonObject.getString("avgSpeedR")+" Km/Hr");
                                travelSummaryModel.setValue(1);

                                tsmaaray.set(i,travelSummaryModel);

                                Adapter.notifyItemChanged(i,tsmaaray);

                                if(i<vehicle_type.size()-1) {
                                    i++;
                                    setnewdata();
                                }
                                if(i==vehicle_type.size()-1)
                                {
                                    apply_disable=0;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if(i<vehicle_type.size()-1) { ;
                                    setnewdata();
                                }
                            }
                        }

                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(heading, response, Snackbar.LENGTH_LONG);
                            snackbar1.show();
                            if(i<vehicle_type.size()-1) { ;
                                setnewdata();
                            }
                        }
                    }

                });

    }

    

}
