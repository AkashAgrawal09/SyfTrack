package com.gpstrack.syftrack.WebService;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyPostReq
{
    Context context;
    public VolleyPostReq(Context context) {
        this.context = context;
    }

    public void getjsonObject(String URL, JSONObject jsonObjectfinal, final VolleyPostReq.MyListener listener)
    {
        final ProgressDialog dialog=new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading...");
        dialog.show();
        final RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST, URL,jsonObjectfinal,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(dialog!=null)
                        {
                            dialog.dismiss();
                        }
                        listener.onServercode(200);
                        listener.onServerResponse(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog.hide();
                String error_string="";
                if(error.networkResponse!=null) {
                    String json = new String(error.networkResponse.data);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        error_string = jsonObject.getString("error");
                        listener.onServercode(400);
                        listener.onServerResponse(error_string);
                    } catch (Exception e) {
                        listener.onServercode(400);
                        listener.onServerResponse(e.toString());
                    }
                }
                else
                {
                    listener.onServercode(400);
                    listener.onServerResponse("No Internet Connection Found");
                }
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }


    public interface MyListener
    {
        public void onServercode(int code);
        public void onServerResponse(String response);
    }
}
