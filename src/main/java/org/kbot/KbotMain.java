package org.kbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.kbot.FileHandlers.CheckPermission;
import org.kbot.FileHandlers.KbotToken;
import org.kbot.FileHandlers.Whitelist;
import org.kbot.FunFeatures.V0XpointChecker;
import org.kbot.FunFeatures.V0Xpoints;
import org.kbot.UtilityCommands.DeleteMessage;
import org.kbot.UtilityCommands.Purge;
import org.kbot.UtilityCommands.Uptime;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class KbotMain {

    static String token = KbotToken.read("token.txt"); //replace token.txt with the filename of the txt with your token

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken(token).setIntents(Intent.MESSAGE_CONTENT, Intent.GUILD_MESSAGES).login().join();
        System.out.println("K-Bot logged in.");
        int startTime = (int) (System.currentTimeMillis() / 1000);
        api.updateActivity(ActivityType.WATCHING,"KiLAB's vids on repeat");

        //SlashCommand.with("ping", "Checks the functionality of this command").createGlobal(api).join();
        //SlashCommand.with("future", "Future Config.").createGlobal(api).join();
        //SlashCommand.with("uptime", "Gets the uptime of the bot.").createGlobal(api).join();
        //SlashCommand.with("purge","Deletes the specified number of messages.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "Messages", "Amount of messages to delete.", true))).createGlobal(api).join();
        //SlashCommand.with("delete","Deletes the specified message by ID.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "MessageID", "MessageID of the message you want to delete.", true))).createGlobal(api).join();
        SlashCommand.with("pointcheck","Check your V0Xpoints.").createGlobal(api).join();
        /*
        String slashCommandID = "1092253336855646278";
            try {
                api.getGlobalSlashCommandById(Long.parseLong(slashCommandID)).get().delete();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
         */

        //slash commands
        api.addSlashCommandCreateListener(event -> {

            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals("ping")) {
                interaction.createImmediateResponder().setContent("Pong. Plain and simple.").respond();
            } else
            if (interaction.getCommandName().equals("uptime")) {
                interaction.createImmediateResponder().setContent(Uptime.uptime(startTime)).respond();
            } else

            if (interaction.getCommandName().equals("purge")) {
                Purge.purge(interaction);
            } else

            if (interaction.getCommandName().equals("delete")) {
                DeleteMessage.deleteMessage(interaction, api);
            }

            if (interaction.getCommandName().equals("pointcheck")) {
                V0XpointChecker.pointCheck(interaction);
            }

            if (interaction.getCommandName().equals("future")) {
                interaction.createImmediateResponder().setContent("future config placeholder").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        });

        api.addMessageCreateListener(mc -> {
            String m = mc.getMessageContent();

            System.out.println(m);

            if (m.toLowerCase().contains("nigg")) {
                mc.getChannel().sendMessage("__**come on get something original**__");
            } else {

            if (m.contains("HAV0X")) {
                int up = 1;
                V0Xpoints.hV0Xpoints(mc, api, up);
            } else {

            if (m.toLowerCase().contains("havox")) {
                int down = -1;
                V0Xpoints.hV0Xpoints(mc, api, down);
            }
            }
            }
        });
    }
}
