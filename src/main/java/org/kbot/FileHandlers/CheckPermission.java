package org.kbot.FileHandlers;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteraction;
import java.io.FileNotFoundException;
import java.util.List;

public class CheckPermission {

    public static boolean checkPermission(SlashCommandInteraction interaction, PermissionType permcheck) {
        boolean hasPerm = interaction.getServer().get().hasPermission(interaction.getUser(), permcheck);

        if (Whitelist.whitelisted(interaction.getUser().getIdAsString())) {
            hasPerm = true;
        }

        return hasPerm;
    }
}
