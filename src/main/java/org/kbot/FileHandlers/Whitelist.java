package org.kbot.FileHandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Whitelist {
    public static boolean whitelisted(String userID) throws FileNotFoundException {
        boolean isWhitelisted = false;
        File f = new File("whitelist.txt");
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String whitelistedID = s.nextLine();
            if(whitelistedID.contains(userID)) {
                isWhitelisted = true;
            }
        }
        return isWhitelisted;
    }
}
