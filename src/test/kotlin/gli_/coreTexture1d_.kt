package gli_

import glm_.vec1.Vec1
import glm_.vec1.Vec1b
import glm_.vec1.Vec1i
import glm_.vec2.Vec2
import glm_.vec2.Vec2b
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

class coreTexture1d_ : StringSpec() {

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

        "load store" {

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
/*
                       //                        {
                       //                            std::array<glm::u8vec1, 8> TestSamples{
                       //                            {
                       //                                glm::u8vec1(255),
                       //                                glm::u8vec1(224),
                       //                                glm::u8vec1(192),
                       //                                glm::u8vec1(128),
                       //                                glm::u8vec1(64),
                       //                                glm::u8vec1(32),
                       //                                glm::u8vec1(16),
                       //                                glm::u8vec1(0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_R8_UINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_R8_UNORM_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_R8_SRGB_PACK8, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u8vec2, 8> TestSamples{
                       //                            {
                       //                                glm::u8vec2(255,   0),
                       //                                glm::u8vec2(255, 128),
                       //                                glm::u8vec2(255, 255),
                       //                                glm::u8vec2(128, 255),
                       //                                glm::u8vec2(0, 255),
                       //                                glm::u8vec2(0, 255),
                       //                                glm::u8vec2(0,   0),
                       //                                glm::u8vec2(255,   0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RG8_UINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RG8_UNORM_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RG8_SRGB_PACK8, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u8vec3, 8> TestSamples{
                       //                            {
                       //                                glm::u8vec3(255,   0,   0),
                       //                                glm::u8vec3(255, 128,   0),
                       //                                glm::u8vec3(255, 255,   0),
                       //                                glm::u8vec3(128, 255,   0),
                       //                                glm::u8vec3(0, 255,   0),
                       //                                glm::u8vec3(0, 255, 255),
                       //                                glm::u8vec3(0,   0, 255),
                       //                                glm::u8vec3(255,   0, 255)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGB8_UINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RGB8_UNORM_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RGB8_SRGB_PACK8, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u8vec4, 8> TestSamples{
                       //                            {
                       //                                glm::u8vec4(255,   0,   0, 255),
                       //                                glm::u8vec4(255, 128,   0, 255),
                       //                                glm::u8vec4(255, 255,   0, 255),
                       //                                glm::u8vec4(128, 255,   0, 255),
                       //                                glm::u8vec4(0, 255,   0, 255),
                       //                                glm::u8vec4(0, 255, 255, 255),
                       //                                glm::u8vec4(0,   0, 255, 255),
                       //                                glm::u8vec4(255,   0, 255, 255)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGBA8_UINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RGBA8_UNORM_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RGBA8_SRGB_PACK8, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u16vec1, 8> TestSamples{
                       //                            {
                       //                                glm::u16vec1(65535),
                       //                                glm::u16vec1(32767),
                       //                                glm::u16vec1(192),
                       //                                glm::u16vec1(128),
                       //                                glm::u16vec1(64),
                       //                                glm::u16vec1(32),
                       //                                glm::u16vec1(16),
                       //                                glm::u16vec1(0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_R16_UINT_PACK16, TestSamples);
                       //                            Error += run(gli::FORMAT_R16_UNORM_PACK16, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u16vec2, 8> TestSamples{
                       //                            {
                       //                                glm::u16vec2(255,   0),
                       //                                glm::u16vec2(255, 128),
                       //                                glm::u16vec2(255, 255),
                       //                                glm::u16vec2(128, 255),
                       //                                glm::u16vec2(0, 255),
                       //                                glm::u16vec2(0, 255),
                       //                                glm::u16vec2(0,   0),
                       //                                glm::u16vec2(255,   0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RG16_UINT_PACK16, TestSamples);
                       //                            Error += run(gli::FORMAT_RG16_UNORM_PACK16, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u16vec3, 8> TestSamples{
                       //                            {
                       //                                glm::u16vec3(255,   0,   0),
                       //                                glm::u16vec3(255, 128,   0),
                       //                                glm::u16vec3(255, 255,   0),
                       //                                glm::u16vec3(128, 255,   0),
                       //                                glm::u16vec3(0, 255,   0),
                       //                                glm::u16vec3(0, 255, 255),
                       //                                glm::u16vec3(0,   0, 255),
                       //                                glm::u16vec3(255,   0, 255)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGB16_UINT_PACK16, TestSamples);
                       //                            Error += run(gli::FORMAT_RGB16_UNORM_PACK16, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u16vec4, 8> TestSamples{
                       //                            {
                       //                                glm::u16vec4(255,   0,   0, 255),
                       //                                glm::u16vec4(255, 128,   0, 255),
                       //                                glm::u16vec4(255, 255,   0, 255),
                       //                                glm::u16vec4(128, 255,   0, 255),
                       //                                glm::u16vec4(0, 255,   0, 255),
                       //                                glm::u16vec4(0, 255, 255, 255),
                       //                                glm::u16vec4(0,   0, 255, 255),
                       //                                glm::u16vec4(255,   0, 255, 255)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGBA16_UINT_PACK16, TestSamples);
                       //                            Error += run(gli::FORMAT_RGBA16_UNORM_PACK16, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u32vec1, 8> TestSamples{
                       //                            {
                       //                                glm::u32vec1(65535),
                       //                                glm::u32vec1(32767),
                       //                                glm::u32vec1(192),
                       //                                glm::u32vec1(128),
                       //                                glm::u32vec1(64),
                       //                                glm::u32vec1(32),
                       //                                glm::u32vec1(16),
                       //                                glm::u32vec1(0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_R32_UINT_PACK32, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u32vec2, 8> TestSamples{
                       //                            {
                       //                                glm::u32vec2(255,   0),
                       //                                glm::u32vec2(255, 128),
                       //                                glm::u32vec2(255, 255),
                       //                                glm::u32vec2(128, 255),
                       //                                glm::u32vec2(0, 255),
                       //                                glm::u32vec2(0, 255),
                       //                                glm::u32vec2(0,   0),
                       //                                glm::u32vec2(255,   0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RG32_UINT_PACK32, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u32vec3, 8> TestSamples{
                       //                            {
                       //                                glm::u32vec3(255,   0,   0),
                       //                                glm::u32vec3(255, 128,   0),
                       //                                glm::u32vec3(255, 255,   0),
                       //                                glm::u32vec3(128, 255,   0),
                       //                                glm::u32vec3(0, 255,   0),
                       //                                glm::u32vec3(0, 255, 255),
                       //                                glm::u32vec3(0,   0, 255),
                       //                                glm::u32vec3(255,   0, 255)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGB32_UINT_PACK32, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::u32vec4, 8> TestSamples{
                       //                            {
                       //                                glm::u32vec4(255,   0,   0, 255),
                       //                                glm::u32vec4(255, 128,   0, 255),
                       //                                glm::u32vec4(255, 255,   0, 255),
                       //                                glm::u32vec4(128, 255,   0, 255),
                       //                                glm::u32vec4(0, 255,   0, 255),
                       //                                glm::u32vec4(0, 255, 255, 255),
                       //                                glm::u32vec4(0,   0, 255, 255),
                       //                                glm::u32vec4(255,   0, 255, 255)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGBA32_UINT_PACK32, TestSamples);
                       //                        }
                       */
//                return Error
        }
    }
}