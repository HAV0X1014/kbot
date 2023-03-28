package org.kbot

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.activity.ActivityType
import org.javacord.api.entity.message.MessageFlag
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.user.UserStatus
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandOption
import org.json.JSONArray
import org.json.JSONObject
import org.kbot.command.Command
import org.kbot.util.getOption
import org.kbot.util.randomColour
import org.kbot.util.string
import java.awt.Color
import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * @author surge
 * @since 28/03/2023
 */
object Entrypoint {

    // local token
    private val token = File("token.txt").readText()

    // bot instance
    lateinit var bot: DiscordApi

    // logger
    val logger: Logger = LogManager.getLogger("K-Bot")

    // list of commands
    private val commands = mutableListOf<Command>()

    @JvmStatic
    fun main(args: Array<String>) {
        // create bot
        bot = DiscordApiBuilder()
            .setToken(token)
            .login()
            .join()

        logger.info("Successfully logged in as '${bot.yourself.name}'")

        Command("ping", "holy FUCK a ping command?! pretty nifty i would say") {
            // we can use `it` as a way to reference the `SlashCommandCreateEvent`
            // pretty nifty i would say
            it.interaction
                .createImmediateResponder()
                .setContent("pohng?!")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond()
        }

        Command("help", "helps somebody somewhere in the world", listOf(SlashCommandOption.createStringOption("command", "the command to help?", true))) {
            // find first command with that name
            val command = this.commands.firstOrNull { command -> command.name == it.getOption("command").string() }

            it.interaction
                .createImmediateResponder()
                .addEmbed(EmbedBuilder()
                    .setTitle(command?.name ?: "Command not found!")
                    .setDescription(command?.description ?: "")
                    .setColor(if (command != null) randomColour() else Color.RED)
                )
                .setFlags(MessageFlag.EPHEMERAL)
                .respond()
        }

        Command("country", "generates a country") {
            val json = JSONArray(this.javaClass.getResourceAsStream("/countries.json")!!.reader().readText())

            it.interaction
                .createImmediateResponder()
                .setContent(json.getJSONObject(Random.nextInt(json.length())).getString("name"))
                .setFlags(MessageFlag.EPHEMERAL)
                .respond()
        }

        // cool lambdas
        bot.addSlashCommandCreateListener { event ->
            val interaction = event.slashCommandInteraction

            // find first command
            commands.firstOrNull { it.name == interaction.commandName }?.execute(event) ?: // this `?:` operator checks if the left value is null, and if it is, executes the stuff on the right
                interaction.createImmediateResponder()
                    .setContent("Command not found!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond()
        }
    }

    /**
     * Registers the given [command] to the client-side commands list
     */
    fun register(command: Command) {
        SlashCommand.with(command.name, command.description)
            .also {
                command.arguments.forEach(it::addOption)
            }
            .createGlobal(this.bot)
            .join()

        commands.add(command)
    }

}