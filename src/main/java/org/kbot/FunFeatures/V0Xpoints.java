package org.kbot.FunFeatures;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import java.io.*;

public class V0Xpoints {
    public static void hV0Xpoints(MessageCreateEvent mc, DiscordApi api, int updown) {
        String userID = mc.getMessageAuthor().getIdAsString();
        File file = new File("V0XpointScores/"+ userID + ".txt");
        int score = 0;

        try {
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter("V0XpointScores/" + userID + ".txt"));
                writer.write("0");
                writer.close();
            } else {
                BufferedReader reader = new BufferedReader(new FileReader("V0XpointScores/" + userID + ".txt"));
                String line = reader.readLine();
                if (line != null) {
                    score = Integer.parseInt(line);
                }
                reader.close();
            }
            score = score + updown;
            BufferedWriter writer = new BufferedWriter(new FileWriter("V0XpointScores/" + userID + ".txt", false));
            writer.write(String.valueOf(score));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
