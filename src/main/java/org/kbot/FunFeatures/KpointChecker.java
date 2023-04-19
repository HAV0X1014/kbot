package org.kbot.FunFeatures;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.kbot.FileHandlers.ReadFirstLine;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class KpointChecker {
    public static void pointCheck(SlashCommandInteraction interaction) {
        String userID = interaction.getUser().getIdAsString();
        String score = ReadFirstLine.read("KpointScores/" + userID + ".txt");
        EmbedBuilder e = new EmbedBuilder();
        e.setTitle("Point Check");
        e.addField("\u200b","<@" + userID + ">\nScore : ``" + score + "``!\nIncrease it by spelling KiLAB right!");
        e.setColor(Color.red);

        interaction.createImmediateResponder().setContent("").addEmbed(e).respond();
    }

    public static void user(SlashCommandInteraction interaction) {
        String userID = interaction.getArgumentUserValueByIndex(0).get().getIdAsString();
        String score = ReadFirstLine.read("KpointScores/" + userID + ".txt");
        EmbedBuilder e = new EmbedBuilder();
        e.setTitle("Point Check");
        e.addField("\u200b", "<@" + userID + ">\nScore : ``" + score + "``\nIncrease it by spelling KiLAB right!");
        e.setColor(Color.red);

        interaction.createImmediateResponder().setContent("").addEmbed(e).respond();
    }

    public static void top(SlashCommandInteraction interaction) {
        String[] userIDs = new String[5];
        String[] userIDsTemp = new String[5];
        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<String> users = new ArrayList<>();
        ArrayList<Integer> storedScores = new ArrayList<>();
        ArrayList<Integer> storedScoresCopy = new ArrayList<>();

        try (Stream<Path> filePathStream= Files.walk(Paths.get("KpointScores/"))) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    users.add(String.valueOf(filePath));
                    storedScores.add(Integer.parseInt(ReadFirstLine.read(String.valueOf(filePath))));
                    storedScoresCopy.add(Integer.parseInt(ReadFirstLine.read(String.valueOf(filePath))));
                }
            });

            //this loop finds the top score, removes it, then finds the next highest score, removes that, etc
            //until there are no more elements in the storedScore arraylist
            for (int i=0; i < 5; i++) {
                Integer maxVal = Collections.max(storedScores); //get max value
                Integer topScoreIndex = storedScores.indexOf(maxVal); //get index of that max value
                scores.add(maxVal); //add the max value to an ArrayList to save it from being removed
                //you need to look up the element of the maxVal in the storedScores, then find the index of that, then find the filename of the scorecard
                int topScoreToRemove = topScoreIndex; //reassign Integer to int
                storedScores.remove(topScoreToRemove); //remove the top score from the storedScores ArrayList
            }

            for (int i=0; i < 5; i++) {
                String userIndex = users.get(storedScoresCopy.indexOf(scores.get(i))); //find the index of the top score in the ORIGINAL storedScores ArrayList. use this to find the userID
                //remove the filename parts and only get the userID
                userIDsTemp[i] = userIndex.replace("KpointScores/", "");
                userIDs[i] = userIDsTemp[i].replace(".txt","");
            }

            EmbedBuilder e = new EmbedBuilder();
            e.setTitle("Top 5 Scorers");
            e.addField("\u200b","<@" + userIDs[0] + "> : " + scores.get(0) + "\n<@" + userIDs[1] + "> : " + scores.get(1) + "\n<@" + userIDs[2] +"> : " + scores.get(2) + "\n<@" + userIDs[3] + "> : " + scores.get(3) + "\n<@" + userIDs[4] + "> : " + scores.get(4));
            e.setColor(Color.orange);
            interaction.createImmediateResponder().setContent("").addEmbed(e).respond();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
