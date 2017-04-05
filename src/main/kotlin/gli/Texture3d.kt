package gli

import glm.vec._3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** 3d texture  */
class Texture3d : Texture {

    /** Create an empty texture 3D. */
    constructor() : super()

    /** Create a texture3d and allocate a new storage_linear.   */
    constructor(format: Format, extent: Vec3i, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._3D, format, extent, 1, 1, levels, swizzles)

    /** Create a texture3d and allocate a new storage_linear with a complete mipmap chain.  */
    constructor(format: Format, extent: Vec3i, swizzles: Swizzles = Swizzles()) :
            super(Target._3D, format, extent, 1, 1, gli.levels(extent), swizzles)

    /** Create a texture3d view with an existing storage_linear.    */
    constructor(texture: Texture) : super(texture, Target._3D, texture.format)

    /** Create a texture3d view with an existing storage_linear.    */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()) :
            super(texture, Target._3D, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture3d view, reference a subset of an existing texture3d instance.  */
    constructor(texture: Texture, baseLevel: Int, maxLevel: Int) :
            super(texture, Target._3D, texture.format,
                    texture.baseLayer, texture.maxLayer,
                    texture.baseFace, texture.maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)
}