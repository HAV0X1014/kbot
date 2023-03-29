package org.kbot.command

import org.javacord.api.event.interaction.SlashCommandCreateEvent
import org.javacord.api.interaction.SlashCommandOption
import org.kbot.Entrypoint

/**
 * @param name The name of this command, exactly what it would be used as, e.g. /ping
 * @param description The description of this command
 * @param action The code that will be called when this command is executed
 *
 * @author surge
 * @since 28/03/2023
 */
abstract class Command(val name: String, val description: String, val arguments: List<SlashCommandOption> = listOf()) {

    init {
        // register to client-side command list
        Entrypoint.register(this)
    }

    /**
     * Executes this command
     */
    abstract fun execute(event: SlashCommandCreateEvent)

}