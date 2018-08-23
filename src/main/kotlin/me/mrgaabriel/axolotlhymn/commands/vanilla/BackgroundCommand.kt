package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*
import java.net.*

class BackgroundCommand : AbstractCommand(
        "background",
        "Mude o seu papel de parede do perfil",
        "url/imagem",
        listOf("papeldeparede", "bg")
) {

    override fun run(message: Message, args: Array<String>) {
        val profile = AxolotlHymnLauncher.hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        if (message.attachments.isNotEmpty()) {
            val attachment = message.attachments.first()

            if (attachment.isImage) {
                if (profile.money < 200) {
                    message.channel.sendMessage(":money_with_wings: **|** ${message.author.asMention} Você não tem Clyns suficientes!").queue()
                    return
                }

                profile.money -= 200
                profile.backgroundUrl = attachment.url
                AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                        Filters.eq("_id", message.author.id),
                        profile
                )

                message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Background trocado com sucesso!\n:money_with_wings: **|** -200 Clyns!").queue()
            } else {
                message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Insira uma imagem!").queue()
            }

            return
        }



        if (args.isNotEmpty() && isValidUrl(args[0])) {
            if (profile.money < 200) {
                message.channel.sendMessage(":money_with_wings: **|** ${message.author.asMention} Você não tem Clyns suficientes!").queue()
                return
            }

            profile.money -= 200
            profile.backgroundUrl = args[0]
            AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                    Filters.eq("_id", message.author.id),
                    profile
            )

            message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Background trocado com sucesso!\n:money_with_wings: **|** -200 Clyns!").queue()
        } else {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Insira uma imagem!").queue()
        }
    }

    fun isValidUrl(link: String): Boolean {
        try {
            val url = URL(link)

            return true
        } catch (e: MalformedURLException) {
            return false
        }
    }
}