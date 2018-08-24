package me.mrgaabriel.axolotlhymn.commands.vanilla

import com.mongodb.client.model.*
import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.AxolotlHymnLauncher.hymn
import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*
import java.time.*

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

        if (args.isNotEmpty() && hymn.commandMap.any { it.label == args[0] || it.aliases.any { it == args[0] } }) {
            val command = hymn.commandMap.firstOrNull { it.label == args[0] || it.aliases.any { it == args[0] } }!!

            val builder = EmbedBuilder()

            builder.setAuthor(message.author.name + "#" + message.author.discriminator, null, message.author.effectiveAvatarUrl)
            builder.setTitle(":thinking: Ajuda do comando `t!${command.label}`")

            val labels = mutableListOf(command.label)
            labels.addAll(command.aliases)

            builder.setDescription("${command.description}\n\n:small_blue_diamond: Como usar!?\n`t!${command.label} ${command.usage}`\n\n:heavy_plus_sign: Alternativas\n:small_blue_diamond: ${labels.joinToString(", ", transform = {"`t!$it`"})}")

            builder.setColor(HymnUtils.hexToColor(profile.favColor))

            builder.setTimestamp(OffsetDateTime.now())
            builder.setFooter("${message.author.name}#${message.author.discriminator}", null)

            message.channel.sendMessage(builder.build()).queue()
            return
        }

        val color = HymnUtils.hexToColor(profile.favColor) ?: return

        val builder = EmbedBuilder()

        builder.setAuthor(message.author.name + "#" + message.author.discriminator, null, message.author.effectiveAvatarUrl)
        builder.setTitle("Axolotl Hymn - Comandos")
        builder.setColor(color)

        builder.setFooter("Mostrando ${hymn.commandMap.filter { !it.hideInHelp }.size} comandos de ${hymn.commandMap.filter { !it.hideInHelp }.size}", null)
        builder.setTimestamp(OffsetDateTime.now())

        builder.setDescription("**Comandos disponíveis (${hymn.commandMap.filter { !it.hideInHelp }.size}):**\n${hymn.commandMap.filter { !it.hideInHelp }.joinToString(", ", transform = { "**`${it.label}`**" })}\n\n**Caso tenha dúvida em algum comando, use `t!help (comando)`**")

        message.channel.sendMessage(builder.build()).queue()
    }
}