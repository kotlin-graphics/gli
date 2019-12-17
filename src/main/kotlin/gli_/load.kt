package gli_

import glm_.vec3.Vec3i
import kool.*
import org.lwjgl.stb.*
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.*
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.awt.image.DataBufferUShort
import java.io.*
import java.lang.IllegalArgumentException
import java.net.URI
import java.nio.*
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by elect on 01/05/17.
 */

interface load {

    fun load(uri: URI) = load(Paths.get(uri))
    fun load(uri: URI, flipY: Boolean) = load(Paths.get(uri), flipY)

    fun load(filename: String) = load(Paths.get(filename))
    fun load(filename: String, flipY: Boolean) = load(Paths.get(filename), flipY)

    fun load(path: Path, flipY: Boolean = false) = when (path.extension) {
        "dds" -> gli.loadDds(path)
        "kmg" -> gli.loadKmg(path)
        "ktx" -> gli.loadKtx(path)
        "jpeg", "jpg", "png", "gif", "bmp", "tga" -> loadImage(path.toFile(), flipY)
        else -> throw Error("unsupported extension: ${path.fileName}")
    }

    fun load(buffer: ByteBuffer, type: String, flipY: Boolean = false): Texture {
        return when (type) {
            "dds" -> gli.loadDds(buffer)
            "kmg" -> gli.loadKmg(buffer)
            "ktx" -> gli.loadKtx(buffer)
            "jpeg", "jpg", "png", "gif", "bmp", "tga" -> loadImageFromMem(buffer, flipY)
            else -> throw IllegalArgumentException("Type not supported")
        }
    }

    private fun loadImage(file: File, flipY: Boolean): Texture {

        if (!file.exists()) throw NoSuchFileException(file)

        val width: Int
        val height: Int
        val compCount: Int

        var imageBuffer: ByteBuffer

        Stack.with { mem ->
            val widthBuf = mem.ints(1)
            val heightBuf = mem.ints(1)
            val compCountBuf = mem.ints(1)

            imageBuffer = STBImage.stbi_load(file.absolutePath, widthBuf, heightBuf, compCountBuf, 0)
                    ?: throw IOException("Couldn't load image at $file.")

            width = widthBuf.get()
            height = heightBuf.get()
            compCount = compCountBuf.get()
        }

        if (flipY) imageBuffer.flipY(width, height)

        return createTexture(imageBuffer, width, height, compCount)
    }

    private fun loadImageFromMem(buffer: ByteBuffer, flipY: Boolean): Texture = Stack { mem ->

        val pWidth = mem.ints(1)
        val pHeight = mem.ints(1)
        val pCompCount = mem.ints(1)

        val imageBuffer = stbi_load_from_memory(buffer, pWidth, pHeight, pCompCount, 0)
                ?: throw IOException("Couldn't load image")

        val width = pWidth[0]
        val height = pHeight[0]
        val compCount = pCompCount[0]

        if (flipY) imageBuffer.flipY(width, height)

        return createTexture(imageBuffer, width, height, compCount)
    }

    /**
     * creates a texture from a buffer containing the image.
     *
     * Each pixel is [compCount] bytes in size.
     *
     * [compCount]:
     *
     * 1 : gray
     *
     * 2 : gray alpha
     *
     * 3 : red green blue
     *
     * 4 : red green blue alpha
     *
     * @param compCount the number of color-components per pixel
     */
    fun createTexture(buffer: ByteBuffer, width: Int, height: Int, compCount: Int = 4): Texture {
        val extent = Vec3i(width, height, 1)
        return when (compCount) {
            1 -> Texture(Target._2D, Format.R8_UNORM_PACK8, extent, 1, 1, 1)
            2 -> Texture(Target._2D, Format.RG8_UNORM_PACK8, extent, 1, 1, 1)
            3 -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1)
            4 -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1)
            else -> throw IllegalArgumentException("compCount should be in range 1..4")
        }.apply {
            // 1 to 1
            val dst = data()
            assert(dst.rem >= buffer.rem)
            dst.put(buffer)
        }
    }

    fun load(image: BufferedImage, flipY: Boolean = false): Texture {
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
                (image.raster.dataBuffer as DataBufferUShort).data.forEach { dst[i++] = it }
            }
            TYPE_USHORT_555_RGB -> throw Error("TYPE_USHORT_555_RGB unsupported, implement? What about alpha?")
            TYPE_BYTE_GRAY -> Texture(Target._2D, Format.R8_UNORM_PACK8, extent, 1, 1, 1).apply {
                // 1 to 1
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferInt).data.forEach { dst[i++] = it }
            }
            TYPE_USHORT_GRAY -> Texture(Target._2D, Format.R16_UNORM_PACK16, extent, 1, 1, 1).apply {
                // 1 to 1
                val dst = data()
                var i = 0
                (image.raster.dataBuffer as DataBufferInt).data.forEach { dst[i++] = it }
            }
            else -> throw Error("not yet supported")
        }
    }

    fun BufferedImage.flipY() {
        lateinit var scanline1: Any
        lateinit var scanline2: Any
        for (i in 0 until height / 2) {
            scanline1 = raster.getDataElements(0, i, width, 1, scanline1)
            scanline2 = raster.getDataElements(0, height - i - 1, width, 1, scanline2)
            raster.setDataElements(0, i, width, 1, scanline2)
            raster.setDataElements(0, height - i - 1, width, 1, scanline1)
        }
    }
}
