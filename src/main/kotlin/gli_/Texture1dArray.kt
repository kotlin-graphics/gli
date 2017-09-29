package gli_

import glm_.vec1.Vec1i
import glm_.vec3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** 1d array texture    */
class Texture1dArray : Texture {

    /** Create an empty texture 1D array    */
    constructor() : super()

    /** Create a texture1d_array and allocate a new storage_linear  */
    constructor(
            format: Format,
            extent: Vec1i,
            layers: Int,
            levels: Int,
            swizzles: Swizzles = Swizzles()
    ) :
            super(Target._1D_ARRAY, format, Vec3i(extent.x, 1, 1), layers, 1, levels, swizzles)

    /** Create a texture1d_array and allocate a new storage_linear with a complete mipmap chain */
    constructor(
            format: Format,
            extent: Vec1i,
            layers: Int,
            swizzles: Swizzles = Swizzles()
    ) :
            super(Target._1D_ARRAY, format, Vec3i(extent.x, 1, 1), layers, 1, gli.levels(extent), swizzles)

    /** Create a texture1d_array view with an existing storage_linear   */
    constructor(
            texture: Texture
    ) :
            super(texture, Target._1D_ARRAY, texture.format)

    /** Create a texture1d_array view with an existing storage_linear   */
    constructor(
            texture: Texture,
            format: Format,
            baseLayer: Int, maxLayer: Int,
            baseFace: Int, maxFace: Int,
            baseLevel: Int, maxLevel: Int,
            swizzles: Swizzles = Swizzles()
    ) :
            super(texture, Target._1D_ARRAY, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture view, reference a subset of an exiting storage_linear  */
    constructor(texture: Texture,
                baseLayer: Int, maxLayer: Int,
                baseLevel: Int, maxLevel: Int
    ) :
            super(texture, Target._1D_ARRAY, texture.format,
                    texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                    texture.baseFace, texture.maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)

    /** Create a view of the texture identified by Layer in the texture array   */
    operator fun get(layer: Int): Texture1d {

        assert(notEmpty() && layer < layers())

        return Texture1d(
                this, format,
                baseLayer + layer, baseLayer + layer,
                baseFace, maxFace,
                baseLevel, maxLevel)
    }

    fun extent_(level: Int = 0) = Vec1i(super.extent(level))

    inline fun <reified T> load(texelCoord: Vec1i, layer: Int, level: Int) =
            super.load<T>(Vec3i(texelCoord.x, 0, 0), layer, 0, level)

    inline fun <reified T> store(texelCoord: Vec1i, layer: Int, level: Int, texel: T) =
            super.store(Vec3i(texelCoord.x, 0, 0), layer, 0, level, texel)
}