package gli

import glm.vec2.Vec2i
import glm.vec3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** cube array texture    */
class TextureCubeArray : Texture {

    /** Create an empty texture cube array    */
    constructor() : super()

    /** Create a texture cube array and allocate a new storage_linear  */
    constructor(format: Format, extent: Vec2i, layers: Int, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target.CUBE_ARRAY, format, Vec3i(extent, 1), layers, 6, levels, swizzles)

    /** Create a texture2d_array and allocate a new storage_linear with a complete mipmap chain */
    constructor(format: Format, extent: Vec2i, layers: Int, swizzles: Swizzles = Swizzles()) :
            super(Target.CUBE_ARRAY, format, Vec3i(extent, 1), layers, 6, levels(extent), swizzles)

    /** Create a texture2d_array view with an existing storage_linear   */
    constructor(texture: Texture) : super(texture, Target.CUBE_ARRAY, texture.format)

    /** Create a texture2d_array view with an existing storage_linear   */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()) :
            super(texture, Target.CUBE_ARRAY, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture view, reference a subset of an exiting storage_linear  */
    constructor(texture: Texture,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int) :
            super(texture, Target.CUBE_ARRAY, texture.format,
                    texture.baseLayer + baseLayer, texture.maxLayer + maxLayer,
                    texture.baseFace + baseFace, texture.maxFace + maxFace,
                    texture.baseLevel + baseLevel, texture.maxLevel + maxLevel)
}