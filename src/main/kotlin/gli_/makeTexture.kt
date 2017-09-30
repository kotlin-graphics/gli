package gli_

import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i

interface makeTexture {

    fun makeTexture1d(format: Format, extent: Vec1i, levels: Int = gli.levels(Vec3i(extent.x, 1, 1))) =
            Texture(Target._1D, format, Vec3i(extent.x, 1, 1), 1, 1, levels)

    fun makeTexture1dArray(format: Format, extent: Vec1i, layers: Int, levels: Int = gli.levels(Vec3i(extent.x, 1, 1))) =
            Texture(Target._1D_ARRAY, format, Vec3i(extent.x, 1, 1), layers, 1, levels)

    fun makeTexture2d(format: Format, extent: Vec2i, levels: Int = gli.levels(Vec3i(extent, 1))) =
            Texture(Target._2D, format, Vec3i(extent, 1), 1, 1, levels)

    fun makeTexture2dArray(format: Format, extent: Vec2i, layer: Int, levels: Int = gli.levels(Vec3i(extent, 1))) =
            Texture(Target._2D_ARRAY, format, Vec3i(extent, 1), layer, 1, levels)

    fun makeTexture3d(format: Format, extent: Vec3i, levels: Int = gli.levels(extent)) =
            Texture(Target._3D, format, extent, 1, 1, levels)

    fun makeTextureCube(format: Format, extent: Vec2i, levels: Int = gli.levels(Vec3i(extent, 1))) =
            Texture(Target.CUBE, format, Vec3i(extent, 1), 1, 6, levels)

    fun makeTextureCubeArray(format: Format, extent: Vec2i, layer: Int, levels: Int = gli.levels(Vec3i(extent, 1))) =
            Texture(Target.CUBE_ARRAY, format, Vec3i(extent, 1), layer, 6, levels)
}