package gli_

import gli_.tga.TgaImageReaderSpi
import kool.set
import glm_.vec3.Vec3i
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.*
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.awt.image.DataBufferUShort
import java.lang.IllegalArgumentException
import java.net.URI
import java.nio.*
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry

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
        "jpeg", "jpg", "png", "gif", "bmp", "wbmp" -> loadImage(path, flipY)
        "tga" -> {
            if (!tgaAdded) {
                IIORegistry.getDefaultInstance().registerServiceProvider(TgaImageReaderSpi())
                tgaAdded = true
            }
            loadImage(path, flipY)
        }
        else -> throw Error("unsupported extension: ${path.fileName}")
    }

    fun load(buffer: ByteBuffer, type: String, flipY: Boolean = false): Texture {
        return when(type) {
            "dds"  -> gli.loadDds(buffer)
            "kmg"  -> gli.loadKmg(buffer)
            "ktx"  -> gli.loadKtx(buffer)
            "jpeg", "jpg", "png", "gif", "bmp", "wbmp" -> {
                val image = ImageIO.read(ByteBufferBackedInputStream(buffer))
                gli.createTexture(image)
            }
            "tga"  -> {
                if(!tgaAdded){
                    IIORegistry.getDefaultInstance().registerServiceProvider(TgaImageReaderSpi())
                    tgaAdded = true
                }
                val image = ImageIO.read(ByteBufferBackedInputStream(buffer)).also { if(flipY) it.flipY() }
                createTexture(image)
            }
            else -> throw IllegalArgumentException("Type not supported")
        }
    }

    fun loadImage(filename: String, flipY: Boolean = false) = loadImage(Paths.get(filename), flipY)

    fun loadImage(uri: URI, flipY: Boolean = false) = loadImage(Paths.get(uri), flipY)

    /** Loads a texture storage_linear from DDS memory. Returns an empty storage_linear in case of failure.
     *  @param uri Uri of the file to open including filaname and filename extension */
    fun loadImage(path: Path, flipY: Boolean = false): Texture {
        val image = ImageIO.read(path.toFile())
        if (flipY) image.flipY()
        return createTexture(image)
    }

    /**
     * creates a texture from an BufferedImage
     */
    fun createTexture(image: BufferedImage): Texture {
        val extent = Vec3i(image.width, image.height, 1)
        return when (image.type) {
            TYPE_INT_RGB -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
                        val dst = data()
                        var i = 0
                        (image.raster.dataBuffer as DataBufferInt).data.forEach {
                            dst[i++] = it ushr 16
                            dst[i++] = it ushr 8
                            dst[i++] = it
                        }
                    }
            TYPE_INT_ARGB -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
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
            TYPE_INT_ARGB_PRE -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
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
            TYPE_INT_BGR -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
                        // switch blue and red
                        val dst = data()
                        var i = 0
                        (image.raster.dataBuffer as DataBufferInt).data.forEach {
                            dst[i++] = it
                            dst[i++] = it ushr 8
                            dst[i++] = it ushr 16
                        }
                    }
            TYPE_3BYTE_BGR -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
                        // switch blue and red
                        val dst = data()
                        val src = (image.raster.dataBuffer as DataBufferByte).data
                        for (i in src.indices step 3) {
                            dst[i] = src[i + 2]
                            dst[i + 1] = src[i + 1]
                            dst[i + 2] = src[i]
                        }
                    }
            TYPE_4BYTE_ABGR -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
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
            TYPE_4BYTE_ABGR_PRE -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
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
            TYPE_USHORT_565_RGB -> Texture(Target._2D, Format.R5G6B5_UNORM_PACK16, extent, 1, 1, 1)
                    .apply {
                        // 1 to 1
                        val dst = data()
                        var i = 0
                        (image.raster.dataBuffer as DataBufferUShort).data.forEach { dst[i++] = it }
                    }
            TYPE_USHORT_555_RGB -> throw Error("TYPE_USHORT_555_RGB unsupported, implement? What about alpha?")
            TYPE_BYTE_GRAY -> Texture(Target._2D, Format.R8_UNORM_PACK8, extent, 1, 1, 1)
                    .apply {
                        // 1 to 1
                        val dst = data()
                        var i = 0
                        (image.raster.dataBuffer as DataBufferInt).data.forEach { dst[i++] = it }
                    }
            TYPE_USHORT_GRAY -> Texture(Target._2D, Format.R16_UNORM_PACK16, extent, 1, 1, 1)
                    .apply {
                        // 1 to 1
                        val dst = data()
                        var i = 0
                        (image.raster.dataBuffer as DataBufferInt).data.forEach { dst[i++] = it }
                    }
            else -> throw Error("not yet supported")
        }
    }

//fun BufferedImage.toByteBuffer() = (raster.dataBuffer as DataBufferByte).data.toByteBuffer() TODO
}

private var tgaAdded = false