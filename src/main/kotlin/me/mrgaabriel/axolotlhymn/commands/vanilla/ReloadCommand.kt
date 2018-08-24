package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.google.gson.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.data.*
import me.mrgaabriel.axolotlhymn.listeners.*
import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.entities.*
import java.io.*

class ReloadCommand : AbstractCommand(
        "reload",
        "Recarrega o bot",
        "função",
        hideInHelp = true
) {

    override fun run(message: Message, args: Array<String>) {
        if (!AxolotlHymnLauncher.hymn.config.owners.contains(message.author.id)) {
            message.channel.sendMessage("${message.author.asMention} **Sem permissão!**").queue()
            return
        }

        val arg0 = args[0].toLowerCase()

        if (arg0 == "commands") {
            AxolotlHymnLauncher.hymn.loadCommands()

            message.channel.sendMessage("${message.author.asMention} Comandos recarregados com sucesso!").queue()
            return
        }

        if (arg0 == "config") {
            val file = File("config.json")
            val config = Gson().fromJson(file.readText(Charsets.UTF_8), HymnConfig::class.java)

            AxolotlHymnLauncher.hymn.config = config
            message.channel.sendMessage("${message.author.asMention} Configuração recarregada com sucesso!").queue()

            return
        }

        if (arg0 == "listeners") {
            AxolotlHymnLauncher.hymn.jda.registeredListeners.forEach {
                AxolotlHymnLauncher.hymn.jda.removeEventListener(it)
            }

            AxolotlHymnLauncher.hymn.jda.addEventListener(MessageReceiver(AxolotlHymnLauncher.hymn))
            message.channel.sendMessage("${message.author.asMention} Listeners recarregados com sucesso!").queue()
            return
        }

        if (arg0 == "mongo") {
            AxolotlHymnLauncher.hymn.loadMongo()

            message.channel.sendMessage("${message.author.asMention} MongoDB recarregado com sucesso!").queue()
            return
        }

        val file = File("config.json")
        val config = Gson().fromJson(file.readText(Charsets.UTF_8), HymnConfig::class.java)
        AxolotlHymnLauncher.hymn.config = config

        AxolotlHymnLauncher.hymn.jda.registeredListeners.forEach {
            AxolotlHymnLauncher.hymn.jda.removeEventListener(it)
        }

        AxolotlHymnLauncher.hymn.jda.addEventListener(MessageReceiver(AxolotlHymnLauncher.hymn))

        AxolotlHymnLauncher.hymn.loadMongo()
        AxolotlHymnLauncher.hymn.loadCommands()

        message.channel.sendMessage("${message.author.asMention} Bot recarregado com sucesso!").queue()
    }
}