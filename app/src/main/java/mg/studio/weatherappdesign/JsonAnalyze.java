package mg.studio.weatherappdesign;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonAnalyze {
    public static WeatherInfo[] getWeather(String jsonText){
        WeatherInfo[] weatherInfo = new WeatherInfo[5];
        try{
            JSONObject jsonObject = new JSONObject(jsonText);
            JSONObject dataInfo = jsonObject.getJSONObject("data");
            String city = dataInfo.getString("city");
            JSONArray forecast = (JSONArray) dataInfo.get("forecast");
            String week1 = "天一二三四五六";
            String[] week2 = {"Sun", "Mon", "Tue", "Wdn", "Thu", "Fri", "Sat"};
            for(int i = 0; i<5; i++){
                JSONObject weather = (JSONObject) forecast.get(i);
                weatherInfo[i] = new WeatherInfo();
                weatherInfo[i].setCity(city);
                weatherInfo[i].setWeather(weather.getString("type"));
                //weatherInfo[i].setDate(weather.getString("date"));

                String strWeek = weather.getString("date");
                strWeek = strWeek.substring(strWeek.length()-1);
                String dayOfWeek = week2[week1.indexOf(strWeek)];

                weatherInfo[i].setDate(dayOfWeek);

                String strTemp = null;
                String highTem = weather.getString("high");
                String lowTem = weather.getString("low");
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(highTem);
                highTem = m.replaceAll("").trim();
                m = p.matcher(lowTem);
                lowTem = m.replaceAll("").trim();
                strTemp = lowTem + "~" + highTem;

                weatherInfo[i].setCurrent_temp(strTemp);
            }

        }catch (Exception e){ e.printStackTrace();}

        return weatherInfo;
    }
}
