package gli_


object gli :
        clear,
        copy,
        duplicate,
        levels,
        load,
        loadDds,
        loadKmg,
        loadKtx,
        makeTexture,
        save,
        saveDds,
        saveKmg,
        saveKtx,
        view {

    @JvmField val gl = gli_.gl
    val dx = gli_.dx

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


class Java {

    companion object {
        @JvmField
        val gli = gli_.gli
    }
}