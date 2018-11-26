package gli_

import kool.adr
import kool.Buffer
import kool.pos
import glm_.i
import glm_.size
import glm_.vec3.Vec3i
import java.net.URI
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

interface loadKmg {

    /** Loads a texture storage_linear from KMG (Khronos Image) file. Returns an empty storage_linear in case of failure.
     *  @param filename String of the file to open including filaname and filename extension */
    fun loadKmg(filename: String) = loadKmg(Paths.get(filename))

    fun loadKmg(uri: URI) = loadKmg(Paths.get(uri))

    /** Loads a texture storage_linear from KMG (Khronos Image) file. Returns an empty storage_linear in case of failure.
     *  @param path Path of the file to open including filaname and filename extension  */
    fun loadKmg(path: Path): Texture {

        val buffer = FileChannel.open(path, StandardOpenOption.READ).use { channel ->
            Buffer(channel.size().i).also {
                while (channel.read(it) > 0);
                it.pos = 0
                it.order(ByteOrder.nativeOrder())
            }
        }

        return loadKmg(buffer)
    }

    /** Loads a texture storage_linear from KMG (Khronos Image) file. Returns an empty storage_linear in case of failure.
     *  @param data buffer of the texture container data to read  */
    fun loadKmg(data: ByteBuffer): Texture {

        assert(data.size >= kmg.Header10.size)

        // KMG100
        run {
            if (kmg.FOURCC_KMG100.all { it == data.get() })    // CONSUMED
                return loadKmg100(data)
        }

        return Texture()
    }

    private fun loadKmg100(data: ByteBuffer): Texture {

        val header = kmg.Header10(data)

        val texture = Texture(
                target = Target.of(header.target),
                format = Format.of(header.format),
                extent = Vec3i(header.pixelWidth, header.pixelHeight, header.pixelDepth),
                layers = header.layers,
                faces = header.faces,
                levels = header.levels,
                swizzles = Swizzles(header.swizzleRed, header.swizzleGreen, header.swizzleBlue, header.swizzleAlpha))

        for (layer in 0 until texture.layers())
            for (level in 0 until texture.levels()) {

                val faceSize = texture.size(level)
                for (face in 0 until texture.faces()) {

                    val dst = texture.data(layer, face, level)
                    memCopy(data.adr, dst.adr, faceSize)

                    data.pos += faceSize
                    assert(data.pos <= data.size)
                }
            }

        return Texture(
                texture, texture.target, texture.format,
                texture.baseLayer, texture.maxLayer,
                texture.baseFace, texture.maxFace,
                header.baseLevel, header.maxLevel,
                texture.swizzles)
    }
}