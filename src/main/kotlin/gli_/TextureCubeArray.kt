package gli_

import glm_.glm
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import kotlin.reflect.KClass

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
            super(Target.CUBE_ARRAY, format, Vec3i(extent, 1), layers, 6, glm.levels(extent), swizzles)

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
                    texture.baseLayer + baseLayer, texture.baseLayer + maxLayer,
                    texture.baseFace + baseFace, texture.baseFace + maxFace,
                    texture.baseLevel + baseLevel, texture.baseLevel + maxLevel)

    /** Create a view of the texture identified by Layer in the texture array   */
    operator fun get(layer: Int): TextureCube {

        assert(layer < layers())

        return TextureCube(
                this, format,
                baseLayer + layer, baseLayer + layer,
                baseFace, maxFace,
                baseLevel, maxLevel)
    }

    fun extent_(level: Int = 0) = Vec2i(super.extent(level))

    internal inline fun <reified T> load(texelCoord: Vec2i, layer: Int, face: Int, level: Int): T = super.load(Vec3i(texelCoord, 0), layer, face, level)
    fun <T> load(kClass: KClass<*>, texelCoord: Vec2i, layer: Int, face: Int, level: Int): T = super.load(kClass, Vec3i(texelCoord, 0), layer, face, level)

    internal inline fun <reified T> store(texelCoord: Vec2i, layer: Int, face: Int, level: Int, texel: T) = super.store(Vec3i(texelCoord,0), layer, face, level, texel)
    fun <T> store(kClass: KClass<*>, texelCoord: Vec2i, layer: Int, face: Int, level: Int, texel: T) = super.store(kClass, Vec3i(texelCoord,0), layer, face, level, texel)
}