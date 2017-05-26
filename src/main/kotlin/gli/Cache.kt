package gli

class Cache(
        storage: Storage,
        format: Format,
        baseLayer: Int, layers: Int,
        baseFace: Int, maxFace: Int,
        baseLevel: Int, maxLevel: Int
) {

    val faces = maxFace - baseFace + 1
    val levels = maxLevel - baseLevel + 1

    val globalMemorySize = storage.layerSize(baseFace, maxFace, baseLevel, maxLevel) * layers

    private val imageMemorySize = LongArray(16)

    private val baseOffsets = IntArray(layers * faces * levels)

    init {
        assert(gli.levels(storage.extent(0)) < imageMemorySize.size)

        for(layer in 0 until layers)

            for(face in 0 until faces)

                for(level in 0 until levels) {

                    val index = indexCache(layer, face, level)

                    baseOffsets[index] = storage.baseOffset(baseLayer + layer, baseFace + face, baseLevel + level)
                }

        for(level in 0 until levels) {

            val srcExtent = storage.extent(baseLevel + level)
            val dstExtent = srcExtent * format.blockExtend / storage.blockExtend

//            ds
        }
    }

    private fun indexCache(layer:Int, face:Int, level: Int) = ((layer * faces) + face) * levels + level
}