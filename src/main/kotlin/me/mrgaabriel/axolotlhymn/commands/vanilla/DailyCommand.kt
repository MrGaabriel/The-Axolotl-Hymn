package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*
import java.util.*

class DailyCommand : AbstractCommand(
        "daily",
        "Coleta Clyns diários"
) {

    override fun run(message: Message, args: Array<String>) {
        val hymn = AxolotlHymnLauncher.hymn

        val profile = hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        if (profile.lastDaily + 86400000 > System.currentTimeMillis()) {
            message.channel.sendMessage("\uD83D\uDD50 **|** ${message.author.asMention} Você já usou este comando em menos de um dia, por favor espere!").queue()
            return
        }

        val money = SplittableRandom().nextInt(200, 720)

        profile.money += money
        profile.lastDaily = System.currentTimeMillis()
        AxolotlHymnLauncher.hymn.usersColl.replaceOne(
                Filters.eq("_id", message.author.id),
                profile
        )

        message.channel.sendMessage(":small_blue_diamond: **|** ${message.author.asMention} Você ganhou $money clyns como prêmio diário!\n:small_blue_diamond: **|** Volte aqui em 24 horas!").queue()
    }
}