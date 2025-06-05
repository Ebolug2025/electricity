package com.etranzact.vasgate.electricity.kedc.utils;

import java.util.HashMap;

public abstract class InitProcessor
{
    private static HashMap<String, String> initParameters = null;

    public static HashMap<String, String> getInitParameters() { return initParameters; }

    public static void setInitParameters(HashMap<String, String> aInitParameters) { initParameters = aInitParameters; }

    public static String getProp(String key) {
        String prop = initParameters.get(key.toUpperCase());
        return prop;
    }
}
