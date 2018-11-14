package gli_

import java.awt.image.BufferedImage
import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0

fun pathOf(filename: String, vararg more: String): Path = Paths.get(filename, *more)
fun URI.toPath(): Path = Paths.get(this)
fun uriOf(str: String): URI = ClassLoader.getSystemResource(str).toURI()

val Path.extension get() = toString().substringAfterLast(".").toLowerCase()

fun BufferedImage.flipY(): BufferedImage {
    var scanline1: Any? = null
    var scanline2: Any? = null
    for (i in 0 until height / 2) {
        scanline1 = raster.getDataElements(0, i, width, 1, scanline1)
        scanline2 = raster.getDataElements(0, height - i - 1, width, 1, scanline2)
        raster.setDataElements(0, i, width, 1, scanline2)
        raster.setDataElements(0, height - i - 1, width, 1, scanline1)
    }
    return this
}