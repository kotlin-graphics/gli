package gli_

import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

interface save {

    /** Save a texture storage_linear to file.
     *  @param texture Source texture to save
     *  @param path Path for where to save the file. It must include the filaname and filename extension.
     *  The function use the filename extension included in the path to figure out the file container to use.
     *  @return Returns false if the function fails to save the file.   */
    fun save(texture: Texture, path: Path): Boolean = when (val ext = path.extension) {
        "dds" -> gli.saveDds(texture, path)
        "kmg" -> gli.saveKmg(texture, path)
        "ktx" -> gli.saveKtx(texture, path)
        "bmp", "gif", "ico", "icns", "iff", "jpeg", "jpg", "pam", "pbm", "pct", "pgm", "pict", "png", "pnm", "ppm",
        "targa", "tga", "tif", "tiff", "wbmp" -> gli.saveImage(texture, path)
        else -> error("unsupported extension: $ext")
    }

    /** Save a texture storage_linear to file.
     *  @param texture Source texture to save
     *  @param filename: String for where to save the file. It must include the filaname and filename extension.
     *  The function use the filename extension included in the path to figure out the file container to use.
     *  @return Returns false if the function fails to save the file.   */
    fun save(texture: Texture, filename: String): Boolean = save(texture, Paths.get(filename))

    /** Save a texture storage_linear to file.
     *  @param texture Source texture to save
     *  @param uri: Uri for where to save the file. It must include the filaname and filename extension.
     *  The function use the filename extension included in the path to figure out the file container to use.
     *  @return Returns false if the function fails to save the file.   */
    fun save(texture: Texture, uri: URI): Boolean = save(texture, Paths.get(uri))
}