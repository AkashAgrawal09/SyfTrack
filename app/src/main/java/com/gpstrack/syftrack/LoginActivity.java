package com.gpstrack.syftrack;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gpstrack.syftrack.Model.GetSet;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.DeviceInformation;
import com.gpstrack.syftrack.Utils.Online;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    String userid_text, password_text,regID;
    String gmailid, phoneNumber;
    Button login;
    JSONObject jkob;
    RelativeLayout heading;
    EditText userid,password;
    TextView loginbynumber;
    CheckBox checkBox;
    Context mContext = this;
    public static final int R_PERM = 2822;
    private static final int REQUEST= 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new AppUtils(LoginActivity.this).getLogSkipStatus()) {
            Intent in = new Intent(LoginActivity.this, DashBoard.class);
            startActivity(in);
            finishAffinity();
        }
        setContentView(R.layout.activity_login);

        String[] PERMISSIONS = {android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.GET_ACCOUNTS, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!hasPermissions(mContext, PERMISSIONS))
        {
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
        }
        else
        {
            initilizedvariable();
        }

    }

    String getIMEI(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    @Override
    public void onClick(View v)
    {
        if(Online.isOnline(LoginActivity.this))
        {
            userid_text = userid.getText().toString();
            password_text = password.getText().toString();

            JSONObject job = new JSONObject();
            try
            {
                String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;
                job.put("Operator", null);
                JSONArray jsonArray=new JSONArray();
                jsonArray.put(getIMEI(getApplicationContext()));
                job.put("MobileIMEI", jsonArray);
                job.put("MobileModel",  manufacturer+" "+model);
                job.put("OSVersion", Build.VERSION.RELEASE);
                JSONObject appversion=new JSONObject();
                appversion.put("ID","9");
                appversion.put("VersionName","1.8");

                job.put("AppVersion", appversion);
                job.put("emailID", gmailid);
                JSONObject notifi_detail=new JSONObject();
                notifi_detail.put("RegID",regID);
                notifi_detail.put("ServerID","AIzaSyCTkMAeu3NDfVcLGCSUlagswrWHUINofXw");
                notifi_detail.put("ProjectID","890335683401");

                jkob = new JSONObject();
                jkob.put("LoginBy","1");
                jkob.put("UserID", userid_text);
                jkob.put("Password", password_text);
                jkob.put("PhoneDetails", job);
                jkob.put("NotificationDetails",notifi_detail);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if((password_text==null) || (userid_text==null) || userid_text.equals("") || password_text.equals("")){
                Snackbar snackbar1 = Snackbar.make(heading, "Please Enter Userid/Password", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }
            else{
                CallAPI();
            }
        }
        else{
            Snackbar snackbar1 = Snackbar.make(heading, "No Internet Connection. Please check your internet connection", Snackbar.LENGTH_LONG);
            snackbar1.show();
        }
    }

    private void CallAPI()
    {
        final ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading...");
        dialog.show();
        String DATA_URL= "http://coreapi.gpsapphub.com/api/Login";
        final RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST, DATA_URL,jkob,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        try {
                            if (response.getString("partnerId")!= null)
                            {
                                GetSet getSet=(GetSet)getApplicationContext();

                                String partnerID = response.getString("partnerId");
                                getSet.setPartnerid(partnerID);
                                String userID = response.getString("userID");
                                getSet.setUserid(userID);
                                String Time=response.getString("timezone");
                                getSet.setTimezone(Time);
                                new AppUtils(LoginActivity.this).setUserName(userID);
                                new AppUtils(LoginActivity.this).setPartnerID(partnerID);
                                new AppUtils(LoginActivity.this).setTimeZone(Time);

                                if(checkBox.isChecked())
                                {
                                    new AppUtils(LoginActivity.this).setLogSkipStatus(true);
                                }
                                else
                                {
                                    new AppUtils(LoginActivity.this).setLogSkipStatus(false);
                                }
                                new AppUtils(LoginActivity.this).setlogintype("id");
                                Intent in = new Intent(LoginActivity.this, DashBoard.class);
                                startActivity(in);
                                finishAffinity();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String error_string="";
                dialog.hide();
                if(error.networkResponse!=null) {
                    String json = new String(error.networkResponse.data);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        error_string = jsonObject.getString("error");
                        Snackbar snackbar1 = Snackbar.make(heading, error_string, Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    } catch (Exception e) {
                        Snackbar snackbar1 = Snackbar.make(heading, e.toString(), Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                }
                else
                {
                    Snackbar snackbar1 = Snackbar.make(heading,"No Internet Conection Found", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                }

            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    initilizedvariable();
                    //  callNextActivity();
                } else
                {
                    Toast.makeText(mContext, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }

    }

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }

        }

        return true;
    }

    public void initilizedvariable()
    {
        userid = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        heading = findViewById(R.id.heading);
        loginbynumber=findViewById(R.id.loginbynumber);
        checkBox = findViewById(R.id.remberme);
        regID = FirebaseInstanceId.getInstance().getToken();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userid.getWindowToken(), 0);

        gmailid = DeviceInformation.GmailID(LoginActivity.this);
        phoneNumber = DeviceInformation.phoneNumber(LoginActivity.this);


        login.setOnClickListener(this);
        loginbynumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,OTP.class));
            }
        });
    }

}
