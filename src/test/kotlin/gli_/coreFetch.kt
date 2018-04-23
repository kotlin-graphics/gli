package gli_

import glm_.b
import glm_.glm
import glm_.vec1.Vec1b
import glm_.vec2.Vec2b
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec3.Vec3b
import glm_.vec4.Vec4
import glm_.vec4.Vec4b
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreFetch : StringSpec() {

    init {

        "fetch r8 unorm" {

            val path = pathOf("r8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.R8_UNORM_PACK8, Vec2i(2)).apply {
                store(Vec2i(0, 0), 0, Vec1b(1))
                store(Vec2i(1, 0), 0, Vec1b(2))
                store(Vec2i(1, 1), 0, Vec1b(3))
                store(Vec2i(0, 1), 0, Vec1b(4))
                store(Vec2i(0, 0), 1, Vec1b(5))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val a = load<Byte>(Vec2i(0, 0), 0)
                a shouldBe 1.b
                val b = load<Byte>(Vec2i(1, 0), 0)
                b shouldBe 2.b
                val c = load<Byte>(Vec2i(1, 1), 0)
                c shouldBe 3.b
                val d = load<Byte>(Vec2i(0, 1), 0)
                d shouldBe 4.b
                val e = load<Byte>(Vec2i(0, 0), 1)
                e shouldBe 5.b

                val A = load<Vec1b>(Vec2i(0, 0), 0)
                A shouldBe Vec1b(1)
                val B = load<Vec1b>(Vec2i(1, 0), 0)
                B shouldBe Vec1b(2)
                val C = load<Vec1b>(Vec2i(1, 1), 0)
                C shouldBe Vec1b(3)
                val D = load<Vec1b>(Vec2i(0, 1), 0)
                D shouldBe Vec1b(4)
                val E = load<Vec1b>(Vec2i(0, 0), 1)
                E shouldBe Vec1b(5)
            }
            textureA shouldBe textureB

            Files.delete(path)
        }

        "fetch rg8 unorm" {

            val path = pathOf("rg8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.RG8_UNORM_PACK8, Vec2i(2)).apply {
                store(Vec2i(0, 0), 0, Vec2b(1, 2))
                store(Vec2i(1, 0), 0, Vec2b(3, 4))
                store(Vec2i(1, 1), 0, Vec2b(5, 6))
                store(Vec2i(0, 1), 0, Vec2b(7, 8))
                store(Vec2i(0, 0), 1, Vec2b(9, 5))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec2b>(Vec2i(0, 0), 0)
                A shouldBe Vec2b(1, 2)
                val B = load<Vec2b>(Vec2i(1, 0), 0)
                B shouldBe Vec2b(3, 4)
                val C = load<Vec2b>(Vec2i(1, 1), 0)
                C shouldBe Vec2b(5, 6)
                val D = load<Vec2b>(Vec2i(0, 1), 0)
                D shouldBe Vec2b(7, 8)
                val E = load<Vec2b>(Vec2i(0, 0), 1)
                E shouldBe Vec2b(9, 5)
            }

            textureA shouldBe textureB

            Files.delete(path)
        }

        "fetch rgb8 unorm" {

            val path = pathOf("rgb8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec3b(255, 0, 0))
                store(Vec2i(1, 0), 0, Vec3b(255, 255, 0))
                store(Vec2i(1, 1), 0, Vec3b(0, 255, 0))
                store(Vec2i(0, 1), 0, Vec3b(0, 0, 255))
                store(Vec2i(0, 0), 1, Vec3b(255, 128, 0))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec3b>(Vec2i(0, 0), 0)
                A shouldBe Vec3b(255, 0, 0)
                val B = load<Vec3b>(Vec2i(1, 0), 0)
                B shouldBe Vec3b(255, 255, 0)
                val C = load<Vec3b>(Vec2i(1, 1), 0)
                C shouldBe Vec3b(0, 255, 0)
                val D = load<Vec3b>(Vec2i(0, 1), 0)
                D shouldBe Vec3b(0, 0, 255)
                val E = load<Vec3b>(Vec2i(0, 0), 1)
                E shouldBe Vec3b(255, 128, 0)
            }

            textureA shouldBe textureB

            Files.delete(path)
        }

        "fetch rgba8 unorm" {

            val path = pathOf("rgba8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec4b(255, 0, 0, 255))
                store(Vec2i(1, 0), 0, Vec4b(255, 255, 0, 255))
                store(Vec2i(1, 1), 0, Vec4b(0, 255, 0, 255))
                store(Vec2i(0, 1), 0, Vec4b(0, 0, 255, 255))
                store(Vec2i(0, 0), 1, Vec4b(255, 128, 0, 255))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec4b>(Vec2i(0, 0), 0)
                A shouldBe Vec4b(255, 0, 0, 255)
                val B = load<Vec4b>(Vec2i(1, 0), 0)
                B shouldBe Vec4b(255, 255, 0, 255)
                val C = load<Vec4b>(Vec2i(1, 1), 0)
                C shouldBe Vec4b(0, 255, 0, 255)
                val D = load<Vec4b>(Vec2i(0, 1), 0)
                D shouldBe Vec4b(0, 0, 255, 255)
                val E = load<Vec4b>(Vec2i(0, 0), 1)
                E shouldBe Vec4b(255, 128, 0, 255)
            }

            textureA shouldBe textureB

            Files.delete(path)
        }

        "fetch rgb10a2 unorm" {

            val path = pathOf("rgb10a2_unorm_4pixels.dds")

            val colorR = glm.packUnorm3x10_1x2(Vec4(1.0f, 0.0f, 0.0f, 1.0f))
            val colorY = glm.packUnorm3x10_1x2(Vec4(1.0f, 1.0f, 0.0f, 1.0f))
            val colorG = glm.packUnorm3x10_1x2(Vec4(0.0f, 1.0f, 0.0f, 1.0f))
            val colorB = glm.packUnorm3x10_1x2(Vec4(0.0f, 0.0f, 1.0f, 1.0f))
            val colorO = glm.packUnorm3x10_1x2(Vec4(1.0f, 0.5f, 0.0f, 1.0f))

            val textureA = Texture2d(Format.RGB10A2_UNORM_PACK32, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, colorR)
                store(Vec2i(1, 0), 0, colorY)
                store(Vec2i(1, 1), 0, colorG)
                store(Vec2i(0, 1), 0, colorB)
                store(Vec2i(0, 0), 1, colorO)
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Int>(Vec2i(0, 0), 0)
                A shouldBe colorR
                val B = load<Int>(Vec2i(1, 0), 0)
                B shouldBe colorY
                val C = load<Int>(Vec2i(1, 1), 0)
                C shouldBe colorG
                val D = load<Int>(Vec2i(0, 1), 0)
                D shouldBe colorB
                val E = load<Int>(Vec2i(0, 0), 1)
                E shouldBe colorO
            }

            textureA shouldBe textureB

            Files.delete(path)
        }

        "fetch srgb8 unorm" {

            val path = pathOf("srgb8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.RGB8_SRGB_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec3b(glm.convertLinearToSRGB(Vec3(255, 0, 0))))
                store(Vec2i(1, 0), 0, Vec3b(glm.convertLinearToSRGB(Vec3(255, 255, 0))))
                store(Vec2i(1, 1), 0, Vec3b(glm.convertLinearToSRGB(Vec3(0, 255, 0))))
                store(Vec2i(0, 1), 0, Vec3b(glm.convertLinearToSRGB(Vec3(0, 0, 255))))
                store(Vec2i(0, 0), 1, Vec3b(glm.convertLinearToSRGB(Vec3(255, 128, 0))))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec3b>(Vec2i(0, 0), 0)
                A shouldBe Vec3b(glm.convertLinearToSRGB(Vec3(255, 0, 0)))
                val B = load<Vec3b>(Vec2i(1, 0), 0)
                B shouldBe Vec3b(glm.convertLinearToSRGB(Vec3(255, 255, 0)))
                val C = load<Vec3b>(Vec2i(1, 1), 0)
                C shouldBe Vec3b(glm.convertLinearToSRGB(Vec3(0, 255, 0)))
                val D = load<Vec3b>(Vec2i(0, 1), 0)
                D shouldBe Vec3b(glm.convertLinearToSRGB(Vec3(0, 0, 255)))
                val E = load<Vec3b>(Vec2i(0, 0), 1)
                E shouldBe Vec3b(glm.convertLinearToSRGB(Vec3(255, 128, 0)))
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch srgba8 unorm" {

            val path = pathOf("srgba8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.RGBA8_SRGB_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec4b(glm.convertLinearToSRGB(Vec4(1.0f, 0.0f, 0.0f, 1.0f))))
                store(Vec2i(1, 0), 0, Vec4b(glm.convertLinearToSRGB(Vec4(1.0f, 1.0f, 0.0f, 1.0f))))
                store(Vec2i(1, 1), 0, Vec4b(glm.convertLinearToSRGB(Vec4(0.0f, 1.0f, 0.0f, 1.0f))))
                store(Vec2i(0, 1), 0, Vec4b(glm.convertLinearToSRGB(Vec4(0.0f, 0.0f, 1.0f, 1.0f))))
                store(Vec2i(0, 0), 1, Vec4b(glm.convertLinearToSRGB(Vec4(1.0f, 0.5f, 0.0f, 1.0f))))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec4b>(Vec2i(0, 0), 0)
                A shouldBe Vec4b(glm.convertLinearToSRGB(Vec4(1.0f, 0.0f, 0.0f, 1.0f)))
                val B = load<Vec4b>(Vec2i(1, 0), 0)
                B shouldBe Vec4b(glm.convertLinearToSRGB(Vec4(1.0f, 1.0f, 0.0, 1.0f)))
                val C = load<Vec4b>(Vec2i(1, 1), 0)
                C shouldBe Vec4b(glm.convertLinearToSRGB(Vec4(0.0f, 1.0f, 0.0f, 1.0f)))
                val D = load<Vec4b>(Vec2i(0, 1), 0)
                D shouldBe Vec4b(glm.convertLinearToSRGB(Vec4(0.0f, 0.0f, 1.0f, 1.0f)))
                val E = load<Vec4b>(Vec2i(0, 0), 1)
                E shouldBe Vec4b(glm.convertLinearToSRGB(Vec4(1.0f, 0.5f, 0.0f, 1.0f)))
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch bgr8 unorm" {

            val path = pathOf("bgr8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec3b(0, 0, 255))
                store(Vec2i(1, 0), 0, Vec3b(0, 255, 255))
                store(Vec2i(1, 1), 0, Vec3b(0, 255, 0))
                store(Vec2i(0, 1), 0, Vec3b(255, 0, 0))
                store(Vec2i(0, 0), 1, Vec3b(0, 128, 255))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec3b>(Vec2i(0, 0), 0)
                A shouldBe Vec3b(0, 0, 255)
                val B = load<Vec3b>(Vec2i(1, 0), 0)
                B shouldBe Vec3b(0, 255, 255)
                val C = load<Vec3b>(Vec2i(1, 1), 0)
                C shouldBe Vec3b(0, 255, 0)
                val D = load<Vec3b>(Vec2i(0, 1), 0)
                D shouldBe Vec3b(255, 0, 0)
                val E = load<Vec3b>(Vec2i(0, 0), 1)
                E shouldBe Vec3b(0, 128, 255)
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch bgra8 unorm" {

            val path = pathOf("bgra8_unorm_4pixels.dds")

            val textureA = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec4b(0, 0, 255, 255))
                store(Vec2i(1, 0), 0, Vec4b(0, 255, 255, 255))
                store(Vec2i(1, 1), 0, Vec4b(0, 255, 0, 255))
                store(Vec2i(0, 1), 0, Vec4b(255, 0, 0, 255))
                store(Vec2i(0, 0), 1, Vec4b(0, 128, 255, 255))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec4b>(Vec2i(0, 0), 0)
                A shouldBe Vec4b(0, 0, 255, 255)
                val B = load<Vec4b>(Vec2i(1, 0), 0)
                B shouldBe Vec4b(0, 255, 255, 255)
                val C = load<Vec4b>(Vec2i(1, 1), 0)
                C shouldBe Vec4b(0, 255, 0, 255)
                val D = load<Vec4b>(Vec2i(0, 1), 0)
                D shouldBe Vec4b(255, 0, 0, 255)
                val E = load<Vec4b>(Vec2i(0, 0), 1)
                E shouldBe Vec4b(0, 128, 255, 255)
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch rgba8u" {

            val path = pathOf("rgba8u_4pixels.dds")

            val textureA = Texture2d(Format.RGBA8_UINT_PACK8, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec4b(255, 0, 0, 255))
                store(Vec2i(1, 0), 0, Vec4b(255, 255, 0, 255))
                store(Vec2i(1, 1), 0, Vec4b(0, 255, 0, 255))
                store(Vec2i(0, 1), 0, Vec4b(0, 0, 255, 255))
                store(Vec2i(0, 0), 1, Vec4b(255, 128, 0, 255))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec4b>(Vec2i(0, 0), 0)
                A shouldBe Vec4b(255, 0, 0, 255)
                val B = load<Vec4b>(Vec2i(1, 0), 0)
                B shouldBe Vec4b(255, 255, 0, 255)
                val C = load<Vec4b>(Vec2i(1, 1), 0)
                C shouldBe Vec4b(0, 255, 0, 255)
                val D = load<Vec4b>(Vec2i(0, 1), 0)
                D shouldBe Vec4b(0, 0, 255, 255)
                val E = load<Vec4b>(Vec2i(0, 0), 1)
                E shouldBe Vec4b(255, 128, 0, 255)
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch rgba16f" {

            val path = pathOf("rgba16f_4pixels.dds")

            val textureA = Texture2d(Format.RGBA16_SFLOAT_PACK16, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, glm.packHalf4x16(Vec4(1.0f, 0.0f, 0.0f, 1.0f)))
                store(Vec2i(1, 0), 0, glm.packHalf4x16(Vec4(1.0f, 1.0f, 0.0f, 1.0f)))
                store(Vec2i(1, 1), 0, glm.packHalf4x16(Vec4(0.0f, 1.0f, 0.0f, 1.0f)))
                store(Vec2i(0, 1), 0, glm.packHalf4x16(Vec4(0.0f, 0.0f, 1.0f, 1.0f)))
                store(Vec2i(0, 0), 1, glm.packHalf4x16(Vec4(1.0f, 0.5f, 0.0f, 1.0f)))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Long>(Vec2i(0, 0), 0)
                A shouldBe glm.packHalf4x16(Vec4(1.0f, 0.0f, 0.0f, 1.0f))
                val B = load<Long>(Vec2i(1, 0), 0)
                B shouldBe glm.packHalf4x16(Vec4(1.0f, 1.0f, 0.0f, 1.0f))
                val C = load<Long>(Vec2i(1, 1), 0)
                C shouldBe glm.packHalf4x16(Vec4(0.0f, 1.0f, 0.0f, 1.0f))
                val D = load<Long>(Vec2i(0, 1), 0)
                D shouldBe glm.packHalf4x16(Vec4(0.0f, 0.0f, 1.0f, 1.0f))
                val E = load<Long>(Vec2i(0, 0), 1)
                E shouldBe glm.packHalf4x16(Vec4(1.0f, 0.5f, 0.0f, 1.0f))
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch rgb32f" {

            val path = pathOf("rgb32f_4pixels.dds")

            val textureA = Texture2d(Format.RGB32_SFLOAT_PACK32, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, Vec3(1.0f, 0.0f, 0.0f))
                store(Vec2i(1, 0), 0, Vec3(1.0f, 1.0f, 0.0f))
                store(Vec2i(1, 1), 0, Vec3(0.0f, 1.0f, 0.0f))
                store(Vec2i(0, 1), 0, Vec3(0.0f, 0.0f, 1.0f))
                store(Vec2i(0, 0), 1, Vec3(1.0f, 0.5f, 0.0f))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Vec3>(Vec2i(0, 0), 0)
                A shouldBe Vec3(1.0f, 0.0f, 0.0f)
                val B = load<Vec3>(Vec2i(1, 0), 0)
                B shouldBe Vec3(1.0f, 1.0f, 0.0f)
                val C = load<Vec3>(Vec2i(1, 1), 0)
                C shouldBe Vec3(0.0f, 1.0f, 0.0f)
                val D = load<Vec3>(Vec2i(0, 1), 0)
                D shouldBe Vec3(0.0f, 0.0f, 1.0f)
                val E = load<Vec3>(Vec2i(0, 0), 1)
                E shouldBe Vec3(1.0f, 0.5f, 0.0f)
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch rgb9e5" {

            val path = pathOf("rgb9e5_4pixels.dds")

            val textureA = Texture2d(Format.RGB9E5_UFLOAT_PACK32, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, 1)
                store(Vec2i(1, 0), 0, 3)
                store(Vec2i(1, 1), 0, 5)
                store(Vec2i(0, 1), 0, 7)
                store(Vec2i(0, 0), 1, 9)
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Int>(Vec2i(0, 0), 0)
                A shouldBe 1
                val B = load<Int>(Vec2i(1, 0), 0)
                B shouldBe 3
                val C = load<Int>(Vec2i(1, 1), 0)
                C shouldBe 5
                val D = load<Int>(Vec2i(0, 1), 0)
                D shouldBe 7
                val E = load<Int>(Vec2i(0, 0), 1)
                E shouldBe 9
            }

            textureA shouldBe textureB
            Files.delete(path)
        }

        "fetch rg11b10f" {

            val path = pathOf("rg11b10f_4pixels.dds")

            val textureA = Texture2d(Format.RG11B10_UFLOAT_PACK32, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, glm.packF2x11_1x10(Vec3(1.0f, 0.0f, 0.0f)))
                store(Vec2i(1, 0), 0, glm.packF2x11_1x10(Vec3(1.0f, 1.0f, 0.0f)))
                store(Vec2i(1, 1), 0, glm.packF2x11_1x10(Vec3(0.0f, 1.0f, 0.0f)))
                store(Vec2i(0, 1), 0, glm.packF2x11_1x10(Vec3(0.0f, 0.0f, 1.0f)))
                store(Vec2i(0, 0), 1, glm.packF2x11_1x10(Vec3(1.0f, 0.5f, 0.0f)))
                gli.saveDds(this, path)
            }

            val textureB = Texture2d(gli.loadDds(path)).apply {
                val A = load<Int>(Vec2i(0, 0), 0)
                A shouldBe glm.packF2x11_1x10(Vec3(1.0f, 0.0f, 0.0f))
                val B = load<Int>(Vec2i(1, 0), 0)
                B shouldBe glm.packF2x11_1x10(Vec3(1.0f, 1.0f, 0.0f))
                val C = load<Int>(Vec2i(1, 1), 0)
                C shouldBe glm.packF2x11_1x10(Vec3(0.0f, 1.0f, 0.0f))
                val D = load<Int>(Vec2i(0, 1), 0)
                D shouldBe glm.packF2x11_1x10(Vec3(0.0f, 0.0f, 1.0f))
                val E = load<Int>(Vec2i(0, 0), 1)
                E shouldBe glm.packF2x11_1x10(Vec3(1.0f, 0.5f, 0.0f))
            }
            val textureC = Texture2d(Format.RG11B10_UFLOAT_PACK32, Vec2i(2, 2)).apply {
                store(Vec2i(0, 0), 0, glm.packF2x11_1x10(Vec3(1.0f, 0.0f, 0.0f)))
                store(Vec2i(1, 0), 0, glm.packF2x11_1x10(Vec3(1.0f, 1.0f, 0.0f)))
                store(Vec2i(1, 1), 0, glm.packF2x11_1x10(Vec3(0.0f, 1.0f, 0.0f)))
                store(Vec2i(0, 1), 0, glm.packF2x11_1x10(Vec3(0.0f, 0.0f, 1.0f)))
                store(Vec2i(0, 0), 1, glm.packF2x11_1x10(Vec3(1.0f, 0.5f, 0.0f)))
            }

            textureA shouldBe textureB
            textureA shouldBe textureC
            Files.delete(path)
        }
    }
}
