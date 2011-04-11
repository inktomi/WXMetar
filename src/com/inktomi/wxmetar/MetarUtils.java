package com.inktomi.wxmetar;

import com.inktomi.wxmetar.metar.Cloud;
import com.inktomi.wxmetar.metar.Metar;

/**
 * Created by IntelliJ IDEA.
 * User: mruno
 * Date: 4/11/11
 * Time: 8:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetarUtils {

    /**
     * Returns the lowest cloud that forms a ceiling.. anything broken or overcast.
     * @param metar the metar to check for clouds in
     * @return the cloud that forms the ceiling, or null if no ceiling.
     */
    public static Cloud getCeiling(final Metar metar){

        for( Cloud c : metar.clouds){
            if( c.cloudType == Cloud.Type.BKN || c.cloudType == Cloud.Type.OVC ){
                return c;
            }
        }

        return null;
    }

}
