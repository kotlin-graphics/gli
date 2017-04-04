package gli

import gli.Swizzle.*
import glm.vec._1.Vec1i
import glm.vec._2.Vec2i
import glm.vec._3.Vec3i
import glm.vec._4.Vec4b
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreSwizzle : StringSpec() {

    init {

        "swizzle" {

            run {
                val texture = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
                texture.swizzles shouldBe Swizzles(RED, GREEN, BLUE, ALPHA)
            }

            run {
                val texture = Texture(Target._2D, Format.BGRA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
                texture.swizzles shouldBe Swizzles(BLUE, GREEN, RED, ALPHA)
            }

            run {
                val texture = Texture(Target._2D, Format.BGRA8_UNORM_PACK8, Vec3i(1), 1, 1, 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                texture.swizzles shouldBe Swizzles(BLUE, GREEN, RED, ALPHA)
            }

            run {
                val texture = Texture(Target._2D, Format.BGRA8_UNORM_PACK8, Vec3i(1), 1, 1, 1, Swizzles(BLUE, GREEN, RED, ALPHA))
                texture.swizzles shouldBe Swizzles(RED, GREEN, BLUE, ALPHA)
            }
        }

        "texture 1d" {

            val textureA = Texture(Target._1D, Format.RGBA8_UNORM_PACK8, Vec3i(4, 1, 1), 1, 1, 1)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            run {

                val textureB = Texture(Target._1D, Format.RGBA8_UNORM_PACK8, Vec3i(4, 1, 1), 1, 1, 1)
                textureB.clear(Vec4b(0, 127, 255, 192))
                textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureB

                val textureC = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1)
                textureC.clear(Vec4b(255, 127, 0, 192))

                textureA shouldBe textureC

                val textureD = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1)
                textureD.clear(Vec4b(0, 127, 255, 192))
                textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureD
            }
        }

        "texture 1d array"{

            val textureA = Texture(Target._1D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(4, 1, 1), 2, 1, 4)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1, 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1, 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            val textureB = Texture(Target._1D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(4, 1, 1), 2, 1, 4)
            textureB.clear(Vec4b(0, 127, 255, 192))
            textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

            textureA shouldBe textureB

            val textureC = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 2, 4)
            textureC.clear(Vec4b(255, 127, 0, 192))

            textureA shouldBe textureC

            val textureD = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 2, 4)
            textureD.clear(Vec4b(0, 127, 255, 192))
            textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

            textureA shouldBe textureD
            textureC shouldBe textureD
        }

        "texture 2d" {

            val textureA = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 1, 1, 2)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            run {

                val textureB = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 1, 1, 2)
                textureB.clear(Vec4b(0, 127, 255, 192))
                textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureB

                val textureC = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureC.clear(Vec4b(255, 127, 0, 192))

                textureA shouldBe textureC

                val textureD = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureD.clear(Vec4b(0, 127, 255, 192))
                textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureD
                textureC shouldBe textureD
            }
        }

        "texture 2d array"{

            val textureA = Texture(Target._2D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 2, 1, 4)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1, 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(4), 1, 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            val textureB = Texture(Target._2D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 2, 1, 4)
            textureB.clear(Vec4b(0, 127, 255, 192))
            textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

            textureA shouldBe textureB

            val textureC = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2, 4)
            textureC.clear(Vec4b(255, 127, 0, 192))

            textureA shouldBe textureC

            val textureD = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2, 4)
            textureD.clear(Vec4b(0, 127, 255, 192))
            textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

            textureA shouldBe textureD
            textureC shouldBe textureD
        }

        "texture 3d" {

            val textureA = Texture(Target._3D, Format.RGBA8_UNORM_PACK8, Vec3i(4), 1, 1, 2)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            run {

                val textureB = Texture(Target._3D, Format.RGBA8_UNORM_PACK8, Vec3i(4), 1, 1, 2)
                textureB.clear(Vec4b(0, 127, 255, 192))
                textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureB

                val textureC = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 2)
                textureC.clear(Vec4b(255, 127, 0, 192))

                textureA shouldBe textureC

                val textureD = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(4), 2)
                textureD.clear(Vec4b(0, 127, 255, 192))
                textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureD
                textureC shouldBe textureD
            }
        }

        "texture cube" {

            val textureA = Texture(Target.CUBE, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 1, 6, 2)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = TextureCube(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = TextureCube(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            run {

                val textureB = Texture(Target.CUBE, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 1, 6, 2)
                textureB.clear(Vec4b(0, 127, 255, 192))
                textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureB

                val textureC = TextureCube(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureC.clear(Vec4b(255, 127, 0, 192))

                textureA shouldBe textureC

                val textureD = TextureCube(Format.RGBA8_UNORM_PACK8, Vec2i(4), 2)
                textureD.clear(Vec4b(0, 127, 255, 192))
                textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureD
                textureC shouldBe textureD
            }
        }

        "texture cube array" {

            val textureA = Texture(Target.CUBE_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 3, 6, 2)
            textureA.clear(Vec4b(255, 127, 0, 192))

            run {

                val textureA1 = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1)
                textureA.swizzles shouldBe textureA1.swizzles

                val textureA2 = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1, Swizzles(RED, GREEN, BLUE, ALPHA))
                textureA.swizzles shouldBe textureA2.swizzles

                textureA1.swizzles shouldBe textureA2.swizzles
            }

            run {

                val textureB = Texture(Target.CUBE_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(4, 4, 1), 3, 6, 2)
                textureB.clear(Vec4b(0, 127, 255, 192))
                textureB.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureB

                val textureC = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 3, 2)
                textureC.clear(Vec4b(255, 127, 0, 192))

                textureA shouldBe textureC

                val textureD = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(4), 3, 2)
                textureD.clear(Vec4b(0, 127, 255, 192))
                textureD.swizzles(Vec4b::class, Swizzles(BLUE, GREEN, RED, ALPHA))

                textureA shouldBe textureD
                textureC shouldBe textureD
            }
        }
    }
}