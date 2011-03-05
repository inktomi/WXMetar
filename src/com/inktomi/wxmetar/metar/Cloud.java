package com.inktomi.wxmetar.metar;

/**
 * Created by IntelliJ IDEA.
 * User: matthew
 * Date: 3/4/11
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cloud {
    public Encoding code;
    public int altitude;

    public enum Encoding {
        SKC("Clear"),
        CLR("Clear"),
        FEW("Few"),
        SCT("Scattered"),
        BKN("Broken"),
        OVC("Overcast");


        Encoding(String type) {
            this.type = type;
        }

        private String type;

        public String getType() {
            return type;
        }
    }
}
