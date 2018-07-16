package gli_

import glm_.BYTES
import glm_.buffer.adr
import glm_.buffer.bufferBig
import glm_.buffer.free
import glm_.buffer.pos
import glm_.glm
import glm_.size
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

interface saveKtx {

    /** Save a texture storage_linear to a KTX file.
     *  @param texture Source texture to save
     *  @param path Path for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to KTX anyway but keep the requested
     *  filename extension.
     *  @return Returns false if the function fails to save the file.   */
    fun saveKtx(texture: Texture, path: Path): Boolean {

        if (texture.empty()) return false

        gl.profile = gl.Profile.KTX
        val format = gl.translate(texture.format, texture.swizzles)
        val target = texture.target

        val desc = texture.format.formatInfo

        val data = bufferBig(computeKtxStorageSize(texture))

        ktx.FOURCC_KTX10.forEach{ data.put(it) }

        ktx.Header10().apply {

            endianness = 0x04030201
            glType = format.type.i
            glTypeSize = if (format.type == gl.TypeFormat.NONE) 1 else desc.blockSize
            glFormat = format.external.i
            glInternalFormat = format.internal.i
            glBaseInternalFormat = format.external.i
            pixelWidth = texture.extent().x
            pixelHeight = if (!target.isTarget1d) texture.extent().y else 0
            pixelDepth = if (target == Target._3D) texture.extent().z else 0
            numberOfArrayElements = if (target.isTargetArray) texture.layers() else 0
            numberOfFaces = if (target.isTargetCube) texture.faces() else 1
            numberOfMipmapLevels = texture.levels()
            bytesOfKeyValueData = 0

            to(data)
        }

        for (level in 0 until texture.levels()) {

            var imageSize = data.pos
            data.pos += Int.BYTES

            for (layer in 0 until texture.layers())
                for (face in 0 until texture.faces()) {

                    val faceSize = texture.size(level)

                    memCopy(texture.data(layer, face, level).adr, data.adr, faceSize)

                    val paddedSize = glm.ceilMultiple(faceSize, 4)

                    imageSize += paddedSize
                    data.pos += paddedSize

                    assert(data.pos <= data.size)
                }

            imageSize = glm.ceilMultiple(imageSize, 4)
        }

        FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            data.pos = 0
            while (data.hasRemaining()) it.write(data)
        }

        data.free()

        return true
    }

    /** Save a texture storage_linear to a KTX file.
     *  @param texture Source texture to save
     *  @param filename String for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to KTX anyway but keep the requested
     *  filename extension.
     *  @return Returns false if the function fails to save the file.   */
    fun saveKtx(texture: Texture, filename: String) = saveKtx(texture, pathOf(filename))

    fun computeKtxStorageSize(texture: Texture): Int {

        val blockSize = texture.format.blockSize
        var totalSize = ktx.FOURCC_KTX10.size + ktx.Header10.size

        for (level in 0 until texture.levels()) {

            totalSize += Int.BYTES

            for (layer in 0 until texture.layers())
                for (face in 0 until texture.faces()) {

                    val faceSize = texture.size(level)
                    val paddedSize = glm.max(blockSize, glm.ceilMultiple(faceSize, 4))

                    totalSize += paddedSize
                }
        }
        return totalSize
    }
}