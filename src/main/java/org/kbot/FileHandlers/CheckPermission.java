package org.kbot.FileHandlers;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.List;

public class CheckPermission {

    public static boolean checkPermission(SlashCommandInteraction event, PermissionType permcheck) {
        boolean hasPerm = false;
        List<Role> userRoles = event.getUser().getRoles(event.getServer().get());
        for (Role role : userRoles) {
            if (role.getPermissions().getAllowedPermission().contains(permcheck)) {
                hasPerm = true;
            } else {
                hasPerm = false;
            }
        }
        return hasPerm;
    }
}
