package gli_

import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0

fun pathOf(filename: String, vararg more: String): Path = Paths.get(filename, *more)
fun pathOf(uri: URI): Path = Paths.get(uri)
fun uriOf(str: String) = ClassLoader.getSystemResource(str).toURI()

val Path.extension get() = toString().substringAfterLast(".").toLowerCase()

var ByteBuffer.ptr
    get() = position()
    set(value) {
        position(value)
    }