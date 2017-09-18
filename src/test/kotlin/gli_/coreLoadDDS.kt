//package gli_
//
//import io.kotlintest.specs.StringSpec
//
///**
// * Created by GBarbieri on 05.04.2017.
// */
//
//class coreLoadDDS : StringSpec() {
//
//    init {
//
//        "load dds" {
//
//            val params = arrayListOf(
//                    Pair("kueken7_bgrx8_unorm.dds", Format.BGR8_UNORM_PACK32),
//                    Pair("kueken7_rgba_dxt5_unorm1.dds", Format.RGBA_DXT5_UNORM_BLOCK16),
//                    Pair("kueken7_rgba_dxt5_unorm2.dds", Format.RGBA_DXT5_UNORM_BLOCK16),
//                    Pair("array_r8_uint.dds", Format.R8_UINT_PACK8),
//                    Pair("kueken7_rgba_astc4x4_srgb.dds", Format.RGBA_ASTC_4X4_SRGB_BLOCK16),
//                    Pair("kueken7_bgra8_srgb.dds", Format.BGRA8_SRGB_PACK8),
//                    Pair("kueken7_r16_unorm.dds", Format.R16_UINT_PACK16),
//                    Pair("kueken7_r8_sint.dds", Format.R8_SINT_PACK8),
//                    Pair("kueken7_r8_uint.dds", Format.R8_UINT_PACK8),
//                    Pair("kueken7_rgba4_unorm.dds", Format.BGRA4_UNORM_PACK16),
//                    Pair("kueken7_r5g6b5_unorm.dds", Format.B5G6R5_UNORM_PACK16),
//                    Pair("kueken7_rgb5a1_unorm.dds", Format.BGR5A1_UNORM_PACK16),
//                    Pair("kueken7_rgba_dxt1_unorm.dds", Format.RGBA_DXT1_UNORM_BLOCK8),
//                    Pair("kueken7_rgba_dxt1_srgb.dds", Format.RGBA_DXT1_SRGB_BLOCK8),
//                    Pair("kueken8_rgba_dxt1_unorm.dds", Format.RGBA_DXT1_UNORM_BLOCK8),
//                    Pair("kueken7_rgba_dxt5_unorm.dds", Format.RGBA_DXT5_UNORM_BLOCK16),
//                    Pair("kueken7_rgba_dxt5_srgb.dds", Format.RGBA_DXT5_SRGB_BLOCK16),
//                    Pair("kueken7_rgb_etc1_unorm.dds", Format.RGB_ETC_UNORM_BLOCK8),
//                    Pair("kueken7_rgb_atc_unorm.dds", Format.RGB_ATC_UNORM_BLOCK8),
//                    Pair("kueken7_rgba_atc_explicit_unorm.dds", Format.RGBA_ATCA_UNORM_BLOCK16),
//                    Pair("kueken7_rgba_atc_interpolate_unorm.dds", Format.RGBA_ATCI_UNORM_BLOCK16),
//                    Pair("kueken7_rgb_pvrtc_2bpp_unorm.dds", Format.RGB_PVRTC1_16X8_UNORM_BLOCK32),
//                    Pair("kueken7_rgb_pvrtc_4bpp_unorm.dds", Format.RGB_PVRTC1_8X8_UNORM_BLOCK32),
//                    Pair("kueken7_r_ati1n_unorm.dds", Format.R_ATI1N_UNORM_BLOCK8),
//                    Pair("kueken7_rg_ati2n_unorm.dds", Format.RG_ATI2N_UNORM_BLOCK16),
//                    Pair("kueken7_bgr8_unorm.dds", Format.BGR8_UNORM_PACK8),
//                    Pair("kueken7_rgba8_srgb.dds", Format.RGBA8_SRGB_PACK8),
//                    Pair("kueken7_bgra8_unorm.dds", Format.BGRA8_UNORM_PACK8),
//                    Pair("kueken7_a8_unorm.dds", Format.A8_UNORM_PACK8),
//                    Pair("kueken7_l8_unorm.dds", Format.L8_UNORM_PACK8),
//                    Pair("kueken7_rgb10a2_unorm.dds", Format.RGB10A2_UNORM_PACK32),
//                    Pair("kueken7_rgb10a2u.dds", Format.RGB10A2_UINT_PACK32),
//                    Pair("kueken7_rgba8_snorm.dds", Format.RGBA8_SNORM_PACK8),
//                    Pair("kueken7_rgba16_sfloat.dds", Format.RGBA16_SFLOAT_PACK16),
//                    Pair("kueken7_rg11b10_ufloat.dds", Format.RG11B10_UFLOAT_PACK32),
//                    Pair("kueken7_rgb9e5_ufloat.dds", Format.RGB9E5_UFLOAT_PACK32)
//            )
//
//            params.forEach { test(it) }
//        }
//
//
//    }
//
//    fun test(params: Pair<String, Format>) {
//
//        val textureA = gli.loadDDS(javaClass.getResource("/" + params.first).toURI())
//        assert(textureA.format == params.second)
//    }
//}