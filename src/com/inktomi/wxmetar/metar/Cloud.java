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

/**
 * Created by IntelliJ IDEA.
 * User: matthew
 * Date: 3/4/11
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cloud {
    public Type cloudType;
    public int altitude;

    public enum Type {
        SKC("Sky Clear"),
        CLR("Clear"),
        FEW("Few"),
        SCT("Scattered"),
        BKN("Broken"),
        OVC("Overcast");

        private String name;

        Type(String type) {
            this.name = type;
        }

        public String getName() {
            return name;
        }
    }
}
