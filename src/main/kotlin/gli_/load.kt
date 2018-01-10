package gli_

import glm_.set
import glm_.vec3.Vec3i
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_3BYTE_BGR
import java.awt.image.BufferedImage.TYPE_4BYTE_ABGR
import java.awt.image.DataBufferByte
import java.io.File
import java.net.URI
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Created by elect on 01/05/17.
 */

interface load {

    fun load(uri: URI) = load(Paths.get(uri))

    fun load(filename: String) = load(Paths.get(filename))

    fun load(path: Path) = when (path.extension) {
        "dds" -> gli.loadDds(path)
        "kmg" -> gli.loadKmg(path)
        "ktx" -> gli.loadKtx(path)
        "jpeg", "jpg", "png", "gif", "bmp", "wbmp" -> loadImage(path)
        else -> throw Error("unsupported extension: ${path.fileName}")
    }

    fun loadImage(filename: String) = loadImage(Paths.get(filename))

    fun loadImage(uri: URI) = loadImage(Paths.get(uri))

    /** Loads a texture storage_linear from DDS memory. Returns an empty storage_linear in case of failure.
     *  @param uri Uri of the file to open including filaname and filename extension */
    fun loadImage(path: Path): Texture {
        val image = ImageIO.read(path.toFile())//.flipY() TODO test
        val data = (image.raster.dataBuffer as DataBufferByte).data
        val format = when (image.type) {
            TYPE_3BYTE_BGR -> { // switch blue and red
                repeat(image.width * image.height) {
                    val tmp = data[it * 3]  // save blue
                    data[it * 3] = data[it * 3 + 2] // write red
                    data[it * 3 + 2] = tmp  // write blue
                }
                Format.RGB8_UNORM_PACK8
            }
            TYPE_4BYTE_ABGR -> {
                repeat(image.width * image.height) {
                    // switch alpha and red
                    var tmp = data[it * 4]  // save alpha
                    data[it * 4] = data[it * 4 + 3] // write red
                    data[it * 4 + 3] = tmp  // write alpha
                    // switch blue and green
                    tmp = data[it * 4 + 1]  // save blue
                    data[it * 4 + 1] = data[it * 4 + 2] // write green
                    data[it * 4 + 2] = tmp  // write blue
                }
                Format.RGBA8_UNORM_PACK8
            }
            else -> throw Error("not yet supported")
        }
        return Texture(Target._2D, format, Vec3i(image.width, image.height, 1), 1, 1, 1)
                .apply { with(data()) { for (i in 0 until size) set(i, data[i]) } }
    }

//fun BufferedImage.toByteBuffer() = (raster.dataBuffer as DataBufferByte).data.toByteBuffer() TODO

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
}