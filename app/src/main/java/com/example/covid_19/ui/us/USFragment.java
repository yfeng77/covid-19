package com.example.covid_19.ui.us;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.covid_19.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class USFragment extends Fragment {

    TextView Tcases,TcasesI,Tdeaths,TdeathsI,Trecovered,TrecoveredI;
    public static final String TAG = "MyTag";
    RequestQueue requestQueue;  // Assume this exists.


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tcases = (TextView) view.findViewById(R.id.CasesN);
        TcasesI = (TextView) view.findViewById(R.id.CasesI);
        Tdeaths = (TextView) view.findViewById(R.id.DeathN);
        TdeathsI = (TextView) view.findViewById(R.id.DeathI);
        Trecovered = (TextView) view.findViewById(R.id.RecoveredN);
        //TrecoveredI = (TextView) view.findViewById(R.id.RecoveredI);

        WebView myWebView = (WebView) view.findViewById(R.id.webView);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl("https://lamp.cse.fau.edu/~yfeng2016/covidchart/USchart.html");


        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();
        
        String url = "https://corona.lmao.ninja/v2/countries/US";
        // Formulate the request and handle the response.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String cases = response.getString ("cases");
                            String casesI = response.getString ("todayCases");
                            String deaths = response.getString ("deaths");
                            String deathsI = response.getString ("todayDeaths");
                            String recovered = response.getString ("recovered");

                            Tcases.setText(cases);
                            TcasesI.setText("+"+casesI);
                            Tdeaths.setText(deaths);
                            TdeathsI.setText("+"+deathsI);
                            Trecovered.setText(recovered);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);

        getstate();

    }

    public void getstate()
    {
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();

        String url2 = "https://corona.lmao.ninja/v2/states";
        // Formulate the request and handle the response.
        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            TableLayout tl = (TableLayout) getActivity().findViewById(R.id.CityStats);

                            for (int i = 0; i < response.length(); i++) {

                                TableRow tr=new TableRow(getActivity());
                                TextView Tlocation = new TextView(getActivity());
                                Tlocation.setPadding(15,0,5,10);
                                TextView Tcases = new TextView(getActivity());
                                Tcases.setPadding(0,0,90,10);
                                TextView Tdeath = new TextView(getActivity());
                                Tdeath.setPadding(0,0,120,10);

                                JSONObject obj = response.getJSONObject(i);
                                String location = obj.getString ("state");
                                Tlocation.setText(location);
                                tr.addView(Tlocation);

                                String cases = obj.getString ("cases");
                                Tcases.setText(cases);
                                tr.addView(Tcases);

                                String deaths = obj.getString ("deaths");
                                Tdeath.setText(deaths);
                                tr.addView(Tdeath);
                                

                                tl.addView(tr);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Set the tag on the request.
        jsonarrayRequest.setTag(TAG);

        requestQueue.add(jsonarrayRequest);

    }

}