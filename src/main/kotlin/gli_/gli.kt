package gli_

import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths


object gli :
        clear,
        copy,
        levels,
        load,
        loadDds,
        loadKmg,
        loadKtx,
        save,
        saveDds,
        saveKmg,
        saveKtx,
        view {

    val gl = gli_.gl

    /** Texture filtring modes  */
    enum class Filter {
        INVALID,    // -1
        NONE,
        NEAREST,
        LINEAR;

        val i = ordinal - 1
    }

    val FILTER_FIRST = Filter.NEAREST
    val FILTER_LAST = Filter.LINEAR
    val FILTER_COUNT = FILTER_LAST.i - FILTER_FIRST.i + 1
}

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0


// util function
inline fun wasInit(f: () -> Unit): Boolean {
    try {
        f()
    } catch (e: UninitializedPropertyAccessException) {
        return false
    }
    return true
}

fun pathOf(filename: String, vararg more: String): Path = Paths.get(filename, *more)
fun pathOf(uri: URI): Path = Paths.get(uri)
fun uriOf(str: String) = ClassLoader.getSystemResource(str).toURI()

val Path.extension get() = toString().substringAfterLast(".").toLowerCase()

var ByteBuffer.ptr
    get() = position()
    set(value) {
        position(value)
    }

//fun Files.Companion.delete(str: String): Unit = delete(pathOf(str))