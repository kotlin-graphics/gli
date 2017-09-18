package gli_

interface view {

    /** Create an image view of an existing image, sharing the same memory storage_linear.  */
    fun view(image: Image) = image

    /** Create a texture view of an existing texture, sharing the same memory storage_linear.   */
    fun view(texture: Texture) = texture

    /// Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only to a subset of layers, levels and faces.
    fun view(
            texture: Texture,
            baseLayer: Int, maxLayer: Int,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)
        assert(baseFace in 0 until texture.faces() && maxFace in 0 until texture.faces() && baseFace <= maxFace)
        assert(baseLayer in 0 until texture.layers() && maxLayer in 0 until texture.layers() && baseLayer <= maxLayer)

        return Texture(
                texture, texture.target, texture.format,
                texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                texture.baseFace + baseFace, texture.baseFace + maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but a different format.
     *  The format must be a compatible format, a format which block size match the original format.    */
    fun view(texture: Texture, format: Format): Texture {
        assert(texture.notEmpty() && texture.format.blockSize == format.blockSize)
        return Texture(texture, texture.target, format)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of levels.  */
    fun view(
            texture: Texture1d,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)

        return Texture(
                texture, Target._1D, texture.format,
                texture.baseLayer, texture.maxLayer,
                texture.baseFace, texture.maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of levels and layers.   */
    fun view(
            texture: Texture1dArray,
            baseLayer: Int, maxLayer: Int,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)
        assert(baseLayer in 0 until texture.layers() && maxLayer in 0 until texture.layers() && baseLayer <= maxLayer)

        return Texture(
                texture, Target._1D_ARRAY, texture.format,
                texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                texture.baseFace, texture.maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of levels.  */
    fun view(
            texture: Texture2d,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)

        return Texture(
                texture, Target._2D, texture.format,
                texture.baseLayer, texture.maxLayer,
                texture.baseFace, texture.maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of levels and layers.   */
    fun view(
            texture: Texture2dArray,
            baseLayer: Int, maxLayer: Int,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)
        assert(baseLayer in 0 until texture.layers() && maxLayer in 0 until texture.layers() && baseLayer <= maxLayer)

        return Texture(
                texture, Target._2D_ARRAY, texture.format,
                texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                texture.baseFace, texture.maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of levels.  */
    fun view(
            texture: Texture3d,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)

        return Texture(
                texture, Target._3D, texture.format,
                texture.baseLayer, texture.maxLayer,
                texture.baseFace, texture.maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of levels and faces.    */
    fun view(
            texture: TextureCube,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)
        assert(baseFace in 0 until texture.faces() && maxFace in 0 until texture.faces() && baseFace <= maxFace)

        return Texture(
                texture, Target.CUBE, texture.format,
                texture.baseLayer, texture.maxLayer,
                texture.baseFace, texture.baseFace + maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }

    /** Create a texture view of an existing texture, sharing the same memory storage_linear but giving access only
     *  to a subset of layers, levels and faces.    */
    fun view(
            texture: TextureCubeArray,
            baseLayer: Int, maxLayer: Int,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int): Texture {

        assert(texture.notEmpty())
        assert(baseLevel in 0 until texture.levels() && maxLevel in 0 until texture.levels() && baseLevel <= maxLevel)
        assert(baseFace in 0 until texture.faces() && maxFace in 0 until texture.faces() && baseFace <= maxFace)
        assert(baseLayer in 0 until texture.layers() && maxLayer in 0 until texture.layers() && baseLayer <= maxLayer)

        return Texture(
                texture, Target.CUBE_ARRAY, texture.format,
                texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                texture.baseFace + baseFace, texture.baseFace + maxFace,
                texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
    }
}