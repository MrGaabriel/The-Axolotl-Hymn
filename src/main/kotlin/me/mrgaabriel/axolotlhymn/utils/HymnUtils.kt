package me.mrgaabriel.axolotlhymn.utils

import org.apache.commons.lang3.*
import java.awt.*
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.geom.*
import java.awt.image.*
import java.io.*
import java.net.*
import java.util.*
import javax.imageio.*


object HymnUtils {

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Throws(ClassNotFoundException::class, IOException::class)
    fun getClasses(packageName: String): Array<Class<*>> {
        val classLoader = Thread.currentThread().contextClassLoader!!
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path)
        val dirs = ArrayList<File>()
        while (resources.hasMoreElements()) {
            val resource = resources.nextElement()
            dirs.add(File(resource.file))
        }
        val classes = ArrayList<Class<*>>()
        for (directory in dirs) {
            classes.addAll(findClasses(directory, packageName))
        }
        return classes.toTypedArray()
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    @Throws(ClassNotFoundException::class)
    fun findClasses(directory: File, packageName: String): List<Class<*>> {
        val classes = ArrayList<Class<*>>()
        if (!directory.exists()) {
            return classes
        }
        val files = directory.listFiles()
        for (file in files!!) {
            if (file.isDirectory) {
                assert(!file.name.contains("."))
                classes.addAll(findClasses(file, packageName + "." + file.name))
            } else if (file.name.endsWith(".class")) {
                classes.add(Class.forName(packageName + '.'.toString() + file.name.substring(0, file.name.length - 6)))
            }
        }
        return classes
    }

    fun hexToColor(colorStr: String): Color? {
        try {
            val r = Integer.valueOf(colorStr.substring(1, 3), 16)
            val g = Integer.valueOf(colorStr.substring(3, 5), 16)
            val b = Integer.valueOf(colorStr.substring(5, 7), 16)

            return Color(r, g, b)
        } catch (e: NumberFormatException) {
            return null
        } catch (e: StringIndexOutOfBoundsException) {
            return null
        }
    }

    fun getImageFromURL(link: String): BufferedImage {
        val url = URL(link)
        val conn = url.openConnection()

        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0")
        conn.connect()

        return ImageIO.read(conn.getInputStream())
    }
}

fun Array<String>.remove(idx: Int): Array<String> {
    return ArrayUtils.remove(this, idx)
}

fun BufferedImage.makeRoundedCorner(cornerRadius: Int): BufferedImage {
    val w = this.width
    val h = this.height
    val output = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

    val g2 = output.createGraphics()

    // This is what we want, but it only does hard-clipping, i.e. aliasing
    // g2.setClip(new RoundRectangle2D ...)

    // so instead fake soft-clipping by first drawing the desired clip shape
    // in fully opaque white with antialiasing enabled...
    g2.composite = AlphaComposite.Src
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.color = Color.WHITE
    g2.fill(RoundRectangle2D.Float(0f, 0f, w.toFloat(), h.toFloat(), cornerRadius.toFloat(), cornerRadius.toFloat()))

    // ... then compositing the image on top,
    // using the white shape from above as alpha source
    g2.composite = AlphaComposite.SrcAtop
    g2.drawImage(this, 0, 0, null)

    g2.dispose()

    return output
}

fun Image.resize(width: Int, height: Int, maintainRatio: Boolean): BufferedImage {
    //long startTime = getStartTime();

    var outputWidth = width
    var outputHeight = height

    val _width = this.getWidth(null)
    val _height = this.getHeight(null)

    if (maintainRatio) {

        var ratio = 0.0

        if (width > height) {
            ratio = width.toDouble() / _width.toDouble()
        } else {
            ratio = height.toDouble() / _height.toDouble()
        }

        val dw = width * ratio
        val dh = height * ratio

        outputWidth = Math.round(dw).toInt()
        outputHeight = Math.round(dh).toInt()

        if (outputWidth > width || outputHeight > height) {
            outputWidth = width
            outputHeight = height
        }
    }


    //Resize the image (create new buffered image)
    val outputImage = this.getScaledInstance(outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB)
    val bi: BufferedImage? = BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB)
    val g2d: Graphics2D? = bi!!.createGraphics()
    g2d!!.drawImage(outputImage, 0, 0, null)
    g2d.dispose()

    return bi
}