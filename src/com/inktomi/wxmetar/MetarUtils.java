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
     *
     * @param metar the metar to check for clouds in
     * @return the cloud that forms the ceiling, or null if no ceiling.
     */
    public static Cloud getCeiling(final Metar metar) {

        for (Cloud c : metar.clouds) {
            if (c.cloudType == Cloud.Type.BKN || c.cloudType == Cloud.Type.OVC) {
                return c;
            }
        }

        return null;
    }

}
