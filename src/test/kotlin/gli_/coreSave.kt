package gli_

import glm_.vec1.Vec1b
import glm_.vec2.Vec2b
import glm_.vec2.Vec2i
import glm_.vec3.Vec3b
import glm_.vec4.Vec4b
import io.kotest.matchers.shouldBe
import io.kotest.core.spec.style.StringSpec
import java.nio.file.Files

class coreSave : StringSpec() {

    init {

        "l8 unorm" {

            val texture = Texture2d(Format.L8_UNORM_PACK8, Vec2i(4))
            texture clear Vec1b(127)

            val dds = "orange_l8_unorm.dds"
            gli.save(texture, dds)
            val textureL8unormDDS = gli.load(dds)
            Files.delete(pathOf(dds))

            val ktx = "orange_l8_unorm.ktx"
            gli.save(texture, ktx)
            val textureL8unormKTX = gli.load(ktx)
            Files.delete(pathOf(ktx))

            texture shouldBe textureL8unormDDS
            texture shouldBe textureL8unormKTX
            textureL8unormDDS shouldBe textureL8unormKTX
        }

        "la8 unorm" {

            val texture = Texture2d(Format.LA8_UNORM_PACK8, Vec2i(4))
            texture clear Vec2b(255, 127)

            val dds = "orange_la8_unorm.dds"
            gli.save(texture, dds)
            val textureLA8unormDDS = gli.load(dds)
            Files.delete(pathOf(dds))

            val ktx = "orange_la8_unorm.ktx"
            gli.save(texture, ktx)
            val textureLA8unormKTX = gli.load(ktx)
            Files.delete(pathOf(ktx))

            texture shouldBe textureLA8unormDDS
            texture shouldBe textureLA8unormKTX
            textureLA8unormDDS shouldBe textureLA8unormKTX
        }

        "rgba8 unorm" {

            val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4))
            texture clear Vec4b(255, 127, 0, 255)

            val dds = "orange_rgba8_unorm.dds"
            gli.save(texture, dds)
            val textureRGBA8unormDDS = gli.load(dds)
            Files.delete(pathOf(dds))

            val ktx = "orange_rgba8_unorm.ktx"
            gli.save(texture, ktx)
            val textureRGBA8unormKTX = gli.load(ktx)
            Files.delete(pathOf(ktx))

            texture shouldBe textureRGBA8unormDDS
            texture shouldBe textureRGBA8unormKTX
            textureRGBA8unormDDS shouldBe textureRGBA8unormKTX
        }

        "using ImageIO" {
            // without alpha
            run {
                val texture = Texture2d(Format.RGB8_UNORM_PACK8, Vec2i(4)) // no alpha, jpeg cant handle that
                texture clear Vec3b(255, 127, 255)

                // ico and bmp cant handle not 4 component images
                // icns and wbmp always fail
                val extensions = listOf(/*"bmp",*/ "gif", /*"ico",*/ /*"icns",*/ "iff", "jpeg", "jpg",
                        "pam", "pbm", "pct", "pgm", "pict", "png", "pnm", "ppm", "targa", "tga", "tif", "tiff"/*, "wbmp"*/)

                for (ext in extensions) {
                    val filename = "temp.$ext"
                    gli.save(texture, filename) shouldBe true

                    gli.load(filename)

                    Files.delete(pathOf(filename))
                }
            }
            // with alpha
            run {
                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, Vec2i(4))
                texture clear Vec4b(255, 127, 255, 255)

                // jpeg cant handle transparency,
                // icns, targa and wbmp always fail
                val extensions = listOf("bmp", "gif", "ico", /*"icns",*/ "iff", /*"jpeg", "jpg",*/
                        "pam", /*"pbm",*/ "pct", /*"pgm",*/ "pict", "png", /*"pnm", "ppm", "targa", "tga",*/ "tif", "tiff"/*, "wbmp"*/)

                for (ext in extensions) {
                    val filename = "temp.$ext"
                    gli.save(texture, filename) shouldBe true

                    gli.load(filename)

                    Files.delete(pathOf(filename))
                }
            }
        }
    }
}