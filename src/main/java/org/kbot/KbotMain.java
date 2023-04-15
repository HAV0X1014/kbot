package org.kbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.*;
import org.kbot.FileHandlers.*;
import org.kbot.UtilityCommands.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class KbotMain {
    static boolean debugmessagelog;
    static String token = KbotToken.read("ServerFiles/token.txt"); //replace token.txt with the filename of the txt with your token

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken(token).setIntents(Intent.MESSAGE_CONTENT, Intent.GUILD_MESSAGES).login().join();
        System.out.println("K-Bot logged in.");
        int startTime = (int) (System.currentTimeMillis() / 1000);
        User self = api.getYourself();
        String time = new Date().toString();

        api.updateActivity(ActivityType.WATCHING,"KiLAB's vids on repeat");

        //SlashCommand.with("ping", "Checks the functionality of this command").createGlobal(api).join();
        //SlashCommand.with("uptime", "Gets the uptime of the bot.").createGlobal(api).join();
        //SlashCommand.with("purge","Deletes the specified number of messages.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "Messages", "Amount of messages to delete.", true))).createGlobal(api).join();
        //SlashCommand.with("delete","Deletes the specified message by ID.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "MessageID", "MessageID of the message you want to delete.", true))).createGlobal(api).join();
        //SlashCommand.with("pfp","Get the avatar of a member.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.USER, "User","The user's PFP you want.", false))).createGlobal(api).join();
        //SlashCommand.with("serverinfo","Get info about the server.").createGlobal(api).join();
        //SlashCommand.with("setlogchannel","Set the deleted message log channel.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "Channel", "The channel you want to log deleted messages to.", true))).createGlobal(api).join();

        //SlashCommand.with("future", "Future Config.").createGlobal(api).join();

        /*
        String slashCommandID = "1094046846906802296";
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
                interaction.createImmediateResponder().setContent("Pong. Plain and simple.").setFlags(MessageFlag.EPHEMERAL).respond();
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

            if (interaction.getCommandName().equals("setlogchannel")) {
                interaction.createImmediateResponder().setContent(SetLogChannel.setlogchannel(interaction)).respond();
            }

            if (interaction.getCommandName().equals("future")) {
                interaction.createImmediateResponder().setContent("future config placeholder").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        });

        //legacy commands and V0Xpoints
        api.addMessageCreateListener(mc -> {
            String m = mc.getMessageContent();

            String author = mc.getMessageAuthor().toString();
            String server = mc.getServer().get().toString();
            if (debugmessagelog) {
                if (!mc.getMessageAuthor().equals(self)) {
                    try {
                        Webhook.send(ReadFirstLine.read("ServerFiles/webhookURL.txt"), "'" + m + "'\n\n- " + author + "\n- At " + time + " \n- " + server);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (m.toLowerCase().startsWith(".bestclient") || m.toLowerCase().startsWith("!bestclient")) {
                Color seppuku = new Color(153,0,238);
                EmbedBuilder e = new EmbedBuilder()
                        .setTitle("Seppuku")
                        .setDescription("Seppuku is one of the best clients of all time, ever!")
                        .setAuthor("Seppuku","https://github.com/seppukudevelopment/seppuku", "https://github.com/seppukudevelopment/seppuku/raw/master/res/seppuku_full.png")
                        .addField("Seppuku Download", "https://github.com/seppukudevelopment/seppuku/releases")
                        .addInlineField("Github", "https://github.com/seppukudevelopment/seppuku")
                        .addInlineField("Website", "https://seppuku.pw")
                        .setColor(seppuku)
                        .setFooter("Seppuku","https://github.com/seppukudevelopment/seppuku")
                        .setImage("https://github.com/seppukudevelopment/seppuku/blob/master/res/seppuku_full.png?raw=true")
                        .setThumbnail("https://github.com/seppukudevelopment/seppuku/blob/master/src/main/resources/assets/seppukumod/textures/seppuku-logo.png?raw=true");
                mc.getMessage().reply(e);
            }

            if (m.startsWith("!ml on")) {
                String id = mc.getMessageAuthor().getIdAsString();
                try {
                    if (Whitelist.whitelisted(id)) {
                        debugmessagelog = true;
                        mc.getMessage().reply("Debug Message Log on.");
                    } else {
                        mc.getMessage().reply("You are not on the debug whitelist.");
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else

            if (m.startsWith("!ml off")) {
                String id = mc.getMessageAuthor().getIdAsString();
                try {
                    if (Whitelist.whitelisted(id)) {
                        debugmessagelog = false;
                        mc.getMessage().reply("Debug Message Log off.");
                    } else {
                        mc.getMessage().reply("You are not on the debug whitelist.");
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //deleted message logger
        api.addMessageDeleteListener(md -> {
           if (!md.getMessageAuthor().get().getIdAsString().equals(self)) {
               Channel logChannel = md.getServer().get().getChannelById(DeletedMessageLogChannel.channelRetriever(md.getServer().get().getIdAsString())).get();

               EmbedBuilder e = new EmbedBuilder();
               e.setAuthor(md.getMessageAuthor().get().asUser().get());
               e.setColor(Color.orange);
               e.addField("\u200b","'" + md.getMessageContent().get() + "' \n\n - " + time + "\n" + "Deleted in : " + md.getChannel().toString());
               logChannel.asServerTextChannel().get().sendMessage(e);
           }
        });
    }
}
