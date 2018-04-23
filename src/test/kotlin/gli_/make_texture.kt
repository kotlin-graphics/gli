package gli_

import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import glm_.vec4.Vec4ub
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class make_texture : StringSpec() {

    init {

        "is make_texture equivalent to ctor" {

            run {
                val textureA = gli.makeTexture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4), 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4), 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4, 1, 1)
                val textureC = Texture(Target._1D, Format.RGBA8_UNORM_PACK8, extentC, 1, 1, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }

            run {
                val textureA = gli.makeTexture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 3, 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 3, 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4, 1, 1)
                val textureC = Texture(Target._1D_ARRAY, Format.RGBA8_UNORM_PACK8, extentC, 3, 1, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }

            run {
                val textureA = gli.makeTexture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4, 4, 1)
                val textureC = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, extentC, 1, 1, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }

            run {
                val textureA = gli.makeTexture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 3, 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 3, 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4, 4, 1)
                val textureC = Texture(Target._2D_ARRAY, Format.RGBA8_UNORM_PACK8, extentC, 3, 1, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }

            run {
                val textureA = gli.makeTexture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4)
                val textureC = Texture(Target._3D, Format.RGBA8_UNORM_PACK8, extentC, 1, 1, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }

            run {
                val textureA = gli.makeTextureCube(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = TextureCube(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4, 4, 1)
                val textureC = Texture(Target.CUBE, Format.RGBA8_UNORM_PACK8, extentC, 1, 6, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }

            run {
                val textureA = gli.makeTextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 3, 2)
                textureA clear Vec4ub(255, 127, 0, 255)

                val textureB = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 3, 2)
                textureB clear Vec4ub(255, 127, 0, 255)

                val extentC = Vec3i(4, 4, 1)
                val textureC = Texture(Target.CUBE_ARRAY, Format.RGBA8_UNORM_PACK8, extentC, 3, 6, 2)
                textureC clear Vec4ub(255, 127, 0, 255)

                textureA shouldBe textureB
                textureA shouldBe textureC
                textureB shouldBe textureC
            }
        }
    }
}