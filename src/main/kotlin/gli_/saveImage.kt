package gli_

import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.nio.file.Path
import javax.imageio.ImageIO

interface saveImage {

    fun saveImage(texture: Texture, path: Path): Boolean {

        if (texture.empty()) return false

        val (w, h, _) = texture.extent(0)
        val format = texture.format
        val compCount = format.formatInfo.component

        val image = when (format) {
            Format.RGB8_UNORM_PACK8 -> BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR).apply {
                val src = texture.data()
                val dst = (raster.dataBuffer as DataBufferByte).data
                for (i in dst.indices step 3) {
                    dst[i + 0] = src[i + 2]
                    dst[i + 1] = src[i + 1]
                    dst[i + 2] = src[i + 0]
                }
            }
            Format.RGBA8_UNORM_PACK8 -> BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR).apply {
                val src = texture.data()
                val dst = (raster.dataBuffer as DataBufferByte).data
                for (i in dst.indices step 4) {
                    dst[i + 0] = src[i + 3]
                    dst[i + 1] = src[i + 2]
                    dst[i + 2] = src[i + 1]
                    dst[i + 3] = src[i + 0]
                }
            }
            else -> error("unsupported format")
        }

        val ext = path.fileName.toString().substringAfterLast('.')
        val file = path.toFile()
        file.createNewFile()
        return ImageIO.write(image, ext, file)
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