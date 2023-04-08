package org.kbot.UtilityCommands;

import org.javacord.api.entity.Icon;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Pfp {
    public static void pfp(SlashCommandInteraction interaction) {
        if (interaction.getArgumentByIndex(0).isPresent() == Boolean.parseBoolean(null)) {
            Icon icon = interaction.getUser().getAvatar();
            String pfp = icon.getUrl().toString();
            interaction.createImmediateResponder().setContent(pfp).respond();
        } else {
            if (interaction.getArgumentByIndex(0).isPresent()) {
                Icon icon = interaction.getArgumentUserValueByIndex(0).get().getAvatar();
                String pfp = icon.getUrl().toString();
                interaction.createImmediateResponder().setContent(pfp).respond();
            }
        }
    }
}
