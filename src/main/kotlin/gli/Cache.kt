package gli

import glm_.vec3.Vec3i
import glm_.glm

/** Pre compute at texture instance creation some information for faster access to texels   */
class Cache(
        storage: Storage,
        format: Format,
        baseLayer: Int, layers: Int,
        baseFace: Int, maxFace: Int,
        baseLevel: Int, maxLevel: Int
) {

    val faces = maxFace - baseFace + 1
    val levels = maxLevel - baseLevel + 1

    private val baseOffsets = IntArray(layers * faces * levels)
    private val imageExtent = Array(16, { Vec3i() })
    private val imageMemorySize = IntArray(16)

    init {

        assert(gli.levels(storage.extent(0)) < imageMemorySize.size)

        for (layer in 0 until layers)

            for (face in 0 until faces)

                for (level in 0 until levels) {

                    val index = indexCache(layer, face, level)

                    baseOffsets[index] = storage.baseOffset(baseLayer + layer, baseFace + face, baseLevel + level)
                }

        for (level in 0 until levels) {

            val srcExtent = storage.extent(baseLevel + level)
            val dstExtent = srcExtent * format.blockExtend / storage.blockExtend

            imageExtent[level] = glm.max(dstExtent, 1)
            imageMemorySize[level] = storage.levelSize(baseLevel + level)
        }
    }

    private val globalMemorySize = storage.layerSize(baseFace, maxFace, baseLevel, maxLevel) * layers

    private fun indexCache(layer: Int, face: Int, level: Int) = ((layer * faces) + face) * levels + level

    /** Base addresses of each images of a texture. */
    fun baseAddress(layer: Int, face: Int, level: Int) = baseOffsets[indexCache(layer, face, level)]

    /** In texels   */
    fun extent(level: Int) = imageExtent[level]

    /** In bytes    */
    fun memorySize(level: Int) = imageMemorySize[level]

    /** In bytes    */
    val memorySize get() = globalMemorySize
}