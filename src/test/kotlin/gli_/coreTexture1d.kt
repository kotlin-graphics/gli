package gli_

import glm_.vec1.Vec1
import glm_.vec1.Vec1b
import glm_.vec1.Vec1i
import glm_.vec2.Vec2
import glm_.vec2.Vec2b
import glm_.vec2.Vec2ub
import glm_.vec3.Vec3
import glm_.vec3.Vec3b
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 04.04.2017.
 */

class coreTexture1d : StringSpec() {

    inline fun <reified T> run(format: Format, testSamples: Array<T>) {

        val dimensions = Vec1i(16)
        val texelCoord = Array(8, { Vec1i(it) })

        val textureA = Texture1d(format, dimensions)
        textureA.clear()
        testSamples.forEachIndexed { i, test ->
            textureA.data<T>(0, 0, 1)[i] = test
        }

        val textureB = Texture1d(format, dimensions)
        textureB.clear()
        testSamples.forEachIndexed { i, test ->
            textureB.store(texelCoord[i], 1, test)
        }

        val loadedSamplesA = Array(8, { textureA.load<T>(texelCoord[it], 1) })

        val loadedSamplesB = Array(8, { textureB.load<T>(texelCoord[it], 1) })

        for (i in 0..7)
            loadedSamplesA[i] shouldBe testSamples[i]

        for (i in 0..7)
            loadedSamplesB[i] shouldBe testSamples[i]

        textureA shouldBe textureB

        val textureC = Texture1d(textureA, 1, 1)
        val textureD = Texture1d(textureB, 1, 1)

        textureC shouldBe textureD
    }

    init {

        "alloc" {

            val sizes = intArrayOf(16, 32, 15, 17, 1)

            for (format in FORMAT_FIRST..FORMAT_LAST)
                sizes.forEach {

                    val size = Vec3i(it)

                    val textureA = Texture1d(format, size, gli.levels(size))
                    val textureB = Texture1d(format, size, gli.levels(size))

                    textureA shouldBe textureB
                }
        }

        "query" {

            val texture = Texture1d(Format.RGBA8_UINT_PACK8, Vec1i(2), 2)

            texture.size() shouldBe Vec4b.size * 3
            texture.format shouldBe Format.RGBA8_UINT_PACK8
            texture.levels() shouldBe 2
            texture.notEmpty() shouldBe true
            texture.extent() shouldBe Vec3i(2, 1, 1)
        }

        "tex access" {

            run {
                val texture = Texture1d(Format.RGBA8_UINT_PACK8, Vec1i(2), 2)
                texture.notEmpty() shouldBe true

                val image0 = texture[0]
                val image1 = texture[1]

                val size0 = image0.size()
                val size1 = image1.size()

                size0 shouldBe Vec4b.size * 2
                size1 shouldBe Vec4b.size * 1

                image0.data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)
                image1.data<Vec4b>()[0] = Vec4b(0, 127, 255, 255)

                val pointerA = image0.data<Vec4b>()[0]
                val pointerB = image1.data<Vec4b>()[0]

                val pointer0 = texture.data<Vec4b>()[0]
                val pointer1 = texture.data<Vec4b>()[2]

                pointerA shouldBe pointer0
                pointerB shouldBe pointer1

                val colorA = image0.data<Vec4b>()[0]
                val colorB = image1.data<Vec4b>()[0]

                val color0 = pointer0
                val color1 = pointer1

                colorA shouldBe color0
                colorB shouldBe color1

                color0 shouldBe Vec4b(255, 127, 0, 255)
                color1 shouldBe Vec4b(0, 127, 255, 255)
            }

            run {
                val texture = Texture1d(Format.RGBA8_UINT_PACK8, Vec1i(2), 1)

                val sizeA = texture.size()
                sizeA shouldBe Vec4b.size * 2

                val image0 = texture[0]

                val size0 = image0.size()
                size0 shouldBe Vec4b.size * 2

                image0.data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                val pointerA = image0.data<Vec4b>()[0]
                val pointer0 = texture.data<Vec4b>()[0]
                pointerA shouldBe pointer0

                val colorA = pointerA
                val color0 = pointer0

                color0 shouldBe colorA
                color0 shouldBe Vec4b(255, 127, 0, 255)
            }
        }

        "size" {

            class Test(val format: Format, val dimensions: Vec1i, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, Vec1i(4), 16),
                    Test(Format.R8_UINT_PACK8, Vec1i(4), 4))

            tests.forEach {
                val texture = Texture1d(it.format, Vec1i(4), 1)
                val image = texture[0]

                image.size() shouldBe it.size
                texture.size() shouldBe it.size
            }
        }

        "load store" {

            run(Format.R32_SFLOAT_PACK32, arrayOf(
                    Vec1(+0.0f),
                    Vec1(+1.0f),
                    Vec1(-1.0f),
                    Vec1(+0.5f),
                    Vec1(-0.5f),
                    Vec1(+0.2f),
                    Vec1(-0.2f),
                    Vec1(+0.9f)))

            run(Format.RG32_SFLOAT_PACK32, arrayOf(
                    Vec2(-1.0f, -1.0f),
                    Vec2(-0.5f, -0.5f),
                    Vec2(+0.0f, +0.0f),
                    Vec2(+0.5f, +0.5f),
                    Vec2(+1.0f, +1.0f),
                    Vec2(-1.0f, +1.0f),
                    Vec2(-0.5f, +0.5f),
                    Vec2(+0.0f, +0.0f)))

            run(Format.RGB32_SFLOAT_PACK32, arrayOf(
                    Vec3(-1.0f, +0.0f, +1.0f),
                    Vec3(-0.5f, +0.0f, +0.5f),
                    Vec3(-0.2f, +0.0f, +0.2f),
                    Vec3(-0.0f, +0.0f, +0.0f),
                    Vec3(+0.1f, +0.2f, +0.3f),
                    Vec3(-0.1f, -0.2f, -0.3f),
                    Vec3(+0.7f, +0.8f, +0.9f),
                    Vec3(-0.7f, -0.8f, -0.9f)))

            run(Format.RGBA32_SFLOAT_PACK32, arrayOf(
                    Vec4(-1.0f, +0.0f, +1.0f, 1.0f),
                    Vec4(-0.5f, +0.0f, +0.5f, 1.0f),
                    Vec4(-0.2f, +0.0f, +0.2f, 1.0f),
                    Vec4(-0.0f, +0.0f, +0.0f, 1.0f),
                    Vec4(+0.1f, +0.2f, +0.3f, 1.0f),
                    Vec4(-0.1f, -0.2f, -0.3f, 1.0f),
                    Vec4(+0.7f, +0.8f, +0.9f, 1.0f),
                    Vec4(-0.7f, -0.8f, -0.9f, 1.0f)))

            arrayOf(
                    Vec1b(-128),
                    Vec1b(-127),
                    Vec1b(+127),
                    Vec1b(+64),
                    Vec1b(-64),
                    Vec1b(+1),
                    Vec1b(-1),
                    Vec1b(+0)).let {

                run(Format.R8_SINT_PACK8, it)
                run(Format.R8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec2b(-128, -96),
                    Vec2b(-64, 96),
                    Vec2b(-128, 64),
                    Vec2b(127, 32),
                    Vec2b(0, 126),
                    Vec2b(-48, 48),
                    Vec2b(-127, 127),
                    Vec2b(64, 0)).let {

                run(Format.RG8_UINT_PACK8, it)
                run(Format.RG8_UNORM_PACK8, it)
            }

            arrayOf(
                    Vec3b(-128, 0, 0),
                    Vec3b(-128, 127, 0),
                    Vec3b(-128, -96, 0),
                    Vec3b(127, -128, 0),
                    Vec3b(0, 127, 0),
                    Vec3b(0, 127, -127),
                    Vec3b(0, 64, -64),
                    Vec3b(-32, 32, 96)).let {

                run(Format.RGB8_SINT_PACK8, it)
                run(Format.RGB8_SNORM_PACK8, it)
            }

            arrayOf(
                    Vec4b(-127, 0, 0, 127),
                    Vec4b(-128, 96, 0, -128),
                    Vec4b(127, 64, 0, 1),
                    Vec4b(0, -64, 0, 2),
                    Vec4b(-95, 32, 0, 3),
                    Vec4b(95, -32, 127, 4),
                    Vec4b(-63, 16, -128, -1),
                    Vec4b(63, -16, -127, -2)).let {

                run(Format.RGBA8_SINT_PACK8, it)
                run(Format.RGBA8_SNORM_PACK8, it)
            }

//            arrayOf()
//                    glm::u8vec1(255),
//                    glm::u8vec1(224),
//                    glm::u8vec1(192),
//                    glm::u8vec1(128),
//                    glm::u8vec1(64),
//                    glm::u8vec1(32),
//                    glm::u8vec1(16),
//                    glm::u8vec1(0)
//                }};
//
//                Error += run(gli::FORMAT_R8_UINT_PACK8, TestSamples);
//                Error += run(gli::FORMAT_R8_UNORM_PACK8, TestSamples);
//                Error += run(gli::FORMAT_R8_SRGB_PACK8, TestSamples);
//            }

            arrayOf(
                    Vec2ub(255,   0),
                    Vec2ub(255, 128),
                    Vec2ub(255, 255),
                    Vec2ub(128, 255),
                    Vec2ub(0, 255),
                    Vec2ub(0, 255),
                    Vec2ub(0,   0),
                    Vec2ub(255,   0)).let {

                run(Format.RG8_UINT_PACK8, it)
                run(Format.RG8_UNORM_PACK8, it)
                run(Format.RG8_SRGB_PACK8, it)
            }

//            std::array<glm::u8vec3, 8> TestSamples{
//            {
//                glm::u8vec3(255,   0,   0),
//                glm::u8vec3(255, 128,   0),
//                glm::u8vec3(255, 255,   0),
//                glm::u8vec3(128, 255,   0),
//                glm::u8vec3(0, 255,   0),
//                glm::u8vec3(0, 255, 255),
//                glm::u8vec3(0,   0, 255),
//                glm::u8vec3(255,   0, 255)
//            }};
//
//            Error += run(gli::FORMAT_RGB8_UINT_PACK8, TestSamples);
//            Error += run(gli::FORMAT_RGB8_UNORM_PACK8, TestSamples);
//            Error += run(gli::FORMAT_RGB8_SRGB_PACK8, TestSamples);
        }
    }
}