package gli_

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.nio.file.Files

class coreLoadKtx : StringSpec() {

    init {

        "load ktx" {

            val filenames = listOf(
                    "kueken7_rgba4_unorm.ktx",
                    "kueken7_r5g6b5_unorm.ktx",
                    "kueken7_rgb5a1_unorm.ktx",
                    "kueken7_rgb8_unorm.ktx",
                    "kueken7_rgba_dxt5_srgb.ktx",
                    "kueken7_rgb_dxt1_srgb.ktx",
                    "kueken7_rgba8_srgb.ktx",
                    "kueken7_rgb8_srgb.ktx",
                    "kueken7_rg11b10_ufloat.ktx",
                    "kueken7_rgb9e5_ufloat.ktx",
                    "kueken7_rgba_astc4x4_srgb.ktx",
                    "kueken7_rgba_astc8x5_srgb.ktx",
                    "kueken7_rgba_astc12x12_srgb.ktx",
                    "kueken7_rgb_etc1_unorm.ktx",
                    "kueken7_rgb_etc2_srgb.ktx",
                    "kueken7_rgba_etc2_srgb.ktx",
                    "kueken7_rgba_etc2_a1_srgb.ktx",
                    "kueken7_r_eac_snorm.ktx",
                    "kueken7_r_eac_unorm.ktx",
                    "kueken7_rg_eac_snorm.ktx",
                    "kueken7_rg_eac_unorm.ktx",
                    "kueken7_rgb_pvrtc_2bpp_srgb.ktx",
                    "kueken7_rgb_pvrtc_4bpp_srgb.ktx",
//                    "kueken7_rgba_pvrtc2_2bpp_unorm.ktx",
//                    "kueken7_rgba_pvrtc2_2bpp_srgb.ktx",
                    "kueken7_rgba_pvrtc2_4bpp_unorm.ktx",
                    "kueken7_rgba_pvrtc2_4bpp_srgb.ktx")

            filenames.forEach {

                val textureA = gli.loadKtx(uriOf(it))
                gli.saveKtx(textureA, it)
                val textureB = gli.loadKtx(it)
                Files.delete(pathOf(it))

                textureA shouldBe textureB
            }
        }
    }
}