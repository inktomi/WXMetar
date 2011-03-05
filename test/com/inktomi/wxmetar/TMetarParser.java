package com.inktomi.wxmetar;

import com.inktomi.wxmetar.metar.Metar;
import org.junit.Test;

import static org.junit.Assert.*;


public class TMetarParser {
    public void testParse() {
        // KLAS 050156Z VRB03KT 10SM SCT250 17/M09 A3022 RMK AO2 SLP226 T01721089
        // KLAS 050556Z 24007KT 10SM FEW250 13/M06 A3025 RMK AO2 SLP236 T01331056 10194 20122 50005
        // KHND 050556Z 16004KT 10SM CLR 09/M06 A3030 RMK AO2 50006 T00881055 10188 20088 SLP236
        // KCLE 050551Z 17012G24KT 10SM SCT070 SCT085 OVC110 09/03 A3005 RMK AO2 SLP180 8/57/ 60004 T00940028 10094 20061 56026
        // KMIA 050553Z 08014G18KT 10SM SCT038 22/14 A3016 RMK AO2 SLP212 60000 T02170139 10233 20217 58010
        // KSEA 050553Z 15008KT 10SM -RA OVC032 06/04 A3003 RMK AO2 RAE10B45 SLP179 P0000 60012 T00560039 10083 20050 51011
        // KLAX 050553Z 15003KT 10SM CLR 14/11 A3018 RMK AO2 SLP219 T01440106 10161 20133 53008
        // KL35 051752Z AUTO 26006KT 10SM CLR 11/M19 A3030 RMK AO2
    }

    // 26006KT, 17012G24KT, VRB03KT

    @Test
    public void testWindGusting() {
        Metar test = new Metar();

        MetarParser.parseWinds("17012G24KT", test);

        assertEquals((long) test.winds.windGusts, 24l);
        assertEquals((long) test.winds.windDirection, 170l);
        assertFalse(test.winds.variable);
    }

    @Test
    public void testWindVariable() {
        Metar test = new Metar();

        MetarParser.parseWinds("VRB03KT", test);

        assertEquals((long) test.winds.windGusts, -1l);
        assertEquals((long) test.winds.windDirection, -1l);
        assertTrue(test.winds.variable);
    }

    @Test
    public void testWindNormal() {
        Metar test = new Metar();

        MetarParser.parseWinds("26006KT", test);

        assertEquals((long) test.winds.windGusts, -1l);
        assertEquals((long) test.winds.windDirection, 260l);
        assertEquals((long) test.winds.windSpeed, 6l);
        assertFalse(test.winds.variable);
    }
}
