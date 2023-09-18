package org.kbot.FileHandlers;

import java.util.Arrays;

import static org.kbot.KbotMain.configFile;

public class Whitelist {
    public static boolean whitelisted(String userID) {
        String[] whitelistedMembersArray = ConfigHandler.getArray("Whitelist", configFile);
        boolean whitelisted = false;
        String whitelistedMembers = Arrays.toString(whitelistedMembersArray);
        if (whitelistedMembers.contains(userID)) {
            whitelisted = true;
        }

        return whitelisted;
    }
}