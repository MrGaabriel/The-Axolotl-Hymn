package me.mrgaabriel.axolotlhymn.commands

import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*
import org.slf4j.*

abstract class AbstractCommand(val label: String,
                               val description: String = "Insira uma descrição aqui",
                               val usage: String = "",
                               val aliases: List<String> = listOf(),
                               val hideInHelp: Boolean = false) {

    val logger = LoggerFactory.getLogger(AbstractCommand::class.java)

    abstract fun run(message: Message, args: Array<String>)

    fun matches(message: Message): Boolean {
        val content = message.contentRaw
        val contentSplitted = content.split(" ")

        val labels = mutableListOf(label)
        labels.addAll(aliases)

        val usedLabel = contentSplitted[0].toLowerCase()

        val prefix = "t!"
        val valid = labels.any { prefix + it == usedLabel }

        if (valid) {
            try {
                val start = System.currentTimeMillis()
                logger.info("${ConsoleColors.YELLOW}[COMMAND EXECUTED]${ConsoleColors.RESET} (${message.guild.name} -> #${message.channel.name}) ${message.author.name}#${message.author.discriminator} -> ${message.contentRaw}")

                val args = if (contentSplitted.size > 1) {
                    contentSplitted.joinToString(" ").substring(contentSplitted[0].length + 1).split(" ").toTypedArray()
                } else {
                    arrayOf()
                }

                if (message.channel.id == "445378804685209601" && !message.member.hasPermission(Permission.MESSAGE_MANAGE)) {
                    message.channel.sendMessage("${message.author.asMention} Você não pode executar comandos aqui, por favor use no <#445378832216752143>!").queue()
                    logger.info("${ConsoleColors.YELLOW}[COMMAND STATUS]${ConsoleColors.RESET} (${message.guild.name} -> #${message.channel.name}) ${message.author.name}#${message.author.discriminator} -> ${message.contentRaw} - OK! Finished in ${System.currentTimeMillis() - start}ms")
                    return true
                }

                run(message, args)

                logger.info("${ConsoleColors.YELLOW}[COMMAND STATUS]${ConsoleColors.RESET} (${message.guild.name} -> #${message.channel.name}) ${message.author.name}#${message.author.discriminator} -> ${message.contentRaw} - OK! Finished in ${System.currentTimeMillis() - start}ms")
                return true
            } catch (e: Exception) {
                message.channel.sendMessage("${message.author.asMention} Um erro aconteceu durante a execução deste comando! :cry:").queue()

                logger.error("${ConsoleColors.YELLOW}[COMMAND STATUS]${ConsoleColors.RESET} (${message.guild.name} -> #${message.channel.name}) ${message.author.name}#${message.author.discriminator} -> ${message.contentRaw} - ERROR!", e)
                return true
            }
        }

        return false
    }
}