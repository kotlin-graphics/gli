package gli

import glm.vec._1.Vec1i
import gli.Swizzle.*
import glm.vec._3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** 1d array texture    */
class Texture1dArray : Texture {

    /** Create an empty texture 1D array    */
    constructor() : super()

    /** Create a texture1d_array and allocate a new storage_linear  */
    constructor(format: Format, extent: Vec1i, layers: Int, levels: Int, swizzles: Swizzles = Swizzles(RED, GREEN, BLUE, ALPHA)) :
            super(Target._1D_ARRAY, format, Vec3i(extent.x, 1, 1), layers, 1, levels, swizzles)

    /** Create a texture1d_array and allocate a new storage_linear with a complete mipmap chain */
    constructor(format: Format, extent: Vec1i, layers: Int, swizzles: Swizzles = Swizzles(RED, GREEN, BLUE, ALPHA)) :
            super(Target._1D_ARRAY, format, Vec3i(extent.x, 1, 1), layers, 1, levels(extent), swizzles)

    /** Create a texture1d_array view with an existing storage_linear   */
    constructor(texture: Texture) : super(texture, Target._1D_ARRAY, texture.format)

    /** Create a texture1d_array view with an existing storage_linear   */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles(RED, GREEN, BLUE, ALPHA)) :
            super(texture, Target._1D_ARRAY, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture view, reference a subset of an exiting storage_linear  */
    constructor(texture: Texture,
                baseLayer: Int, maxLayer: Int,
                baseLevel: Int, maxLevel: Int) :
            super(texture, Target._1D_ARRAY, texture.format,
                    texture.baseLayer + baseLayer, texture.maxLayer + maxLayer,
                    texture.baseFace, texture.maxFace,
                    texture.baseLevel + baseLevel, texture.maxLevel + maxLevel)
}