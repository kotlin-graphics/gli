package gli_

import glm_.vec2.Vec2i
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreGL : StringSpec() {

    init {

        "ktx" {

            gli.gl.profile = Gl.Profile.KTX

            val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatA = Gl.translate(textureA.format, textureA.swizzles)

            with(formatA) {

                internal shouldBe Gl.InternalFormat.RGB8_UNORM
                external shouldBe Gl.ExternalFormat.RGB
                type shouldBe Gl.TypeFormat.U8

                // Swizzle is not supported by KTX, so we always return the constant default swizzle
                swizzles shouldBe Gl.Swizzles()
            }

            val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatB = Gl.translate(textureB.format, textureB.swizzles)

            with(formatB) {

                internal shouldBe Gl.InternalFormat.RGB8_UNORM
                external shouldBe Gl.ExternalFormat.BGR
                type shouldBe Gl.TypeFormat.U8

                // Swizzle is not supported by KTX, so we always return the constant default swizzle
                swizzles shouldBe Gl.Swizzles()
            }
        }

        "gl32" {

            gli.gl.profile = Gl.Profile.GL32

            val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatA = Gl.translate(textureA.format, textureA.swizzles)

            with(formatA) {

                internal shouldBe Gl.InternalFormat.RGB8_UNORM
                external shouldBe Gl.ExternalFormat.RGB
                type shouldBe Gl.TypeFormat.U8

                // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                swizzles shouldBe Gl.Swizzles()
            }

            val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatB = Gl.translate(textureB.format, textureB.swizzles)

            with(formatB) {

                internal shouldBe Gl.InternalFormat.RGB8_UNORM
                external shouldBe Gl.ExternalFormat.BGR
                type shouldBe Gl.TypeFormat.U8

                // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                swizzles shouldBe Gl.Swizzles()
            }
            run {
                val textureC = Texture2d(Format.R5G6B5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatC = Gl.translate(textureC.format, textureC.swizzles)

                with(formatC) {

                    internal shouldBe Gl.InternalFormat.R5G6B5
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.UINT16_R5G6B5_REV

                    // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                    swizzles shouldBe Gl.Swizzles()

                    val formatD2 = Gl.find(internal, external, type)
                    formatD2 shouldBe textureC.format
                }
            }

            run {
                val textureD = Texture2d(Format.B5G6R5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatD = Gl.translate(textureD.format, textureD.swizzles)

                with(formatD) {

                    internal shouldBe Gl.InternalFormat.R5G6B5
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.UINT16_R5G6B5

                    // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                    swizzles shouldBe Gl.Swizzles()

                    val formatD2 = Gl.find(internal, external, type)
                    formatD2 shouldBe textureD.format
                }
            }
        }

        "gl33" {

            Gl.profile = Gl.Profile.GL33

            run {
                val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatA = Gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    internal shouldBe Gl.InternalFormat.RGB8_UNORM
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.GREEN, Gl.Swizzle.BLUE, Gl.Swizzle.ONE)
                }
            }
            run {
                val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = Gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe Gl.InternalFormat.RGB8_UNORM
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.BLUE, Gl.Swizzle.GREEN, Gl.Swizzle.RED, Gl.Swizzle.ONE)
                }
            }
            run {
                val textureC = Texture2d(Format.R5G6B5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatC = Gl.translate(textureC.format, textureC.swizzles)

                with(formatC) {
                    internal shouldBe Gl.InternalFormat.R5G6B5
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.UINT16_R5G6B5_REV
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.GREEN, Gl.Swizzle.BLUE, Gl.Swizzle.ONE)

                    val formatD2 = Gl.find(internal, external, type)
                    formatD2 shouldBe textureC.format
                }
            }
            run {
                val textureD = Texture2d(Format.B5G6R5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatC = Gl.translate(textureD.format, textureD.swizzles)

                with(formatC) {
                    internal shouldBe Gl.InternalFormat.R5G6B5
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.UINT16_R5G6B5
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.GREEN, Gl.Swizzle.BLUE, Gl.Swizzle.ONE)

                    val formatD2 = Gl.find(internal, external, type)
                    formatD2 shouldBe textureD.format
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val format = Gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe Gl.InternalFormat.RG8_UNORM
                    external shouldBe Gl.ExternalFormat.RG
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.GREEN)
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ONE))
                val format = Gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe Gl.InternalFormat.RG8_UNORM
                    external shouldBe Gl.ExternalFormat.RG
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.ONE)
                }
            }
        }

        "es20" {

            Gl.profile = Gl.Profile.ES20

            run {
                val textureA = Texture2d(Format.RGBA16_SFLOAT_PACK16, Vec2i(2), 1, Swizzles())
                val formatA = Gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    // With OpenGL ES 2, the internal format is the external format so Internal is pretty much moot but
                    // this is the responsibility of GLI user.
                    internal shouldBe Gl.InternalFormat.RGBA16F
                    external shouldBe Gl.ExternalFormat.RGBA
                    type shouldBe Gl.TypeFormat.F16_OES
                }
            }
            run {
                val textureB = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = Gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe Gl.InternalFormat.BGRA8_UNORM
                    external shouldBe Gl.ExternalFormat.BGRA
                    type shouldBe Gl.TypeFormat.U8
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val format = Gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe Gl.InternalFormat.LUMINANCE8_ALPHA8
                    external shouldBe Gl.ExternalFormat.LUMINANCE_ALPHA
                    type shouldBe Gl.TypeFormat.U8
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ONE))
                val formatB = Gl.translate(texture.format, texture.swizzles)

                with(formatB) {
                    internal shouldBe Gl.InternalFormat.LUMINANCE8_ALPHA8
                    external shouldBe Gl.ExternalFormat.LUMINANCE_ALPHA
                    type shouldBe Gl.TypeFormat.U8
                }
            }
        }

        "es30" {

            Gl.profile = Gl.Profile.ES30

            run {
                val textureA = Texture2d(Format.RGBA16_SFLOAT_PACK16, Vec2i(2), 1, Swizzles())
                val formatA = Gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    // With OpenGL ES 2, the internal format is the external format so Internal is pretty much moot but
                    // this is the responsibility of GLI user.
                    internal shouldBe Gl.InternalFormat.RGBA16F
                    external shouldBe Gl.ExternalFormat.RGBA
                    type shouldBe Gl.TypeFormat.F16
                    swizzles shouldBe Gl.Swizzles()
                }
            }

            run {
                val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatA = Gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    internal shouldBe Gl.InternalFormat.RGB8_UNORM
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.GREEN, Gl.Swizzle.BLUE, Gl.Swizzle.ONE)
                }
            }

            run {
                val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = Gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe Gl.InternalFormat.RGB8_UNORM
                    external shouldBe Gl.ExternalFormat.RGB
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.BLUE, Gl.Swizzle.GREEN, Gl.Swizzle.RED, Gl.Swizzle.ONE)
                }
            }

            run {
                val textureB = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = Gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe Gl.InternalFormat.RGBA8_UNORM
                    external shouldBe Gl.ExternalFormat.RGBA
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.BLUE, Gl.Swizzle.GREEN, Gl.Swizzle.RED, Gl.Swizzle.ALPHA)
                }
            }

            run {
                val textureB = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.BLUE, Swizzle.GREEN, Swizzle.RED, Swizzle.ALPHA))
                val formatB = Gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe Gl.InternalFormat.RGBA8_UNORM
                    external shouldBe Gl.ExternalFormat.RGBA
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.GREEN, Gl.Swizzle.BLUE, Gl.Swizzle.ALPHA)
                }
            }

            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA))
                val format = Gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe Gl.InternalFormat.RG8_UNORM
                    external shouldBe Gl.ExternalFormat.RG
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.GREEN)
                }
            }

            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ONE))
                val format = Gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe Gl.InternalFormat.RG8_UNORM
                    external shouldBe Gl.ExternalFormat.RG
                    type shouldBe Gl.TypeFormat.U8
                    swizzles shouldBe Gl.Swizzles(Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.RED, Gl.Swizzle.ONE)
                }
            }
        }
    }
}