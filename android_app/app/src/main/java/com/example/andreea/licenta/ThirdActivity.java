package com.example.andreea.licenta;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThirdActivity extends AppCompatActivity {

    TextView textPlant;
    TextView textDisease;
    TextView textTreatment;
    TextView textWeather;

    TextView cityField, detailsField, currentTemperatureField, weatherIcon;
    TextView dateT, tempT, detailsT, weatherIconT;
//    ProgressBar loader;
    Typeface weatherFont;
    String city = "Dhaka, BD";
    /* Please Put your API KEY here */
    String OPEN_WEATHER_MAP_API = "d3932301313e81919161f57a28829272";
    /* Please Put your API KEY here */
    LocationManager locationManager;
    String provider;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    LocationManager mLocationManager;
    long sunrise, sunset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.var4);

        String message_plant =  getIntent().getStringExtra("value_plant");
        String message_disease =  getIntent().getStringExtra("value_disease");
        String message_treatment =  getIntent().getStringExtra("value_treatment");
        boolean use_weather = getIntent().getBooleanExtra("use_weather", false);

        cityField = (TextView) findViewById(R.id.cityField);
        detailsField = (TextView) findViewById(R.id.details);
        currentTemperatureField = (TextView) findViewById(R.id.temp);
        weatherIcon = (TextView) findViewById(R.id.weatherIcon);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);

        dateT = (TextView)findViewById(R.id.dateT);
        detailsT = (TextView)findViewById(R.id.detailsT);
        tempT = (TextView) findViewById(R.id.tempT);
        weatherIconT = (TextView) findViewById(R.id.weatherIconT);
        weatherIconT.setTypeface(weatherFont);

        textPlant = (TextView) findViewById(R.id.plantText);
        textDisease = (TextView) findViewById(R.id.diseaseText);
        textTreatment = (TextView) findViewById(R.id.treatmentText);
        textWeather = (TextView) findViewById(R.id.weatherText);

        textPlant.setText(message_plant);
        textDisease.setText(message_disease);
        textTreatment.setText(message_treatment);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }


        double latitude = getLatitude();
        double longitude = getLongitude();
        if (use_weather){
            taskLoadUp_coordinates(latitude, longitude);
            get_bestTime(latitude, longitude);
        } else{
            textWeather.setTextColor(Color.RED);
            textWeather.setText("Location and weather not used");
            dateT.setText("");
            detailsT.setText("");
            tempT.setText("");
            weatherIconT.setText("");

            cityField.setText("");
            currentTemperatureField.setText("");
            detailsField.setText("");
            weatherIcon.setText("");
        }

    }

    public double getLatitude(){
        GpsTracker gpstracker=new GpsTracker(getApplicationContext());
        double lat=gpstracker.getLatitude();
        return lat;
    }

    public double getLongitude(){
        GpsTracker gpstracker=new GpsTracker(getApplicationContext());
        double longitude=gpstracker.getLongitude();
        return longitude;
    }

    public void get_bestTime(double lat, double lon){
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeatherForecast task = new DownloadWeatherForecast();
            task.execute(Double.toString(lat), Double.toString(lon));
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void taskLoadUp_coordinates(double lat, double lon){
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(Double.toString(lat), Double.toString(lon));
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadWeather extends AsyncTask < String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + args[0] + "&lon=" + args[1]
                    + "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    weatherIcon.setTextColor(Color.BLACK);
                    sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
                    sunset = json.getJSONObject("sys").getLong("sunset") * 1000;
                    weatherIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class WeatherObject{
        Date date;
        int id;
        double wind;
        double temp;

        public WeatherObject(int id, Date date, double wind, double temp){
            this.id = id;
            this.date = date;
            this.wind = wind;
            this.temp = temp;
        }

        public boolean valid_date(){
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if ((hour == 6) || (hour == 9) || (hour == 12) || (hour == 15) || (hour == 18)){
                return true;
            }
            return false;
        }

        public boolean valid_wind(){
            if (this.wind < 25){
                return true;
            }
            return false;
        }

        public boolean valid_temp(){
            if (this.temp < 27.5){
                return true;
            }
            return false;
        }

        public boolean valid_precip(){
            if (this.id == 800 || this.id == 801 || this.id == 802 || this.id == 803){
                return true;
            }
            return false;
        }

        public boolean is_valid(){
            if (this.valid_date() && this.valid_temp() && this.valid_precip() && this.valid_wind()){
                return true;
            }
            return false;
        }

    }

    class DownloadWeatherForecast extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/forecast?lat=" + args[0] + "&lon=" + args[1]
                    + "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
//                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONArray weather_list = json.getJSONArray("list");
                    JSONObject main = weather_list.getJSONObject(0).getJSONObject("main");
                    JSONObject details = weather_list.getJSONObject(0).getJSONArray("weather").getJSONObject(0);
                    ArrayList<WeatherObject> weatherObjects = new ArrayList<WeatherObject>();
                    for(int i = 0; i < weather_list.length(); i++){
                        Date date = new Date(weather_list.getJSONObject(i).getLong("dt") * 1000);
                        double temp = weather_list.getJSONObject(i).getJSONObject("main").getDouble("temp");
                        double wind = weather_list.getJSONObject(i).getJSONObject("wind").getDouble("speed");
                        int id = weather_list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                        WeatherObject obj = new WeatherObject(id, date, wind, temp);
                        weatherObjects.add(obj);
                    }

                    int valid_id = -1;
                    for (int i = 0; i < weatherObjects.size() - 1; i++){
                        if (weatherObjects.get(i).is_valid() && weatherObjects.get(i+1).is_valid()){
                            valid_id = i;
                            break;
                        }
                    }

                    System.out.print(valid_id);
                    WeatherObject bestWeatherObject = weatherObjects.get(valid_id);
                    DateFormat df = DateFormat.getDateTimeInstance();

//                    cityField.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US));
                    detailsT.setText(weather_list.getJSONObject(valid_id).getJSONArray("weather").getJSONObject(0).getString("description").toUpperCase(Locale.US));
                    tempT.setText(String.format("%.2f", bestWeatherObject.temp) + "°");
                    dateT.setText(df.format(bestWeatherObject.date));
                    weatherIconT.setTextColor(Color.BLACK);
                    weatherIconT.setText(Html.fromHtml(Function.setWeatherIcon(bestWeatherObject.id,
                            sunrise,
                            sunset)));

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
