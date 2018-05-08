package gli_

import glm_.BYTES
import glm_.buffer.adr
import glm_.buffer.bufferBig
import glm_.buffer.pos
import glm_.glm
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

interface loadKtx {

    /** Loads a texture storage_linear from KTX memory. Returns an empty storage_linear in case of failure.
     *  @param filename String of the file to open including filaname and filename extension */
    fun loadKtx(filename: String) = loadKtx(Paths.get(filename))

    fun loadKtx(uri: URI) = loadKtx(Paths.get(uri))

    /** Loads a texture storage_linear from KTX file. Returns an empty storage_linear in case of failure.
     *  @param path Path of the file to open including filaname and filename extension  */
    fun loadKtx(path: Path): Texture {

        val buffer = FileChannel.open(path, StandardOpenOption.READ).use { channel ->
            bufferBig(channel.size().i).also {
                while (channel.read(it) > 0) Unit
                it.pos = 0
                it.order(ByteOrder.nativeOrder())
            }
        }

        return loadKtx(buffer)
    }

    /** Loads a texture storage_linear from KTX memory. Returns an empty storage_linear in case of failure.
     *  @param data buffer of the texture container data to read  */
    fun loadKtx(data: ByteBuffer): Texture {

        assert(data.size >= ktx.Header10.size)

        // KTX10
        run {
            if (ktx.FOURCC_KTX10.all { it == data.get() })
                return loadKtx10(data)
        }

        return Texture()
    }

    private fun loadKtx10(data: ByteBuffer): Texture {

        val header = ktx.Header10(data)

        // Skip key value data
        data.pos += header.bytesOfKeyValueData

        gl.profile = gl.Profile.KTX
        val format = gl.find(
                gl.InternalFormat.of(header.glInternalFormat),
                gl.ExternalFormat.of(header.glFormat),
                gl.TypeFormat.of(header.glType))
        assert(format != Format.INVALID)

        val blockSize = format.blockSize

        val texture = Texture(
                target = header.target,
                format = format,
                extent = Vec3i(
                        x = header.pixelWidth,
                        y = glm.max(header.pixelHeight, 1),
                        z = glm.max(header.pixelDepth, 1)),
                layers = glm.max(header.numberOfArrayElements, 1),
                faces = glm.max(header.numberOfFaces, 1),
                levels = glm.max(header.numberOfMipmapLevels, 1))

        for (level in 0 until texture.levels()) {

            data.pos += Int.BYTES

            for (layer in 0 until texture.layers())
                for (face in 0 until texture.faces()) {

                    val faceSize = texture.size(level)
                    val dst = texture.data(layer, face, level)
                    memCopy(data.adr, dst.adr, faceSize)
                    data.pos += glm.max(blockSize, glm.ceilMultiple(faceSize, 4))
                }
        }
        return texture
    }
}