package gli

import glm.vec._2.Vec2i
import glm.vec._3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** Cube map texture  */
class TextureCube : Texture {

    /** Create an empty texture cube. */
    constructor() : super()

    /** Create a textureCube and allocate a new storage_linear.   */
    constructor(format: Format, extent: Vec2i, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target.CUBE, format, Vec3i(extent, 1), 1, 6, levels, swizzles)

    /** Create a textureCube and allocate a new storage_linear with a complete mipmap chain.  */
    constructor(format: Format, extent: Vec2i, swizzles: Swizzles = Swizzles()) :
            super(Target.CUBE, format, Vec3i(extent, 1), 1, 6, gli.levels(extent), swizzles)

    /** Create a textureCube view with an existing storage_linear.    */
    constructor(texture: Texture) : super(texture, Target.CUBE, texture.format)

    /** Create a textureCube view with an existing storage_linear.    */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()) :
            super(texture, Target.CUBE, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a textureCube view, reference a subset of an existing textureCube instance.  */
    constructor(texture: Texture,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int) :
            super(texture, Target.CUBE, texture.format,
                    texture.baseLayer, texture.maxLayer,
                    texture.baseFace + baseFace, texture.maxFace + maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
}