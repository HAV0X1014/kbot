package org.kbot;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

public class KbotMain {

    static String token = KbotToken.read("token.txt"); //replace token.txt with the filename of the txt with your token

    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println("K-Bot logged in.");
        //SlashCommand.with("ping", "Checks the functionality of this command").createGlobal(api).join();
        //SlashCommand.with("future", "Future Config.").createGlobal(api).join();

        //slash commands
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equals("ping")) {
                slashCommandInteraction.createImmediateResponder().setContent("Pohng?").setFlags(MessageFlag.EPHEMERAL).respond();
            } else if (slashCommandInteraction.getCommandName().equals("future")) {
                slashCommandInteraction.createImmediateResponder().setContent("future config placeholder").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        });

    }
}
