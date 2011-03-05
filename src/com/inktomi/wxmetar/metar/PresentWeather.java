package com.inktomi.wxmetar.metar;

public enum PresentWeather {
    DZ("Drizzle"),
    RA("Rain"),
    SN("Snow"),
    SG("Snow Grains"),
    IC("Ice Crystals"),
    PL("Ice Pellets"),
    GR("Hail"),
    GS("Small Hail"),
    UP("Unknown Precipitation"),
    BR("Mist"),
    FG("Fog"),
    FU("Smoke"),
    VA("Volcanic Ash"),
    DU("Widespread Dust"),
    SA("Sand"),
    HZ("Haze"),
    PY("Spray"),
    PO("Dust Whirls"),
    SQ("Squalls"),
    FC("Funnel Cloud"),
    SS("Sandstorm"),
    DS("Duststorm");

    private String name;
    private Modifier modifier;

    PresentWeather(String name) {
        this.name = name;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public enum Modifier {
        LIGHT("-"),
        MODERATE(" "),
        HEAVY("+"),
        VC("");

        private String code;

        Modifier(String code) {
            this.code = code;
        }

        public Modifier getByCode(final String code){
            if( code.equals("") ){
                return Modifier.VC;
            } else if ( code.equals("+") ){
                return Modifier.HEAVY;
            } else if ( code.equals(" ") ){
                return Modifier.MODERATE;
            } else if ( code.equals("-") ){
                return Modifier.LIGHT;
            }

            return null;
        }
    }
}
