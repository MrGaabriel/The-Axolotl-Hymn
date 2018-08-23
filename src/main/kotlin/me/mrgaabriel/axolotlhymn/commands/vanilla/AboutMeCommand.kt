package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*

class AboutMeCommand : AbstractCommand(
        "sobremim",
        "Altera o seu texto de \"sobre mim\"",
        "texto",
        listOf("aboutme", "about", "sobre")
) {

    override fun run(message: Message, args: Array<String>) {
        if (args.isEmpty()) {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Como usar... `t!sobremim`!?\n:small_blue_diamond: **|** `t!sobremim texto`").queue()
            return
        }

        val text = args.joinToString(" ").replace("`", "")

        val profile = AxolotlHymnLauncher.hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        if (profile.money < 30) {
            message.channel.sendMessage(":money_with_wings: **|** ${message.author.asMention} Você não tem dinheiro o suficiente! Você precisa de mais `${30 - profile.money}` Clyns para fazer isto!").queue()
            return
        }

        profile.money -= 30
        profile.about = text
        AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                Filters.eq("_id", message.author.id),
                profile
        )

        message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Texto pessoal trocado para `$text`!\n:money_with_wings: **|** Você perdeu `30` Clyns!").queue()
    }
}