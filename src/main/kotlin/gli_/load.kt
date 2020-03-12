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

/**
 * Created by elect on 01/05/17.
 */

interface load {

    fun load(uri: URI) = load(Paths.get(uri))
    fun load(uri: URI, flipY: Boolean) = load(Paths.get(uri), flipY)

    fun load(filename: String) = load(Paths.get(filename))
    fun load(filename: String, flipY: Boolean) = load(Paths.get(filename), flipY)

    fun load(path: Path, flipY: Boolean = false) = when (val ext = path.extension) {
        "dds" -> gli.loadDds(path)
        "kmg" -> gli.loadKmg(path)
        "ktx" -> gli.loadKtx(path)
        "bmp", "gif", "ico", "icns", "iff", "jpeg", "jpg", "pam", "pbm", "pct", "pgm", "pict", "png", "pnm", "ppm",
        "targa", "tga", "tif", "tiff", "wbmp" -> gli.loadImage(path.toFile(), flipY)
        else -> error("unsupported extension: $ext")
    }

    fun load(buffer: ByteBuffer, type: String, flipY: Boolean = false): Texture = when (type) {
        "dds" -> gli.loadDds(buffer)
        "kmg" -> gli.loadKmg(buffer)
        "ktx" -> gli.loadKtx(buffer)
        "bmp", "gif", "ico", "icns", "iff", "jpeg", "jpg", "pam", "pbm", "pct", "pgm", "pict", "png", "pnm", "ppm",
        "targa", "tga", "tif", "tiff", "wbmp" -> gli.loadImageFromMem(buffer, flipY)
        else -> error("Type not supported")
    }
}