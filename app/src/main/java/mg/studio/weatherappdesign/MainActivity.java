package mg.studio.weatherappdesign;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    WeatherInfo[] weatherInfos = null;
    WeatherDisplay[] weatherDisplays = new WeatherDisplay[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //new DownloadUpdate().execute();
    }

    public void init(){

        Calendar calendar = Calendar.getInstance();
        java.text.DateFormat format2 = new java.text.SimpleDateFormat("MM/dd/yyyy");
        String strDate = format2.format(new Date());
        ((TextView)findViewById(R.id.tv_date)).setText(strDate);
        ((TextView) findViewById(R.id.day_of_week)).setText(getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        Log.d("Tag", strDate.toString());

        Thread synTask = new Thread(){
            @Override
            public void run() {
                int[] iv = {R.id.iv01, R.id.iv02, R.id.iv03, R.id.iv04};
                int[] tv = {R.id.tv01, R.id.tv02, R.id.tv03, R.id.tv04};

                for(int i = 0; i<4; i++){
                    weatherDisplays[i] = new WeatherDisplay();
                    ImageView imgview = (ImageView) findViewById(iv[i]);
                    TextView textview = (TextView) findViewById(tv[i]);
                    weatherDisplays[i].setImgView(imgview);
                    weatherDisplays[i].setTexView(textview);
                    tvAddClick(weatherDisplays[i].getImgView(), i);
                }
            }
        };
        synTask.start();
    }

    public void btnClick(View view) {
        weatherDisplays[0].getTexView().
                setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_selector));
        for(int i =1; i < 4; i++){
            weatherDisplays[i].getTexView()
                    .setBackgroundColor(Color.argb(0,0,0,0));
        }
        if(IsNetConnect.isNetworkAvalible(this)) {
            new DownloadUpdate().execute();
            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
        }
        else {Toast.makeText(MainActivity.this, "Internet is Unavaliable", Toast.LENGTH_SHORT).show(); }
    }

    public void tvAddClick(final ImageView imageview, final int i){
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDisplays[i].getTexView()
                        .setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_selector));
                if (weatherInfos != null){
                    ((TextView) findViewById(R.id.temperature_of_the_day)).setText(weatherInfos[i].getCurrent_temp());
                }
                int j = i-1;
                while(j>=0){
                    weatherDisplays[j].getTexView().setBackgroundColor(Color.argb(0,0,0,0));
                    j--;
                };
                j = i+1;
                while(j<=3){
                    weatherDisplays[j].getTexView().setBackgroundColor(Color.argb(0,0,0,0));
                    j++;
                };
            }
        });
    }

    public String getDayOfWeek(int order){
        Log.d("Tag", String.valueOf(order));
        String dayOfWeek = "";
        String [] week = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        dayOfWeek = week[order-1];
        return dayOfWeek;
    }

    private class DownloadUpdate extends AsyncTask<String, Void, WeatherInfo[]> {


        @Override
        protected WeatherInfo[] doInBackground(String... strings) {
            //String stringUrl = "https://mpianatra.com/Courses/info.txt";
            String stringUrl = "http://wthrcdn.etouch.cn/weather_mini?city=重庆";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            InputStream inputStream = null;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                String json = buffer.toString();
                WeatherInfo[] weatherInfo = JsonAnalyze.getWeather(json);
                weatherInfos = weatherInfo;
                return weatherInfo;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try{
                    reader.close();
                    inputStream.close();
                }catch (IOException e){e.printStackTrace();}
            }

            return null;
        }

        @Override
        protected void onPostExecute(WeatherInfo[] weatherInfo) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(weatherInfo[0].getCurrent_temp());
            String strType = weatherInfo[0].getWeather();
            ImageView ivCondition = ((ImageView)findViewById(R.id.img_weather_condition));
            if (strType.contains("雨")) ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.rainy_up));
            else if(strType.contains("晴")) ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
            else if(strType.contains("阴")) ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
            else ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.windy_small));

            for(int i = 0; i < 4; i++){
                strType = weatherInfo[i].getWeather();
                ivCondition =  weatherDisplays[i].getImgView();
                if (strType.contains("雨")) ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
                else if(strType.contains("晴")) ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
                else if(strType.contains("阴")) ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
                else ivCondition.setImageDrawable(getResources().getDrawable(R.drawable.windy_small));

                weatherDisplays[i].getTexView().setText(weatherInfo[i].getDate());
            }
        }
    }
}
