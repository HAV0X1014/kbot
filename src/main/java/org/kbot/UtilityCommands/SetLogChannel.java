package org.kbot.UtilityCommands;


import org.javacord.api.entity.channel.Channel;

import org.javacord.api.entity.permission.*;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.kbot.FileHandlers.CheckPermission;
import org.kbot.FileHandlers.Whitelist;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SetLogChannel {
    public static String setlogchannel(SlashCommandInteraction interaction) {
        String logChannelID = interaction.getArgumentByIndex(0).get().getChannelValue().get().asTextChannel().get().getIdAsString();
        String serverID = interaction.getServer().get().getIdAsString();
        String returnString;

        try {
            if (CheckPermission.checkPermission(interaction, PermissionType.MANAGE_CHANNELS) || Whitelist.whitelisted(interaction.getUser().getIdAsString())) {
                FileWriter fw;
                try {
                    fw = new FileWriter("ServerFiles/" + serverID + ".txt");

                    fw.write(logChannelID);
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                returnString = "Log channel set to <#" + logChannelID + ">";
            } else {
                returnString = "You do not have MANAGE_CHANNELS permissions.";
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    return returnString;
    }
}
