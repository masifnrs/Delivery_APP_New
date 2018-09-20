package com.test.delivery_app_new;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.test.delivery_app_new.adapter.CustomListAdapter;
import com.test.delivery_app_new.app.AppController;
import com.test.delivery_app_new.model.Delivery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    String jasonUri = "getFixtures.jason";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = "https://mock-api-mobile.dev.lalamove.com/deliveries?offset=0&&limit=20";//"https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";//"http://192.168.43.129/web/api/edmx/";
    private ProgressDialog pDialog;
    private List<Delivery> deliveriesList = new ArrayList<Delivery>();
    private ListView listView;
    private CustomListAdapter adapter;
    static String EngnewDetail[]=new String[200];
    static int pos;
    static String TAG_engtitel;
    ImageView image;
    //***********************************************
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean mobileDataEnabled = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        try
        {
            Class cmClass = Class.forName(connManager.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"

           mobileDataEnabled = (boolean)method.invoke(connManager);
        } catch (Exception e) {
            // Some problem accessible private API
            // TO do whatever error handling you want here
        }




        //if (mWifi.isConnected()|| mobileDataEnabled == true) {

            fetchRecord();
        //}
        /*else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please check your internet connection");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();

                        }
                    });
            alertDialog.show();
        }*/

    }
    private  void createVolleyRequest()
    {
        JsonArrayRequest deliveryReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Delivery delivery = new Delivery();
                                delivery.setTitle(obj.getString("description"));
                                delivery.setLatDetails(obj.getJSONObject("location").getString("lat"));
                                delivery.setLongDetails(obj.getJSONObject("location").getString("lng"));
                                delivery.setAddress(obj.getJSONObject("location").getString("address"));
                                delivery.setThumbnailUrl(obj.getString("imageUrl"));
                                // adding movie to movies array
                                deliveriesList.add(delivery);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        pDialog.hide();
                        // notifying list adapter about data changes
                        adapter.notifyDataSetChanged();

                        // so that it renders the list view with updated data
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }){
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(deliveryReq);
    }
    private void fetchRecord()
    {
        //******************************************************************************************
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, deliveriesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), DeliveryDetailsActivity.class);
                in.putExtra("lat", deliveriesList.get(position).getLatDetails());
                in.putExtra("long", deliveriesList.get(position).getLongDetails());
                in.putExtra("address", deliveriesList.get(position).getAddress());
                startActivity(in);
                }
        });

       pDialog = new ProgressDialog(this);
//        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        DeliveryTask dt = new DeliveryTask(pDialog);
        dt.execute();
        //Creating volley request obj
    }

    /** A class, to download delivery items */
    private class DeliveryTask extends AsyncTask<Void, Void, Void> {

        String data = null;
        ProgressDialog pDialog1;
        public DeliveryTask(ProgressDialog progress) {
            this.pDialog1 = progress;
        }
        public void onPreExecute() {
            super.onPreExecute();
            //pDialog1.show();
        }
        // Invoked by execute() method of this object
        @Override
        protected Void doInBackground(Void... url) {
            try{
                createVolleyRequest();
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return null;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(Void v){
//            if(pDialog1.isShowing())
//            {
//                pDialog1.dismiss();
//            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
