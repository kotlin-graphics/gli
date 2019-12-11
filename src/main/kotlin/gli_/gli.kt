package gli_

import glm_.L
import org.lwjgl.system.MemoryUtil


object gli :
        clear,
        copy,
        duplicate,
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

    @JvmField
    val gl = gli_.gl
    val dx = gli_.dx

    /** Texture filtring modes  */
    enum class Filter {
        INVALID,    // -1
        NONE,
        NEAREST,
        LINEAR;

        val i = ordinal - 1

        companion object {
            val FIRST = NEAREST
            val LAST = LINEAR
            val COUNT = LAST.i - FIRST.i + 1
        }
    }
}


class Java {

    companion object {
        @JvmField
        val gli = gli_.gli
    }
}

fun memCopy(src: Long, dst: Long, bytes: Int) = MemoryUtil.memCopy(src, dst, bytes.L)


const val GLI_VERSION_MAJOR = 0
const val GLI_VERSION_MINOR = 8
const val GLI_VERSION_PATCH = 3
const val GLI_VERSION_REVISION = 0
const val GLI_VERSION_BUILD = 13
const val GLI_VERSION = GLI_VERSION_MAJOR * 1_000 + GLI_VERSION_MINOR * 100 + GLI_VERSION_PATCH * 10 + GLI_VERSION_REVISION + GLI_VERSION_BUILD / 10f
