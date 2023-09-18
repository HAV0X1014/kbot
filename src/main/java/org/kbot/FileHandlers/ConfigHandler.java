package org.kbot.FileHandlers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    public static String getString(String option, String configFile) {
        JSONObject obj = new JSONObject(configFile);
        return obj.getJSONObject("Config").getString(option);
    }
    public static String[] getArray(String option, String configFile) {
        JSONObject obj = new JSONObject(configFile);
        JSONArray jsonArray = obj.getJSONObject("Config").getJSONArray(option);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list.toArray(new String[list.size()]);
    }
    public static boolean getBoolean(String option, String configFile) {
        JSONObject obj = new JSONObject(configFile);
        return obj.getJSONObject("Config").getBoolean(option);
    }
}
