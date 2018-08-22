package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.bson.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*

class RepCommand : AbstractCommand(
        "rep",
        "Dê reputação a algum usuário",
        "usuário",
        listOf("reputação", "reputation", "reputacao")
) {

    override fun run(message: Message, args: Array<String>) {
        if (args.size != 1) {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Como usar... `t!rep`?\n:small_blue_diamond: **|** Use `t!rep usuário`!").queue()
            return
        }

        val user = message.mentionedUsers.get(0)

        if (user == null) {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Como usar... `t!rep`?\n:small_blue_diamond: **|** Use `t!rep usuário`!").queue()
            return
        }

        if (user == message.author) {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Usuário inválido!").queue()
            return
        }

        var profile = AxolotlHymnLauncher.hymn.usersColl.find(
                Filters.eq("_id", user.id)
        ).first()

        if (profile == null) {
            AxolotlHymnLauncher.hymn.usersColl.insertOne(
                    UserProfile(user.id)
            )

            profile =  AxolotlHymnLauncher.hymn.usersColl.find(
                    Filters.eq("_id", user.id)
            ).firstOrNull()!!
        }

        if (profile.lastUsedRep + 3600000  > System.currentTimeMillis()) {
            message.channel.sendMessage(":no_good: **|** ${message.author.asMention} Você já usou este comando em menos de uma hora! Por favor, espere.").queue()
            return
        }

        profile.lastUsedRep = System.currentTimeMillis()
        profile.rep += 1

        AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                Filters.eq("_id", user.id),
                profile
        )

        message.channel.sendMessage("<a:aAxolotlBounce:470327201620033536> **|** ${message.author.asMention} gentilmente deu +1 rep para o usuário ${user.asMention}!").queue()
    }
}