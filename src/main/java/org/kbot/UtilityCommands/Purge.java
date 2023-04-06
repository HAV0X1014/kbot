package org.kbot.UtilityCommands;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.kbot.FileHandlers.CheckPermission;
import org.kbot.FileHandlers.Whitelist;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

public class Purge {
    public static void purge(SlashCommandInteraction interaction) {
        String num = interaction.getArgumentStringValueByIndex(0).orElse("Invalid");
        int deleteAmt = Integer.parseInt(num);
        String returnMessage;
        if (CheckPermission.checkPermission(interaction, PermissionType.MANAGE_MESSAGES)) {
            try {
                interaction.getChannel().get().getMessages(deleteAmt).get().deleteAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            returnMessage = "Purged "+ deleteAmt + " messages.";
        } else {
            returnMessage = "You do not have MANAGE_MESSAGES permissions, oops!";
        }
        interaction.createImmediateResponder().setContent(returnMessage).setFlags(MessageFlag.EPHEMERAL).respond();
    }
}
