package gli_

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoad : StringSpec() {

    init {

        "load" {

            class Params(val filename: String, val format: Format)

            fun uri(filename: String) = ClassLoader.getSystemResource(filename).toURI()

            fun loadFileKtx(params: Params) {

                val ktx = params.filename + ".ktx"
                val textureKTX = gli.load(uri(ktx))
                textureKTX.format shouldBe params.format

                val dds = params.filename + ".dds"
                gli.save(textureKTX, dds)
                val textureSavedDDS = gli.load(dds)
                textureSavedDDS.format shouldBe params.format
                textureSavedDDS shouldBe textureKTX
                Files.delete(pathOf(dds))

                gli.save(textureKTX, ktx)
                val textureSavedKTX = gli.load(ktx)
                textureSavedKTX.format shouldBe params.format
                textureSavedDDS shouldBe textureSavedKTX
                Files.delete(pathOf(ktx))
            }

            fun loadFileKmg(params: Params) {

                val ktx = params.filename + ".ktx"
                val textureKTX = gli.load(uri(ktx))
                textureKTX.format shouldBe params.format

                val kmg = params.filename + ".kmg"
                gli.save(textureKTX, kmg)
                val textureSavedKMG = gli.load(kmg)
                textureSavedKMG.format shouldBe params.format
                textureSavedKMG shouldBe textureKTX
                Files.delete(pathOf(kmg))

                gli.save(textureKTX, ktx)
                val textureSavedKTX = gli.load(ktx)
                textureSavedKTX.format shouldBe params.format
                textureSavedKTX shouldBe textureKTX
                Files.delete(pathOf(ktx))
            }

            fun loaFileDds(params: Params) {

                val dds = params.filename + ".dds"
                val textureDDS = gli.load(uri(dds))
                textureDDS.format == params.format

                val kmg = params.filename + ".kmg"
                gli.save(textureDDS, kmg)
                val textureSavedKMG = gli.load(kmg)
                Files.delete(pathOf(kmg))

                textureSavedKMG.format shouldBe params.format
                textureSavedKMG shouldBe textureDDS

                gli.save(textureDDS, dds)
                val textureSavedDDS = gli.load(dds)
                Files.delete(pathOf(dds))

                textureSavedDDS.format shouldBe params.format
                textureSavedDDS shouldBe textureDDS
            }

            val params = listOf(
                    Params("array_r8_uint", Format.R8_UINT_PACK8),
                    Params("kueken7_rgba8_unorm", Format.RGBA8_UNORM_PACK8),
                    Params("kueken7_rgba8_srgb", Format.RGBA8_SRGB_PACK8),
                    Params("kueken7_bgra8_unorm", Format.BGRA8_UNORM_PACK8),
                    Params("kueken7_bgra8_srgb", Format.BGRA8_SRGB_PACK8),
                    Params("kueken7_r5g6b5_unorm", Format.B5G6R5_UNORM_PACK16),
                    Params("kueken7_rgba4_unorm", Format.BGRA4_UNORM_PACK16),
                    Params("kueken7_rgb5a1_unorm", Format.BGR5A1_UNORM_PACK16),
                    Params("kueken8_rgba8_srgb", Format.RGBA8_SRGB_PACK8),
                    Params("kueken7_rgba_dxt5_unorm", Format.RGBA_DXT5_UNORM_BLOCK16))

            params.forEach {
                loadFileKtx(it)
                loadFileKmg(it)
            }
        }
    }
}