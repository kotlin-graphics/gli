package gli

import glm.set
import glm.vec3.Vec3i
import uno.buffer.toByteBuffer
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_3BYTE_BGR
import java.awt.image.BufferedImage.TYPE_4BYTE_ABGR
import java.awt.image.DataBufferByte
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.ByteBuffer
import javax.imageio.ImageIO

/**
 * Created by elect on 01/05/17.
 */

fun load(path: String) = load(Texture::class.java, path)

fun load(context: Class<*>, path: String) = load(context.classLoader.getResource(path))

fun load(url: URL) = load(url.toURI())

fun load(uri: URI) = load(File(uri))

fun load(file: File) = when (file.extension) {
    "dds" -> loadDDS(file)
    "jpeg", "jpg", "png", "gif", "bmp", "wbmp" -> loadImage(file)
    else -> throw Error("unsupported extension: ${file.extension}")
}


fun loadImage(path: String) = loadImage(Texture::class.java, path)

fun loadImage(context: Class<*>, path: String) = loadImage(context.classLoader.getResource(path))

fun loadImage(url: URL) = loadImage(url.toURI())

fun loadImage(uri: URI) = loadImage(File(uri))

fun loadImage(file: File) = loadImage(ImageIO.read(file).flipY())

fun loadImage(image: BufferedImage): Texture {

    val data = (image.raster.dataBuffer as DataBufferByte).data

    val format: Format

    when (image.type) {

        TYPE_3BYTE_BGR -> {

            format = Format.RGB8_UNORM_PACK8

            repeat(image.width * image.height) {
                // swith blue and red
                val tmp = data[it * 3]  // save blue
                data[it * 3] = data[it * 3 + 2] // write red
                data[it * 3 + 2] = tmp  // write blue
            }
        }
        TYPE_4BYTE_ABGR -> {

            format = Format.RGBA8_UNORM_PACK8

            repeat(image.width * image.height) {
                // swith alpha and red
                var tmp = data[it * 4]  // save alpha
                data[it * 4] = data[it * 4 + 3] // write red
                data[it * 4 + 3] = tmp  // write alpha
                // swith blue and green
                tmp = data[it * 4 + 1]  // save blue
                data[it * 4 + 1] = data[it * 4 + 2] // write green
                data[it * 4 + 2] = tmp  // write blue
            }
        }
        else -> throw Error("not yet supported")
    }

    val texture = Texture(Target._2D, format, Vec3i(image.width, image.height, 1), 1, 1, 1)

    repeat(texture.size()) { texture.data()[it] = data[it] }

    return texture
}

fun BufferedImage.toByteBuffer() = (raster.dataBuffer as DataBufferByte).data.toByteBuffer()

// TODO credits JOGAMP
fun BufferedImage.flipY(): BufferedImage {

    var scanline1: Any? = null
    var scanline2: Any? = null

    for (i in 0 until height / 2) {

        scanline1 = raster.getDataElements(0, i, width, 1, scanline1)
        scanline2 = raster.getDataElements(0, height - i - 1, width, 1, scanline2)
        raster.setDataElements(0, i, width, 1, scanline2)
        raster.setDataElements(0, height - i - 1, width, 1, scanline1)
    }

    return this
}