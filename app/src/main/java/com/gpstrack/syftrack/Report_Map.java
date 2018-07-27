package com.gpstrack.syftrack;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gpstrack.syftrack.Adapter.Reportmap_adapter;
import com.gpstrack.syftrack.Adapter.TravelAdapter;
import com.gpstrack.syftrack.Fragment.TravelSummary;
import com.gpstrack.syftrack.Model.TravelSummaryModel;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.TravelSummaryAdapterVolley;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class Report_Map extends AppCompatActivity implements View.OnClickListener
{
    TextView vehicle_number;
    String partnerid,userid,timezone,todaydate;
    String deviceSo;
    int server_code;
    LinearLayout heading;
    RecyclerView recyleview;
    RadioButton yesterday,last3days;
    ArrayList<TravelSummaryModel> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__map);
        heading=findViewById(R.id.heading);
        recyleview=findViewById(R.id.recyleview);
        yesterday=findViewById(R.id.yes);
        last3days=findViewById(R.id.last3days);

        Intent i=getIntent();

        vehicle_number=findViewById(R.id.vehicle_number);
        vehicle_number.setText(i.getStringExtra("vehicleno"));
        deviceSo=i.getStringExtra("deviseSo");

        userid=  new AppUtils(Report_Map.this).getUserName();
        partnerid= new AppUtils(Report_Map.this).getPartnerID();
        timezone= new AppUtils(Report_Map.this).getTimeZone();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        todaydate=    dateFormat.format(cal.getTime());

        yesterday.setOnClickListener(this);
        last3days.setOnClickListener(this);

    setnewdata(todaydate+" 00:00:00",todaydate+" 23:59:59");

    }

    public void setnewdata(String submit_fromdate,String submit_todate)
    {
        String submit_fromdatenew=StringFormatter.convertToUTF8(submit_fromdate);
        String submit_todatenew=StringFormatter.convertToUTF8(submit_todate);
        String device= String.valueOf(deviceSo);
        String url="http://coreapi.gpsapphub.com/api/TravelSummaryReport/"+partnerid+"/"+device+"/"+submit_fromdatenew+"/"+submit_todatenew+"/"+timezone;
        new TravelSummaryAdapterVolley(Report_Map.this).getjsonObject(url,
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
                                if(arrayList.size()>0)
                                {
                                    arrayList.clear();
                                }
                                TravelSummaryModel travelSummaryModel=new TravelSummaryModel();
                                JSONObject jsonObject=new JSONObject(response);

                                travelSummaryModel.setIdle(jsonObject.getString("dormat"));
                                travelSummaryModel.setDistnace(jsonObject.getString("distance")+" Km");
                                travelSummaryModel.setMaxspeed(jsonObject.getString("maxSpeedR")+" Km/Hr");
                                travelSummaryModel.setAvgspeed(jsonObject.getString("avgSpeedR")+" Km/Hr");

                                arrayList.add(travelSummaryModel);
                                setdata();

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

    public void setdata() {
        yesterday.setClickable(true);
        last3days.setClickable(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyleview.setLayoutManager(linearLayoutManager);
        recyleview.setHasFixedSize(true);

        Reportmap_adapter Adapter = new Reportmap_adapter(Report_Map.this, arrayList);
        recyleview.setAdapter(Adapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Report_Map.this,DashBoard.class));
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId()==R.id.yes)
        {
            yesterday.setClickable(false);
            last3days.setClickable(false);
            setnewdata(todaydate+" 00:00:00",todaydate+" 23:59:59");
        }
        else
        {
            yesterday.setClickable(false);
            last3days.setClickable(false);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -3);
            String datenew=    dateFormat.format(cal.getTime());

            setnewdata(datenew+" 00:00:00",todaydate+" 23:59:59");
        }
    }
}
