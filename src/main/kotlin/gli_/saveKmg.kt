package gli_

import glm_.buffer.adr
import glm_.buffer.bufferBig
import glm_.size
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

interface saveKmg {

    /** Save a texture storage_linear to a KMG (Khronos Image) file.
     *  @param texture Source texture to save
     *  @param path Path for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to KMG anyway but keep the requested
     *  filename extension.
     *  @return Returns false if the function fails to save the file.   */
    fun saveKmg(texture: Texture, path: Path): Boolean {

        if (texture.empty()) return false

        val data = bufferBig(kmg.FOURCC_KMG100.size + kmg.Header10.size + texture.size)

        kmg.FOURCC_KMG100.forEach { data.put(it) }

        val swizzle = texture.swizzles

        kmg.Header10().apply {
            endianness = 0x04030201
            format = texture.format.i
            target = texture.target.i
            swizzleRed = swizzle[0].i
            swizzleGreen = swizzle[1].i
            swizzleBlue = swizzle[2].i
            swizzleAlpha = swizzle[3].i
            pixelWidth = texture.extent().x
            pixelHeight = texture.extent().y
            pixelDepth = texture.extent().z
            layers = texture.layers()
            levels = texture.levels()
            faces = texture.faces()
            generateMipmaps = gli.Filter.NONE.i
            baseLevel = texture.baseLevel
            maxLevel = texture.maxLevel

            to(data)
        }

        for (layer in 0 until texture.layers())
            for (level in 0 until texture.levels()) {

                val faceSize = texture.size(level)
                for (face in 0 until texture.faces()) {

                    val src = texture.data(layer, face, level)
                    memCopy(src.adr, data.adr, faceSize)

                    data.ptr += faceSize
                    assert(data.ptr <= data.size)
                }
            }

        FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            data.ptr = 0
            while (data.hasRemaining()) it.write(data)
        }

        return true
    }

    /** Save a texture storage_linear to a KMG (Khronos Image) file.
     *  @param texture Source texture to save
     *  @param filename String for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to KMG anyway but keep the requested
     *  filename extension.
     *  @return Returns false if the function fails to save the file.   */
    fun saveKmg(texture: Texture, filename: String) = saveKmg(texture, pathOf(filename))
}