package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.AxolotlHymnLauncher.hymn
import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*

class HelpCommand : AbstractCommand(
        "help",
        "Consiga ajuda dos comandos",
        aliases = listOf("ajuda", "comandos", "commands"),
        hideInHelp = true
) {

    override fun run(message: Message, args: Array<String>) {
        val profile = hymn.usersColl.find(
                Filters.eq("_id", message.author.id)
        ).first()!!

        val color = HymnUtils.hexToColor(profile.favColor) ?: return

        val builder = EmbedBuilder()

        builder.setAuthor(message.author.name + "#" + message.author.discriminator, null, message.author.effectiveAvatarUrl)
        builder.setTitle("Axolotl Hymn - Comandos")
        builder.setColor(color)

        builder.setDescription("**Comandos disponíveis (${hymn.commandMap.filter { !it.hideInHelp }.size}):**\n\n${hymn.commandMap.joinToString(", ", transform = { "**`${it.label}`**" })}")

        message.channel.sendMessage(builder.build()).queue()
    }
}