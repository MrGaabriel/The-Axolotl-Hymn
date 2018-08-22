package me.mrgaabriel.axolotlhymn.listeners

import me.mrgaabriel.axolotlhymn.*
import net.dv8tion.jda.core.events.message.guild.*
import net.dv8tion.jda.core.hooks.*

class MessageReceiver : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.message.author.isBot)
            return

        AxolotlHymnLauncher.hymn.commandMap.forEach {
            if (it.matches(event.message))
                return
        }
    }
}