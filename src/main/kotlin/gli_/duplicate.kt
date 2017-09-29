package gli_

import glm_.L
import org.lwjgl.system.MemoryUtil.memAddress
import org.lwjgl.system.MemoryUtil.memCopy
import org.lwjgl.system.libc.LibCString.nmemcpy


interface duplicate {
    /** Duplicate an image and create a new image with a new storage_linear allocation. */
    fun duplicate(image: Image): Image {

        val result = Image(image.format, image.extent())

        memCopy(memAddress(image.data()), memAddress(result.data()), image.data()!!.remaining())

        return result
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(texture: Texture): Texture {

        val duplicate = Texture(
                texture.target,
                texture.format,
                texture.extent(),
                texture.layers(),
                texture.faces(),
                texture.levels())

        duplicateImages(
                texture, duplicate,
                0, texture.layers() - 1,
                0, texture.faces() - 1,
                0, texture.levels() - 1)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation but a different format.
     *  The format must be a compatible format, a format which block size match the original format.    */
    fun duplicate(texture: Texture, format: Format): Texture {

        assert(texture.format.blockSize == format.blockSize)

        val duplicate = Texture(
                texture.target,
                format,
                texture.extent(),
                texture.layers(),
                texture.faces(),
                texture.levels())

        duplicateImages(
                texture, duplicate,
                0, texture.layers() - 1,
                0, texture.faces() - 1,
                0, texture.levels() - 1)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: Texture1d,
            baseLevel: Int, maxLevel: Int
    ): Texture1d {

        assert(baseLevel <= maxLevel && baseLevel < texture.levels())
        assert(maxLevel < texture.levels())

        val duplicate = Texture1d(
                texture.format,
                texture.extent(baseLevel),
                maxLevel - baseLevel + 1)

        memCopy(
                memAddress(texture.data(0, 0, baseLevel)),
                memAddress(duplicate.data()),
                duplicate.size)

        return duplicate
    }

    /** Duplicate a subset of a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: Texture1dArray,
            baseLayer: Int, maxLayer: Int,
            baseLevel: Int, maxLevel: Int
    ): Texture {

        assert(baseLevel <= maxLevel && baseLevel < texture.levels())
        assert(maxLevel < texture.levels())
        assert(baseLayer <= maxLayer && baseLayer < texture.layers())
        assert(maxLayer < texture.layers())

        val duplicate = Texture1dArray(
                texture.format,
                texture[baseLayer].extent_(baseLevel),
                maxLayer - baseLayer + 1,
                maxLevel - baseLevel + 1)

        for (layer in 0 until duplicate.layers())
            memCopy(
                    memAddress(texture.data(layer + baseLayer, 0, baseLevel)),
                    memAddress(duplicate.data(layer, 0, 0)),
                    duplicate[layer].size)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: Texture2d,
            baseLevel: Int, maxLevel: Int
    ): Texture2d {

        assert(baseLevel <= maxLevel && baseLevel < texture.levels() && maxLevel < texture.levels())

        val duplicate = Texture2d(
                texture.format,
                texture.extent(baseLevel),
                maxLevel - baseLevel + 1)

        memCopy(
                memAddress(texture.data(0, 0, baseLevel)),
                memAddress(duplicate.data()),
                duplicate.size)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: Texture2dArray,
            baseLayer: Int, maxLayer: Int,
            baseLevel: Int, maxLevel: Int
    ): Texture {

        assert(baseLevel <= maxLevel && baseLevel < texture.levels())
        assert(maxLevel < texture.levels())
        assert(baseLayer <= maxLayer && baseLayer < texture.layers())
        assert(maxLayer < texture.layers())

        val duplicate = Texture2dArray(
                texture.format,
                texture.extent_(baseLevel),
                maxLayer - baseLayer + 1,
                maxLevel - baseLevel + 1)

        for (layer in 0 until duplicate.layers())
            memCopy(
                    memAddress(texture.data(layer + baseLayer, 0, baseLevel)),
                    memAddress(duplicate.data(layer, 0, 0)),
                    duplicate[layer].size)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: Texture3d,
            baseLevel: Int, maxLevel: Int
    ): Texture3d {

        assert(baseLevel <= maxLevel && baseLevel < texture.levels() && maxLevel < texture.levels())

        val duplicate = Texture3d(
                texture.format,
                texture.extent(baseLevel),
                maxLevel - baseLevel + 1)

        memCopy(
                memAddress(texture.data(0, 0, baseLevel)),
                memAddress(duplicate.data()),
                duplicate.size)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: TextureCube,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int
    ): Texture {

        assert(baseLevel in 0 until texture.levels() && baseLevel <= maxLevel && maxLevel < texture.levels())
        assert(baseFace <= maxFace && baseFace < texture.faces())
        assert(maxFace < texture.faces())

        val duplicate = TextureCube(
                texture.format,
                texture[baseFace].extent_(baseLevel),
                maxLevel - baseLevel + 1)

        for (face in 0 until duplicate.faces())
            memCopy(
                    memAddress(texture[face + baseFace][baseLevel].data()),
                    memAddress(duplicate[face].data()),
                    duplicate[face].size)

        return duplicate
    }

    /** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
    fun duplicate(
            texture: TextureCubeArray,
            baseLayer: Int, maxLayer: Int,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int
    ): Texture {

        assert(baseLevel <= maxLevel && baseLevel < texture.levels())
        assert(maxLevel < texture.levels())
        assert(baseFace <= maxFace && baseFace < texture.faces())
        assert(maxFace < texture.faces())
        assert(baseLayer <= maxLayer && baseLayer < texture.layers())
        assert(maxLayer < texture.layers())

        val duplicate = TextureCubeArray(
                texture.format,
                texture[baseLayer][baseFace].extent_(baseLevel),
                maxLayer - baseLayer + 1,
                maxLevel - baseLevel + 1)

        for (layer in 0 until duplicate.layers())
            for (face in 0 until duplicate[layer].faces())
                memCopy(
                        memAddress(texture[layer + baseLayer][face + baseFace][baseLevel].data()),
                        memAddress(duplicate[layer][face].data()),
                        duplicate[layer][face].size)

        return duplicate
    }


    private fun duplicateImages(
            src: Texture, dst: Texture,
            baseLayer: Int, maxLayer: Int,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int) {

        assert(baseLayer in 0..maxLayer && maxLayer < src.layers())
        assert(baseFace in 0..maxFace && maxFace < src.faces())
        assert(baseLevel in 0..maxLevel && maxLevel < src.levels())

        var levelsSize = 0
        for (levelIndex in 0..maxLevel - baseLevel) {
            assert(dst.size(levelIndex) == src.size(levelIndex))
            levelsSize += dst.size(levelIndex)
        }

        for (layerIndex in 0..maxLayer - baseLayer)
            for (faceIndex in 0..maxFace - baseFace) {
                val d = memAddress(dst.data(layerIndex, faceIndex, baseLevel))
                val s = memAddress(src.data(baseLayer + layerIndex, baseFace + faceIndex, baseLevel))
                nmemcpy(d, s, levelsSize.L)
            }
    }
}
