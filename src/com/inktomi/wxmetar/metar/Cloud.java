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
