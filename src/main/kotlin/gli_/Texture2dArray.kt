package gli_

import glm_.vec2.Vec2i
import glm_.vec3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** 2d array texture    */
class Texture2dArray : Texture {

    /** Create an empty texture 2D array    */
    constructor() : super()

    /** Create a texture2d_array and allocate a new storage_linear  */
    constructor(format: Format, extent: Vec2i, layers: Int, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._2D_ARRAY, format, Vec3i(extent, 1), layers, 1, levels, swizzles)

    /** Create a texture2d_array and allocate a new storage_linear with a complete mipmap chain */
    constructor(format: Format, extent: Vec2i, layers: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._2D_ARRAY, format, Vec3i(extent, 1), layers, 1, gli.levels(extent), swizzles)

    /** Create a texture2d_array view with an existing storage_linear   */
    constructor(texture: Texture) : super(texture, Target._2D_ARRAY, texture.format)

    /** Create a texture2d_array view with an existing storage_linear   */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()) :
            super(texture, Target._2D_ARRAY, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture view, reference a subset of an exiting storage_linear  */
    constructor(texture: Texture,
                baseLayer: Int, maxLayer: Int,
                baseLevel: Int, maxLevel: Int) :
            super(texture, Target._2D_ARRAY, texture.format,
                    texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                    texture.baseFace, texture.maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)

    /** Create a view of the texture identified by Layer in the texture array   */
    operator fun get(layer: Int): Texture2d {

        assert(layer < layers())

        return Texture2d(this, format,
                baseLayer + layer, baseLayer + layer,
                baseFace, maxFace,
                baseLevel, maxLevel)
    }
}