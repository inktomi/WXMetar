package com.inktomi.wxmetar.metar;

import android.view.Window;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: matthew
 * Date: 3/4/11
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Metar {
    public int dayOfMonth;
    public int zuluTime;
    public Wind winds;
    public float visibility;
    public List<Cloud> clouds;
    public int temperature;
    public int dewPoint;
    public float altimeter;

}
