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
    private Intensity intensity;
    private Qualifier qualifier;

    PresentWeather(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    public Qualifier getQualifier() {
        return qualifier;
    }

    public void setQualifier(Qualifier qualifier) {
        this.qualifier = qualifier;
    }

    public enum Intensity {
        LIGHT("-"),
        MODERATE(" "),
        HEAVY("+"),
        VC("");

        private String code;

        Intensity(String code) {
            this.code = code;
        }
    }

    public enum Qualifier {
        MI("Shallow"),
        PR("Partial"),
        BC("Patches"),
        DR("Low Drifting"),
        BL("Blowing"),
        SH("Shower(s)"),
        TS("Thunderstorm"),
        FZ("Freezing");

        private String name;

        Qualifier(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
