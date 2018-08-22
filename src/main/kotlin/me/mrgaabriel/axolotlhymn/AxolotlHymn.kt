package me.mrgaabriel.axolotlhymn

import com.google.gson.*
import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.data.*
import me.mrgaabriel.axolotlhymn.listeners.*
import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.*
import net.dv8tion.jda.core.entities.*
import org.slf4j.*
import java.io.*

class AxolotlHymn(var config: HymnConfig) {

    lateinit var jda: JDA

    val commandMap = mutableListOf<AbstractCommand>()

    var axolotl: Guild? = null
    var builder = JDABuilder(AccountType.BOT)
            .setToken(config.clientToken)
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .setGame(Game.streaming("Tommorrowland - (2019 - Winter) | t!help", "https://www.twitch.tv/MrGaabriel"))
            .setCorePoolSize(128)

    val logger = LoggerFactory.getLogger(this::class.java)

    fun start() {
        logger.info("Iniciando Axolotl Hymn!")

        jda = builder.buildBlocking()
        jda.addEventListener(MessageReceiver())

        axolotl = jda.getGuildById("445377287043416075") ?: null

        logger.info("OK! Iniciado com sucesso")
        val messages = arrayOf(
                "Informações:",
                "Guilds: ${jda.guilds.joinToString(", ", transform={it.name})}",
                "${jda.guilds.size} guilds",
                "${jda.users.size} usuários",
                "${jda.textChannels.size} canais",
                "${axolotl?.members?.size} membros no Axolotl"
        )

        messages.forEach { logger.info(it) }

        loadCommands()
    }

    fun loadCommands() {
        commandMap.clear()

        val classes = HymnUtils.getClasses("me.mrgaabriel.axolotlhymn.commands")
        classes.forEach { clazz ->
            if (AbstractCommand::class.java.isAssignableFrom(clazz) && clazz != AbstractCommand::class.java) {
                try {
                    val command = clazz.newInstance() as AbstractCommand

                    commandMap.add(command)
                    logger.info("Comando ${clazz.simpleName} carregado com sucesso!")
                } catch (e: Exception) {
                    logger.info("Comando ${clazz.simpleName} teve um erro ao ser carregado!")
                    e.printStackTrace()
                }
            }
        }
    }
}

object AxolotlHymnLauncher {

    lateinit var hymn: AxolotlHymn

    val logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val file = File("config.json")
        if (!file.exists()) {
            logger.warn("É a primeira vez que você está rodando o bot!")
            logger.warn("Configure-o no arquivo \"config.json\"")

            file.writeText(Gson().toJson(HymnConfig()), Charsets.UTF_8)
            System.exit(0)
        }

        val config = Gson().fromJson(file.readText(Charsets.UTF_8), HymnConfig::class.java)
        logger.info("Configurações:")
        logger.info("TOKEN: ${config.clientToken}")
        logger.info("ID: ${config.clientId}")
        logger.info("DONOS: ${config.owners.joinToString(", ")}")

        hymn = AxolotlHymn(config)

        hymn.start()
    }

}