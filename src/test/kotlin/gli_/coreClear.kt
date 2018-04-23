package gli_

import glm_.b
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3b
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 21.04.2017.
 */

class coreClear : StringSpec() {

    init {

        "clear" {

            fun testTexture(size: Vec1i, format: Format, colors: List<Any>) {

                val textureA = Texture1d(format, size)

                for (level in 0 until textureA.levels())
                    textureA[level] clear colors[level]

                val textureB = Texture1d(textureA)

                textureB shouldBe textureA

                val textureC = Texture1d(gli.duplicate(textureA))
                textureC shouldBe textureA

                val textureD = Texture1d(gli.duplicate(textureB))
                textureD shouldBe textureB

                val textureE = gli.duplicate(textureC, 1, 2)
                val textureF = Texture1d(textureC, 1, 2)
                textureE shouldBe textureF

                val textureG = gli.duplicate(textureD, 1, 2)
                val textureH = Texture1d(textureD, 1, 2)
                textureG shouldBe textureH

                textureG clear colors.last()
                textureH clear colors.last()
                textureG shouldBe textureH

                textureG.clear()
                textureH.clear()
                textureG shouldBe textureH
            }

            fun testTexture(size: Vec2i, format: Format, colors: List<Any>) {

                val textureA = Texture2d(format, size)

                for (level in 0 until textureA.levels())
                    textureA[level] clear colors[level]

                val textureB = Texture2d(textureA)

                textureB shouldBe textureA

                val textureC = Texture2d(gli.duplicate(textureA))
                textureC shouldBe textureA

                val textureD = Texture2d(gli.duplicate(textureB))
                textureD shouldBe textureB

                val textureE = gli.duplicate(textureC, 1, 2)
                val textureF = Texture2d(textureC, 1, 2)
                textureE shouldBe textureF

                val textureG = gli.duplicate(textureD, 1, 2)
                val textureH = Texture2d(textureD, 1, 2)
                textureG shouldBe textureH

                textureG clear colors.last()
                textureH clear colors.last()
                textureG shouldBe textureH

                textureG.clear()
                textureH.clear()
                textureG shouldBe textureH
            }

            fun testTexture(size: Vec3i, format: Format, colors: List<Any>) {

                val textureA = Texture3d(format, size)

                for (level in 0 until textureA.levels())
                    textureA[level] clear colors[level]

                val textureB = Texture3d(textureA)

                textureB shouldBe textureA

                val textureC = Texture3d(gli.duplicate(textureA))
                textureC shouldBe textureA

                val textureD = Texture3d(gli.duplicate(textureB))
                textureD shouldBe textureB

                val textureE = gli.duplicate(textureC, 1, 2)
                val textureF = Texture3d(textureC, 1, 2)
                textureE shouldBe textureF

                val textureG = gli.duplicate(textureD, 1, 2)
                val textureH = Texture3d(textureD, 1, 2)
                textureG shouldBe textureH

                textureG clear colors.last()
                textureH clear colors.last()
                textureG shouldBe textureH

                textureG.clear()
                textureH.clear()
                textureG shouldBe textureH
            }

            val colorDXT1 = listOf(255L, 127L, 64L, 32L, 16L, 8L, 4L)
            val colorR8_UNORM = listOf(255.b, 127.b, 64.b, 32.b, 16.b, 8.b, 4.b)
            val colorRGB8_UNORM = listOf(
                    Vec3b(255, 0, 0),
                    Vec3b(255, 127, 0),
                    Vec3b(255, 255, 0),
                    Vec3b(0, 255, 0),
                    Vec3b(0, 255, 255),
                    Vec3b(0, 0, 255),
                    Vec3b(255, 0, 255))
            val colorRGBA8_UNORM = listOf(
                    Vec4b(255, 0, 0, 255),
                    Vec4b(255, 127, 0, 255),
                    Vec4b(255, 255, 0, 255),
                    Vec4b(0, 255, 0, 255),
                    Vec4b(0, 255, 255, 255),
                    Vec4b(0, 0, 255, 255),
                    Vec4b(255, 0, 255, 255))
            val colorRGBA32F = listOf(
                    Vec4(1.0, 0, 0, 1.0),
                    Vec4(1.0, 0.5, 0, 1.0),
                    Vec4(1.0, 1.0, 0, 1.0),
                    Vec4(0, 1.0, 0, 1.0),
                    Vec4(0, 1.0, 1.0, 1.0),
                    Vec4(0, 0, 1.0, 1.0),
                    Vec4(1.0, 0, 1.0, 1.0))

            val sizes = listOf(12, 32, 16, 17, 15, 5)

            for (i in sizes) {
                testTexture(Vec2i(i), Format.RGB_DXT1_UNORM_BLOCK8, colorDXT1)
                testTexture(Vec3i(i), Format.RGB_DXT1_UNORM_BLOCK8, colorDXT1)
                testTexture(Vec1i(i), Format.R8_UNORM_PACK8, colorR8_UNORM)
                testTexture(Vec2i(i), Format.R8_UNORM_PACK8, colorR8_UNORM)
                testTexture(Vec3i(i), Format.R8_UNORM_PACK8, colorR8_UNORM)
                testTexture(Vec1i(i), Format.RGB8_UNORM_PACK8, colorRGB8_UNORM)
                testTexture(Vec2i(i), Format.RGB8_UNORM_PACK8, colorRGB8_UNORM)
                testTexture(Vec3i(i), Format.RGB8_UNORM_PACK8, colorRGB8_UNORM)
                testTexture(Vec1i(i), Format.RGBA8_UNORM_PACK8, colorRGBA8_UNORM)
                testTexture(Vec2i(i), Format.RGBA8_UNORM_PACK8, colorRGBA8_UNORM)
                testTexture(Vec2i(i), Format.RGBA8_UNORM_PACK8, colorRGBA8_UNORM)
                testTexture(Vec1i(i), Format.RGBA32_SFLOAT_PACK32, colorRGBA32F)
                testTexture(Vec2i(i), Format.RGBA32_SFLOAT_PACK32, colorRGBA32F)
                testTexture(Vec3i(i), Format.RGBA32_SFLOAT_PACK32, colorRGBA32F)
            }
        }

        "can clear level" {

            run {
                val textureMember = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1)
                textureMember clear Vec4b(255, 127, 0, 255)

                val textureExternal = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4), 1)
                gli.clear(textureExternal, Vec4b(255, 127, 0, 255))

                textureMember shouldBe textureExternal
            }
            run {
                val textureMember = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4))
                textureMember clear Vec4b(127, 95, 63, 255)
                textureMember.clear(0, 0, 1, Vec4b(255, 127, 0, 255))

                textureMember.data<Vec4b>(0, 0, 0)[0] shouldBe Vec4b(127, 95, 63, 255)
                textureMember.data<Vec4b>(0, 0, 1)[0] shouldBe Vec4b(255, 127, 0, 255)
                textureMember.data<Vec4b>(0, 0, 2)[0] shouldBe Vec4b(127, 95, 63, 255)

                val textureExternal = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4))
                gli.clear(textureExternal, Vec4b(127, 95, 63, 255))
                gli.clear(textureExternal, 0, 0, 1, Vec4b(255, 127, 0, 255))

                textureExternal.data<Vec4b>(0, 0, 0)[0] shouldBe Vec4b(127, 95, 63, 255)
                textureExternal.data<Vec4b>(0, 0, 1)[0] shouldBe Vec4b(255, 127, 0, 255)
                textureExternal.data<Vec4b>(0, 0, 2)[0] shouldBe Vec4b(127, 95, 63, 255)

                textureMember shouldBe textureExternal
            }
        }

        "can clear layer" {

            val textureMember = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(2), 2, 1)
            textureMember.clear(0, 0, 0, Vec4b(255, 127, 0, 255))
            textureMember.clear(1, 0, 0, Vec4b(255, 127, 0, 255))

            val textureExternalA = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(2), 2, 1)
            gli.clearLayer(textureExternalA, 0, textureExternalA.layers(), Vec4b(255, 127, 0, 255))

            textureMember shouldBe textureExternalA

            val textureExternalB = Texture2dArray(Format.RGBA8_UNORM_PACK8, Vec2i(2), 2, 1)
            gli.clearLayer(textureExternalB, 0, Vec4b(255, 127, 0, 255))
            gli.clearLayer(textureExternalB, 1, Vec4b(255, 127, 0, 255))

            textureMember shouldBe textureExternalB
        }
    }
}
