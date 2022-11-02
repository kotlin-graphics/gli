package gli_

import glm_.glm
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import kotlin.reflect.KClass

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

    /** vec3i extend    */
    constructor(format: Format, extent: Vec3i, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._2D, format, extent, 1, 1, levels, swizzles)

    /** Create a texture2d and allocate a new storage_linear with a complete mipmap chain.  */
    constructor(format: Format, extent: Vec2i, swizzles: Swizzles = Swizzles()) :
            this(format, Vec3i(extent, 1), swizzles)

    constructor(format: Format, extent: Vec3i, swizzles: Swizzles = Swizzles()) :
            super(Target._2D, format, extent, 1, 1, glm.levels(extent), swizzles)

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

    /** for duplicate   */
    constructor(format: Format, extent: Vec3i, layers: Int, faces: Int, levels: Int, swizzles: Swizzles = Swizzles()) :
            super(Target._2D, format, extent, layers, faces, levels, swizzles)

    /** Create a view of the image identified by Level in the mipmap chain of the texture.  */
    operator fun get(level: Int): Image {
        assert(level < levels())
        return Image(storage!!, format, baseLayer, baseFace, baseLevel + level)
    }

    fun extent_(level: Int = 0) = Vec2i(super.extent(level))

    internal inline fun <reified T> load(texelCoord: Vec2i, level: Int): T = super.load(Vec3i(texelCoord, 0), 0, 0, level)
    fun <T> load(kclass: KClass<*>, texelCoord: Vec2i, level: Int): T = super.load(kclass, Vec3i(texelCoord, 0), 0, 0, level)
    internal inline fun <reified T> store(texelCoord: Vec2i, level: Int, texel: T) = super.store(Vec3i(texelCoord, 0), 0, 0, level, texel)
    fun <T> store(kclass: KClass<*>, texelCoord: Vec2i, level: Int, texel: T) = super.store(kclass, Vec3i(texelCoord, 0), 0, 0, level, texel)
}