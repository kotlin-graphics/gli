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
fun URI.toPath(): Path = Paths.get(this)

val Path.extension get() = toString().substringAfterLast(".").toLowerCase()

// TODO remove
fun flipY(buffer: ByteBuffer, width: Int, height: Int) = flipY(buffer.toByteArray(), width, height)

fun flipY(buffer: ByteArray, width: Int, height: Int): ByteBuffer {

    val result = BufferUtils.createByteBuffer(buffer.size)

    for (scanLineIndex in 0 until height) {
        val srcScanLineStart = width * scanLineIndex
        val dstScanLineStart = width * (height - scanLineIndex - 1)
        result.pos = dstScanLineStart
        result.put(buffer, srcScanLineStart, width)
    }
    result.pos = 0

    return result
}

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