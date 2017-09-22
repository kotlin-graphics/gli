package gli_

import glm_.BYTES
import glm_.b
import java.nio.ByteBuffer


object kmg {

    val FOURCC_KMG100 = byteArrayOf(0xAB.b, 0x4B.b, 0x49.b, 0x4D.b, 0x20.b, 0x31.b, 0x31.b, 0xBB.b, 0x0D.b, 0x0A.b, 0x1A.b, 0x0A)

    class Header10 {

        var endianness = 0
        var format = 0
        var target = 0
        var swizzleRed = 0
        var swizzleGreen = 0
        var swizzleBlue = 0
        var swizzleAlpha = 0
        var pixelWidth = 0
        var pixelHeight = 0
        var pixelDepth = 0
        var layers = 0
        var levels = 0
        var faces = 0
        var generateMipmaps = 0
        var baseLevel = 0
        var maxLevel = 0

        companion object {
            val size = 16 * Int.BYTES
        }

        constructor()

        constructor(buffer: ByteBuffer) {

            endianness = buffer.int
            format = buffer.int
            target = buffer.int
            swizzleRed = buffer.int
            swizzleGreen = buffer.int
            swizzleBlue = buffer.int
            swizzleAlpha = buffer.int
            pixelWidth = buffer.int
            pixelHeight = buffer.int
            pixelDepth = buffer.int
            layers = buffer.int
            levels = buffer.int
            faces = buffer.int
            generateMipmaps = buffer.int
            baseLevel = buffer.int
            maxLevel = buffer.int
        }

        fun to(data: ByteBuffer) = data
                .putInt(endianness)
                .putInt(format)
                .putInt(target)
                .putInt(swizzleRed)
                .putInt(swizzleGreen)
                .putInt(swizzleBlue)
                .putInt(swizzleAlpha)
                .putInt(pixelWidth)
                .putInt(pixelHeight)
                .putInt(pixelDepth)
                .putInt(layers)
                .putInt(levels)
                .putInt(faces)
                .putInt(generateMipmaps)
                .putInt(baseLevel)
                .putInt(maxLevel)
    }
}