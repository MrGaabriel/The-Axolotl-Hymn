package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.bson.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.message.guild.react.*
import net.dv8tion.jda.core.hooks.*

class MarryCommand : AbstractCommand(
        "marry",
        "Se case com alguém e seja feliz para sempre",
        "usuário",
        listOf("casar", "webcasar")
) {

    override fun run(message: Message, args: Array<String>) {
        if (args.isEmpty()) {
            message.channel.sendMessage(
                    ":thinking: **|** ${message.author.asMention} Como usar... `t!marry`?" + "\n" + ":small_blue_diamond: **|** `t!marry usuário`"
            ).queue()
            return
        }

        val profile = AxolotlHymnLauncher.hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        if (profile.married) {
            message.channel.sendMessage(
                    ":tbinking: **|** ${message.author.asMention} Você já está casado!"
            ).queue()

            return
        }

        val user = message.mentionedUsers.get(0)

        if (user == null) {
            message.channel.sendMessage(
                    ":thinking: **|** ${message.author.asMention} Como usar... `t!marry`?" + "\n" + ":small_blue_diamond: **|** `t!marry usuário`"
            ).queue()

            return
        }

        val userProfile = AxolotlHymnLauncher.hymn.usersColl.find(
                Filters.eq("_id", user.id)
        ).firstOrNull() ?: UserProfile(user.id)

        if (userProfile.married) {
            message.channel.sendMessage(
                    ":thinking: **|** ${message.author.asMention} Este usuário já está casado!"
            ).queue()

            return
        }

        val price = 1200

        if (profile.money < price) {
            message.channel.sendMessage(
                    ":money_with_wings: **|** ${message.author.asMention} Você não tem dinheiro suficiente para se casar!"
            ).queue()

            return
        } else if (userProfile.money < price) {
            message.channel.sendMessage(
                    ":money_with_wings: **|** ${message.author.asMention} O usuário ${user.asMention} não tem dinheiro suficiente para se casar!"
            ).queue()

            return
        }

        val jda = AxolotlHymnLauncher.hymn.jda

        message.channel.sendMessage(":ring: **|** ${user.asMention} ${message.author.asMention} quer se casar com você! Clique no :ring: para aceitar!").queue({ msg ->
            msg.addReaction("\uD83D\uDC8D").queue({
                jda.addEventListener(object: ListenerAdapter() {
                    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
                        if (event.messageId == msg.id && event.member.user == user && event.reactionEmote.name == "\uD83D\uDC8D") {
                            msg.delete().queue()

                            userProfile.married = true
                            userProfile.marriedUser = message.author.id
                            userProfile.money -= price

                            profile.married = true
                            profile.marriedUser = user.id
                            profile.money -= price

                            AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                                    Filters.eq("_id", user.id),
                                    userProfile,
                                    ReplaceOptions().upsert(true)
                            )


                            AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                                    Filters.eq("_id", message.author.id),
                                    profile,
                                    ReplaceOptions().upsert(true)
                            )

                            message.channel.sendMessage(":heart: **|** ${message.author.asMention} Vocês se casaram! Felicidade para os dois!").queue()
                        }
                    }
                })
            })
        })
    }
}