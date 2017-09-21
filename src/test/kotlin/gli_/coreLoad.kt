package gli_

import io.kotlintest.specs.StringSpec
import java.io.File
import java.net.URI
import java.nio.file.Files

class coreLoad : StringSpec() {

    init {

        "load" {

            fun uri(filename: String, extension: String) = ClassLoader.getSystemResource("$filename$extension").toURI()

            fun loadFileKtx(param: Pair<String, Format>) {

                val a = uri(param.first, ".ktx")

                val textureKTX = gli.load(uri(param.first, ".ktx"))
//                Error += textureKTX.format() == Params.Format ? 0 : 1;
//
//                gli::save(TextureKTX, Params.Filename + ".dds");
//                gli::texture TextureSavedDDS (gli::load(Params.Filename + ".dds"));
//                Error += TextureSavedDDS.format() == Params.Format ? 0 : 1;
//                Error += TextureSavedDDS == textureKTX ? 0 : 1;
//
//                gli::save(TextureKTX, Params.Filename + ".ktx");
//                gli::texture TextureSavedKTX (gli::load(Params.Filename + ".ktx"));
//                Error += TextureSavedKTX.format() == Params.Format ? 0 : 1;
//                Error += TextureSavedDDS == TextureSavedKTX ? 0 : 1;

            }

            val params = listOf(
                    "array_r8_uint" to Format.R8_UINT_PACK8,
                    "kueken7_rgba8_unorm" to Format.RGBA8_UNORM_PACK8,
                    "kueken7_rgba8_srgb" to Format.RGBA8_SRGB_PACK8,
                    "kueken7_bgra8_unorm" to Format.BGRA8_UNORM_PACK8,
                    "kueken7_bgra8_srgb" to Format.BGRA8_SRGB_PACK8,
                    "kueken7_r5g6b5_unorm" to Format.B5G6R5_UNORM_PACK16,
                    "kueken7_rgba4_unorm" to Format.BGRA4_UNORM_PACK16,
                    "kueken7_rgb5a1_unorm" to Format.BGR5A1_UNORM_PACK16,
                    "kueken8_rgba8_srgb" to Format.RGBA8_SRGB_PACK8,
                    "kueken7_rgba_dxt5_unorm" to Format.RGBA_DXT5_UNORM_BLOCK16)

            params.forEach {

                loadFileKtx(it)
            }
        }
    }
}