package com.gpstrack.syftrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.gpstrack.syftrack.Model.GetSet;
import com.gpstrack.syftrack.Utils.AppUtils;
import com.gpstrack.syftrack.Utils.StringFormatter;
import com.gpstrack.syftrack.WebService.VolleyJsonReq;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity
{
    ImageView icon;
    TextView loginbyid,infoOtp;
    LinearLayout signup, verify;
    EditText phoneno, otp;
    ProgressBar progressBar;
    CountryCodePicker ccp;
    ProgressDialog progressDialog;
    Button verifyBtn,register;
    int server_code;
    CheckBox checkBox;
    PhoneAuthProvider.ForceResendingToken resendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;
    String verification_code;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (new AppUtils(OTP.this).getnumberrember()) {
            Intent in = new Intent(OTP.this, DashBoard.class);
            startActivity(in);
            finishAffinity();
        }
        setContentView(R.layout.activity_otp);

        auth = FirebaseAuth.getInstance();

        icon=findViewById( R.id.icon );
        loginbyid=findViewById( R.id.loginbyid );
        verify=findViewById( R.id.verify );

        verify = findViewById(R.id.verify);
        signup = findViewById(R.id.signup);
        phoneno = findViewById(R.id.phoneno);
        otp = findViewById(R.id.otp);
        verifyBtn = findViewById(R.id.verifyBtn);
        signup.setVisibility(View.VISIBLE);
        verify.setVisibility(View.GONE);
        ccp = findViewById(R.id.ccp);
        infoOtp = findViewById(R.id.info_otp);
        progressBar = findViewById(R.id.progress);
        register=findViewById(R.id.registerbtn);
        checkBox = findViewById(R.id.remberme);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneno.getText().toString();
                if (number != null && number.length() == 10) {
                    callAPI();

                } else {
                    phoneno.setError("Please enter valid number");
                }


            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veryfyPhoneNumber(verification_code, otp.getText().toString());
            }
        });


        loginbyid.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( OTP.this,LoginActivity.class ) );

            }
        } );
    }

    public  void sendOTP(final String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OTP.this,// Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        progressDialog = new ProgressDialog(OTP.this);
                        progressDialog.setMessage("Verifying Automatically..");
                        progressDialog.show();
                        infoOtp.setText("OTP received");
                        progressBar.setVisibility(View.GONE);
                        otp.setText(String.format("%06d", new Random().nextInt(1000000)));
                        signInWithNumber(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(OTP.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verification_code = s;
                        resendToken = forceResendingToken;
                        infoOtp.setText("OTP Sent.. Please Enter OTP");
                        progressBar.setVisibility(View.GONE);
                        otp.setText("");
                        Toast.makeText(OTP.this, "OTP sent to " + number, Toast.LENGTH_SHORT).show();
                    }

                });

    }

    public  void signInWithNumber(PhoneAuthCredential credential){
        auth.signInWithCredential( credential ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    new AppUtils(OTP.this).setlogintype("number");

                    if(checkBox.isChecked())
                    {
                        new AppUtils(OTP.this).setnumberremeber(true);
                    }
                    else
                    {
                        new AppUtils(OTP.this).setnumberremeber(false);
                    }

                    startActivity(new Intent(OTP.this,DashBoard.class));
                    finishAffinity();

                }
            }
        } );

    }

    private void veryfyPhoneNumber(String verification_code, String input_code) {
        infoOtp.setText("Please wait ! verifying...");
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential( verification_code,input_code );
        signInWithNumber( credential );
    }

    private void callAPI()
    {
        String url="http://coreapi.gpsapphub.com/api/Login/"+phoneno.getText().toString()+"/syftrack";
        new VolleyJsonReq(OTP.this).getjsonObject(url,
                new VolleyJsonReq.MyListener() {
                    @Override
                    public void onServercode(int code)
                    {
                        server_code = code;
                    }

                    @Override
                    public void onServerResponse(String response)
                    {
                        if (server_code == 200)
                        {
                            try
                            {
                                JSONObject jsonObject=new JSONObject(response);
                                if (jsonObject.getString("partnerId")!= null) {
                                    GetSet getSet = (GetSet) getApplicationContext();

                                    String partnerID = jsonObject.getString("partnerId");
                                    getSet.setPartnerid(partnerID);
                                    String userID = jsonObject.getString("userID");
                                    getSet.setUserid(userID);
                                    String Time = jsonObject.getString("timezone");
                                    getSet.setTimezone(Time);
                                    new AppUtils(OTP.this).setUserName(userID);
                                    new AppUtils(OTP.this).setPartnerID(partnerID);
                                    new AppUtils(OTP.this).setTimeZone(Time);


                                        sendOTP(ccp.getSelectedCountryCodeWithPlus() + phoneno.getText().toString());
                                    //init Verify view
                                        verify.setVisibility(View.VISIBLE);
                                        signup.setVisibility(View.GONE);
                                        infoOtp.setText("Waiting for OTP !!");

                                }
                                } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            Toast.makeText(OTP.this, "NUmber doesn't exsist", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

}
