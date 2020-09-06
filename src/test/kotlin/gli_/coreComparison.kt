package gli_

import glm_.glm
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import glm_.vec4.Vec4b
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.core.spec.style.StringSpec

class coreComparison : StringSpec() {

    init {

        "texture1d" {

            val textureA = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(32), glm.levels(32))

            run {
                val textureB = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(32), glm.levels(32))

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }
            run {
                val textureC = Texture1d(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
            run {
                val textureD = Texture1d(textureA, textureA.baseLevel, textureA.maxLevel)

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }
            run {
                val textureE = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(32))

                textureE[textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }
            run {
                val textureB = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(32), 1)

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }
            run {
                val textureB = Texture1d(Format.RGBA8_SNORM_PACK8, Vec1i(32))

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }
            run {
                val textureB = Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(64))

                val textureC = Texture1d(textureB, textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }

        "texture 1d array" {

            val textureA = Texture1dArray(
                    format = Format.RGBA8_UNORM_PACK8,
                    extent = Vec1i(32),
                    layers = 1)

            run {
                val textureB = Texture1dArray(
                        format = Format.RGBA8_UNORM_PACK8,
                        extent = Vec1i(32),
                        layers = 1)

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }

            run {
                val textureC = Texture1dArray(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }

            run {
                val textureD = Texture1dArray(
                        texture = textureA,
                        baseLayer = textureA.baseLayer,
                        maxLayer = textureA.maxLayer,
                        baseLevel = textureA.baseLevel,
                        maxLevel = textureA.maxLevel)

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }

            run {
                val textureE = Texture1dArray(
                        format = Format.RGBA8_UNORM_PACK8,
                        extent = Vec1i(32),
                        layers = 1)

                textureE[0][textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }

            run {
                val textureB = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(32), 1, 1)

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture1dArray(Format.RGBA8_SNORM_PACK8, Vec1i(32), 1)

                textureB[0][textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture1dArray(Format.RGBA8_UNORM_PACK8, Vec1i(64), 1)

                val textureC = Texture1dArray(textureB,
                        textureB.baseLayer, textureB.maxLayer,
                        textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }

        "texture 2d" {

            val textureA = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(32))

            run {
                val textureB = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(32))

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }

            run {
                val textureC = Texture2d(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }

            run {
                val textureD = Texture2d(gli.view(
                        textureA,
                        textureA.baseLevel, textureA.maxLevel))

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }

            run {
                val textureE = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(32))

                textureE[textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }

            run {
                val textureB = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture2d(Format.RGBA8_SNORM_PACK8, Vec2i(32))

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(64))

                val textureC = gli.view(
                        textureB,
                        textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }

        "texture 2d array" {

            val textureA = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

            run {
                val textureB = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }

            run {
                val textureC = Texture2dArray(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }

            run {
                val textureD = Texture2dArray(textureA,
                        textureA.baseLayer, textureA.maxLayer,
                        textureA.baseLevel, textureA.maxLevel)

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }

            run {
                val textureE = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

                textureE[0][textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }

            run {
                val textureB = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1, 1)

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture2dArray(Format.RGBA8_SNORM_PACK8, Vec2i(32), 1)

                textureB[0][textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(64), 1)

                val textureC = Texture2dArray(textureB,
                        textureB.baseLayer, textureB.maxLayer,
                        textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }

        "texture 3d" {

            val textureA = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(32))

            run {
                val textureB = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(32))

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }

            run {
                val textureC = Texture3d(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }

            run {
                val textureD = Texture3d(textureA, textureA.baseLevel, textureA.maxLevel)

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }

            run {
                val textureE = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(32))

                textureE[textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }

            run {
                val textureB = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(32), 1)

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture3d(Format.RGBA8_SNORM_PACK8, Vec3i(32))

                textureB[textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = Texture3d(Format.RGBA8_UNORM_PACK8, Vec3i(64))

                val textureC = Texture3d(textureB, textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }

        "texture cube" {

            val size = Vec2i(16)
            val textureA = TextureCube(Format.RGBA8_UNORM_PACK8, size)

            run {
                val textureB = TextureCube(Format.RGBA8_UNORM_PACK8, size)

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }

            run {
                val textureC = TextureCube(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }

            run {
                val textureD = TextureCube(textureA,
                        textureA.baseFace, textureA.maxFace,
                        textureA.baseLevel, textureA.maxLevel)

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }

            run {
                val textureE = TextureCube(Format.RGBA8_UNORM_PACK8, size)

                textureE[textureE.faces() - 1][textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }

            run {
                val textureB = TextureCube(Format.RGBA8_UNORM_PACK8, size, 1)

                textureB[textureB.faces() - 1][textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = TextureCube(Format.RGBA8_SNORM_PACK8, size)

                textureB[textureB.faces() - 1][textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = TextureCube(Format.RGBA8_UNORM_PACK8, size shl 1)

                val textureC = TextureCube(textureB,
                        textureB.baseFace, textureB.maxFace,
                        textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }

        "texture cube array" {

            val textureA = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

            run {
                val textureB = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

                textureA shouldBe textureB
                (textureA != textureB) shouldBe false
            }

            run {
                val textureC = TextureCubeArray(textureA)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }

            run {
                val textureD = TextureCubeArray(textureA,
                        textureA.baseLayer, textureA.maxLayer,
                        textureA.baseFace, textureA.maxFace,
                        textureA.baseLevel, textureA.maxLevel)

                textureA shouldBe textureD
                (textureA != textureD) shouldBe false
            }

            run {
                val textureE = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1)

                textureE[0][textureE.faces() - 1][textureE.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureE
                (textureA == textureE) shouldBe false
            }

            run {
                val textureB = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(32), 1, 1)

                textureB[0][textureB.faces() - 1][textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = TextureCubeArray(Format.RGBA8_SNORM_PACK8, Vec2i(32), 1)

                textureB[0][textureB.faces() - 1][textureB.levels() - 1].data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                textureA shouldNotBe textureB
                (textureA == textureB) shouldBe false
            }

            run {
                val textureB = TextureCubeArray(Format.RGBA8_UNORM_PACK8, Vec2i(64), 1)

                val textureC = TextureCubeArray(textureB,
                        textureB.baseLayer, textureB.maxLayer,
                        textureB.baseFace, textureB.maxFace,
                        textureB.baseLevel + 1, textureB.maxLevel)

                textureA shouldBe textureC
                (textureA != textureC) shouldBe false
            }
        }
    }
}