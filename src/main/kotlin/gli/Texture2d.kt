package gli

import glm.vec._2.Vec2i
import glm.vec._3.Vec3i

/**
 * Created by GBarbieri on 03.04.2017.
 */

/** 2d texture  */
class Texture2d : Texture {

    /** Create an empty texture 2D. */
    constructor() : super()

    /** Create a texture2d and allocate a new storage_linear.   */
    constructor(format: Format, extent: Vec2i, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._2D, format, Vec3i(extent, 1), 1, 1, levels, swizzles)

    /** Create a texture2d and allocate a new storage_linear with a complete mipmap chain.  */
    constructor(format: Format, extent: Vec2i, swizzles: Swizzles = Swizzles()) :
            super(Target._2D, format, Vec3i(extent, 1), 1, 1, gli.levels(extent), swizzles)

    /** Create a texture2d view with an existing storage_linear.    */
    constructor(texture: Texture) : super(texture, Target._2D, texture.format)

    /** Create a texture2d view with an existing storage_linear.    */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()) :
            super(texture, Target._2D, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture2d view, reference a subset of an existing texture2d instance.  */
    constructor(texture: Texture, baseLevel: Int, maxLevel: Int) :
            super(texture, Target._2D, texture.format,
                    texture.baseLayer, texture.maxLayer,
                    texture.baseFace, texture.maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
}