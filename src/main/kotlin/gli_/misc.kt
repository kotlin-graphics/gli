package gli_

import glm_.vec2.Vec2i
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

fun pathOf(filename: String, vararg more: String): Path = Paths.get(filename, *more)
//fun URI.toPath(): Path = Paths.get(this)

val Path.extension get() = toString().substringAfterLast(".").lowercase(Locale.getDefault())

fun ByteBuffer.flipY(size: Vec2i) = flipY(size.x, size.y)

fun ByteBuffer.flipY(width: Int, height: Int) {
    for (line in 0 until height / 2)
        for (x in 0 until width) {
            val up = width * (height - line - 1) + x
            val down = width * line + x
            val tmp = get(down)
            put(down, get(up))
            put(up, tmp)
        }
}

operator fun Array<dx.Format>.get(index: Format): dx.Format = get(index.i - Format.FIRST.i)

fun BufferedImage.flipY() {
    var scanline1: Any? = null
    var scanline2: Any? = null
    for (i in 0 until height / 2) {
        scanline1 = raster.getDataElements(0, i, width, 1, scanline1)
        scanline2 = raster.getDataElements(0, height - i - 1, width, 1, scanline2)
        raster.setDataElements(0, i, width, 1, scanline2)
        raster.setDataElements(0, height - i - 1, width, 1, scanline1)
    }
}

//fun main() {
//    val file = File("C:\\Users\\elect\\Pictures\\1.png")
//    println(file.exists() && file.canRead())
//    val image = ImageIO.read(file)
//    image.flipY()
//    ImageIO.write(image, "png", File("flipped.png"))
//}