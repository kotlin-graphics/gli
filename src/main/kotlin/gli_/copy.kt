package gli_

interface copy {

    /** Copy a specific image of a texture  */
    fun copy(
            textureSrc: Texture, layerSrc: Int, faceSrc: Int, levelSrc: Int,
            textureDst: Texture, layerDst: Int, faceDst: Int, levelDst: Int
    ) =
            textureDst.copy(textureSrc, layerSrc, faceSrc, levelSrc, layerDst, faceDst, levelDst)

    /** Copy a texture  */
    fun copy(
            textureSrc: Texture,
            textureDst: Texture
    ) =
            copyLayer(textureSrc, 0, textureDst, 0, textureDst.layers())

    /** Copy multiple levels of a texture   */
    fun copyLevel(
            textureSrc: Texture, baseLevelSrc: Int,
            textureDst: Texture, baseLevelDst: Int,
            levelCount: Int
    ) {
        for (layerIndex in 0 until textureSrc.layers())
            for (faceIndex in 0 until textureSrc.faces())
                for (levelIndex in 0 until levelCount)
                    textureDst.copy(
                            textureSrc,
                            layerIndex, faceIndex, baseLevelSrc + levelIndex,
                            layerIndex, faceIndex, baseLevelDst + levelIndex)
    }

    /** Copy an entire level of a texture   */
    fun copyLevel(
            textureSrc: Texture, baseLevelSrc: Int,
            textureDst: Texture, baseLevelDst: Int
    ) =
            copyLevel(textureSrc, baseLevelSrc, textureDst, baseLevelDst, 1)

    /** Copy multiple faces of a texture    */
    fun copyFace(
            textureSrc: Texture, baseFaceSrc: Int,
            textureDst: Texture, baseFaceDst: Int,
            faceCount: Int
    ) {
        for (layerIndex in 0 until textureSrc.layers())
            for (faceIndex in 0 until faceCount)
                for (levelIndex in 0 until textureSrc.levels())
                    textureDst.copy(
                            textureSrc,
                            layerIndex, baseFaceSrc + faceIndex, levelIndex,
                            layerIndex, baseFaceDst + faceIndex, levelIndex)
    }

    /** Copy an entire face of a texture    */
    fun copyFace(
            textureSrc: Texture, baseFaceSrc: Int,
            textureDst: Texture, baseFaceDst: Int
    ) =
            copyFace(textureSrc, baseFaceSrc, textureDst, baseFaceDst, 1)

    /** Copy multiple layers of a texture   */
    fun copyLayer(
            textureSrc: Texture, baseLayerSrc: Int,
            textureDst: Texture, baseLayerDst: Int,
            layerCount: Int
    ) {
        for (layerIndex in 0 until layerCount)
            for (faceIndex in 0 until textureSrc.faces())
                for (levelIndex in 0 until textureSrc.levels())
                    textureDst.copy(
                            textureSrc,
                            baseLayerSrc + layerIndex, faceIndex, levelIndex,
                            baseLayerDst + layerIndex, faceIndex, levelIndex)
    }

    /** Copy an entire layer of a texture   */
    fun copyLayer(
            textureSrc: Texture, baseLayerSrc: Int,
            textureDst: Texture, baseLayerDst: Int
    ) =
            copyLayer(textureSrc, baseLayerSrc, textureDst, baseLayerDst, 1)
}