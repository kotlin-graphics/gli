package gli_

import glm_.L
import org.lwjgl.system.MemoryUtil.memAddress
import org.lwjgl.system.MemoryUtil.memCopy
import org.lwjgl.system.libc.LibCString.nmemcpy


/** Duplicate an image and create a new image with a new storage_linear allocation. */
fun duplicate(image: Image): Image {

    val result = Image(image.format, image.extent())

    memCopy(memAddress(image.data()), memAddress(result.data()), image.data()!!.remaining())

    return result
}

/** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
fun duplicate(texture: Texture1d): Texture1d {

    val duplicate = Texture1d(
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
fun duplicate(texture: Texture1d, format: Format): Texture1d {

    val duplicate = Texture1d(
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
fun duplicate(texture: Texture1d, baseLevel: Int, maxLevel: Int): Texture1d {

    assert(baseLevel <= maxLevel && baseLevel < texture.levels() && maxLevel < texture.levels())

    val duplicate = Texture1d(
            texture.format,
            texture.extent(baseLevel),
            maxLevel - baseLevel + 1)

    nmemcpy(memAddress(duplicate.data()), memAddress(texture.data(0, 0, baseLevel)), duplicate.size.L)

    return duplicate
}

/** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
fun duplicate(texture: Texture2d): Texture2d {

    val duplicate = Texture2d(
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
fun duplicate(texture: Texture2d, format: Format): Texture2d {

    val duplicate = Texture2d(
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
fun duplicate(texture: Texture2d, baseLevel: Int, maxLevel: Int): Texture2d {

    assert(baseLevel <= maxLevel && baseLevel < texture.levels() && maxLevel < texture.levels())

    val duplicate = Texture2d(
            texture.format,
            texture.extent(baseLevel),
            maxLevel - baseLevel + 1)

    nmemcpy(memAddress(duplicate.data()), memAddress(texture.data(0, 0, baseLevel)), duplicate.size.L)

    return duplicate
}

/** Duplicate a texture and create a new texture with a new storage_linear allocation.  */
fun duplicate(texture: Texture3d): Texture3d {

    val duplicate = Texture3d(
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
fun duplicate(texture: Texture3d, format: Format): Texture3d {

    val duplicate = Texture3d(
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
fun duplicate(texture: Texture3d, baseLevel: Int, maxLevel: Int): Texture3d {

    assert(baseLevel <= maxLevel && baseLevel < texture.levels() && maxLevel < texture.levels())

    val duplicate = Texture3d(
            texture.format,
            texture.extent(baseLevel),
            maxLevel - baseLevel + 1)

    nmemcpy(memAddress(duplicate.data()), memAddress(texture.data(0, 0, baseLevel)), duplicate.size.L)

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
