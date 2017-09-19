package gli_


object gli :
        clear,
        levels,
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