/*
 * Copyright (c) 2011.
 *
 * You are free:
 * to Share — to copy, distribute and transmit the work
 * to Remix — to adapt the work
 *
 *
 * Under the following conditions:
 * Attribution — You must attribute the work in the manner specified by the author or licensor (but not in any way that suggests that they endorse you or your use of the work).
 *
 * Noncommercial — You may not use this work for commercial purposes.
 *
 * Share Alike — If you alter, transform, or build upon this work, you may distribute the resulting work only under the same or similar license to this one.
 *
 * For more information, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 */

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
