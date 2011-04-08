package com.inktomi.wxmetar;

import com.inktomi.wxmetar.metar.Cloud;
import com.inktomi.wxmetar.metar.Metar;
import com.inktomi.wxmetar.metar.PresentWeather;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class TMetarParser {
    Metar test;

    public void testParse() {
        // KLAS 052256Z 00000KT 10SM OVC250 19/M08 A3006 RMK AO2 SLP172 T01941078
        // KHND 052256Z 01005KT 10SM CLR 19/M07 A3012 RMK AO2 T01941066 SLP170

        // KLAS 050156Z VRB03KT 10SM SCT250 17/M09 A3022 RMK AO2 SLP226 T01721089
        // KLAS 050556Z 24007KT 10SM FEW250 13/M06 A3025 RMK AO2 SLP236 T01331056 10194 20122 50005
        // KHND 050556Z 16004KT 10SM CLR 09/M06 A3030 RMK AO2 50006 T00881055 10188 20088 SLP236
        // KCLE 050551Z 17012G24KT 10SM SCT070 SCT085 OVC110 09/03 A3005 RMK AO2 SLP180 8/57/ 60004 T00940028 10094 20061 56026
        // KMIA 050553Z 08014G18KT 10SM SCT038 22/14 A3016 RMK AO2 SLP212 60000 T02170139 10233 20217 58010
        // KSEA 050553Z 15008KT 10SM -RA OVC032 06/04 A3003 RMK AO2 RAE10B45 SLP179 P0000 60012 T00560039 10083 20050 51011
        // KLAX 050553Z 15003KT 10SM CLR 14/11 A3018 RMK AO2 SLP219 T01440106 10161 20133 53008
        // KL35 051752Z AUTO 26006KT 10SM CLR 11/M19 A3030 RMK AO2
    }

    @Before
    public void setup() {
        test = new Metar();
    }

    @Test
    public void testParseMetar() {
        test = MetarParser.parseMetar("KLAS 050156Z VRB03KT 10SM SCT250 13/M06 A3022 RMK AO2 SLP226 T01721089");

        assertEquals("KLAS", test.station);
        assertFalse(test.auto);

        assertEquals(5, test.dayOfMonth);
        assertEquals(1, test.zuluHour);
        assertEquals(56, test.zuluMinute);

        // Winds
        assertTrue(test.winds.variable);
        assertEquals(3, (long) test.winds.windSpeed);

        // Visibility
        assertEquals(10, (long) test.visibility);

        // Clouds
        assertEquals(1, test.clouds.size());
        assertEquals(Cloud.Type.SCT, test.clouds.get(0).cloudType);
        assertEquals(25000, test.clouds.get(0).altitude);

        // Temp and dew point
        assertEquals(13, test.temperature);
        assertEquals(-6, test.dewPoint);

        // Altimeter
        assertEquals(30.22f, test.altimeter, 1e-8f);
    }

    @Test
    public void testParseMetarFractionalVisibility() {
        test = MetarParser.parseMetar("KLAS 050156Z VRB03KT 1 1/2SM -SHRA SCT070 SCT085 OVC110 17/M09 A3022 RMK AO2 SLP226 T01721089");

        assertEquals("KLAS", test.station);
        assertFalse(test.auto);

        assertEquals(5, test.dayOfMonth);
        assertEquals(1, test.zuluHour);
        assertEquals(56, test.zuluMinute);

        // Winds
        assertTrue(test.winds.variable);
        assertEquals(3, (long) test.winds.windSpeed);

        // Visibility
        assertEquals(1.5, test.visibility, 1e-8);

        // Present Weather
        assertEquals(PresentWeather.RA, test.presentWeather);
        assertEquals(PresentWeather.Intensity.LIGHT, test.presentWeather.getIntensity());
        assertEquals(PresentWeather.Qualifier.SH, test.presentWeather.getQualifier());

        // Clouds
        assertEquals(3, test.clouds.size());
        assertEquals(Cloud.Type.SCT, test.clouds.get(0).cloudType);
        assertEquals(7000, test.clouds.get(0).altitude);
        assertEquals(Cloud.Type.SCT, test.clouds.get(1).cloudType);
        assertEquals(8500, test.clouds.get(1).altitude);
        assertEquals(Cloud.Type.OVC, test.clouds.get(2).cloudType);
        assertEquals(11000, test.clouds.get(2).altitude);

        // Temp and dew point
        assertEquals(17, test.temperature);
        assertEquals(-9, test.dewPoint);

        // Altimeter
        assertEquals(30.22f, test.altimeter, 1e-8f);
    }

    @Test
    public void testAuto() {
        MetarParser.parseModifier("AUTO", test);

        assertTrue(test.auto);
    }

    @Test
    public void testCorrectedAuto() {
        MetarParser.parseModifier("COR", test);

        assertTrue(test.auto);
    }

    @Test
    public void testCloudsinWindParser() {
        assertFalse(MetarParser.parseWinds("SCT250", test));
        assertFalse(MetarParser.parseWinds("OVC110", test));
        assertFalse(MetarParser.parseWinds("A3005", test));
        assertFalse(MetarParser.parseWinds("SLP212", test));
        assertFalse(MetarParser.parseWinds("11/M19", test));
    }

    @Test
    public void testWindGusting() {
        MetarParser.parseWinds("17012G24KT", test);

        assertEquals(24l, test.winds.windGusts, 1e-8f);
        assertEquals(170, test.winds.windDirection);
        assertEquals(12l, test.winds.windSpeed, 1e-8f);
        assertFalse(test.winds.variable);
    }

    @Test
    public void testWindVariable() {
        MetarParser.parseWinds("VRB03KT", test);

        assertEquals(-1l, (long) test.winds.windGusts);
        assertEquals(-1, test.winds.windDirection);
        assertTrue(test.winds.variable);
    }

    @Test
    public void testWindNormal() {
        MetarParser.parseWinds("26006KT", test);

        assertEquals(-1l, (long) test.winds.windGusts);
        assertEquals(260, test.winds.windDirection);
        assertEquals(6l, (long) test.winds.windSpeed);
        assertFalse(test.winds.variable);
    }

    @Test
    public void testFractionalVisibility() {
        MetarParser.parseVisibility("1", "1/2SM", test);

        assertEquals(1.5f, test.visibility, 1e-8f);
    }

    @Test
    public void testFullVisibility() {
        MetarParser.parseVisibility("10SM", "SCT250", test);

        assertEquals(10l, (long) test.visibility);
    }

    @Test
    public void testInvalidVisibility() {
        assertFalse(MetarParser.parseVisibility("10194", "20122", test));
        assertFalse(MetarParser.parseVisibility("T00940028", "10094", test));
        assertFalse(MetarParser.parseVisibility("RAE10B45", "SLP179", test));
        assertFalse(MetarParser.parseVisibility("VRB03KT", "10SM", test));
    }

    @Test
    public void testLightRain() {
        MetarParser.parsePresentWeather("-RA", test);

        assertEquals(PresentWeather.RA, test.presentWeather);
        assertEquals(PresentWeather.Intensity.LIGHT, test.presentWeather.getIntensity());
    }

    @Test
    public void testHeavyRain() {
        MetarParser.parsePresentWeather("+RA", test);

        assertEquals(PresentWeather.RA, test.presentWeather);
        assertEquals(PresentWeather.Intensity.HEAVY, test.presentWeather.getIntensity());
    }

    @Test
    public void testModerateRain() {
        MetarParser.parsePresentWeather("RA", test);

        assertEquals(PresentWeather.RA, test.presentWeather);
        assertEquals(PresentWeather.Intensity.MODERATE, test.presentWeather.getIntensity());
    }

    @Test
    public void testLightShoweringRain() {
        MetarParser.parsePresentWeather("-SHRA", test);

        assertEquals(PresentWeather.RA, test.presentWeather);
        assertEquals(PresentWeather.Intensity.LIGHT, test.presentWeather.getIntensity());
        assertEquals(PresentWeather.Qualifier.SH, test.presentWeather.getQualifier());
    }

    @Test
    public void testHeavyBlowingSnow() {
        MetarParser.parsePresentWeather("+BLSN", test);

        assertEquals(PresentWeather.SN, test.presentWeather);
        assertEquals(PresentWeather.Intensity.HEAVY, test.presentWeather.getIntensity());
        assertEquals(PresentWeather.Qualifier.BL, test.presentWeather.getQualifier());
    }

    @Test
    public void testCloudsClear() {
        MetarParser.parseClouds("CLR", test);

        assertEquals(Cloud.Type.CLR, test.clouds.get(0).cloudType);
    }

    @Test
    public void testCloudsSKT() {
        MetarParser.parseClouds("SCT085", test);

        assertEquals(Cloud.Type.SCT, test.clouds.get(0).cloudType);
        assertEquals(8500, test.clouds.get(0).altitude);
    }

    @Test
    public void testTemperatureAndDewpoint() {
        MetarParser.parseTempDewpoint("14/01", test);

        assertEquals(14, test.temperature);
        assertEquals(1, test.dewPoint);
    }

    @Test
    public void testNegTemperatureAndNegDewpoint() {
        MetarParser.parseTempDewpoint("M01/M10", test);

        assertEquals(-1, test.temperature);
        assertEquals(-10, test.dewPoint);
    }

    @Test
    public void testTemperatureAndNegDewpoint() {
        MetarParser.parseTempDewpoint("01/M10", test);

        assertEquals(1, test.temperature);
        assertEquals(-10, test.dewPoint);
    }

    @Test
    public void testAltimeter() {
        MetarParser.parseAltimeter("A2992", test);

        assertEquals(29.92f, test.altimeter, 1e-8f);
    }
}
