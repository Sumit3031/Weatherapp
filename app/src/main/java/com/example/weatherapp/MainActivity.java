package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "66258c6ace6ad42c4caaac4cc0d0fbb0";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById((R.id.etCountry));
        tvResult = findViewById(R.id.tvResult);

    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("Please Enter the city name");

        } else {
            if (!country.equals("")) {
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            } else {
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,tempUrl, new Response.Listener<String>() {
                public void onResponse(String response) {
                    //Log.d("response", response);
                    String output=" ";
                    try {
                        JSONObject jsonResponse=new JSONObject(response);
                        JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                        String description =jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMAin=jsonResponse.getJSONObject("main");
                        double temp=jsonObjectMAin.getDouble("temp")-273.15;
                        double feelsLike=jsonObjectMAin.getDouble("feels_like")-273.15;
                        float pressure=jsonObjectMAin.getInt("pressure");
                        int humidity=jsonObjectMAin.getInt("humidity");
                        JSONObject jsonObjectWind=jsonResponse.getJSONObject("wind");
                        String wind =jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds=jsonResponse.getJSONObject("clouds");
                        String clouds=jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys=jsonResponse.getJSONObject("sys");
                        String countryName=jsonObjectSys.getString("country");
                        String cityname=jsonResponse.getString("name");
                        tvResult.setTextColor(Color.rgb(0,0,0));
                        tvResult.setTextSize(20);
                        output+=  "              Current weather of "+ cityname+" ("+ countryName+")"
                                + "\n              Temp: " + df.format(temp)+" °C"
                                + "\n              Feels Like"+ df.format(feelsLike)+ " °C"
                                + "\n              Humidity: " + humidity + "%"
                                + "\n              Description: "+ description
                                + "\n              Wind Speed: " + wind+ "m/s"
                                + "\n              Cloudiness " + clouds + "%"
                                + "\n              Pressure: " + pressure+ "hpa";
                                tvResult.setText(output);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}

