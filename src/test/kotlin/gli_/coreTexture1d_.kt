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

    inline fun <reified T : Any> run(format: Format, testSamples: Array<T>) {

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
                       //
                       //
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
//                return Error
        }
    }
}