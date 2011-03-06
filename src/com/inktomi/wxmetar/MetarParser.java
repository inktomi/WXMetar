package com.inktomi.wxmetar;

import com.inktomi.wxmetar.metar.Cloud;
import com.inktomi.wxmetar.metar.Metar;
import com.inktomi.wxmetar.metar.PresentWeather;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetarParser {
    private static Pattern NUMBER = Pattern.compile("^(\\d+)$");
    private static Pattern FRACTION = Pattern.compile("^(?:(\\d+) */ *(\\d+))SM$");

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

        // The day of the month is the first 2
        rval.dayOfMonth = Integer.parseInt(StringUtils.substring(tokens[1], 0, 2));

        // The time is from 0 to length-1
        rval.zuluTime = Integer.parseInt(StringUtils.substring(tokens[1], 2, tokens[1].length() - 1));

        // We start our actual parsing at the third element
        for (int i = 2; i < tokens.length; i++) {
            String token = tokens[i];
            String nextToken = null;
            if (tokens.length > i + 1) {
                nextToken = tokens[i + 1];
            }

            // First, we have AUTO or COR
            if (parseModifier(token, rval)) {
                continue;
            }

            if (parseWinds(token, rval)) {
                continue;
            }

            if (null != nextToken && parseVisibility(token, nextToken, rval)) {
                Matcher numberMatcher = NUMBER.matcher(token);
                Matcher fractionMatcher = FRACTION.matcher(nextToken);

                if (!StringUtils.endsWith(token, "SM")
                        && numberMatcher.matches()
                        && StringUtils.endsWith(nextToken, "SM")
                        && fractionMatcher.matches()) {

                    // Increment i one here since we had a fraction: 1 1/2SM
                    i = i + 1;
                }

                continue;
            }

            if (parsePresentWeather(token, rval)) {
                continue;
            }

            if (parseClouds(token, rval)) {
                continue;
            }

            if (parseTempDewpoint(token, rval)) {
                continue;
            }

            if (parseAltimeter(token, rval)) {
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
                metar.winds.windSpeed = Float.parseFloat(StringUtils.substring(token, 3, postionOfG));
            }

            // Is it just a normal wind measurement?
            // 26006
            if (postionOfG == -1 && !metar.winds.variable) {
                metar.winds.windSpeed = Float.parseFloat(StringUtils.substring(token, 2, token.length()));
            }
        }

        return rval;
    }

    // 1 1/2SM (damnit)
    // 10SM
    static boolean parseVisibility(String token, String nextToken, final Metar metar) {
        boolean rval = Boolean.FALSE;

        Matcher numberMatcher = NUMBER.matcher(token);
        Matcher fractionMatcher = FRACTION.matcher(nextToken);

        // Check for that fraction
        if (!StringUtils.endsWith(token, "SM")
                && numberMatcher.matches()
                && StringUtils.endsWith(nextToken, "SM")
                && fractionMatcher.matches()) {

            // add them together
            float visMiles = Float.parseFloat(token); // this should be something like 1 or 2

            // Assemble the fraction
            float visFraction = Float.parseFloat(fractionMatcher.group(1)) / Float.parseFloat(fractionMatcher.group(2));

            metar.visibility = visMiles + visFraction;

            rval = Boolean.TRUE;
        }

        // Get the SM out of the way
        if (StringUtils.endsWith(token, "SM")) {
            metar.visibility = Float.parseFloat(StringUtils.substring(token, 0, token.length() - 2));
        }

        return rval;
    }

    static boolean parsePresentWeather(String token, final Metar metar) {
        boolean rval = Boolean.FALSE;

        String qualifier = null;
        String weather = token.replace("+", "").replace("-", "");
        // Strip off the qualifier if the length == 4
        if (weather.length() == 4) {
            qualifier = StringUtils.substring(weather, 0, 2);
            weather = StringUtils.substring(weather, 2, 4);
        }

        // Strip off any possible modifier, and try to find a match
        PresentWeather presentWeather = null;
        try {
            presentWeather = PresentWeather.valueOf(weather);
        } catch (IllegalArgumentException e) {
            return rval;
        }

        if (null != presentWeather) {
            // Check for a modifier
            presentWeather.setIntensity(PresentWeather.Intensity.MODERATE);

            if (StringUtils.startsWith(token, "+")) {
                presentWeather.setIntensity(PresentWeather.Intensity.HEAVY);
            }

            if (StringUtils.startsWith(token, "-")) {
                presentWeather.setIntensity(PresentWeather.Intensity.LIGHT);
            }

            if (null != qualifier) {
                presentWeather.setQualifier(PresentWeather.Qualifier.valueOf(qualifier));
            }

            metar.presentWeather = presentWeather;

            rval = Boolean.TRUE;
        }

        return rval;
    }

    // CLR
    // SCT070 SCT085 OVC110
    static boolean parseClouds(String token, final Metar metar) {
        boolean rval = Boolean.FALSE;

        // Try to see if the first three characters match a Cloud.Type
        Cloud.Type cloudType = null;
        try {
            cloudType = Cloud.Type.valueOf(StringUtils.substring(token, 0, 3));
        } catch (IllegalArgumentException e) {
            return rval;
        }

        if (null != cloudType) {
            rval = Boolean.TRUE;

            Cloud c = new Cloud();
            c.cloudType = cloudType;

            if (!cloudType.equals(Cloud.Type.CLR)) {
                c.altitude = Integer.parseInt(StringUtils.substring(token, 3, token.length()));
            }

            metar.clouds.add(c);
        }

        return rval;
    }

    // 14/11  14/M01
    static boolean parseTempDewpoint(String token, final Metar metar) {
        boolean rval = Boolean.FALSE;

        // Is it in the right pattern?
        Pattern dewPoint = Pattern.compile("^(M)?(\\d{2})/(M)?(\\d{2})$");
        Matcher dewPointMatcher = dewPoint.matcher(token);

        if (dewPointMatcher.matches()) {
            rval = Boolean.TRUE;

            metar.temperature = Integer.parseInt(dewPointMatcher.group(2));

            // Is the temperature negative?
            if (null != dewPointMatcher.group(1)) {
                metar.temperature = 0 - metar.temperature;
            }

            metar.dewPoint = Integer.parseInt(dewPointMatcher.group(4));

            // Is the dewpoint negative?
            if (null != dewPointMatcher.group(3)) {
                metar.dewPoint = 0 - metar.dewPoint;
            }
        }

        return rval;
    }

    static boolean parseAltimeter(String token, final Metar metar) {
        boolean rval = Boolean.FALSE;

        if (StringUtils.startsWith(token, "A")
                && token.length() == 5) {
            rval = Boolean.TRUE;

            Integer alt = Integer.parseInt(StringUtils.substring(token, 1, token.length()));

            metar.altimeter = (float) alt / 100;
        }

        return rval;
    }
}
