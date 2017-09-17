package gli

import glm_.b
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec3.Vec3b
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
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
                    textureA[level].clear(colors[level])

                val textureB = Texture1d(textureA)

                assert(textureB == textureA)

                val textureC = gli.duplicate(textureA)
                assert(textureC == textureA)

                val textureD = gli.duplicate(textureB)
                assert(textureD == textureB)

                val textureE = gli.duplicate(textureC, 1, 2)
                val textureF = Texture1d(textureC, 1, 2)
                assert(textureE == textureF)

                val textureG = gli.duplicate(textureD, 1, 2)
                val textureH = Texture1d(textureD, 1, 2)
                assert(textureG == textureH)

                colors.last().let {
                    when (it) {
                        is Byte -> {
                            textureG.clear(it)
                            textureH.clear(it)
                        }
                        is Long -> {
                            textureG.clear(it)
                            textureH.clear(it)
                        }
                        else -> throw Error()
                    }
                    assert(textureG == textureH)
                }
                textureG.clear()
                textureH.clear()
                assert(textureG == textureH)
            }

            fun testTexture(size: Vec2i, format: Format, colors: List<Any>) {

                val textureA = Texture2d(format, size)

                for (level in 0 until textureA.levels())
                    textureA[level].clear(colors[level])

                val textureB = Texture2d(textureA)

                assert(textureB == textureA)

                val textureC = gli.duplicate(textureA)
                assert(textureC == textureA)

                val textureD = gli.duplicate(textureB)
                assert(textureD == textureB)

                val textureE = gli.duplicate(textureC, 1, 2)
                val textureF = Texture2d(textureC, 1, 2)
                assert(textureE == textureF)

                val textureG = gli.duplicate(textureD, 1, 2)
                val textureH = Texture2d(textureD, 1, 2)
                assert(textureG == textureH)

                colors.last().let {
                    when (it) {
                        is Byte -> {
                            textureG.clear(it)
                            textureH.clear(it)
                        }
                        is Long -> {
                            textureG.clear(it)
                            textureH.clear(it)
                        }
                        else -> throw Error()
                    }
                    assert(textureG == textureH)
                }
                textureG.clear()
                textureH.clear()
                assert(textureG == textureH)
            }

            fun testTexture(size: Vec3i, format: Format, colors: List<Any>) {

                val textureA = Texture3d(format, size)

                for (level in 0 until textureA.levels())
                    textureA[level].clear(colors[level])

                val textureB = Texture3d(textureA)

                assert(textureB == textureA)

                val textureC = gli.duplicate(textureA)
                assert(textureC == textureA)

                val textureD = gli.duplicate(textureB)
                assert(textureD == textureB)

                val textureE = gli.duplicate(textureC, 1, 2)
                val textureF = Texture3d(textureC, 1, 2)
                assert(textureE == textureF)

                val textureG = gli.duplicate(textureD, 1, 2)
                val textureH = Texture3d(textureD, 1, 2)
                assert(textureG == textureH)

                colors.last().let {
                    when (it) {
                        is Long -> {
                            textureG.clear(it)
                            textureH.clear(it)
                        }
                        else -> throw Error()
                    }
                    assert(textureG == textureH)
                }
                textureG.clear()
                textureH.clear()
                assert(textureG == textureH)
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
//                testTexture<gli::texture1d>(gli::texture1d::extent_type(Sizes[i]), gli::FORMAT_R8_UNORM_PACK8, ColorR8_UNORM);
//                Error += test_texture<gli::texture2d>(gli::texture2d::extent_type(Sizes[i]), gli::FORMAT_R8_UNORM_PACK8, ColorR8_UNORM);
//                Error += test_texture<gli::texture3d>(gli::texture3d::extent_type(Sizes[i]), gli::FORMAT_R8_UNORM_PACK8, ColorR8_UNORM);
//                Error += test_texture<gli::texture1d>(gli::texture1d::extent_type(Sizes[i]), gli::FORMAT_RGB8_UNORM_PACK8, ColorRGB8_UNORM);
//                Error += test_texture<gli::texture2d>(gli::texture2d::extent_type(Sizes[i]), gli::FORMAT_RGB8_UNORM_PACK8, ColorRGB8_UNORM);
//                Error += test_texture<gli::texture3d>(gli::texture3d::extent_type(Sizes[i]), gli::FORMAT_RGB8_UNORM_PACK8, ColorRGB8_UNORM);
//                Error += test_texture<gli::texture1d>(gli::texture1d::extent_type(Sizes[i]), gli::FORMAT_RGBA8_UNORM_PACK8, ColorRGBA8_UNORM);
//                Error += test_texture<gli::texture2d>(gli::texture2d::extent_type(Sizes[i]), gli::FORMAT_RGBA8_UNORM_PACK8, ColorRGBA8_UNORM);
//                Error += test_texture<gli::texture3d>(gli::texture3d::extent_type(Sizes[i]), gli::FORMAT_RGBA8_UNORM_PACK8, ColorRGBA8_UNORM);
//                Error += test_texture<gli::texture1d>(gli::texture1d::extent_type(Sizes[i]), gli::FORMAT_RGBA32_SFLOAT_PACK32, ColorRGBA32F);
//                Error += test_texture<gli::texture2d>(gli::texture2d::extent_type(Sizes[i]), gli::FORMAT_RGBA32_SFLOAT_PACK32, ColorRGBA32F);
//                Error += test_texture<gli::texture3d>(gli::texture3d::extent_type(Sizes[i]), gli::FORMAT_RGBA32_SFLOAT_PACK32, ColorRGBA32F)
            }
        }
    }
}
