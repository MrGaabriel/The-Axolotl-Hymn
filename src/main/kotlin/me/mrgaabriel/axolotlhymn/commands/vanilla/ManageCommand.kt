package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.AxolotlHymnLauncher.hymn
import me.mrgaabriel.axolotlhymn.bson.*
import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.entities.*
import org.apache.commons.lang3.*

class ManageCommand : AbstractCommand(
        "manage"
) {

    override fun run(message: Message, args: Array<String>) {
        if (!AxolotlHymnLauncher.hymn.config.owners.contains(message.author.id)) {
            message.channel.sendMessage("${message.author.asMention} **Sem permissão!**").queue()
            return
        }

        val arg0 = args[0].toLowerCase()

        if (arg0 == "set_clyns") {
            val arg1 = args[1]

            val profile = AxolotlHymnLauncher.hymn.usersColl.find(
                    Filters.eq("_id", arg1)
            ).firstOrNull() ?: UserProfile(arg1)

            profile.money = args[2].toInt()
            AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                    Filters.eq("_id", arg1),
                    profile,
                    ReplaceOptions().upsert(true)
            )

            message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Sucesso!").queue()
        }

        if (arg0 == "set_xp") {
            val arg1 = args[1]

            val profile = AxolotlHymnLauncher.hymn.usersColl.find(
                    Filters.eq("_id", arg1)
            ).firstOrNull() ?: UserProfile(arg1)

            profile.xp = args[2].toInt()
            AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                    Filters.eq("_id", arg1),
                    profile,
                    ReplaceOptions().upsert(true)
            )

            message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Sucesso!").queue()
        }

        if (arg0 == "set_game") {
            val type = Game.GameType.valueOf(args[1])
            val name = args.remove(0).remove(0).joinToString(" ")

            hymn.jda.presence.game = Game.of(type, name, "https://www.twitch.tv/MrGaabriel")
            message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Sucesso!").queue()
        }
    }
}