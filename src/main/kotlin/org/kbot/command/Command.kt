package org.kbot.command

import org.javacord.api.event.interaction.SlashCommandCreateEvent
import org.kbot.Entrypoint

/**
 * @param name The name of this command, exactly what it would be used as, e.g. /ping
 * @param description The description of this command
 * @param action The code that will be called when this command is executed
 *
 * @author surge
 * @since 28/03/2023
 */
class Command(val name: String, val description: String, val action: (SlashCommandCreateEvent) -> Unit) {

    init {
        // register to client-side command list
        Entrypoint.register(this)
    }

    /**
     * Executes this command
     */
    fun execute(event: SlashCommandCreateEvent) {
        Entrypoint.logger.info("Executing '$name'")
        this.action(event)
    }

}