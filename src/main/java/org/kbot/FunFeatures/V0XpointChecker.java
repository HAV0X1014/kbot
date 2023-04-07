package org.kbot.FunFeatures;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class V0XpointChecker {
    public static void pointCheck(SlashCommandInteraction interaction) {
        try {
        String userID = interaction.getUser().getIdAsString();
        BufferedReader reader = new BufferedReader(new FileReader("V0XpointScores/" + userID + ".txt"));
        String score = reader.readLine();
        reader.close();
        interaction.createImmediateResponder().setContent("Your score is ``" + score + "``! Increase it by spelling HAV0X right!").setFlags(MessageFlag.EPHEMERAL).respond();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
