package gli_

import glm_.vec1.Vec1i
import glm_.vec3.Vec3i

/**
 * Created by GBarbieri on 04.04.2017.
 */

/** 1d texture  */
class Texture1d : Texture {

    /** Create an empty texture 1D  */
    constructor() : super()

    /** Create a texture1d and allocate a new storage_linear    */
    constructor(format: Format, extent: Vec1i, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._1D, format, Vec3i(extent.x, 1, 1), 1, 1, levels, swizzles)

    /** vec3i extend    */
    constructor(format: Format, extent: Vec3i, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._1D, format, extent, 1, 1, levels, swizzles)

    /** Create a texture1d and allocate a new storage_linear    */
    constructor(format: Format, extent: Vec1i, swizzles: Swizzles = Swizzles()) :
            super(Target._1D, format, Vec3i(extent.x, 1, 1), 1, 1, gli.levels(extent), swizzles)

    /** Create a texture1d view with an existing storage_linear */
    constructor(texture: Texture) : super(texture, Target._1D, texture.format)

    /** Create a texture1d view with an existing storage_linear */
    constructor(texture: Texture, format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()) :
            super(texture, Target._1D, format, baseLayer, maxLayer, baseFace, maxFace, baseLevel, maxLevel, swizzles)

    /** Create a texture1d view, reference a subset of an existing texture1d instance   */
    constructor(texture: Texture, baseLevel: Int, maxLevel: Int) :
            super(texture, Target._1D, texture.format,
                    texture.baseLayer, texture.maxLayer,
                    texture.baseFace, texture.maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)

    /** for duplicate   */
    constructor(format: Format, extent: Vec3i, layers: Int, faces: Int, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._1D, format, extent, layers, faces, levels, swizzles)

    /** Create a view of the image identified by Level in the mipmap chain of the texture.  */
    operator fun get(level: Int): Image {
        assert(level < levels())
        return Image(storage!!, format, baseLayer, baseFace, baseLevel + level)
    }

    inline fun <reified T> load(texelCoord:Vec1i, level:Int) = super.load<T>(Vec3i(texelCoord.x, 0, 0), 0, 0, level)

    inline fun <reified T : Any> store(texelCoord:Vec1i, level:Int, texel: T) = super.store<T>(Vec3i(texelCoord.x, 0, 0), 0, 0, level, texel)

    override fun dispose() = super.dispose()
}