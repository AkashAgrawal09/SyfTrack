package com.gpstrack.syftrack;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gpstrack.syftrack.Adapter.DistanceAdapter;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.Online;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistanceReport extends AppCompatActivity implements View.OnClickListener
{
    String partnerid,userid,timezone,_date;

    @BindView(R.id.heading)
    LinearLayout heading;

    int server_code;

    @BindView(R.id.selectvehiclehistry)
    AutoCompleteTextView actv;

    ArrayList<JSONObject> jsonObjects=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_report);
        ButterKnife.bind(this);

        userid=  new AppUtils(DistanceReport.this).getUserName();
        partnerid= new AppUtils(DistanceReport.this).getPartnerID();
        timezone= new AppUtils(DistanceReport.this).getTimeZone();

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        _date=dateFormat.format(now.getTime())+" 00:00:00";

     //   callAPI();
    }

    private void callAPI()
    {
        partnerid= StringFormatter.convertToUTF8(partnerid);
        userid= StringFormatter.convertToUTF8(userid);
        timezone= StringFormatter.convertToUTF8(timezone);
        _date=StringFormatter.convertToUTF8(_date);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE,-1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current_date=dateFormat.format(now.getTime())+" 00:00:00";
        current_date=StringFormatter.convertToUTF8(current_date);

        String url="http://coreapi.gpsapphub.com/api/Device/"+partnerid+"/"+userid;

      //  String url="http://coreapi.gpsapphub.com/api/DistanceReport/"+partnerid+"/-1/"+_date+"/"+current_date+"/"+timezone+"/"+userid;
        new VolleyJsonReq(DistanceReport.this).getjson(url,
                new VolleyJsonReq.MyListener() {
                    @Override
                    public void onServercode(int code) {
                        server_code = code;
                    }

                    @Override
                    public void onServerResponse(String response)
                    {
                        if (server_code == 200) {
                            jsonObjects.clear();
                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    jsonObjects.add(jsonObject);

                                }
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

    @Override
    public void onClick(View v)
    {
        if(Online.isOnline(DistanceReport.this))
        {
//            if (v.getId() == R.id.yesterday)
//            {
//                Calendar now = Calendar.getInstance();
//                now.add(Calendar.DATE, -1);
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                _date=dateFormat.format(now.getTime())+" 00:00:00";
//            }
//            else if (v.getId() == R.id.last7)
//            {
//                Calendar now = Calendar.getInstance();
//                now.add(Calendar.DATE, -7);
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                _date=dateFormat.format(now.getTime())+" 00:00:00";
//            }
//            else if (v.getId() == R.id.lastmonth)
//            {
//                Calendar now = Calendar.getInstance();
//                now.add(Calendar.DATE, -30);
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                _date=dateFormat.format(now.getTime())+" 00:00:00";
//            }

            callAPI();
        }
        else
        {
            Snackbar snackbar1 = Snackbar.make(heading, "Please check your internet connection", Snackbar.LENGTH_LONG);
            snackbar1.show();
        }
    }

    public void setdat()
    {
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( this );
//        recyclerView.setLayoutManager( linearLayoutManager );
//        recyclerView.setHasFixedSize(true);
//        DistanceAdapter distanceAdapter=new DistanceAdapter(DistanceReport.this,jsonObjects);
//        recyclerView.setAdapter( distanceAdapter );
    }
}
