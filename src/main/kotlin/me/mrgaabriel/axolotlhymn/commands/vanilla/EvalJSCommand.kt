package me.mrgaabriel.axolotlhymn.commands.vanilla

import me.mrgaabriel.axolotlhymn.*
import me.mrgaabriel.axolotlhymn.commands.*
import net.dv8tion.jda.core.entities.*
import javax.script.*

class EvalJSCommand : AbstractCommand(
        "evaljs",
        "Executa códigos em JavaScript",
        hideInHelp = true
) {

    override fun run(message: Message, args: Array<String>) {
        if (!AxolotlHymnLauncher.hymn.config.owners.contains(message.author.id)) {
            message.channel.sendMessage("${message.author.asMention} **Sem permissão!**").queue()
            return
        }

        val code = args.joinToString(" ")

        val engine = ScriptEngineManager().getEngineByName("nashorn")
        try {
            engine.put("message", message)
            engine.put("args", args)
            engine.put("hymn", AxolotlHymnLauncher.hymn)

            val evaluated = engine.eval(code)

            message.channel.sendMessage("```$evaluated```").queue()
        } catch (e: Exception) {
            val msg = if (e.message != null) {
                e.message
            } else {
                e.stackTrace.get(0).toString()
            }

            message.channel.sendMessage("Erro! ```$msg```").queue()
        }
    }

}