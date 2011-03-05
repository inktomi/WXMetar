package com.inktomi.wxmetar.metar;

import java.util.ArrayList;
import java.util.List;

public class Metar {
    public String station;
    public boolean auto;
    public int dayOfMonth;
    public int zuluTime;
    public Wind winds = new Wind();
    public float visibility;
    public PresentWeather presentWeather;
    public List<Cloud> clouds = new ArrayList<Cloud>();
    public int temperature;
    public int dewPoint;
    public float altimeter;

}
