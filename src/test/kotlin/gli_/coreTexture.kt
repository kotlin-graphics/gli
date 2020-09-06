package gli_

import glm_.b
import kool.adr
import glm_.glm
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import glm_.vec4.Vec4b
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.core.spec.style.StringSpec
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.file.Files
import kotlin.system.measureNanoTime

/**
 * Created by GBarbieri on 04.04.2017.
 */

class coreTexture : StringSpec() {

    init {

        "alloc" {

            val sizes = intArrayOf(16, 32, 15, 17, 1)

            for (target in Target.FIRST..Target.LAST)

                for (format in Format.FIRST .. Format.LAST) {

                    val faces = if (target.isTargetCube) 6 else 1

                    if (format.isCompressed && target.isTarget1d) continue

                    sizes.forEach {

                        val size = Vec3i(it)

                        val textureA = Texture(target, format, size, 1, faces, glm.levels(size))
                        val textureB = Texture(target, format, size, 1, faces, glm.levels(size))

                        textureA shouldBe textureB
                    }
                }
        }

        "clear" {

            //            Vec4b const Orange(255, 127, 0, 255);
//
//            gli.texture.extent_type Size(16, 16, 1);
//            val Texture(gli.TARGET_2D, gli.FORMAT_RGBA8_UNORM_PACK8, Size, 1, 1, gli.levels(Size));
//
//            Texture.clear<Vec4b>(Orange);
        }

        "query" {

            val texture = Texture(Target._2D, Format.RGBA8_UINT_PACK8, Vec3i(1), 1, 1, 1)

            texture.size shouldBe Vec4b.size
            texture.format shouldBe Format.RGBA8_UINT_PACK8
            texture.levels() shouldBe 1
            texture.notEmpty() shouldBe true
            texture.extent() shouldBe Vec3i(1)
        }

        "tex access" {

            run {
                val texture = Texture1d(Format.RGBA8_UINT_PACK8, Vec1i(2), 2)
                texture.notEmpty() shouldBe true

                val image0 = texture[0]
                val image1 = texture[1]

                val size0 = image0.size
                val size1 = image1.size

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
                val texture = Texture(Target._2D, Format.RGBA8_UINT_PACK8, Vec3i(1), 1, 1, 1)

                val sizeA = texture.size
                sizeA shouldBe Vec4b.size * 1

                texture.data<Vec4b>()[0] = Vec4b(255, 127, 0, 255)

                val pointer0 = texture.data<Vec4b>()[0]
                val color0 = pointer0

                color0 shouldBe Vec4b(255, 127, 0, 255)
            }
        }

        "size" {

            class Test(val format: Format, val dimensions: Vec3i, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, Vec3i(1), 4),
                    Test(Format.R8_UINT_PACK8, Vec3i(1), 1))

            tests.forEach { Texture(Target._2D, it.format, Vec3i(1), 1, 1, 1).size shouldBe it.size }
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
                texture clear Vec4b(225, 127, 0, 255)

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
                texture clear Vec4b(225, 127, 0, 255)
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
                texture clear Vec4b(225, 127, 0, 255)

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
                texture clear Vec4b(225, 127, 0, 255)
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
                texture clear Vec4b(225, 127, 0, 255)
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
                texture clear Vec4b(225, 127, 0, 255)
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

            Texture2dArray(texture)[0].data() shouldBe texture.data(0, 0, 0)
            Texture2dArray(texture)[1].data() shouldBe texture.data(1, 0, 0)
        }


        "perf" {

            fun textureLoad(extent: Int) {

                val texture = Texture2d(Format.R8_UNORM_PACK8, Vec2i(extent))
                texture clear 255.b

                var error = 0

                val ns = measureNanoTime {
                    for (levelIndex in 0 until texture.levels()) {
                        val extend_ = texture.extent(levelIndex)
                        for (y in 0 until extend_.y)
                            for (x in 0 until extend_.x) {
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
                texture clear 255.b

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

                val textures = ArrayList<Texture2d>(Format.COUNT)
                for (format in Format.FIRST .. Format.LAST)
                    with(Texture2d(format, Vec2i(4), 9)) {
                        textures += this
                        error += if (this.empty()) 1 else 0
                    }

                var ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val baseAddress = it.data(layerIndex, 0, levelIndex).adr
                                    error += if (baseAddress != NULL) 0 else 1
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
                                    val baseAddress = it.data(layerIndex, 0, levelIndex).adr

                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                    error += if (size != 0) 0 else 1
                                    error += if (baseAddress != NULL) 0 else 1
                                }
                        }
                }
                println("2d texture all access performance test: $ns ns")

                error shouldBe 0
            }

            fun cubeArrayAccess(iterations: Int) {

                var error = 0

                val textures = ArrayList<TextureCubeArray>(Format.COUNT)
                for (format in Format.FIRST .. Format.LAST)
                    with(TextureCubeArray(format, Vec2i(4), 3, 3)) {
                        textures += this
                        error += if (this.empty()) 1 else 0
                    }

                var ns = measureNanoTime {
                    for (index in 0 until iterations)
                        textures.forEach {
                            for (layerIndex in 0 until it.layers())
                                for (levelIndex in 0 until it.levels()) {
                                    val baseAddress = it.data(layerIndex, 0, levelIndex).adr
                                    error += if (baseAddress != NULL) 0 else 1
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
                                    val baseAddress = it.data(layerIndex, 0, levelIndex).adr

                                    error += if (extent.x != 0) 0 else 1
                                    error += if (extent.y != 0) 0 else 1
                                    error += if (size != 0) 0 else 1
                                    error += if (baseAddress != NULL) 0 else 1
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
                        for (format in Format.FIRST .. Format.LAST) {
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
                        for (format in Format.FIRST .. Format.LAST) {
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
                        for (format in Format.FIRST .. Format.LAST) {
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
                        for (format in Format.FIRST .. Format.LAST) {
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