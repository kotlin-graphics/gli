package gli_

import glm_.vec3.Vec3i
import glm_.glm
import org.lwjgl.system.MemoryUtil.memAddress

/** Pre compute at texture instance creation some information for faster access to texels   */
class Cache {

    val faces: Int
    val levels: Int

    private val baseAddresses : LongArray
    private val imageExtent = Array(16, { Vec3i() })
    private val imageMemorySize = IntArray(16)

    private val globalMemorySize: Int

    constructor(
            storage: Storage,
            format: gli_.Format,
            baseLayer: Int, layers: Int,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int
    ) {
        faces = maxFace - baseFace + 1
        levels = maxLevel - baseLevel + 1

        baseAddresses = LongArray(layers * faces * levels)

        assert(glm.levels(storage.extent(0)) < imageMemorySize.size)

        for (layer in 0 until layers)

            for (face in 0 until faces)

                for (level in 0 until levels) {

                    val index = indexCache(layer, face, level)
                    val offset = storage.baseOffset(baseLayer + layer, baseFace + face, baseLevel + level)
                    baseAddresses[index] = memAddress(storage.data()) + offset
                }

        for (level in 0 until levels) {

            val srcExtent = storage.extent(baseLevel + level)
            val dstExtent = srcExtent * format.blockExtend / storage.blockExtent

            imageExtent[level] = glm.max(dstExtent, 1)
            imageMemorySize[level] = storage.levelSize(baseLevel + level)
        }

        globalMemorySize = storage.layerSize(baseFace, maxFace, baseLevel, maxLevel) * layers
    }

    /** JVM constructor for C++ by value    */
    constructor(cache: Cache) {
        faces = cache.faces
        levels = cache.levels
        baseAddresses = cache.baseAddresses.clone()
        repeat(imageExtent.size) {
            imageExtent[it] = cache.imageExtent[it]
            imageMemorySize[it] = cache.imageMemorySize[it]
        }
        globalMemorySize = cache.globalMemorySize
    }

    private fun indexCache(layer: Int, face: Int, level: Int) = ((layer * faces) + face) * levels + level

    /** Base addresses of each images of a texture. */
    fun baseAddress(layer: Int, face: Int, level: Int) = baseAddresses[indexCache(layer, face, level)]

    /** In texels   */
    fun extent(level: Int) = imageExtent[level]

    /** In bytes    */
    fun memorySize(level: Int) = imageMemorySize[level]

    /** In bytes    */
    val memorySize get() = globalMemorySize
}