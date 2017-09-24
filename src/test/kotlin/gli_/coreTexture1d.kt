package gli_

import glm_.b
import glm_.vec1.Vec1
import glm_.vec1.Vec1b
import glm_.vec1.Vec1i
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec
import org.lwjgl.system.MemoryUtil
import java.nio.file.Files
import kotlin.system.measureNanoTime

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

        "load store"                {

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

//            arrayOf(
//                    Vec1b(-128),
//                    Vec1b(-127),
//                    Vec1b(+127),
//                    Vec1b(+64),
//                    Vec1b(-64),
//                    Vec1b(+1),
//                    Vec1b(-1),
//                    Vec1b(+0)).let {
//
//                run(Format.R8_SINT_PACK8, it)
//                run(Format.R8_SNORM_PACK8, it)
//            }
/*
                       //                        {
                       //                            std::array<glm::i8vec2, 8> TestSamples{
                       //                            {
                       //                                glm::i8vec2(-128, -96),
                       //                                glm::i8vec2( -64,  96),
                       //                                glm::i8vec2(-128,  64),
                       //                                glm::i8vec2( 127,  32),
                       //                                glm::i8vec2(   0, 126),
                       //                                glm::i8vec2( -48,  48),
                       //                                glm::i8vec2(-127, 127),
                       //                                glm::i8vec2(  64,   0)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RG8_UINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RG8_UNORM_PACK8, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::i8vec3, 8> TestSamples{
                       //                            {
                       //                                glm::i8vec3(-128,   0,   0),
                       //                                glm::i8vec3(-128, 127,   0),
                       //                                glm::i8vec3(-128, -96,   0),
                       //                                glm::i8vec3(127,-128,   0),
                       //                                glm::i8vec3(0, 127,   0),
                       //                                glm::i8vec3(0, 127,-127),
                       //                                glm::i8vec3(0,  64, -64),
                       //                                glm::i8vec3(-32,  32,  96)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGB8_SINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RGB8_SNORM_PACK8, TestSamples);
                       //                        }
                       //
                       //                        {
                       //                            std::array<glm::i8vec4, 8> TestSamples{
                       //                            {
                       //                                glm::i8vec4(-127,   0,   0, 127),
                       //                                glm::i8vec4(-128,  96,   0,-128),
                       //                                glm::i8vec4(127,  64,   0,   1),
                       //                                glm::i8vec4(0, -64,   0,   2),
                       //                                glm::i8vec4(-95,  32,   0,   3),
                       //                                glm::i8vec4(95, -32, 127,   4),
                       //                                glm::i8vec4(-63,  16,-128,  -1),
                       //                                glm::i8vec4(63, -16,-127,  -2)
                       //                            }};
                       //
                       //                            Error += run(gli::FORMAT_RGBA8_SINT_PACK8, TestSamples);
                       //                            Error += run(gli::FORMAT_RGBA8_SNORM_PACK8, TestSamples);
                       //                        }
                       //
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

        "specialize" {

            val texture = Texture(Target._1D, Format.RGBA8_UNORM_PACK8, Vec1i(1), 1, 1, 1)
            val texture1d = Texture1d(texture)
            val texture1dArray = Texture1dArray(texture)
            val texture2d = Texture2d(texture)
            val texture2dArray = Texture2dArray(texture)
            val texture3d = Texture3d(texture)
            val textureCube = TextureCube(texture)
            val textureCubeArray = TextureCubeArray(texture)

            texture shouldBe texture1d
            texture shouldNotBe texture1dArray
            texture shouldNotBe texture2d
            texture shouldNotBe texture2dArray
            texture shouldNotBe texture3d
            texture shouldNotBe textureCube
            texture shouldNotBe textureCubeArray

            val texture1D_B = Texture(texture1d)
            val texture1DArray_B = Texture(texture1dArray)
            val texture2D_B = Texture(texture2d)
            val texture2DArray_B = Texture(texture2dArray)
            val texture3D_B = Texture(texture3d)
            val textureCube_B = Texture(textureCube)
            val textureCubeArray_B = Texture(textureCubeArray)

            texture shouldBe texture1D_B
            texture shouldNotBe texture1DArray_B
            texture shouldNotBe texture2D_B
            texture shouldNotBe texture2DArray_B
            texture shouldNotBe texture3D_B
            texture shouldNotBe textureCube_B
            texture shouldNotBe textureCubeArray_B

            texture1d shouldBe texture1D_B
            texture1dArray shouldBe texture1DArray_B
            texture2d shouldBe texture2D_B
            texture2dArray shouldBe texture2DArray_B
            texture3d shouldBe texture3D_B
            textureCube shouldBe textureCube_B
            textureCubeArray shouldBe textureCubeArray_B
        }

        "load" {

            // Texture 1D
            run {
                val texture = Texture(Target._1D, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
                texture.clear(Vec4b(225, 127, 0, 255))

                val ktx = "texture_1d.ktx"
                val dds = "texture_1d.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }

            // Texture 1D array
            run {
                val texture = Texture(Target._1D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(1), 2, 1, 1)
                texture.clear(Vec4b(225, 127, 0, 255))
                val ktx = "texture_1d_array.ktx"
                val dds = "texture_1d_array.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }

            // Texture 2D
            run {
                val texture = Texture(Target._2D, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
                texture.clear(Vec4b(225, 127, 0, 255))

                val ktx = "texture_2d.ktx"
                val dds = "texture_2d.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }

            // Texture 2D array
            run {
                val texture = Texture(Target._2D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(1), 2, 1, 1)
                texture.clear(Vec4b(225, 127, 0, 255))
                val ktx = "texture_2d_array.ktx"
                val dds = "texture_2d_array.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }

            // Texture 3D
            run {
                val texture = Texture(Target._3D, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
                val ktx = "texture_3d.ktx"
                val dds = "texture_3d.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }

            // Texture cube
            run {
                val texture = Texture(Target.CUBE, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 6, 1)
                texture.clear(Vec4b(225, 127, 0, 255))
                val ktx = "texture_cube.ktx"
                val dds = "texture_cube.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }

            // Texture cube array
            run {
                val texture = Texture(Target.CUBE_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(1), 2, 6, 1)
                texture.clear(Vec4b(225, 127, 0, 255))
                val ktx = "texture_cube_array.ktx"
                val dds = "texture_cube_array.dds"
                gli.save(texture, ktx)
                gli.save(texture, dds)
                val textureKTX = gli.load(ktx)
                val textureDDS = gli.load(dds)
                Files.delete(pathOf(ktx))
                Files.delete(pathOf(dds))

                texture shouldBe textureKTX
                texture shouldBe textureDDS
            }
        }

        "data" {

            val texture = Texture(Target._2D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(1), 2, 1, 1)

            Texture2dArray(texture)[0].data()[0] shouldBe texture.data(0, 0, 0)[0]
            Texture2dArray(texture)[1].data()[0] shouldBe texture.data(1, 0, 0)[0]
        }

        "perf" {

            fun textureLoad(extent: Int) {

                val texture = Texture2d(Format.R8_UNORM_PACK8, Vec2i(extent))
                texture.clear(255.b)

                var error = 0

                val ns = measureNanoTime {
                    for (levelIndex in 0 until texture.levels()) {
                        val extent = texture.extent(levelIndex)
                        for (y in 0 until extent.y)
                            for (x in 0 until extent.x) {
                                val texel = texture.load<Byte>(Vec2i(x, y), levelIndex)
                                error += if (texel == 255.b) 0 else 1
                            }
                    }
                }
                error shouldBe 0
                println("2D texture load performance test: $ns ns")
            }

            fun textureFetch(extent: Int) {

                val texture = Texture2d(Format.R8_UNORM_PACK8, Vec2i(extent))
                texture.clear(255.b)

                //gli::sampler2d<float> Sampler (texture, gli::WRAP_CLAMP_TO_EDGE) TODO
//
//                std::clock_t TimeBegin = std ::clock()
//
//                for (gli:: texture2d::size_type LevelIndex = 0, LevelCount = Texture.levels(); LevelIndex < LevelCount; ++LevelIndex)
//                {
//                    gli::texture2d::extent_type const extent = texture.extent(LevelIndex)
//                    for (gli:: size_t y = 0; y < extent.y; ++y)
//                    for (gli:: size_t x = 0; x < extent.x; ++x)
//                    {
//                        gli::vec4 const & Texel = Sampler . texel_fetch (gli::texture2d::extent_type(x, y), LevelIndex)
//                        Error += gli::all(gli::epsilonEqual(Texel, gli::vec4(1, 0, 0, 1), 0.001f)) ? 0 : 1
//                        assert(!Error)
//                    }
//                }
//
//                std::clock_t TimeEnd = std ::clock()
//                printf("2D texture fetch performance test: %d\n", TimeEnd - TimeBegin)
            }

            fun texture2dAccess(iterations: Int) {

                var error = 0

                val textures = ArrayList<Texture2d>(FORMAT_COUNT)
                for (format in FORMAT_FIRST..FORMAT_LAST)
                    with(Texture2d(format, Vec2i(4), 9)) {
                        textures += this
                        error += if (this.empty()) 1 else 0
                    }

                var ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val baseAddress = MemoryUtil.memAddress(it.data(layerIndex, 0, levelIndex))
                                    error += if (baseAddress != MemoryUtil.NULL) 0 else 1
                                }
                        }
                }
                println("2d texture data access performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val size = it.size(levelIndex)
                                    error += if (size != 0) 0 else 1
                                }
                        }
                }
                println("2d texture size performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val extent = it.extent(levelIndex)
                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                }
                        }
                }
                println("2d texture extent access performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val extent = it.extent(levelIndex)
                                    val size = it.size(levelIndex)

                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                    error += if (size != 0) 0 else 1
                                }
                        }
                }
                println("2d texture extent and size access performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val extent = it.extent(levelIndex)
                                    val size = it.size(levelIndex)
                                    val baseAddress = MemoryUtil.memAddress(it.data(layerIndex, 0, levelIndex))

                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                    error += if (size != 0) 0 else 1
                                    error += if (baseAddress != MemoryUtil.NULL) 0 else 1
                                }
                        }
                }
                println("2d texture all access performance test: $ns ns")

                error shouldBe 0
            }

            fun cubeArrayAccess(iterations: Int) {

                var error = 0

                val textures = ArrayList<TextureCubeArray>(FORMAT_COUNT)
                for (format in FORMAT_FIRST..FORMAT_LAST)
                    with(TextureCubeArray(format, Vec2i(4), 3, 3)) {
                        textures += this
                        error += if (this.empty()) 1 else 0
                    }

                var ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val baseAddress = MemoryUtil.memAddress(it.data(layerIndex, 0, levelIndex))
                                    error += if (baseAddress != MemoryUtil.NULL) 0 else 1
                                }
                        }
                }
                println("Cube array texture data access performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val size = it.size(levelIndex)
                                    error += if (size != 0) 0 else 1
                                }
                        }
                }
                println("Cube array texture size performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val extent = it.extent(levelIndex)
                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                }
                        }
                }
                println("Cube array texture extent access performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val extent = it.extent(levelIndex)
                                    val size = it.size(levelIndex)

                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                    error += if (size != 0) 0 else 1
                                }
                        }
                }
                println("Cube array texture extent and size access performance test: $ns ns")

                ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val extent = it.extent(levelIndex)
                                    val size = it.size(levelIndex)
                                    val baseAddress = MemoryUtil.memAddress(it.data(layerIndex, 0, levelIndex))

                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                    error += if (size != 0) 0 else 1
                                    error += if (baseAddress != MemoryUtil.NULL) 0 else 1
                                }
                        }
                }
                println("Cube array texture all access performance test: $ns ns")

                error shouldBe 0
            }

            fun genericCreation(iterations: Int) {

                var error = 0

                val ns = measureNanoTime {
                    for (index in 0 until iterations)
                        for (format in FORMAT_FIRST..FORMAT_LAST) {
                            val texture = Texture(Target._2D_ARRAY, format, Vec3i(4, 4, 1), 1, 1, 3)
                            error += if (texture.empty()) 1 else 0
                        }
                }
                println("Generic texture creation performance test: $ns ns")

                error shouldBe 0
            }

            fun _2dArrayCreation(iterations: Int) {

                var error = 0

                val ns = measureNanoTime {
                    for (index in 0 until iterations)
                        for (format in FORMAT_FIRST..FORMAT_LAST) {
                            val texture = Texture2dArray(format, Vec2i(4), 1, 3)
                            error += if (texture.empty()) 1 else 0
                        }
                }
                println("2D array texture creation performance test: $ns ns")

                error shouldBe 0
            }

            fun _2dCreation(iterations: Int) {

                var error = 0

                val ns = measureNanoTime {
                    for (index in 0 until iterations)
                        for (format in FORMAT_FIRST..FORMAT_LAST) {
                            val texture = Texture2d(format, Vec2i(4), 3)
                            error += if (texture.empty()) 1 else 0
                        }
                }
                println("2D texture creation performance test: $ns ns")

                error shouldBe 0
            }

            fun cubeArrayCreation(iterations: Int) {

                var error = 0

                val ns = measureNanoTime {
                    for (index in 0 until iterations)
                        for (format in FORMAT_FIRST..FORMAT_LAST) {
                            val texture = TextureCubeArray(format, Vec2i(4), 1, 3)
                            error += if (texture.empty()) 1 else 0
                        }
                }
                println("Cube array texture creation performance test: $ns ns")

                error shouldBe 0
            }

            val DO_PERF_TEST = false
            val PERF_TEST_ACCESS_ITERATION = if (DO_PERF_TEST) 100000 else 0
            val PERF_TEST_CREATION_ITERATION = if (DO_PERF_TEST) 1000 else 0

            textureLoad(if (DO_PERF_TEST) 8192 else 1024)
            textureFetch(if (DO_PERF_TEST) 8192 else 1024)
//            textureLod_nearest::main(DO_PERF_TEST ? 8192 : 1024);
//            Error += perf_texture_lod_linear::main(DO_PERF_TEST ? 8192 : 1024);
//            Error += perf_generate_mipmaps_nearest::main(DO_PERF_TEST ? 8192 : 1024);
//            Error += perf_generate_mipmaps_linear::main(DO_PERF_TEST ? 8192 : 1024);
            texture2dAccess(PERF_TEST_ACCESS_ITERATION)
            cubeArrayAccess(PERF_TEST_ACCESS_ITERATION)
            genericCreation(PERF_TEST_CREATION_ITERATION)
            _2dArrayCreation(PERF_TEST_CREATION_ITERATION)
            _2dCreation(PERF_TEST_CREATION_ITERATION)
            cubeArrayCreation(PERF_TEST_CREATION_ITERATION)
        }
    }
}