package gli_

interface clear {

    /** Clear a complete texture    */
    fun clear(texture: Texture) = texture.clear()

    /** Clear a complete texture    */
    fun clear(texture: Texture, blockData: Any) = texture clear blockData

    /** Clear a specific image of a texture */
    fun clear(texture: Texture, layer: Int, face: Int, level: Int, blockData: Any) = texture.clear(layer, face, level, blockData)

    /** Clear an entire level of a texture  */
    fun clearLevel(texture: Texture, baseLevel: Int, levelCount: Int, blockData: Any) {
        for (layerIndex in 0 until texture.layers())
            for (faceIndex in 0 until texture.faces())
                for (levelIndex in 0 until levelCount)
                    texture.clear(layerIndex, faceIndex, baseLevel + levelIndex, blockData)
    }

    /** Clear multiple levels of a texture  */
    fun clearLevel(texture: Texture, baseLevel: Int, blockData: Any) = clearLevel(texture, baseLevel, 1, blockData)

    /** Clear an entire face of a texture   */
    fun clearFace(texture: Texture, baseFace: Int, faceCount: Int, blockData: Any) {
        for (layerIndex in 0 until texture.layers())
            for (faceIndex in 0 until faceCount)
                for (levelIndex in 0 until texture.levels())
                    texture.clear(layerIndex, baseFace + faceIndex, levelIndex, blockData)
    }

    /** Clear multiple faces of a texture   */
    fun clearFace(texture: Texture, baseFace: Int, blockData: Any) = clearFace(texture, baseFace, 1, blockData)

    /** Clear an entire layer of a texture  */
    fun clearLayer(texture: Texture, baseLayer: Int, layerCount: Int, blockData: Any) {
        for (layerIndex in 0 until layerCount)
            for (faceIndex in 0 until texture.faces())
                for (levelIndex in 0 until texture.levels())
                    texture.clear(layerIndex + baseLayer, faceIndex, levelIndex, blockData)
    }

    /** Clear multiple layers of a texture  */
    fun clearLayer(texture: Texture, baseLayer: Int, blockData: Any) = clearLayer(texture, baseLayer, 1, blockData)
}