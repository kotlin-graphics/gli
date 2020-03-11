package gli_

import glm_.vec2.Vec2i
import kool.lib.toByteArray
import kool.pos
import org.lwjgl.BufferUtils
import java.awt.image.BufferedImage
import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0

fun pathOf(filename: String, vararg more: String): Path = Paths.get(filename, *more)
//fun URI.toPath(): Path = Paths.get(this)

val Path.extension get() = toString().substringAfterLast(".").toLowerCase()

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

operator fun Array<dx.Format>.get(index: Format): dx.Format =
        get(index.i - Format.FIRST.i)

fun BufferedImage.flipY() {
    lateinit var scanline1: Any
    lateinit var scanline2: Any
    for (i in 0 until height / 2) {
        scanline1 = raster.getDataElements(0, i, width, 1, scanline1)
        scanline2 = raster.getDataElements(0, height - i - 1, width, 1, scanline2)
        raster.setDataElements(0, i, width, 1, scanline2)
        raster.setDataElements(0, height - i - 1, width, 1, scanline1)
    }
}