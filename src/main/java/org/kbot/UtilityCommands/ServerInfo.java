package org.kbot.UtilityCommands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class ServerInfo {
    public static EmbedBuilder serverInfo(SlashCommandInteraction interaction, DiscordApi api) {
        String icon = interaction.getServer().get().getIcon().get().getUrl().toString();
        String creationDate = interaction.getServer().get().getCreationTimestamp().toString();
        long ownerID = interaction.getServer().get().getOwnerId();
        String systemChannelTag = interaction.getServer().get().getSystemChannel().get().getMentionTag();
        String systemChannelID = interaction.getServer().get().getSystemChannel().get().getIdAsString();
        int members = interaction.getServer().get().getMemberCount();
        int roles = interaction.getServer().get().getRoles().size();
        int channels = interaction.getServer().get().getChannels().size();
        String boost = interaction.getServer().get().getBoostLevel().toString();
        int boostCount = interaction.getServer().get().getBoostCount();
        String ownerName;
        try {
            ownerName = api.getUserById(ownerID).get().getDiscriminatedName().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        String serverID = interaction.getServer().get().getIdAsString();
        String serverName = interaction.getServer().get().getName().toString();
        int customEmojis = interaction.getServer().get().getCustomEmojis().size();

        EmbedBuilder e = new EmbedBuilder();
        e.setThumbnail(icon);
        e.setTitle(serverName);
        e.setColor(Color.orange);
        e.addField("Server ID :", serverID);
        e.addField("Server Owner :", "<@!" + ownerID + ">\nTag : ``" + ownerName + "``\nID : ``" + ownerID + "``");
        e.addField("Creation Date : ", creationDate);
        e.addField("System Channel : ", "Channel : " + systemChannelTag + "\nChannel ID : " + systemChannelID);
        e.addField("Server Stats : ", "Members : ``" + members + "``\nRoles : ``" + roles + "``\nChannels : ``" + channels + "``\nEmojis : ``" + customEmojis + "``");
        e.addField("Boosting : ", "Level : ``" + boost + "``\nBoosts : ``" + boostCount + "``");
        return e;
    }
}
