package org.kbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.kbot.FileHandlers.KbotToken;
import org.kbot.FunFeatures.RandFr;
import org.kbot.FunFeatures.V0XpointChecker;
import org.kbot.FunFeatures.V0Xpoints;
import org.kbot.FunFeatures.Wikipedia;
import org.kbot.UtilityCommands.*;

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
        //SlashCommand.with("pointcheck","Check your V0Xpoints.").createGlobal(api).join();
        //SlashCommand.with("wiki","Get a random Wikipedia article.").createGlobal(api).join();
        //SlashCommand.with("pfp","Get the avatar of a member.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.USER, "User","The user's PFP you want.", false))).createGlobal(api).join();
        //SlashCommand.with("serverinfo","Get info about the server.").createGlobal(api).join();
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
            }

            if (interaction.getCommandName().equals("uptime")) {
                interaction.createImmediateResponder().setContent(Uptime.uptime(startTime)).respond();
            }

            if (interaction.getCommandName().equals("purge")) {
                Purge.purge(interaction);
            }

            if (interaction.getCommandName().equals("delete")) {
                DeleteMessage.deleteMessage(interaction, api);
            }

            if (interaction.getCommandName().equals("pfp")) {
                Pfp.pfp(interaction);
            }

            if (interaction.getCommandName().equals("serverinfo")) {
                interaction.createImmediateResponder().setContent("").addEmbed(ServerInfo.serverInfo(interaction, api)).respond();
            }

            if (interaction.getCommandName().equals("pointcheck")) {
                V0XpointChecker.pointCheck(interaction);
            }

            if (interaction.getCommandName().equals("wiki")) {
                interaction.createImmediateResponder().setContent(Wikipedia.randomArticle()).respond();
            }

            if (interaction.getCommandName().equals("miat")) {
                interaction.createImmediateResponder().setContent("https://github.com/balls99dotexe/images/blob/main/miatas/miata" + (int) Math.floor(1 + Math.random() * 13) + ".png?raw=true").respond();
            }

            if (interaction.getCommandName().equals("future")) {
                interaction.createImmediateResponder().setContent("future config placeholder").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        });

        api.addMessageCreateListener(mc -> {
            String m = mc.getMessageContent();

            System.out.println(m);
            if (m.startsWith("[randfr")) {
                mc.getMessage().reply(RandFr.randomFriend());
            } else {

            if (m.toLowerCase().contains("nigg")) {
                mc.getChannel().sendMessage("__**come on get something original**__");
            } else {

            if (m.contains("HAV0X")) {
                int up = 1;
                V0Xpoints.hV0Xpoints(mc, api, up);
            } else {

            if (m.toLowerCase().contains("topi")) {
                int up = 1;
                V0Xpoints.hV0Xpoints(mc, api, up);
            } else {

            if (m.toLowerCase().contains("seppuku")) {
                int up = 1;
                V0Xpoints.hV0Xpoints(mc, api, up);
            } else {

            if (m.toLowerCase().contains("havox")) {
                int down = -1;
                V0Xpoints.hV0Xpoints(mc, api, down);
            } else {

            if (m.toLowerCase().contains("i like kotlin")) {
                int down = -1;
                V0Xpoints.hV0Xpoints(mc, api, down);

            }
            }
            }
            }
            }
            }
            }
        });
    }
}
