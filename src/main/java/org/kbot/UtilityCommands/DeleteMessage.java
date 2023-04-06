package org.kbot.UtilityCommands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.kbot.FileHandlers.CheckPermission;
import org.kbot.FileHandlers.Whitelist;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeleteMessage {
    public static void deleteMessage(SlashCommandInteraction interaction, DiscordApi api) {
        String messageID = interaction.getArgumentStringValueByIndex(0).orElse("Invalid");
        TextChannel channel = interaction.getChannel().get().asTextChannel().get();

        if (CheckPermission.checkPermission(interaction, PermissionType.MANAGE_MESSAGES)) {
            Pattern pattern = Pattern.compile("^[0-9]*?(?=-)");
            Matcher matcher = pattern.matcher(messageID);
            if (matcher.find()) {
                String matchedText = matcher.group();
                channel = api.getTextChannelById(matchedText).get();
            }
            messageID = messageID.replaceAll("^[0-9]*?(-)", "");
            Message message = api.getMessageById(messageID,channel).join();
            if (interaction.getServer().get() == message.getServer().get()) {
                message.delete();
                interaction.createImmediateResponder().setContent("Message Deleted.").setFlags(MessageFlag.EPHEMERAL).respond();
            } else {
                interaction.createImmediateResponder().setContent("You cannot delete messages across servers!").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        } else {
            interaction.createImmediateResponder().setContent("You do not have MANAGE_MESSAGES permissions, oops!");
        }
    }
}
