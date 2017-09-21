package gli_

import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths


object gli :
        clear,
        levels,
        load,
        loadDds,
        saveDds,
        view {

    val gl = gli_.gl
}

infix fun Int.has(b: Int) = (this and b) != 0
infix fun Int.hasnt(b: Int) = (this and b) == 0


// util function
inline fun wasInit(f: () -> Unit): Boolean {
    try {
        f()
    }
    catch(e: UninitializedPropertyAccessException) {
        return false
    }
    return true
}

fun pathOf(filename: String, vararg more: String) = Paths.get(filename, *more)
fun pathOf(uri: URI) = Paths.get(uri)
fun uriOf(str: String) = URI.create(str)