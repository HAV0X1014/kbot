package org.kbot.FileHandlers;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteraction;
import java.io.FileNotFoundException;
import java.util.List;

public class CheckPermission {

    public static boolean checkPermission(SlashCommandInteraction event, PermissionType permcheck) {
        boolean hasPerm = false;
        List<Role> userRoles = event.getUser().getRoles(event.getServer().get());
        String userID = event.getUser().getIdAsString();
        for (Role role : userRoles) {
            if (role.getPermissions().getAllowedPermission().contains(permcheck)) {
                hasPerm = true;
            }
        }

        try {
            if (Whitelist.whitelisted(userID)) {
                hasPerm = true;
            }
            return hasPerm;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
