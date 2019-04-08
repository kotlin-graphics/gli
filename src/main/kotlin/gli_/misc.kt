package gli_

import kool.*
import kool.lib.*
import org.lwjgl.*
import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0

fun pathOf(filename: String, vararg more: String): Path = Paths.get(filename, *more)
fun URI.toPath(): Path = Paths.get(this)

val Path.extension get() = toString().substringAfterLast(".").toLowerCase()

internal fun flipY(buffer: ByteBuffer, width: Int, height: Int)
        = flipY(buffer.toByteArray(), width, height)

internal fun flipY(buffer: ByteArray, width: Int, height: Int) : ByteBuffer {

    val result = BufferUtils.createByteBuffer(buffer.size)

    for(scanLineIndex in 0 until height) {
        val srcScanLineStart = width * scanLineIndex
        val dstScanLineStart = width * (height - scanLineIndex - 1)

        result.pos = dstScanLineStart
        result.put(buffer, srcScanLineStart, width)
    }
    result.pos = 0

    return result
}