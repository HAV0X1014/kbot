package org.kbot.FunFeatures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class RandFr {
    private static HttpURLConnection conn;

    @SuppressWarnings("finally")
    public static String randomFriend() {

        String[] unwantedResult = new String[]{"Alisa_Southerncross" , "Aliyan", "Category:Real_Animal_Friends", "Category:Cryptid_Friends",
                "Category:EX_Friends", "Category:Crossover_Friends", "Chestnut_Horse", "Chibi_Kumamon", "Coco", "Crunchyroll-Hime",
                "Debiru-sama", "De_Brazza's_Monkey", "Dororo", "Draco_Centauros", "Elephant_(Beast_King)", "Gachapin", "Giraffe_(Beast_King)",
                "Giroro", "Godzilla", "Gorilla_(Beast_King)", "HAW-206", "Hello_Kitty_Serval", "Hello_Mimmy_Serval",
                "Hi-no-Tori", "Higejii", "Higumamon", "Hippopotamus_(Beast_King)", "Keroro", "Kururu", "Leo",
                "Lion_(Beast_King)", "Logikoma", "Mongoose_(Beast_King)", "Mukku", "Old_World_Vulture_(Beast_King)",
                "Ostrich_(Beast_King)", "Palcoarai-san", "Palcoarai-san2", "Rabbit_Yukine", "Seal_Brown_Horse",
                "Slow_Loris", "Tachikoma_Type-A", "Tachikoma_Type-B", "Tachikoma_Type-C", "Tachikoma_Type-H",
                "Tachikoma_Type-S", "Tamama", "Tommy", "Uchikoma", "Unico", "Valcoara", "White_Horse",
                "Wildebeest_(Beast_King)", "Witch", "Zebra_(Beast_King)"};
        String line;
        StringBuffer responseContent = new StringBuffer();

        BufferedReader reader;

        try {
            URL url = new URL("https://japari-library.com/wiki/Special:RandomInCategory/Friends");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int status = conn.getResponseCode();

            if (status > 299) {
                return "Failed to get a page.";
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (responseContent.toString() != null) {
                String result = responseContent.toString();
                String[] things = result.split("\"wgPageName\":\"");    //in the HTML returned to us, look for the page title or similar
                result = things[1];                                                //and what is before it. everything before and including it gets removed.
                result = result.replaceAll("\",.*", "");    //what needs to be removed after the page title. removes everything.

                if (Arrays.asList(unwantedResult).contains(result)) {        //checks for unwanted results.
                    System.out.println("Unwanted result.");                    //post to console if there is an unwanted result that gets re-queried.
                    return randomFriend();
                }                                    //restart the page query process.

                result = "https://japari-library.com/wiki/" + result;        //the site as far down as you can go to the page.
                return result.replace(" ", "_");            //any remaining spaces get replaced with underscores.
            } else {
                return "Failed to get a page.";
            }
        }
    }
}
