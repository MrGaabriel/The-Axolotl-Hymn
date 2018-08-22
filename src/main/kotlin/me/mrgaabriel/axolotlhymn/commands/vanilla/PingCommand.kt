package me.mrgaabriel.axolotlhymn.commands.vanilla

import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*

class PingCommand : AbstractCommand(
        "ping",
        "Verifica o ping do bot relativo aos servidores do Discord"
) {

    override fun run(message: Message, args: Array<String>) {
        message.channel.sendMessage("${message.author.asMention} **Pong!** :ping_pong: `${message.jda.ping}ms`").queue()
    }
}