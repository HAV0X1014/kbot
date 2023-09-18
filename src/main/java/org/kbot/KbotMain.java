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
import org.kbot.FunFeatures.Collatz;
import org.kbot.FunFeatures.KpointChecker;
import org.kbot.FunFeatures.Kpoints;
import org.kbot.FunFeatures.Vase64;
import org.kbot.UtilityCommands.*;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class KbotMain {
    static boolean debugmessagelog;
    public static String configFile = ReadFull.read("ServerFiles/config.json");
    static String token = ConfigHandler.getString("Token", configFile);
    static String prefix = ConfigHandler.getString("Prefix", configFile);
    static String botName = ConfigHandler.getString("BotName", configFile);
    static String[] reWordsGoodWords = ConfigHandler.getArray("ReWordsGoodWords", configFile);
    static String[] reWordsGoodWordsExactMatch = ConfigHandler.getArray("ReWordsGoodWordsExactMatch", configFile);
    static String reWordsGoodWordsEmoji = ConfigHandler.getString("ReWordsGoodWordsEmoji", configFile);
    static String[] reWordsBadWords = ConfigHandler.getArray("ReWordsBadWords", configFile);
    static String[] reWordsBadWordsExactMatch = ConfigHandler.getArray("ReWordsBadWordsExactMatch", configFile);
    static String reWordsBadWordsEmoji = ConfigHandler.getString("ReWordsBadWordsEmoji", configFile);
    static Boolean registerSlashCommands = ConfigHandler.getBoolean("RegisterSlashCommands", configFile);
    static String statusText = ConfigHandler.getString("StatusText", configFile);

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken(token).setIntents(Intent.MESSAGE_CONTENT, Intent.GUILD_MESSAGES).login().join();
        System.out.println(botName + " logged in.");
        int startTime = (int) (System.currentTimeMillis() / 1000);
        User self = api.getYourself();
        String time = new Date().toString();

        api.updateActivity(ActivityType.PLAYING,statusText);

        if (registerSlashCommands) {
            SlashCommand.with("ping", "Checks the functionality of this command").createGlobal(api).join();
            SlashCommand.with("uptime", "Gets the uptime of the bot.").createGlobal(api).join();
            SlashCommand.with("purge","Deletes the specified number of messages.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "Messages", "Amount of messages to delete.", true))).createGlobal(api).join();
            SlashCommand.with("delete","Deletes the specified message by ID.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "MessageID", "MessageID of the message you want to delete.", true))).createGlobal(api).join();
            SlashCommand.with("pfp","Get the avatar of a member.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.USER, "User","The user's PFP you want.", false))).createGlobal(api).join();
            SlashCommand.with("serverinfo","Get info about the server.").createGlobal(api).join();
            SlashCommand.with("setlogchannel","Set the deleted message log channel.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "Channel", "The channel you want to log deleted messages to.", true))).createGlobal(api).join();
            SlashCommand.with("kpoints","Check your KiLAB points, the top overall, or another user's points.", Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.USER, "user", "User to check.",false), SlashCommandOption.create(SlashCommandOptionType.BOOLEAN,"top","See the top 5 earners."))).createGlobal(api).join();

            SlashCommand.with("future", "Future Config.").createGlobal(api).join();
            System.out.println("SLASH COMMANDS REGISTERED! Set \"RegisterSlashCommands\" to \"false\" in config.json!");
        }
        /*
        String slashCommandID = "1094842827554426890";
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
            String command = interaction.getCommandName();
            switch (command) {
                case "uptime":
                    interaction.createImmediateResponder().setContent(Uptime.uptime(startTime)).respond();
                    break;
                case "purge":
                    Purge.purge(interaction);
                    break;
                case "delete":
                    DeleteMessage.deleteMessage(interaction, api);
                    break;
                case "pfp":
                    Pfp.pfp(interaction);
                    break;
                case "serverinfo":
                    interaction.createImmediateResponder().setContent("").addEmbed(ServerInfo.serverInfo(interaction, api)).respond();
                    break;
                case "kpoints":
                    if (interaction.getArgumentUserValueByIndex(0).isPresent()) {
                        KpointChecker.user(interaction);
                    }
                    if (interaction.getArgumentBooleanValueByIndex(0).isPresent()) {
                        KpointChecker.top(interaction);
                    }
                    if (!interaction.getOptionByIndex(0).isPresent()) {
                        KpointChecker.pointCheck(interaction);
                    }
                    break;
                case "future":
                    interaction.createImmediateResponder().setContent("future config placeholder").setFlags(MessageFlag.EPHEMERAL).respond();
                    break;
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

            if (m.startsWith(prefix)) {
                System.out.println(m);
            }

            if (m.startsWith(prefix)) {
                String[] parts = m.split(" ",2);
                String command = parts[0].toLowerCase().replace(prefix,"");

                switch (command) {
                    case "base64":
                        mc.getMessage().reply(Vase64.vase64(m));
                        break;
                    case "collatz":
                        if (parts.length > 1) {
                            String number = parts[1].replaceAll("[^0-9]","");
                            mc.getMessage().reply(Collatz.collatz(number));
                        } else {
                            Random ran = new Random();
                            int number = ran.nextInt(100000000);
                            mc.getMessage().reply(Collatz.collatz(String.valueOf(number)));
                        }
                        break;
                    case "bestclient":
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
                        break;
                    case "ml":
                        if (parts.length > 1) {
                            String toggle = parts[1];
                            String id = mc.getMessageAuthor().getIdAsString();
                            switch (toggle) {
                                case "on":
                                    if (Whitelist.whitelisted(id)) {
                                        debugmessagelog = true;
                                        mc.getMessage().reply("Debug Message Log on.");
                                    } else {
                                        mc.getMessage().reply("You are not on the debug whitelist.");
                                    }
                                    break;
                                case "off":
                                    if (Whitelist.whitelisted(id)) {
                                        debugmessagelog = false;
                                        mc.getMessage().reply("Debug Message Log off.");
                                    } else {
                                        mc.getMessage().reply("You are not on the debug whitelist.");
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }

            for (String wordsGoodWordsExactMatch : reWordsGoodWordsExactMatch) {
                if (m.contains(wordsGoodWordsExactMatch)) {
                    Kpoints.kpoints(mc,1);
                    mc.getMessage().addReaction(reWordsGoodWordsEmoji);
                }
            }
            for (String reWordsGoodWord : reWordsGoodWords) {
                if (m.toLowerCase().contains(reWordsGoodWord)) {
                    Kpoints.kpoints(mc,1);
                    mc.getMessage().addReaction(reWordsGoodWordsEmoji);
                }
            }
            for (String wordsBadWordsExactMatch : reWordsBadWordsExactMatch) {
                if (m.contains(wordsBadWordsExactMatch)) {
                    Kpoints.kpoints(mc,-1);
                    mc.getMessage().addReaction(reWordsBadWordsEmoji);
                }
            }
            for (String reWordsBadWord : reWordsBadWords) {
                if (m.toLowerCase().contains(reWordsBadWord)) {
                    Kpoints.kpoints(mc, -1);
                    mc.getMessage().addReaction(reWordsBadWordsEmoji);
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
