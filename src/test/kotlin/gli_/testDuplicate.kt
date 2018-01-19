package gli_

import glm_.glm
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class testDuplicate : StringSpec() {

    init {

        "duplicate" {
            val formatsA = listOf(
                    Format.RGBA8_UNORM_PACK8,
                    Format.RGB8_UNORM_PACK8,
                    Format.RGB_DXT1_UNORM_BLOCK8,
                    Format.RGBA_BP_UNORM_BLOCK16,
                    Format.RGBA32_SFLOAT_PACK32)

            val formatsB = listOf(
                    Format.RGBA8_UNORM_PACK8,
                    Format.RGB8_UNORM_PACK8,
                    Format.RGBA32_SFLOAT_PACK32)

            val textureSize = 32

            testTexture1D(formatsB, Vec1i(textureSize))
            testTexture1DArray(formatsB, Vec1i(textureSize))
            testTexture2D(formatsA, Vec2i(textureSize))
            testTexture2DArray(formatsA, Vec2i(textureSize))
            testTexture3D(formatsA, Vec3i(textureSize))
            testTextureCube(formatsA, Vec2i(textureSize))
            testTextureCubeArray(formatsA, Vec2i(textureSize))
        }
    }

    fun testTexture1D(
            formats: List<Format>,
            textureSize: Vec1i
    ) {

        for (format in formats) {

            val textureA = Texture1d(
                    format,
                    textureSize,
                    glm.levels(textureSize))

            val textureB = Texture1d(gli.duplicate(textureA))

            textureA shouldBe textureB

            val textureC = Texture1d(textureA, 1, 2)

            textureA[1] shouldBe textureC[0]
            textureA[2] shouldBe textureC[1]

            val textureD = Texture1d(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = Texture1d(gli.duplicate(textureA, 0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureE = Texture1d(gli.duplicate(textureA, 1, textureA.levels() - 2))
            textureA[1] shouldBe textureE[0]

            val textureF = Texture1d(textureA, 1, textureA.levels() - 2)

            textureE shouldBe textureF
        }
    }

    fun testTexture1DArray(
            formats: List<Format>,
            textureSize: Vec1i
    ) {
        for (format in formats) {

            val textureA = Texture1dArray(
                    format,
                    textureSize,
                    4)

            val textureB = Texture1dArray(gli.duplicate(textureA))

            textureA shouldBe textureB

            val textureC = Texture1dArray(textureA,
                    0, textureA.layers() - 1,
                    1, 2)

            textureA[0][1] shouldBe textureC[0][0]
            textureA[0][2] shouldBe textureC[0][1]
            textureA[1][1] shouldBe textureC[1][0]
            textureA[1][2] shouldBe textureC[1][1]

            val textureD = Texture1dArray(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = Texture1dArray(gli.duplicate(
                    textureA,
                    0, textureA.layers() - 1,
                    0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureE = Texture1dArray(gli.duplicate(
                    textureA,
                    1, textureA.layers() - 1,
                    0, textureA.levels() - 1))
            textureA[1] shouldBe textureE[0]

            val textureF = Texture1dArray(
                    textureA,
                    1, textureA.layers() - 1,
                    0, textureA.levels() - 1)

            textureE shouldBe textureF

            val textureK = Texture1dArray(
                    format,
                    textureSize,
                    4,
                    glm.levels(textureSize))

            val textureH = Texture1dArray(textureK, 1, 2, 1, 2)
            val textureI = Texture1dArray(gli.duplicate(textureH))

            textureH shouldBe textureI

            val textureJ = (gli.duplicate(textureK, 1, 2, 1, 2))
            textureH shouldBe textureJ
            textureI shouldBe textureJ
        }
    }

    fun testTexture2D(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = Texture2d(format, textureSize)

            val textureB = Texture2d(gli.duplicate(textureA))
            textureA shouldBe textureB

            val textureC = Texture2d(gli.view(
                    textureA, 1, 2))

            textureA[1] shouldBe textureC[0]
            textureA[2] shouldBe textureC[1]

            val textureD = Texture2d(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = Texture2d(gli.duplicate(textureA, 0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureE = Texture2d(gli.duplicate(textureA, 1, textureA.levels() - 1))
            textureA[1] shouldBe textureE[0]

            val textureF = Texture2d(gli.view(
                    textureA, 1, textureA.levels() - 1))

            textureE shouldBe textureF
        }
    }

    fun testTexture2DArray(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = Texture2dArray(
                    format,
                    textureSize,
                    4)

            val textureB = Texture2dArray(gli.duplicate(textureA))

            textureA shouldBe textureB

            val textureC = Texture2dArray(textureA,
                    0, textureA.layers() - 1,
                    1, 2)

            textureA[0][1] shouldBe textureC[0][0]
            textureA[0][2] shouldBe textureC[0][1]
            textureA[1][1] shouldBe textureC[1][0]
            textureA[1][2] shouldBe textureC[1][1]

            val textureD = Texture2dArray(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = Texture2dArray(gli.duplicate(
                    textureA,
                    0, textureA.layers() - 1,
                    0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureE = Texture2dArray(gli.duplicate(
                    textureA,
                    1, textureA.layers() - 1,
                    0, textureA.levels() - 1))
            textureA[1] shouldBe textureE[0]

            val textureF = Texture2dArray(
                    textureA,
                    1, textureA.layers() - 1,
                    0, textureA.levels() - 1)

            textureE shouldBe textureF

            val textureK = Texture2dArray(
                    format,
                    textureSize,
                    4)

            val textureH = Texture2dArray(textureK, 1, 2, 1, 2)
            val textureI = Texture2dArray(gli.duplicate(textureH))

            textureH shouldBe textureI

            val textureJ = Texture2dArray(gli.duplicate(textureK, 1, 2, 1, 2))
            textureH shouldBe textureJ
            textureI shouldBe textureJ
        }
    }

    fun testTexture3D(
            formats: List<Format>,
            textureSize: Vec3i
    ) {
        for (format in formats) {

            val textureA = Texture3d(format, textureSize)

            val textureB = Texture3d(gli.duplicate(textureA))

            textureA shouldBe textureB

            val textureC = Texture3d(textureA, 1, 2)

            textureA[1] shouldBe textureC[0]
            textureA[2] shouldBe textureC[1]

            val textureD = Texture3d(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = Texture3d(gli.duplicate(textureA, 0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureE = Texture3d(gli.duplicate(textureA, 1, textureA.levels() - 1))
            textureA[1] shouldBe textureE[0]

            val textureF = Texture3d(textureA, 1, textureA.levels() - 1)

            textureE shouldBe textureF
        }
    }

    fun testTextureCube(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = TextureCube(
                    format,
                    Vec2i(textureSize))

            val textureB = TextureCube(gli.duplicate(textureA))

            textureA shouldBe textureB

            val textureC = TextureCube(textureA,
                    0, textureA.faces() - 1,
                    1, 2)

            textureA[0][1] shouldBe textureC[0][0]
            textureA[0][2] shouldBe textureC[0][1]
            textureA[1][1] shouldBe textureC[1][0]
            textureA[1][2] shouldBe textureC[1][1]

            val textureD = TextureCube(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = TextureCube(gli.duplicate(
                    textureA,
                    0, textureA.faces() - 1,
                    0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureE = TextureCube(gli.duplicate(
                    textureA,
                    0, textureA.faces() - 1,
                    0, textureA.levels() - 1))
            textureA[1] shouldBe textureE[0]

            val textureF = TextureCube(
                    textureA,
                    0, textureA.faces() - 1,
                    0, textureA.levels() - 1)

            textureE shouldBe textureF

            val textureK = TextureCube(
                    format,
                    textureSize)

            val textureH = TextureCube(textureK, 0, 5, 1, 2)
            val textureI = TextureCube(gli.duplicate(textureH))

            textureH shouldBe textureI

            val textureJ = TextureCube(gli.duplicate(textureK, 0, 5, 1, 2))
            textureH shouldBe textureJ
            textureI shouldBe textureJ
        }
    }

    fun testTextureCubeArray(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = TextureCubeArray(
                    format,
                    textureSize,
                    4)

            val textureB = TextureCubeArray(gli.duplicate(textureA))

            textureA shouldBe textureB

            val textureC = TextureCubeArray(textureA,
                    0, textureA.layers() - 1,
                    0, textureA.faces() - 1,
                    1, 2)

            textureA[0][0][1] shouldBe textureC[0][0][0]
            textureA[0][0][2] shouldBe textureC[0][0][1]
            textureA[0][1][1] shouldBe textureC[0][1][0]
            textureA[0][1][2] shouldBe textureC[0][1][1]

            val textureD = TextureCubeArray(gli.duplicate(textureC))

            textureC shouldBe textureD

            val textureG = TextureCubeArray(gli.duplicate(
                    textureA,
                    0, textureA.layers() - 1,
                    0, textureA.faces() - 1,
                    0, textureA.levels() - 1))
            textureA shouldBe textureG

            val textureK = TextureCubeArray(
                    format,
                    textureSize,
                    4)

            val textureH = TextureCubeArray(textureK, 1, 2, 0, 5, 1, 2)
            val textureI = TextureCubeArray(gli.duplicate(textureH))

            textureH shouldBe textureI

            val textureJ = TextureCubeArray(gli.duplicate(textureK, 1, 2, 0, 5, 1, 2))
            textureH shouldBe textureJ
            textureI shouldBe textureJ
        }
    }
}