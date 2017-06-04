package gli

import glm_.vec2.Vec2i
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 03.04.2017.
 */

class coreGL : StringSpec() {

    init {

        "ktx" {

            gli.gl.profile = gl.Profile.KTX

            val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatA = gl.translate(textureA.format, textureA.swizzles)

            with(formatA) {

                internal shouldBe gl.InternalFormat.RGB8_UNORM
                external shouldBe gl.ExternalFormat.RGB
                type shouldBe gl.TypeFormat.U8

                // Swizzle is not supported by KTX, so we always return the constant default swizzle
                swizzles shouldBe gl.Swizzles()
            }

            val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatB = gl.translate(textureB.format, textureB.swizzles)

            with(formatB) {

                internal shouldBe gl.InternalFormat.RGB8_UNORM
                external shouldBe gl.ExternalFormat.BGR
                type shouldBe gl.TypeFormat.U8

                // Swizzle is not supported by KTX, so we always return the constant default swizzle
                swizzles shouldBe gl.Swizzles()
            }
        }

        "gl32" {

            gli.gl.profile = gl.Profile.GL32

            val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatA = gl.translate(textureA.format, textureA.swizzles)

            with(formatA) {

                internal shouldBe gl.InternalFormat.RGB8_UNORM
                external shouldBe gl.ExternalFormat.RGB
                type shouldBe gl.TypeFormat.U8

                // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                swizzles shouldBe gl.Swizzles()
            }

            val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
            val formatB = gl.translate(textureB.format, textureB.swizzles)

            with(formatB) {

                internal shouldBe gl.InternalFormat.RGB8_UNORM
                external shouldBe gl.ExternalFormat.BGR
                type shouldBe gl.TypeFormat.U8

                // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                swizzles shouldBe gl.Swizzles()
            }
            run {
                val textureC = Texture2d(Format.R5G6B5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatC = gl.translate(textureC.format, textureC.swizzles)

                with(formatC) {

                    internal shouldBe gl.InternalFormat.R5G6B5
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.UINT16_R5G6B5_REV

                    // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                    swizzles shouldBe gl.Swizzles()

                    val formatD2 = gl.find(internal, external, type)
                    formatD2 shouldBe textureC.format
                }
            }

            run {
                val textureD = Texture2d(Format.B5G6R5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatD = gl.translate(textureD.format, textureD.swizzles)

                with(formatD) {

                    internal shouldBe gl.InternalFormat.R5G6B5
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.UINT16_R5G6B5

                    // Swizzle is not supported by OpenGL 3.2, so we always return the constant default swizzle
                    swizzles shouldBe gl.Swizzles()

                    val formatD2 = gl.find(internal, external, type)
                    formatD2 shouldBe textureD.format
                }
            }
        }

        "gl33" {

            gl.profile = gl.Profile.GL33

            run {
                val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatA = gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    internal shouldBe gl.InternalFormat.RGB8_UNORM
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.GREEN, gl.Swizzle.BLUE, gl.Swizzle.ONE)
                }
            }
            run {
                val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe gl.InternalFormat.RGB8_UNORM
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.BLUE, gl.Swizzle.GREEN, gl.Swizzle.RED, gl.Swizzle.ONE)
                }
            }
            run {
                val textureC = Texture2d(Format.R5G6B5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatC = gl.translate(textureC.format, textureC.swizzles)

                with(formatC) {
                    internal shouldBe gl.InternalFormat.R5G6B5
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.UINT16_R5G6B5_REV
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.GREEN, gl.Swizzle.BLUE, gl.Swizzle.ONE)

                    val formatD2 = gl.find(internal, external, type)
                    formatD2 shouldBe textureC.format
                }
            }
            run {
                val textureD = Texture2d(Format.B5G6R5_UNORM_PACK16, Vec2i(2), 1, Swizzles())
                val formatC = gl.translate(textureD.format, textureD.swizzles)

                with(formatC) {
                    internal shouldBe gl.InternalFormat.R5G6B5
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.UINT16_R5G6B5
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.GREEN, gl.Swizzle.BLUE, gl.Swizzle.ONE)

                    val formatD2 = gl.find(internal, external, type)
                    formatD2 shouldBe textureD.format
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val format = gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe gl.InternalFormat.RG8_UNORM
                    external shouldBe gl.ExternalFormat.RG
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.GREEN)
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ONE))
                val format = gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe gl.InternalFormat.RG8_UNORM
                    external shouldBe gl.ExternalFormat.RG
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.ONE)
                }
            }
        }

        "es20" {

            gl.profile = gl.Profile.ES20

            run {
                val textureA = Texture2d(Format.RGBA16_SFLOAT_PACK16, Vec2i(2), 1, Swizzles())
                val formatA = gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    // With OpenGL ES 2, the internal format is the external format so Internal is pretty much moot but
                    // this is the responsibility of GLI user.
                    internal shouldBe gl.InternalFormat.RGBA16F
                    external shouldBe gl.ExternalFormat.RGBA
                    type shouldBe gl.TypeFormat.F16_OES
                }
            }
            run {
                val textureB = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe gl.InternalFormat.BGRA8_UNORM
                    external shouldBe gl.ExternalFormat.BGRA
                    type shouldBe gl.TypeFormat.U8
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val format = gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe gl.InternalFormat.LUMINANCE8_ALPHA8
                    external shouldBe gl.ExternalFormat.LUMINANCE_ALPHA
                    type shouldBe gl.TypeFormat.U8
                }
            }
            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ONE))
                val formatB = gl.translate(texture.format, texture.swizzles)

                with(formatB) {
                    internal shouldBe gl.InternalFormat.LUMINANCE8_ALPHA8
                    external shouldBe gl.ExternalFormat.LUMINANCE_ALPHA
                    type shouldBe gl.TypeFormat.U8
                }
            }
        }

        "es30" {

            gl.profile = gl.Profile.ES30

            run {
                val textureA = Texture2d(Format.RGBA16_SFLOAT_PACK16, Vec2i(2), 1, Swizzles())
                val formatA = gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    // With OpenGL ES 2, the internal format is the external format so Internal is pretty much moot but
                    // this is the responsibility of GLI user.
                    internal shouldBe gl.InternalFormat.RGBA16F
                    external shouldBe gl.ExternalFormat.RGBA
                    type shouldBe gl.TypeFormat.F16
                    swizzles shouldBe gl.Swizzles()
                }
            }

            run {
                val textureA = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatA = gl.translate(textureA.format, textureA.swizzles)

                with(formatA) {
                    internal shouldBe gl.InternalFormat.RGB8_UNORM
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.GREEN, gl.Swizzle.BLUE, gl.Swizzle.ONE)
                }
            }

            run {
                val textureB = Texture2d(Format.BGR8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe gl.InternalFormat.RGB8_UNORM
                    external shouldBe gl.ExternalFormat.RGB
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.BLUE, gl.Swizzle.GREEN, gl.Swizzle.RED, gl.Swizzle.ONE)
                }
            }

            run {
                val textureB = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2), 1, Swizzles())
                val formatB = gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe gl.InternalFormat.RGBA8_UNORM
                    external shouldBe gl.ExternalFormat.RGBA
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.BLUE, gl.Swizzle.GREEN, gl.Swizzle.RED, gl.Swizzle.ALPHA)
                }
            }

            run {
                val textureB = Texture2d(Format.BGRA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.BLUE, Swizzle.GREEN, Swizzle.RED, Swizzle.ALPHA))
                val formatB = gl.translate(textureB.format, textureB.swizzles)

                with(formatB) {
                    internal shouldBe gl.InternalFormat.RGBA8_UNORM
                    external shouldBe gl.ExternalFormat.RGBA
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.GREEN, gl.Swizzle.BLUE, gl.Swizzle.ALPHA)
                }
            }

            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA))
                val format = gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe gl.InternalFormat.RG8_UNORM
                    external shouldBe gl.ExternalFormat.RG
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.GREEN)
                }
            }

            run {
                val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(2), 1, Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ONE))
                val format = gl.translate(texture.format, texture.swizzles)

                with(format) {
                    internal shouldBe gl.InternalFormat.RG8_UNORM
                    external shouldBe gl.ExternalFormat.RG
                    type shouldBe gl.TypeFormat.U8
                    swizzles shouldBe gl.Swizzles(gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.RED, gl.Swizzle.ONE)
                }
            }
        }
    }
}