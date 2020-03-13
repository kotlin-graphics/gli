package gli_

import glm_.BYTES
import glm_.b
import kool.BYTES
import java.nio.ByteBuffer

object ktx {

    val FOURCC_KTX10 = byteArrayOf(0xAB.b, 0x4B.b, 0x54.b, 0x58.b, 0x20.b, 0x31.b, 0x31.b, 0xBB.b, 0x0D.b, 0x0A.b, 0x1A.b, 0x0A.b)
    val FOURCC_KTX20 = byteArrayOf(0xAB.b, 0x4B.b, 0x54.b, 0x58.b, 0x20.b, 0x32.b, 0x30.b, 0xBB.b, 0x0D.b, 0x0A.b, 0x1A.b, 0x0A.b)

    class Header10 {

        var endianness = 0
        var glType = 0
        var glTypeSize = 0
        var glFormat = 0
        var glInternalFormat = 0
        var glBaseInternalFormat = 0
        var pixelWidth = 0
        var pixelHeight = 0
        var pixelDepth = 0
        var numberOfArrayElements = 0
        var numberOfFaces = 0
        var numberOfMipmapLevels = 0
        var bytesOfKeyValueData = 0

        constructor()

        constructor(data: ByteBuffer) {

            endianness = data.int
            glType = data.int
            glTypeSize = data.int
            glFormat = data.int
            glInternalFormat = data.int
            glBaseInternalFormat = data.int
            pixelWidth = data.int
            pixelHeight = data.int
            pixelDepth = data.int
            numberOfArrayElements = data.int
            numberOfFaces = data.int
            numberOfMipmapLevels = data.int
            bytesOfKeyValueData = data.int
        }

        fun to(buffer: ByteBuffer) = buffer
                .putInt(endianness)
                .putInt(glType)
                .putInt(glTypeSize)
                .putInt(glFormat)
                .putInt(glInternalFormat)
                .putInt(glBaseInternalFormat)
                .putInt(pixelWidth)
                .putInt(pixelHeight)
                .putInt(pixelDepth)
                .putInt(numberOfArrayElements)
                .putInt(numberOfFaces)
                .putInt(numberOfMipmapLevels)
                .putInt(bytesOfKeyValueData)

        val target
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

        companion object {
            val size = 13 * Integer.BYTES
        }
    }
}