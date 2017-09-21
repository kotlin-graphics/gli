package gli_

import glm_.BYTES
import glm_.c
import java.nio.ByteBuffer

object ktx {

    val FOURCC_KTX10 = charArrayOf(0xAB.c, 0x4B.c, 0x54.c, 0x58.c, 0x20.c, 0x31.c, 0x31.c, 0xBB.c, 0x0D.c, 0x0A.c, 0x1A.c, 0x0A.c)
    val FOURCC_KTX20 = charArrayOf(0xAB.c, 0x4B.c, 0x54.c, 0x58.c, 0x20.c, 0x32.c, 0x30.c, 0xBB.c, 0x0D.c, 0x0A.c, 0x1A.c, 0x0A.c)

    class Header10(data: ByteBuffer) {

        var endianness = data.int
        var glType = data.int
        var glTypeSize = data.int
        var glFormat = data.int
        var glInternalFormat = data.int
        var glBaseInternalFormat = data.int
        var pixelWidth = data.int
        var pixelHeight = data.int
        var pixelDepth = data.int
        var numberOfArrayElements = data.int
        var numberOfFaces = data.int
        var numberOfMipmapLevels = data.int
        var bytesOfKeyValueData = data.int

        companion object {
            val size = 13 * Int.BYTES
        }
    }

    val Header10.target
        get () = when {
            numberOfFaces > 1 -> when {
                numberOfArrayElements > 0 -> Target.CUBE_ARRAY
                else -> Target.CUBE
            }
            numberOfArrayElements > 0 -> when {
                pixelHeight == 0 -> Target._1D_ARRAY
                else -> Target._2D_ARRAY
            }
            pixelHeight == 0 -> Target._1D
            pixelDepth > 0 -> Target._3D
            else -> Target._2D
        }
}