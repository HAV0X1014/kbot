package org.kbot.util

import org.javacord.api.entity.Attachment
import org.javacord.api.event.interaction.SlashCommandCreateEvent
import org.javacord.api.interaction.SlashCommandInteractionOption
import java.awt.Color
import kotlin.random.Random

/**
 * Gets a [SlashCommandInteractionOption] from it's [name]
 */
fun SlashCommandCreateEvent.getOption(name: String): SlashCommandInteractionOption {
    return this.slashCommandInteraction.getOptionByName(name).get()
}

/**
 * Gets the [String] value of a [SlashCommandInteractionOption]
 */
fun SlashCommandInteractionOption.string(): String {
    return this.stringValue.get()
}

/**
 * Gets the [Attachment] value of a [SlashCommandInteractionOption]
 */
fun SlashCommandInteractionOption.attachment(): Attachment {
    return this.attachmentValue.get()
}

/**
 * Generates a random [Color]
 */
fun randomColour(): Color {
    return Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
}