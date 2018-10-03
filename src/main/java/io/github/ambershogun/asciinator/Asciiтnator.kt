package io.github.ambershogun.asciinator

import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.font.FontRenderContext
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

private const val RAMP = " .`^,:;i!lI><~+_-?[{1)(|/vczXYUJCLOQ0Zhao*#MW&8%B@\$"
private const val FONT_SIZE_RATIO = 2
private val RENDERING_PROPS = HashMap<RenderingHints.Key, Any>()

/**
 * Converts provided image to ASCII style image
 */
object Asciinator {

    init {
        RENDERING_PROPS[RenderingHints.KEY_STROKE_CONTROL] = RenderingHints.VALUE_STROKE_PURE
        RENDERING_PROPS[RenderingHints.KEY_FRACTIONALMETRICS] = RenderingHints.VALUE_FRACTIONALMETRICS_ON
    }

    /**
     * Converts provided image to ASCII style image
     *
     * @param inputFile source image to convert
     * @param width width of converted image in pixels
     * @param bgndColor color of converted image background
     * @param fontColor color of converted image font
     * @param outputFile result file
     */
    @JvmStatic
    fun asciinate(
            inputFile: File,
            width: Int,
            bgndColor: Color,
            fontColor: Color,
            outputFile: File
    ): File {
        val asciiString = makeAsciiString(inputFile, width)
        return textToImage(asciiString, bgndColor, fontColor, outputFile)
    }

    /**
     * Resize and convert provided image to String with ASCII content
     *
     * @param file source image to convert
     * @param widthPixels width of converted image in pixels
     *
     * @return string with ASCII content
     */
    private fun makeAsciiString(file: File, widthPixels: Int): String {
        val inputImage = ImageIO.read(file)
        val heightPixels = (widthPixels / (inputImage.width.toDouble() / inputImage.height.toDouble())).toInt() / FONT_SIZE_RATIO
        val image = BufferedImage(widthPixels, heightPixels, inputImage.type)

        val g2d = image.createGraphics()
        g2d.drawImage(inputImage, 0, 0, widthPixels, heightPixels, null)
        g2d.dispose()

        val result = StringBuilder()

        for (h in 0 until image.height) {
            for (w in 0 until image.width) {
                val color = average(image.getRGB(w, h))
                var index = color / 5
                if (index > 50) index = 50
                result.append(RAMP[index])
            }
            result.appendln()
        }
        return result.toString()
    }

    /**
     * Calculates average saturation of color
     *
     * @param rgb color as integer
     *
     * @return average saturation in range from 0 to 255
     */
    private fun average(rgb: Int): Int {
        val red = rgb shr 16 and 0xff
        val green = rgb shr 8 and 0xff
        val blue = rgb and 0xff

        return (Math.sqrt(red.toDouble()) + Math.sqrt(green.toDouble()) + Math.sqrt(blue.toDouble())).toInt()
    }

    /**
     * Converts provided String to image file
     *
     * @param text string with ASCII content
     * @param backgroundColor color of converted image background
     * @param fontColor color of converted image font
     * @param outputFile result file
     */
    private fun textToImage(text: String, backgroundColor: Color, fontColor: Color, outputFile: File): File {
        val font = Font.decode(Font.MONOSPACED)

        val fontRenderContext = FontRenderContext(null, true, true)

        val lines = text.split("\n")

        val lineMetrics = font.getLineMetrics(lines[0], fontRenderContext)
        val lineRect = font.getStringBounds(lines[0], fontRenderContext)
        val img = BufferedImage(
                Math.ceil(lineRect.width).toInt(),
                Math.ceil(lineRect.height * lines.size).toInt(),
                BufferedImage.TYPE_INT_RGB)
        val graphics2D = img.createGraphics()
        graphics2D.setRenderingHints(RENDERING_PROPS)
        graphics2D.background = backgroundColor
        graphics2D.color = fontColor
        graphics2D.clearRect(0, 0, img.width, img.height)
        graphics2D.font = font

        for ((index, line) in lines.withIndex()) {
            graphics2D.drawString(line, 0f, lineMetrics.height * index)
        }

        graphics2D.dispose()

        ImageIO.write(img, outputFile.extension, outputFile)

        return outputFile
    }
}
