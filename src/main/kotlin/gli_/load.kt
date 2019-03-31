package gli_

import glm_.vec3.Vec3i
import kool.*
import org.lwjgl.stb.*
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import java.io.*
import java.lang.IllegalArgumentException
import java.net.URI
import java.nio.*
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by elect on 01/05/17.
 */

interface load {

    fun load(uri: URI) = load(Paths.get(uri))
    fun load(uri: URI, flipY: Boolean) = load(Paths.get(uri), flipY)

    fun load(filename: String) = load(Paths.get(filename))
    fun load(filename: String, flipY: Boolean) = load(Paths.get(filename), flipY)

    fun load(path: Path, flipY: Boolean = false) = when (path.extension) {
        "dds" -> gli.loadDds(path)
        "kmg" -> gli.loadKmg(path)
        "ktx" -> gli.loadKtx(path)
        "jpeg", "jpg", "png", "gif", "bmp", "tga" -> loadImage(path.toFile(), flipY)
        else -> throw Error("unsupported extension: ${path.fileName}")
    }

    fun load(buffer: ByteBuffer, type: String, flipY: Boolean = false): Texture {
        return when(type) {
            "dds"  -> gli.loadDds(buffer)
            "kmg"  -> gli.loadKmg(buffer)
            "ktx"  -> gli.loadKtx(buffer)
            "jpeg", "jpg", "png", "gif", "bmp", "tga" -> loadImageFromMem(buffer, flipY)
            else -> throw IllegalArgumentException("Type not supported")
        }
    }

    private fun loadImage(file: File, flipY: Boolean): Texture {

        if(!file.exists()) throw NoSuchFileException(file)

        val width: Int
        val height: Int
        val compCount: Int

        var imageBuffer: ByteBuffer

        Stack.with { mem ->
            val widthBuf = mem.ints(1)
            val heightBuf = mem.ints(1)
            val compCountBuf = mem.ints(1)

            imageBuffer = STBImage.stbi_load(file.absolutePath, widthBuf, heightBuf, compCountBuf, 0)
                          ?: throw IOException("Couldn't load image at $file.")

            width = widthBuf.get()
            height = heightBuf.get()
            compCount = compCountBuf.get()
        }

        if(flipY) {
	        imageBuffer = flipY(imageBuffer, width, height)
        }

		return createTexture(imageBuffer, width, height, compCount)
	}

	private fun loadImageFromMem(buffer: ByteBuffer, flipY: Boolean): Texture {

        val width: Int
        val height: Int
        val compCount: Int

        var imageBuffer: ByteBuffer

        Stack.with { mem ->
            val widthBuf = mem.ints(1)
            val heightBuf = mem.ints(1)
            val compCountBuf = mem.ints(1)

            imageBuffer = stbi_load_from_memory(buffer, widthBuf, heightBuf, compCountBuf, 0)
                          ?: throw IOException("Couldn't load image")

            width = widthBuf.get()
            height = heightBuf.get()
            compCount = compCountBuf.get()
        }

		if(flipY) {
			imageBuffer = flipY(imageBuffer, width, height)
		}

        return createTexture(imageBuffer, width, height, compCount)
    }

    /**
     * creates a texture from a buffer containing the buffer.
     *
     * Each pixel is [compCount] bytes in size.
     *
     * [compCount]:
     *
     * 1 : gray
     *
     * 2 : gray alpha
     *
     * 3 : red green blue
     *
     * 4 : red green blue alpha
     *
     * @param compCount the number of color-components per pixel
     */
    fun createTexture(buffer: ByteBuffer, width: Int, height: Int, compCount: Int = 4): Texture {
        val extent = Vec3i(width, height, 1)
        return when(compCount) {
            1 -> Texture(Target._2D, Format.R8_UNORM_PACK8, extent, 1, 1, 1)
            2 -> Texture(Target._2D, Format.RG8_UNORM_PACK8, extent, 1, 1, 1)
            3 -> Texture(Target._2D, Format.RGB8_UNORM_PACK8, extent, 1, 1, 1)
            4 -> Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extent, 1, 1, 1)
            else -> throw IllegalArgumentException("compCount should be in range 1..4")
        }.apply {
            // 1 to 1
            val dst = data()
            assert(dst.rem >= buffer.rem)
            dst.put(buffer)
        }
    }
}

private var tgaAdded = false