package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.utils.HymnUtils.hexToColor
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*

class FavColorCommand : AbstractCommand(
        "favcolor",
        "Muda sua cor favorita que será usada no seu perfil, etc",
        "hexadecimal",
        listOf("color")
) {

    override fun run(message: Message, args: Array<String>) {
        if (args.isEmpty()) {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Como usar... `t!favcolor`!?\n:small_blue_diamond: **|** `t!favcolor hexadecimal`").queue()
            return
        }

        val color = hexToColor(args[0])

        if (color == null) {
            message.channel.sendMessage(":thinking: **|** ${message.author.asMention} Cor inválida! Certifique-se que a cor é um código de cor hexadecimal como este: `#7289DA`").queue()
            return
        }

        val profile = AxolotlHymnLauncher.hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        profile.favColor = args[0]
        AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                Filters.eq("_id", message.author.id),
                profile
        )

        message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Cor favorita mudada para `${args[0]}`").queue()
    }
}