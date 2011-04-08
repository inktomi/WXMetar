package com.inktomi.wxmetar.metar;

import java.util.ArrayList;
import java.util.List;

public class Metar {
    public String station;
    public boolean auto;
    public int dayOfMonth;
    public int zuluHour;
    public int zuluMinute;
    public Wind winds = new Wind();
    public float visibility;
    public PresentWeather presentWeather;
    public List<Cloud> clouds = new ArrayList<Cloud>();
    public int temperature;
    public int dewPoint;
    public float altimeter;

    @Override
    public String toString() {
        return "Metar{" +
                "station='" + station + '\'' +
                ", auto=" + auto +
                ", dayOfMonth=" + dayOfMonth +
                ", zuluHour=" + zuluHour +
                ", zuluMinute=" + zuluMinute +
                ", winds=" + winds +
                ", visibility=" + visibility +
                ", presentWeather=" + presentWeather +
                ", clouds=" + clouds +
                ", temperature=" + temperature +
                ", dewPoint=" + dewPoint +
                ", altimeter=" + altimeter +
                '}';
    }
}
