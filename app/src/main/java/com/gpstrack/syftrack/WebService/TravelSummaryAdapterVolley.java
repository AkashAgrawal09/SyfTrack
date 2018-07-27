package com.gpstrack.syftrack.WebService;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class TravelSummaryAdapterVolley
{
    Context context;

    public TravelSummaryAdapterVolley(Context context) {
        this.context = context;
    }

    public void getjsonObject(String URL, final VolleyJsonReq.MyListener listener)
    {
        final RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonObjectRequest req=new JsonObjectRequest(0, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        requestQueue.stop();
                        listener.onServercode(200);
                        listener.onServerResponse(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                requestQueue.stop();
                String error_string="";
                String json=new String(error.networkResponse.data);
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    error_string=jsonObject.getString("error");
                    listener.onServercode(400);
                    listener.onServerResponse(error_string);
                }catch(Exception e)
                {
                    listener.onServercode(400);
                    listener.onServerResponse(e.toString());
                }
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }


    public void getjsonarray(String URL, final VolleyJsonReq.MyListener listener)
    {
        final RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonArrayRequest req=new JsonArrayRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        listener.onServercode(200);
                        listener.onServerResponse(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String error_string="";
                String json=new String(error.networkResponse.data);
                try{
                    JSONObject jsonObject=new JSONObject(json);
                    error_string=jsonObject.getString("error");
                    listener.onServercode(400);
                    listener.onServerResponse(error_string);
                }catch(Exception e)
                {
                    listener.onServercode(400);
                    listener.onServerResponse(e.toString());
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
