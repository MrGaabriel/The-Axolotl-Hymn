package me.mrgaabriel.axolotlhymn.commands.vanilla

import me.mrgaabriel.axolotlhymn.commands.*
import me.mrgaabriel.axolotlhymn.utils.*
import net.dv8tion.jda.core.entities.*
import java.awt.*
import java.io.*
import java.net.*
import javax.imageio.*

class BrillianceCommand : AbstractCommand(
        "brilliance",
        "Brilliance Team"
) {

    override fun run(message: Message, args: Array<String>) {
        val user = if (message.mentionedUsers.isNotEmpty()) message.mentionedUsers.get(0) else message.author

        val userAvatar = ImageIO.read(URL(user.effectiveAvatarUrl + "?size=1024")).makeRoundedCorner(1200)
                .resize(1024, 1024, true)

        val graphics = userAvatar.createGraphics()

        val brillianceOverlay = ImageIO.read(URL("https://cdn.discordapp.com/attachments/445378804685209601/483434324402896907/Modelo-Brilliance1.png"))
                .resize(userAvatar.width, userAvatar.height, true)

        graphics.drawImage(brillianceOverlay, 0, 0, null)

        val baos = ByteArrayOutputStream()
        baos.use {
            ImageIO.write(userAvatar, "png", it)
        }

        val inputStream = ByteArrayInputStream(baos.toByteArray())

        message.channel.sendFile(inputStream, "brilliance.png").queue()
    }
}