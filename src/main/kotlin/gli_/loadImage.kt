package gli_

import glm_.or
import glm_.vec3.Vec3i
import kool.*
import java.awt.image.*
import java.awt.image.BufferedImage.*
import java.io.File
import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

interface loadImage {

    fun loadImage(file: File, flipY: Boolean): Texture {

        if (!file.exists()) throw NoSuchFileException(file)

        val image = ImageIO.read(file)
        return loadImage(image, flipY)
    }

    fun loadImageFromMem(buffer: ByteBuffer, flipY: Boolean): Texture {

        assert(buffer.hasArray())

        val image = ImageIO.read(ByteBufferBackedInputStream(buffer))

        return loadImage(image, flipY)
    }

    fun loadImage(image: BufferedImage, flipY: Boolean = false): Texture {
        val extent = Vec3i(image.width, image.height, 1)
        if (flipY)
            image.flipY()
        return when (image.type) {
            TYPE_INT_RGB -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1).apply {
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferInt).data.forEach {
                    dst[i++] = it ushr 16
                    dst[i++] = it ushr 8
                    dst[i++] = it
                }
            }
            TYPE_INT_ARGB -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // push alpha at the end
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferInt).data.forEach {
                    dst[i++] = it ushr 16
                    dst[i++] = it ushr 8
                    dst[i++] = it
                    dst[i++] = it ushr 24
                }
            }
            TYPE_INT_ARGB_PRE -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // push alpha at the end and demultiply
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferInt).data.forEach {
                    val a = it ushr 24
                    dst[i++] = (it ushr 16) / a
                    dst[i++] = (it ushr 8) / a
                    dst[i++] = it / a
                    dst[i++] = a
                }
            }
            TYPE_INT_BGR -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // switch blue and red
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferInt).data.forEach {
                    dst[i++] = it
                    dst[i++] = it ushr 8
                    dst[i++] = it ushr 16
                }
            }
            TYPE_3BYTE_BGR -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // switch blue and red
                val dst = data()
                val src = (image.raster.dataBuffer as DataBufferByte).data
                for (i in src.indices step 3) {
                    dst[i] = src[i + 2]
                    dst[i + 1] = src[i + 1]
                    dst[i + 2] = src[i]
                }
            }
            TYPE_4BYTE_ABGR -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // invert
                val dst = data()
                val src = (image.raster.dataBuffer as DataBufferByte).data
                for (i in src.indices step 4) {
                    dst[i] = src[i + 3]
                    dst[i + 1] = src[i + 2]
                    dst[i + 2] = src[i + 1]
                    dst[i + 3] = src[i]
                }
            }
            TYPE_4BYTE_ABGR_PRE -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // invert and demultiply
                val dst = data()
                val src = (image.raster.dataBuffer as DataBufferByte).data
                for (i in src.indices step 4) {
                    val a = src[i]
                    dst[i] = src[i + 3] / a
                    dst[i + 1] = src[i + 2] / a
                    dst[i + 2] = src[i + 1] / a
                    dst[i + 3] = a
                }
            }
            TYPE_USHORT_565_RGB -> Texture(Target._2D, Format.R5G6B5_UNORM_PACK16, extent, 1, 1, 1).apply {
                // 1 to 1
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferUShort).data.forEach {
                    dst.putShort(i++ * Short.BYTES, it)
                }
            }
            TYPE_USHORT_555_RGB -> Texture(Target._2D, Format.A1RGB5_UNORM_PACK16, extent, 1, 1, 1).apply {
                // ~ 1 to 1
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferUShort).data.forEach {
                    val a1rgb5 = it or 0b1000_0000_0000 // hardcode alpha to 1, opaque
                    dst.putShort(i++ * Short.BYTES, a1rgb5)
                }
            }
            TYPE_BYTE_GRAY -> Texture(Target._2D, Format.R8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // 1 to 1
                val dst = data()
                var i = 0
                val dataBuffer = image.raster.dataBuffer as DataBufferByte
                dataBuffer.data.forEach { dst[i++] = it }
            }
            TYPE_USHORT_GRAY -> Texture(Target._2D, Format.R16_UNORM_PACK16, extent, 1, 1, 1).apply {
                // 1 to 1
                val dst = data()
                var i = 0
                val dataBuffer = image.raster.dataBuffer as DataBufferUShort
                dataBuffer.data.forEach { dst.putShort(i++ * Short.BYTES, it) }
            }
            TYPE_BYTE_BINARY, TYPE_BYTE_INDEXED -> {
                val c = image.colorModel as IndexColorModel
                val colors = IntArray(c.mapSize)
                c.getRGBs(colors)
                val dataBuffer = image.raster.dataBuffer as DataBufferByte
                val src = dataBuffer.data
                when {
                    c.hasAlpha() -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1).apply {
                        val dst = data()
                        for (i in src.indices step 4) {
                            dst[i + 0] = src[i + 1]   // r
                            dst[i + 1] = src[i + 2]   // g
                            dst[i + 2] = src[i + 3]   // b
                            dst[i + 3] = src[i + 0]   // a
                        }
                    }
                    else -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1).apply {
                        val dst = data()
                        var j = 0
                        for (i in src.indices step 4) {
                            dst[j++] = src[i + 1]   // r
                            dst[j++] = src[i + 2]   // g
                            dst[j++] = src[i + 3]   // b
                        }
                    }
                }
            }
            else -> error("not yet supported")
        }
    }
}