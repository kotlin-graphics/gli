package gli

import glm.vec3.Vec3i
import glm.vec4.Vec4b
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 04.04.2017.
 */

class coreTexture : StringSpec() {

    init {

        "alloc" {

            val sizes = intArrayOf(16, 32, 15, 17, 1)

            for (target in TARGET_FIRST..TARGET_LAST)

                for (format in FORMAT_FIRST..FORMAT_LAST) {

                    val faces = if (target.isTargetCube) 6 else 1

                    if (format.isCompressed && target.isTarget1d) continue

                    sizes.forEach {

                        val size = Vec3i(it)

                        val textureA = Texture(target, format, size, 1, faces, gli.levels(size))
                        val textureB = Texture(target, format, size, 1, faces, gli.levels(size))

                        textureA shouldBe textureB
                    }
                }
        }

        "query" {

            val texture = Texture(Target._2D, Format.RGBA8_UINT_PACK8, Vec3i(1), 1, 1, 1)

            texture.size() shouldBe Vec4b.SIZE
            texture.format shouldBe Format.RGBA8_UINT_PACK8
            texture.levels() shouldBe 1
            texture.notEmpty() shouldBe true
            texture.extent() shouldBe Vec3i(1)
        }

        // TODO tex access

        "size" {

            class Test(val format: Format, val dimensions: Vec3i, val size: Int)

            val tests = arrayOf(
                    Test(Format.RGBA8_UINT_PACK8, Vec3i(1), 4),
                    Test(Format.R8_UINT_PACK8, Vec3i(1), 1))

            tests.forEach { Texture(Target._2D, it.format, Vec3i(1), 1, 1, 1).size() shouldBe it.size }
        }

        "specialize" {

            val texture = Texture(Target._1D, Format.RGBA8_UNORM_PACK8, Vec3i(1), 1, 1, 1)
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

            // TODO
        }

        // TODO load

        "data" {

            val texture = Texture(Target._2D_ARRAY, Format.RGBA8_UNORM_PACK8, Vec3i(1), 2, 1, 1)

            // TODO
        }

        // TODO perf
    }
}