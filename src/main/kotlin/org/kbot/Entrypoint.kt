package org.kbot

import me.bush.translator.Language
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.entity.message.MessageFlag
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.SlashCommandCreateEvent
import org.javacord.api.interaction.SlashCommand
import org.javacord.api.interaction.SlashCommandBuilder
import org.javacord.api.interaction.SlashCommandOption
import org.json.JSONArray
import org.kbot.command.Command
import org.kbot.util.attachment
import org.kbot.util.getOption
import org.kbot.util.randomColour
import org.kbot.util.string
import java.awt.Color
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.random.Random
import me.bush.translator.Translator

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

        // anonymous objects
        object : Command("ping", "holy FUCK a ping command?! pretty nifty i would say") {
            override fun execute(event: SlashCommandCreateEvent) {
                event.interaction
                    .createImmediateResponder()
                    .setContent("pohng?!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond()
            }
        }

        object : Command("help", "helps somebody somewhere in the world", listOf(SlashCommandOption.createStringOption("command", "the command to help?", true))) {
            override fun execute(event: SlashCommandCreateEvent) {
                // find first command with that name
                val command = commands.firstOrNull { command -> command.name == event.getOption("command")!!.string() }

                event.interaction
                    .createImmediateResponder()
                    .addEmbed(EmbedBuilder()
                        .setTitle(command?.name ?: "Command not found!")
                        .setDescription(command?.description ?: "")
                        .setColor(if (command != null) randomColour() else Color.RED)
                    )
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond()
            }

        }

        object : Command("country", "generates a country") {
            override fun execute(event: SlashCommandCreateEvent) {
                val json = JSONArray(this.javaClass.getResourceAsStream("/countries.json")!!.reader().readText())

                event.interaction
                    .createImmediateResponder()
                    .setContent(json.getJSONObject(Random.nextInt(json.length())).getString("name"))
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond()
            }

        }

        object : Command("uploadconfig", "lets u upload a config", arguments = listOf(
            SlashCommandOption.createStringOption("name", "the config's name", true),
            SlashCommandOption.createAttachmentOption("config", "the config (must be .zip)", true)
        )) {
            override fun execute(event: SlashCommandCreateEvent) {
                val attachment = event.getOption("config")!!.attachment()

                if (!attachment.fileName.endsWith(".zip")) {
                    event.interaction
                        .createImmediateResponder()
                        .setContent("Attachment was not a .zip file!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond()

                    return
                }

                val directory = File("configs/${event.interaction.user.discriminatedName}/")

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                runCatching {
                    attachment.url.openStream().use {
                        Files.copy(it, Paths.get(directory.resolve("${event.getOption("name")!!.string()}.zip").path), StandardCopyOption.REPLACE_EXISTING)
                    }
                }.onSuccess {
                    event.interaction
                        .createImmediateResponder()
                        .setContent("Successfully uploaded config!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond()
                }.onFailure {
                    event.interaction
                        .createImmediateResponder()
                        .setContent("Failed to upload config!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond()

                    logger.error(it)
                }
            }
        }

        object : Command("downloadconfig", "download a config", arguments = listOf(
            SlashCommandOption.createStringOption("author", "who made the config (e.g. name#0000)", true),
            SlashCommandOption.createStringOption("name", "the configs name", true)
        )) {
            override fun execute(event: SlashCommandCreateEvent) {
                val name = event.getOption("author")!!.string()

                val file = File("configs/$name/${event.getOption("name")!!.string()}.zip")

                if (!file.exists()) {
                    event.interaction
                        .createImmediateResponder()
                        .setContent("https://cdn.discordapp.com/attachments/618100326095912961/1090037303226929225/20230325_235735.jpg")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond()

                    return
                }

                event.interaction
                        .createImmediateResponder()
                        .setContent("Attaching config!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond()

                MessageBuilder()
                    .addAttachment(file)
                    .setContent("${event.slashCommandInteraction.user.discriminatedName} requested $name's '${event.getOption("name")!!.string()}' config, here it is!")
                    .send(event.interaction.channel.get())
            }
        }

        object : Command("translate", "translate...", arguments = listOf(
            SlashCommandOption.createStringOption("text", "the text to translate", true),
            SlashCommandOption.createStringOption("target-language", "what to translate to", true)
        )) {
            override fun execute(event: SlashCommandCreateEvent) {
                val content: String = runCatching {
                    Translator().translateBlocking(event.getOption("text")!!.string(), Language.valueOf(event.getOption("target-language")!!.string().uppercase())).translatedText
                }.getOrElse {
                    event.interaction.createImmediateResponder()
                        .setContent("Failed to translate! Stack trace below:\n${it.stackTraceToString()}")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond()
                        .join()

                    return
                }

                event.interaction.createImmediateResponder()
                    .setContent(content)
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond()
                    .join()
            }
        }

        // refresh slash commands!!
        run {
            val builders = mutableSetOf<SlashCommandBuilder>()

            commands.forEach { command ->
                builders.add(SlashCommand.with(command.name, command.description)
                    .also {
                        command.arguments.forEach(it::addOption)
                    })
            }

            bot.bulkOverwriteGlobalApplicationCommands(builders).join()
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
        logger.info("Registering command: '${command.name}'")
        commands.add(command)
    }

}