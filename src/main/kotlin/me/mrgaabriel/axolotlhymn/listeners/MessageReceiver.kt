package me.mrgaabriel.axolotlhymn.listeners

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.bson.*
import net.dv8tion.jda.core.events.message.guild.*
import net.dv8tion.jda.core.hooks.*

class MessageReceiver(val hymn: AxolotlHymn) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.message.author.isBot)
            return

        val xp = event.message.contentDisplay.length / 7

        val profile = hymn.usersColl.find(
                Filters.eq("_id", event.author.id)
        ).firstOrNull()

        if (profile == null) {
            val userProfile = UserProfile(event.author.id)

            userProfile.xp += xp
            hymn.usersColl.insertOne(
                    userProfile
            )
        } else {
            profile.xp += xp

            hymn.usersColl.replaceOne(
                    Filters.eq("_id", event.author.id),
                    profile
            )
        }

        AxolotlHymnLauncher.hymn.commandMap.forEach {
            if (it.matches(event.message))
                return
        }
    }
}