package mg.studio.weatherappdesign;

public class WeatherInfo {
    private String city;
    private String current_temp;
    private String weather;
    private String date;

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrent_temp() {
        return current_temp;
    }
    public void setCurrent_temp(String current_temp) {
        this.current_temp = current_temp;
    }

    public String getWeather() {
        return weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

