package com.inktomi.wxmetar;

import com.inktomi.wxmetar.metar.Metar;
import org.apache.commons.lang.StringUtils;

public class MetarParser {
    public static Metar parseMetar(final String input) {
        Metar finalMetar = new Metar();

        // Trim any extra space off the input
        String inputMetar = StringUtils.trimToEmpty(input);

        // Just to be sure.
        inputMetar = inputMetar.toUpperCase();

        // Split the metar on spaces
        String[] tokens = StringUtils.split(inputMetar);

        if (null != tokens) {
            finalMetar = parseTokens(tokens);
        }

        return finalMetar;
    }

    /**
     * Parses a String[] of metar parts into a Metar
     * <p/>
     * KCLE 050551Z 17012G24KT 10SM SCT070 SCT085 OVC110 09/03 A3005 RMK AO2 SLP180 8/57/ 60004 T00940028 10094 20061 56026
     * KL35 051752Z AUTO 26006KT 10SM CLR 11/M19 A3030 RMK AO2
     *
     * @param tokens the tokens to parse
     * @return the assembled Metar
     */
    private static Metar parseTokens(final String[] tokens) {
        Metar rval = new Metar();

        rval.station = tokens[0];

        // The time is from 0 to length-1
        rval.zuluTime = Integer.parseInt(StringUtils.substring(tokens[1], 0, tokens[1].length() - 1));

        // We start our actual parsing at the third element
        for (int i = 2; i < tokens.length; i++) {
            // First, we have AUTO or COR
            if (parseModifier(tokens[i], rval)) {
                continue;
            }

        }

        return rval;
    }

    // Returns true if we found the item we were looking for to break the loop
    static boolean parseModifier(String token, final Metar metar) {
        boolean rval = Boolean.FALSE;

        if (StringUtils.equals(token, "AUTO")) {
            metar.auto = Boolean.TRUE;
            rval = Boolean.TRUE;
        }

        if (StringUtils.equals(token, "COR")) {
            metar.auto = Boolean.TRUE;
            rval = Boolean.TRUE;
        }

        return rval;
    }

    // todo: Winds over 100 knots
    // 26006KT, 17012G24KT, VRB03KT
    static boolean parseWinds(String token, final Metar metar) {
        boolean rval = Boolean.FALSE;

        // Winds will be the only element that ends in knots
        if (StringUtils.endsWith(token, "KT")) {

            // At this point, we know we should handle this token
            rval = Boolean.TRUE;

            // Remove the KT
            token = StringUtils.remove(token, "KT");

            // Is it variable?
            if (StringUtils.startsWith(token, "VRB")) {
                metar.winds.variable = Boolean.TRUE;

                // Trim of the VRB
                token = StringUtils.remove(token, "VRB");

                metar.winds.windSpeed = Float.parseFloat(token);

                // Stop processing this token
                return rval;
            }

            // At this point, we know the first 3 chars are wind direction
            metar.winds.windDirection = Float.parseFloat(StringUtils.substring(token, 0, 3));

            // Is it gusting? 17012G24
            int postionOfG = StringUtils.indexOf(token, "G");
            if (postionOfG > -1) {
                metar.winds.windGusts = Float.parseFloat(StringUtils.substring(token, postionOfG + 1, token.length()));
            }

            // Is it just a normal wind measurement?
            // 26006
            if (postionOfG == -1 && !metar.winds.variable) {
                metar.winds.windSpeed = Float.parseFloat(StringUtils.substring(token, 2, token.length()));
            }
        }

        return rval;
    }
}
