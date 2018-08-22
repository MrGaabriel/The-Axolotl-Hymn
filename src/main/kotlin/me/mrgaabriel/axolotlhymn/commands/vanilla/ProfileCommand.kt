package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.bson.UserProfile
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*
import java.awt.*

class ProfileCommand : AbstractCommand(
        "profile",
        "Vê o perfil de algum usuário",
        aliases = listOf("perfil", "pcc")
) {

    override fun run(message: Message, args: Array<String>) {
        val user = if (args.isEmpty()) {
            message.author
        } else {
            message.mentionedUsers.get(0)
        }

        if (user == null) {
            message.channel.sendMessage("${message.author.asMention} Usuário não encontrado!")
            return
        }

        val builder = EmbedBuilder()

        val hymn = AxolotlHymnLauncher.hymn

        val profile = hymn.usersColl.find(
                Filters.eq("_id", user.id)
        ).firstOrNull() ?: UserProfile(user.id)

        builder.apply {
            setColor(Color(0, 255, 0))

            setAuthor(user.name + "#" + user.discriminator, null, user.effectiveAvatarUrl)
            setTitle("${user.name} - Perfil")

            setDescription("**Sobre:** `${profile.about}`")

            addField("XP", profile.xp.toString(), true)
            addField("Clyns", profile.money.toString(), true)
            addField("Reputação", profile.rep.toString(), true)
        }

        message.channel.sendMessage(builder.build()).queue()
    }
}