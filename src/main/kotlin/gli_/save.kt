package gli_

import org.lwjgl.stb.*
import java.net.URI
import java.nio.*
import java.nio.file.Path
import java.nio.file.Paths

interface save {

    /** Save a texture storage_linear to file.
     *  @param texture Source texture to save
     *  @param path Path for where to save the file. It must include the filaname and filename extension.
     *  The function use the filename extension included in the path to figure out the file container to use.
     *  @return Returns false if the function fails to save the file.   */
    fun save(texture: Texture, path: Path) = when(path.extension) {
        "dds" -> gli.saveDds(texture, path)
        "kmg" -> gli.saveKmg(texture, path)
        "ktx" -> gli.saveKtx(texture, path)
        "png" -> savePng(texture, path)
        "bmp" -> saveBmp(texture, path)
        "jpg" -> saveJpg(texture, path)
        "tga" -> saveTga(texture, path)
        else -> throw Error()
    }

    /** Save a texture storage_linear to file.
     *  @param texture Source texture to save
     *  @param filename: String for where to save the file. It must include the filaname and filename extension.
     *  The function use the filename extension included in the path to figure out the file container to use.
     *  @return Returns false if the function fails to save the file.   */
    fun save(texture: Texture, filename: String) = save(texture, Paths.get(filename))

    /** Save a texture storage_linear to file.
     *  @param texture Source texture to save
     *  @param uri: Uri for where to save the file. It must include the filaname and filename extension.
     *  The function use the filename extension included in the path to figure out the file container to use.
     *  @return Returns false if the function fails to save the file.   */
    fun save(texture: Texture, uri: URI) = save(texture, Paths.get(uri))

    private inline fun saveUsingStbi(texture: Texture, path: Path, stbiCall: (String, Int, Int, Int, ByteBuffer, Int) -> Boolean): Boolean {

        if (texture.empty()) return false

        val width = texture.extent(0).x
        val height = texture.extent(0).y
        val format = texture.format
        val compCount = format.formatInfo.component

        // TODO is better way to get component size in bits?
        if(format.bitsPerPixel / compCount != 8 && format.bitsPerPixel % 8 == 0) {
            System.err.println("Only textures with 8 bit per color channel are supported")
            return false
        }

        val stride = width * texture.format.bitsPerPixel / 8

        return stbiCall(path.toAbsolutePath().toString(), width, height, compCount, texture.data(), stride)
    }

    fun savePng(texture: Texture, path: Path): Boolean {
        return saveUsingStbi(texture, path) { file, width, height, comp, data, stride ->

            STBImageWrite.stbi_write_png(file, width, height, comp, data, stride)
        }
    }

    /**
     * @param quality Jpg quality from 1 to 100
     */
    fun saveJpg(texture: Texture, path: Path, quality: Int = 80) : Boolean {
        return saveUsingStbi(texture, path) { file, width, height, comp, data, _ ->
            STBImageWrite.stbi_write_jpg(file, width, height, comp, data, quality)
        }
    }

    fun saveBmp(texture: Texture, path: Path): Boolean {
        return saveUsingStbi(texture, path) { file, width, height, comp, data, _ ->
            STBImageWrite.stbi_write_bmp(file, width, height, comp, data)
        }
    }

    fun saveTga(texture: Texture, path: Path) : Boolean {
        return saveUsingStbi(texture, path) { file, width, height, comp, data, _ ->
            STBImageWrite.stbi_write_tga(file, width, height, comp, data)
        }
    }
}