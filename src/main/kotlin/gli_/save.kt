package gli_

import java.net.URI
import java.nio.*
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

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
        "png", "bmp", "jpg", "tga" -> saveImage(texture, path)
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

    fun saveImage(texture: Texture, path: Path): Boolean {

        if (texture.empty()) return false

        val (w, h, _) = texture.extent(0)
        val format = texture.format
        val compCount = format.formatInfo.component

        val image = when(format) {
            Format.RGB8_UNORM_PACK8 -> {
//https://stackoverflow.com/questions/34318373/java-creating-a-bufferedimage-from-a-bytearray
            }
            else -> return false
        }
//        ImageIO.wr
//        val stride = width * texture.format.bitsPerPixel / 8

        return stbiCall(path.toAbsolutePath().toString(), width, height, compCount, texture.data(), stride)
    }

//    /**
//     * @param quality Jpg quality from 1 to 100
//     */
//    fun saveJpg(texture: Texture, path: Path, quality: Int = 80) : Boolean {
//        return saveUsingStbi(texture, path) { file, width, height, comp, data, _ ->
//            STBImageWrite.stbi_write_jpg(file, width, height, comp, data, quality)
//        }
//    }
//
//    fun saveBmp(texture: Texture, path: Path): Boolean {
//        return saveUsingStbi(texture, path) { file, width, height, comp, data, _ ->
//            STBImageWrite.stbi_write_bmp(file, width, height, comp, data)
//        }
//    }
//
//    fun saveTga(texture: Texture, path: Path) : Boolean {
//        return saveUsingStbi(texture, path) { file, width, height, comp, data, _ ->
//            STBImageWrite.stbi_write_tga(file, width, height, comp, data)
//        }
//    }
}