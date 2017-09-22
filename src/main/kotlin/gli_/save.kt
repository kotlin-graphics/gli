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
    fun save(texture: Texture, path: Path) = when(path.extension) {
        "dds" -> gli.saveDds(texture, path)
        "kmg" ->gli.saveKmg(texture, path)
        "ktx" -> gli.saveKtx(texture, path)
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
}