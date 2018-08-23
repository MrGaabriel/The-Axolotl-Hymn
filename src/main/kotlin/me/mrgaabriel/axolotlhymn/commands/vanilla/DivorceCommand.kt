package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.AxolotlHymnLauncher.hymn
import me.mrgaabriel.axolotlhymn.bson.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.exceptions.*

class DivorceCommand : AbstractCommand(
        "divorce",
        "Se divorcie com quem você está casado",
        "usuário",
        listOf("divorciar")
) {

    override fun run(message: Message, args: Array<String>) {
        val profile = hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        if (!profile.married) {
            message.channel.sendMessage(
                    ":thinking: **|** ${message.author.asMention} Você não está casado!"
            ).queue()

            return
        }

        hymn.jda.retrieveUserById(profile.marriedUser).queue({ user ->
            if (user == null)
                return@queue

            user.openPrivateChannel().queue({ channel ->
                channel.sendMessage(":broken_heart: **|** Seu parceiro, ${message.author.asMention} se divorciou de você!").queue({}, { e ->
                    if (e is ErrorResponseException) {
                        if (e.errorCode == 40002) {
                            return@queue // :shrug:
                        }
                    }
                })

                val userProfile = hymn.usersColl.find(
                        Filters.eq("_id", profile.marriedUser)
                ).firstOrNull() ?: UserProfile(profile.marriedUser)

                profile.married = false
                profile.marriedUser = ""

                userProfile.married = false
                userProfile.marriedUser = ""

                hymn.usersColl.replaceOne(
                        Filters.eq("_id", message.author.id),
                        profile,
                        ReplaceOptions().upsert(true)
                )

                hymn.usersColl.replaceOne(
                        Filters.eq("_id", userProfile.id),
                        userProfile,
                        ReplaceOptions().upsert(true)
                )

                message.channel.sendMessage(":broken_heart: **|** ${message.author.asMention} Você se divorciou do usuário <@${userProfile.id}>").queue()
            })
        })
    }
}