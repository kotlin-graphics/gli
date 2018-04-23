package gli_

import glm_.b
import glm_.glm
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class view_ : StringSpec() {

    init {

        "view" {

            val formatsA = listOf(
                    Format.RGBA8_UNORM_PACK8,
                    Format.RGB8_UNORM_PACK8,
                    Format.R8_SNORM_PACK8,
                    Format.RGBA32_SFLOAT_PACK32,
                    Format.RGB_DXT1_UNORM_BLOCK8,
                    Format.RGBA_BP_UNORM_BLOCK16)

            // 1D textures don't support compressed formats
            val formatsB = listOf(
                    Format.RGBA8_UNORM_PACK8,
                    Format.RGB8_UNORM_PACK8,
                    Format.R8_SNORM_PACK8,
                    Format.RGBA32_SFLOAT_PACK32)

            val textureSize = 32

            testView1D(formatsB, Vec1i(textureSize))
            testView1DArray(formatsB, Vec1i(textureSize))
            testView2D(formatsA, Vec2i(textureSize))
            testView2DArray(formatsA, Vec2i(textureSize))
            testView3D(formatsA, Vec3i(textureSize))
            testViewCube(formatsA, Vec2i(textureSize))
            testViewCubeArray(formatsA, Vec2i(textureSize))
        }
    }

    fun testView1D(
            formats: List<Format>,
            textureSize: Vec1i
    ) {
        for (format in formats) {

            val textureA = Texture1d(format, textureSize)
            val textureViewA = Texture1d(gli.view(
                    textureA, textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val textureViewC = Texture1d(gli.view(
                    textureA, textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewC
            textureViewA shouldBe textureViewC

            val textureB = Texture1d(format, textureSize / Vec1i(2))
            val textureViewB = Texture1d(gli.view(
                    textureA, textureA.baseLevel + 1, textureA.maxLevel))

            textureB shouldBe textureViewB

            val textureViewD = Texture1d(gli.view(
                    textureA, textureA.baseLevel + 1, textureA.maxLevel))

            textureB shouldBe textureViewD
            textureViewB shouldBe textureViewD

            val textureD = Texture1d(gli.view(
                    textureA, 1, 3))

            textureA[1] shouldBe textureD[0]
            textureA[2] shouldBe textureD[1]

            val textureE = Texture1d(gli.view(
                    textureD, 1, 1))

            textureE[0] shouldBe textureD[1]
            textureE[0] shouldBe textureA[2]
        }
    }

    fun testView1DArray(
            formats: List<Format>,
            textureSize: Vec1i
    ) {
        for (format in formats) {

            val textureA = Texture1dArray(format, textureSize, 4)

            val textureViewA = Texture1dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val textureViewC = Texture1dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewC
            textureViewC shouldBe textureViewA

            val textureB = Texture1dArray(
                    format, textureSize / Vec1i(2), 4)

            textureA shouldNotBe textureB

            val textureViewB = Texture1dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureA shouldNotBe textureViewB
            textureB shouldBe textureViewB

            val textureViewD = Texture1dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureViewD shouldBe textureViewB

            val textureD = Texture1dArray(gli.view(
                    textureA, 0, textureA.layers() - 1, 1, 3))

            textureA[0][1] shouldBe textureD[0][0]
            textureA[0][2] shouldBe textureD[0][1]

            val textureE = Texture1dArray(gli.view(
                    textureD, 0, textureD.layers() - 1, 0, textureD.levels() - 1))

            textureE shouldBe textureD
            textureE[0] shouldBe textureD[0]

            val textureF = Texture1dArray(gli.view(
                    textureE, 1, 3, 0, textureE.levels() - 1))

            textureF[0] shouldBe textureD[1]
            textureF[0] shouldBe textureE[1]
        }
    }

    fun testView2D(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = Texture2d(format, textureSize, glm.levels(textureSize))

            for (index in 0 until textureA.size)
                textureA.data<Byte>()[index] = index.b

            val textureViewA = Texture2d(gli.view(
                    textureA, textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val textureD = Texture2d(
                    gli.view(textureA, 1, 3))

            textureA[1] shouldBe textureD[0]
            textureA[2] shouldBe textureD[1]

            val textureE = Texture2d(textureD, 1, 1)

            textureE[0] shouldBe textureD[1]
            textureE[0] shouldBe textureA[2]

            val textureViewB = Texture2d(gli.view(
                    textureA,
                    textureA.baseLevel + 1, textureA.maxLevel))

            val textureViewD = Texture2d(gli.view(
                    textureA,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureViewD shouldBe textureViewB
        }
    }

    fun testView2DArray(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = Texture2dArray(format, textureSize, 4)

            val textureViewA = Texture2dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val textureB = Texture2dArray(format, textureSize / Vec2i(2), 4)

            val textureViewB = Texture2dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel + 1, textureA.maxLevel))

            val textureViewD = Texture2dArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureViewB shouldBe textureViewD
            textureB shouldBe textureViewB

            val textureD = Texture2dArray(gli.view(
                    textureA, 0, textureA.layers() - 1, 1, 3))

            textureA[0][1] shouldBe textureD[0][0]
            textureA[0][2] shouldBe textureD[0][1]

            val textureE = Texture2dArray(gli.view(
                    textureD, 0, textureD.layers() - 1, 0, textureD.levels() - 1))

            textureE shouldBe textureD
            textureE[0] shouldBe textureD[0]

            val textureF = Texture2dArray(gli.view(
                    textureE, 1, 3, 0, textureE.levels() - 1))

            textureF[0] shouldBe textureD[1]
            textureF[0] shouldBe textureE[1]
        }
    }

    fun testView3D(
            formats: List<Format>,
            textureSize: Vec3i
    ) {
        for (format in formats) {

            val textureA = Texture3d(format, textureSize, glm.levels(textureSize))
            val textureViewA = Texture3d(gli.view(
                    textureA, textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val sizeB = textureSize / Vec3i(2)
            val textureB = Texture3d(format, sizeB, glm.levels(sizeB))

            val textureViewB = Texture3d(gli.view(
                    textureA, textureA.baseLevel + 1, textureA.maxLevel))

            textureB shouldBe textureViewB

            val textureViewD = Texture3d(gli.view(
                    textureA, textureA.baseLevel + 1, textureA.maxLevel))

            textureViewD shouldBe textureViewB

            val textureD = Texture3d(gli.view(textureA, 1, 3))

            textureA[1] shouldBe textureD[0]
            textureA[2] shouldBe textureD[1]

            val textureE = Texture3d(gli.view(textureD, 1, 1))

            textureE[0] shouldBe textureD[1]
            textureE[0] shouldBe textureA[2]
        }
    }

    fun testViewCube(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = TextureCube(format, textureSize)

            val textureViewA = TextureCube(gli.view(
                    textureA,
                    textureA.baseFace, textureA.maxFace,
                    textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val sizeB = textureSize / Vec2i(2)
            val textureB = TextureCube(format, sizeB)

            val textureViewB = TextureCube(gli.view(
                    textureA,
                    textureA.baseFace, textureA.maxFace,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureB shouldBe textureViewB

            val textureViewD = TextureCube(gli.view(
                    textureA,
                    textureA.baseFace, textureA.maxFace,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureViewD shouldBe textureViewB

            val textureD = TextureCube(gli.view(
                    textureA, 0, textureA.faces() - 1, 1, 3))

            textureA[0][1] shouldBe textureD[0][0]
            textureA[0][2] shouldBe textureD[0][1]

            val textureE = TextureCube(gli.view(
                    textureD, 0, textureD.faces() - 1, 0, textureD.levels() - 1))

            textureE shouldBe textureD
            textureE[0] shouldBe textureD[0]

            val textureF = TextureCube(gli.view(
                    textureE, 1, 3, 0, textureE.levels() - 1))

            textureF[0] shouldBe textureD[1]
            textureF[0] shouldBe textureE[1]
        }
    }

    fun testViewCubeArray(
            formats: List<Format>,
            textureSize: Vec2i
    ) {
        for (format in formats) {

            val textureA = TextureCubeArray(
                    format,
                    textureSize,
                    4)

            val textureViewA = TextureCubeArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseFace, textureA.maxFace,
                    textureA.baseLevel, textureA.maxLevel))

            textureA shouldBe textureViewA

            val sizeB = textureSize / Vec2i(2)
            val textureB = TextureCubeArray(
                    format,
                    sizeB,
                    4)

            val textureViewB = TextureCubeArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseFace, textureA.maxFace,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureB shouldBe textureViewB

            val textureViewD = TextureCubeArray(gli.view(
                    textureA,
                    textureA.baseLayer, textureA.maxLayer,
                    textureA.baseFace, textureA.maxFace,
                    textureA.baseLevel + 1, textureA.maxLevel))

            textureViewD shouldBe textureViewB

            val textureD = TextureCubeArray(gli.view(
                    textureA,
                    0, textureA.layers() - 1,
                    0, textureA.faces() - 1,
                    1, 3))

            textureA[0][0][1] shouldBe textureD[0][0][0]
            textureA[0][0][2] shouldBe textureD[0][0][1]

            val textureE = TextureCubeArray(gli.view(
                    textureD,
                    0, textureA.layers() - 1,
                    0, textureD.faces() - 1,
                    0, textureD.levels() - 1))

            textureE shouldBe textureD
            textureE[0] shouldBe textureD[0]
            textureE[1] shouldBe textureD[1]

            val textureF = TextureCubeArray(gli.view(
                    textureE,
                    0, textureA.layers() - 1,
                    1, 3,
                    0, textureE.levels() - 1))

            textureF[0][0] shouldBe textureD[0][1]
            textureF[1][0] shouldBe textureD[1][1]
            textureF[0][0] shouldBe textureE[0][1]
            textureF[1][0] shouldBe textureE[1][1]
        }
    }
}