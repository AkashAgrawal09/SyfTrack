package com.gpstrack.syftrack.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpstrack.syftrack.Adapter.StoppageAdapter;
import com.gpstrack.syftrack.Model.StoppageSummaryModel;
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
public class StoppageSummary extends Fragment implements View.OnClickListener
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
    ArrayList<StoppageSummaryModel> tsmaaray=new ArrayList<>();
    ArrayList<String> vehicle_number=new ArrayList<>();
    ArrayList<Integer> vehicle_type=new ArrayList<>();

    ArrayList<Integer> deviceSno=new ArrayList<>();

    StoppageAdapter Adapter;

    int i=0;
    int a=0;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    String submit_fromdate,submit_todate;
    public StoppageSummary() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context=getActivity().getApplicationContext();
        getActivity().setTitle("Stoppage Summary");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_stoppage_summary, container, false);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String e = Integer.toString(monthOfYear);
                if (monthOfYear < 10) {
                    int a = 1 + monthOfYear;
                    e = "0" + a;
                } else {
                    e = Integer.toString(monthOfYear + 1);
                }

                String s = Integer.toString(dayOfMonth);
                if (dayOfMonth < 10) {
                    s = "0" + s;
                }
                String b1 = s + "-" + e + "-" + year;
                String b = year + "-" + e + "-" + s;
                if (a == 0) {
                    fromdate.setText(b1);
                    submit_fromdate = b + " 00:00:00";
                } else {
                    todate.setText(b1);
                    submit_todate = b + " 00:00:00";
                }
            }
        };

        return v;
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
        submit_todate=b+" 23:59:59";

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
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.setCanceledOnTouchOutside(false);
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
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.setCanceledOnTouchOutside(false);
            dialog.getDatePicker().setSpinnersShown(true);
            dialog.getDatePicker().setCalendarViewShown(false);
            dialog.show();
        }
        else if(v.getId()==R.id.apply)
        {
            if(Online.isOnline(context))
            {
                if(apply.isClickable())
                {
                    SimpleDateFormat myFormat = new SimpleDateFormat("dd-mm-yyyy");
                    String inputString1 =fromdate.getText().toString();
                    String inputString2 = todate.getText().toString();

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
                           apply.setClickable(false);
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
        new VolleyJsonReq(getActivity()).getjson(url,
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
                                    StoppageSummaryModel travelSummaryModel=new StoppageSummaryModel();
                                    JSONObject js = jsonArray.getJSONObject(i);

                                    travelSummaryModel.setVehicleNo(js.getString("vehicleNumber"));
                                    JSONObject jsonObject=new JSONObject(js.getString("vehicleType"));
                                    vehicle_number.add(js.getString("vehicleNumber"));

                                    travelSummaryModel.setVehicletype(jsonObject.getInt("vehicleTypeID"));
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

        Adapter = new StoppageAdapter(context, tsmaaray);
        recyclerView.setAdapter(Adapter);

        setnewdata();

    }

    public void setnewdata()
    {
        submit_fromdate=StringFormatter.convertToUTF8(submit_fromdate);
        submit_todate=StringFormatter.convertToUTF8(submit_todate);
        String device= String.valueOf(deviceSno.get(i));
        String url="http://coreapi.gpsapphub.com/api/StoppageReport/"+partnerid+"/"+device+"/"+submit_fromdate+"/"+submit_todate+"/"+timezone;
        new TravelSummaryAdapterVolley(getContext()).getjsonarray(url,
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
                                StoppageSummaryModel travelSummaryModel=new StoppageSummaryModel();
                                JSONArray jsonarray=new JSONArray(response);

                                travelSummaryModel.setVehicleNo(vehicle_number.get(i));
                                travelSummaryModel.setVehicletype(vehicle_type.get(i));
                                travelSummaryModel.setTotalstops(String.valueOf(jsonarray.length()));
                                travelSummaryModel.setValue(1);
                                travelSummaryModel.setJsonarray(jsonarray.toString());
                                travelSummaryModel.setLastvalue(0);

                                if(i==vehicle_type.size()-1)
                                {
                                   apply.setClickable(true);
                                    travelSummaryModel.setLastvalue(1);
                                }

                                tsmaaray.set(i,travelSummaryModel);
                                Adapter.notifyItemChanged(i,tsmaaray);

                                if(i<vehicle_type.size()-1) {
                                    i++;
                                    setnewdata();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                setnewdata();
                            }
                        }

                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(heading, response, Snackbar.LENGTH_LONG);
                            snackbar1.show();
                            try {
                                StoppageSummaryModel travelSummaryModel = new StoppageSummaryModel();

                                travelSummaryModel.setVehicleNo(vehicle_number.get(i));
                                travelSummaryModel.setVehicletype(vehicle_type.get(i));
                                travelSummaryModel.setTotalstops("0");
                                travelSummaryModel.setValue(1);
                                travelSummaryModel.setJsonarray("0");
                                travelSummaryModel.setLastvalue(0);
                                if (i == vehicle_type.size() - 1) {
                                    apply.setClickable(true);
                                    travelSummaryModel.setLastvalue(1);
                                    tsmaaray.set(i,travelSummaryModel);
                                    Adapter.notifyItemChanged(i,tsmaaray);

                                } else {
                                    tsmaaray.set(i,travelSummaryModel);
                                    Adapter.notifyItemChanged(i,tsmaaray);
                                    i++;
                                    setnewdata();
                                }
                            }catch (Exception e)
                            {

                            }

                        }
                    }

                });

    }
}
