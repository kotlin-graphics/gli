package gli

import gli.Swizzle.*
import gli.detail.Cap.*
import glm.BYTES
import glm.vec3.Vec3i
import glm.vec4.Vec4i
import java.nio.ByteBuffer

/**
 * Created by elect on 02/04/17.
 */

object detail {

    // ----- gl.inl -----

    fun translate(swizzles: gli.Swizzles) =
            gl.Swizzles(tableS[swizzles.r.i], tableS[swizzles.g.i], tableS[swizzles.b.i], tableS[swizzles.a.i])

    val FORMAT_PROPERTY_BGRA_FORMAT_BIT = 1 shl 0
    val FORMAT_PROPERTY_BGRA_TYPE_BIT = 1 shl 1

    // ----- format.inl ------

    enum class Cap {
        COMPRESSED_BIT,
        COLORSPACE_SRGB_BIT,
        NORMALIZED_BIT,
        SCALED_BIT,
        UNSIGNED_BIT,
        SIGNED_BIT,
        INTEGER_BIT,
        FLOAT_BIT,
        DEPTH_BIT,
        STENCIL_BIT,
        SWIZZLE_BIT,
        LUMINANCE_ALPHA_BIT,
        PACKED8_BIT,
        PACKED16_BIT,
        PACKED32_BIT,
        DDS_GLI_EXT_BIT;

        val i = 1 shl ordinal
    }

    class FormatInfo(val blockSize: Int, val blockExtend: Vec3i, val component: Int, val swizzles: Swizzles, val flags: Int)

    internal val tableF by lazy {

        val table = arrayOf(
                FormatInfo(1, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), PACKED8_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R4G4_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA4_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_BGRA4_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R5G6B5_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_B5G6R5_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB5A1_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_BGR5A1_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 4, Swizzles(ALPHA, RED, GREEN, BLUE), DDS_GLI_EXT_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_A1RGB5_UNORM,

                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_R8_UNORM,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_R8_SNORM,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R8_USCALED,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R8_SSCALED,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_R8_UINT,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_R8_SINT,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or COLORSPACE_SRGB_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R8_SRGB,

                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RG8_UNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_RG8_SNORM,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG8_USCALED,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG8_SSCALED,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RG8_UINT,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RG8_SINT,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i or COLORSPACE_SRGB_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG8_SRGB,

                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_UNORM,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_SNORM,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_USCALED,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_SSCALED,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_UINT,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_SINT,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or COLORSPACE_SRGB_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB8_SRGB,

                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_UNORM,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_SNORM,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_USCALED,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_SSCALED,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_UINT,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), INTEGER_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_SINT,
                FormatInfo(3, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or COLORSPACE_SRGB_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR8_SRGB,

                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA8_UNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA8_SNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_USCALED,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_SSCALED,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA8_UINT,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA8_SINT,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or COLORSPACE_SRGB_BIT.i), //FORMAT_RGBA8_SRGB,

                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i), //FORMAT_BGRA8_UNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGRA8_SNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGRA8_USCALED,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGRA8_SSCALED,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGRA8_UINT,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGRA8_SINT,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or COLORSPACE_SRGB_BIT.i or SWIZZLE_BIT.i), //FORMAT_BGRA8_SRGB,

                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_UNORM_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_SNORM_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_USCALED_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_SSCALED_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_UINT_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_SINT_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or COLORSPACE_SRGB_BIT.i or SWIZZLE_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA8_SRGB_PACK32,

                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or PACKED32_BIT.i), //FORMAT_RGB10A2_UNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or SIGNED_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB10A2_SNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB10A2_USCALE,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB10A2_SSCALE,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i or PACKED32_BIT.i), //FORMAT_RGB10A2_UINT,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i or PACKED32_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB10A2_SINT,

                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i or PACKED32_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR10A2_UNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), NORMALIZED_BIT.i or SIGNED_BIT.i or PACKED32_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR10A2_SNORM,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or PACKED32_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR10A2_USCALE,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or PACKED32_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR10A2_SSCALE,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i or PACKED32_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR10A2_UINT,
                FormatInfo(4, Vec3i(1, 1, 1), 4, Swizzles(BLUE, GREEN, RED, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i or PACKED32_BIT.i or SWIZZLE_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_BGR10A2_SINT,

                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_R16_UNORM_PACK16,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_R16_SNORM_PACK16,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R16_USCALE,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R16_SSCALE,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_R16_UINT_PACK16,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_R16_SINT_PACK16,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_R16_SFLOAT_PACK16,

                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RG16_UNORM_PACK16,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_RG16_SNORM_PACK16,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG16_USCALE,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG16_SSCALE,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RG16_UINT_PACK16,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RG16_SINT_PACK16,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RG16_SFLOAT_PACK16,

                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_UNORM_PACK16,
                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), NORMALIZED_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_SNORM_PACK16,
                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_USCALE,
                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_SSCALE,
                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_UINT_PACK16,
                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_SINT_PACK16,
                FormatInfo(6, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), FLOAT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB16_SFLOAT_PACK16,

                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA16_UNORM_PACK16,
                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA16_SNORM_PACK16,
                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA16_USCALE,
                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), DDS_GLI_EXT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA16_SSCALE,
                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA16_UINT_PACK16,
                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA16_SINT_PACK16,
                FormatInfo(8, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA16_SFLOAT_PACK16,

                FormatInfo(4, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_R32_UINT_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_R32_SINT_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_R32_SFLOAT_PACK32,

                FormatInfo(8, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RG32_UINT_PACK32,
                FormatInfo(8, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RG32_SINT_PACK32,
                FormatInfo(8, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RG32_SFLOAT_PACK32,

                FormatInfo(12, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGB32_UINT_PACK32,
                FormatInfo(12, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RGB32_SINT_PACK32,
                FormatInfo(12, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RGB32_SFLOAT_PACK32,

                FormatInfo(16, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA32_UINT_PACK32,
                FormatInfo(16, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA32_SINT_PACK32,
                FormatInfo(16, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RGBA32_SFLOAT_PACK32,

                FormatInfo(8, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R64_UINT_PACK64,
                FormatInfo(8, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R64_SINT_PACK64,
                FormatInfo(8, Vec3i(1, 1, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), FLOAT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R64_SFLOAT_PACK64,

                FormatInfo(16, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG64_UINT_PACK64,
                FormatInfo(16, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), INTEGER_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG64_SINT_PACK64,
                FormatInfo(16, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), FLOAT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG64_SFLOAT_PACK64,

                FormatInfo(24, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB64_UINT_PACK64,
                FormatInfo(24, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), INTEGER_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB64_SINT_PACK64,
                FormatInfo(24, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), FLOAT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB64_SFLOAT_PACK64,

                FormatInfo(32, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA64_UINT_PACK64,
                FormatInfo(32, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), INTEGER_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA64_SINT_PACK64,
                FormatInfo(32, Vec3i(1, 1, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), FLOAT_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA64_SFLOAT_PACK64,

                FormatInfo(4, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), PACKED32_BIT.i or FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RG11B10_UFLOAT_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), PACKED32_BIT.i or FLOAT_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGB9E5_UFLOAT_PACK32,

                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or INTEGER_BIT.i), //FORMAT_D16_UNORM_PACK16,
                FormatInfo(4, Vec3i(1, 1, 1), 1, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or INTEGER_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_D24_UNORM_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 1, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or FLOAT_BIT.i), //FORMAT_D32_UFLOAT_PACK32,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or STENCIL_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_S8_UNORM_PACK8,
                FormatInfo(3, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or INTEGER_BIT.i or STENCIL_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_D16_UNORM_S8_UINT_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or INTEGER_BIT.i or STENCIL_BIT.i), //FORMAT_D24_UNORM_S8_UINT_PACK32,
                FormatInfo(5, Vec3i(1, 1, 1), 2, Swizzles(RED, GREEN, BLUE, ALPHA), DEPTH_BIT.i or FLOAT_BIT.i or STENCIL_BIT.i), //FORMAT_D32_SFLOAT_S8_UINT_PACK64,

                FormatInfo(8, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_DXT1_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_DXT1_SRGB_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_DXT1_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_DXT1_SRGB_BLOCK8,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_DXT3_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_DXT3_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_DXT5_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_DXT5_SRGB_BLOCK16,
                FormatInfo(8, Vec3i(4, 4, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_R_ATI1N_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 1, Swizzles(RED, ZERO, ZERO, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_R_ATI1N_SNORM_BLOCK8,
                FormatInfo(16, Vec3i(4, 4, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RG_ATI2N_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 2, Swizzles(RED, GREEN, ZERO, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or SIGNED_BIT.i), //FORMAT_RG_ATI2N_SNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or FLOAT_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGB_BP_UFLOAT_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or FLOAT_BIT.i or SIGNED_BIT.i), //FORMAT_RGB_BP_SFLOAT_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_BP_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_BP_SRGB_BLOCK16,

                FormatInfo(8, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_ETC2_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_ETC2_SRGB_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_ETC2_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_ETC2_SRGB_BLOCK8,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_ETC2_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_ETC2_SRGB_BLOCK16,
                FormatInfo(8, Vec3i(4, 4, 1), 1, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R_EAC_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 1, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_R_EAC_SNORM_BLOCK8,
                FormatInfo(16, Vec3i(4, 4, 1), 2, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG_EAC_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 2, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or SIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RG_EAC_SNORM_BLOCK16,

                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_4X4_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_4X4_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(5, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_5X4_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(5, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_5X4_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(5, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_5X5_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(5, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_5X5_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(6, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_6X5_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(6, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_6X5_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(6, 6, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_6X6_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(6, 6, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_6X6_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(8, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_8X5_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(8, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_8X5_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(8, 6, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_8X6_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(8, 6, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_8X6_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(8, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_8X8_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(8, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_8X8_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(10, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X5_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(10, 5, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X5_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(10, 6, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X6_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(10, 6, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X6_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(10, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X8_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(10, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X8_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(10, 10, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X10_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(10, 10, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_10X10_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(12, 10, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_12X10_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(12, 10, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_12X10_SRGB_BLOCK16,
                FormatInfo(16, Vec3i(12, 12, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_12X12_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(12, 12, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i), //FORMAT_RGBA_ASTC_12X12_SRGB_BLOCK16,

                FormatInfo(32, Vec3i(8, 8, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_PVRTC1_8X8_UNORM_BLOCK32,
                FormatInfo(32, Vec3i(8, 8, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_PVRTC1_8X8_SRGB_BLOCK32,
                FormatInfo(32, Vec3i(16, 8, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_PVRTC1_16X8_UNORM_BLOCK32,
                FormatInfo(32, Vec3i(16, 8, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_PVRTC1_16X8_SRGB_BLOCK32,
                FormatInfo(32, Vec3i(8, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC1_8X8_UNORM_BLOCK32,
                FormatInfo(32, Vec3i(8, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC1_8X8_SRGB_BLOCK32,
                FormatInfo(32, Vec3i(16, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC1_16X8_UNORM_BLOCK32,
                FormatInfo(32, Vec3i(16, 8, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC1_16X8_SRGB_BLOCK32,
                FormatInfo(8, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC2_4X4_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC2_4X4_SRGB_BLOCK8,
                FormatInfo(8, Vec3i(8, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC2_8X4_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(8, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or COLORSPACE_SRGB_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_PVRTC2_8X4_SRGB_BLOCK8,

                FormatInfo(8, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_ETC_UNORM_BLOCK8,
                FormatInfo(8, Vec3i(4, 4, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGB_ATC_UNORM_BLOCK8,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_ATCA_UNORM_BLOCK16,
                FormatInfo(16, Vec3i(4, 4, 1), 4, Swizzles(RED, GREEN, BLUE, ALPHA), COMPRESSED_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_RGBA_ATCI_UNORM_BLOCK16,

                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(RED, RED, RED, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or LUMINANCE_ALPHA_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_L8_UNORM_PACK8,
                FormatInfo(1, Vec3i(1, 1, 1), 1, Swizzles(ZERO, ZERO, ZERO, RED), NORMALIZED_BIT.i or UNSIGNED_BIT.i or LUMINANCE_ALPHA_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_A8_UNORM_PACK8,
                FormatInfo(2, Vec3i(1, 1, 1), 2, Swizzles(RED, RED, RED, GREEN), NORMALIZED_BIT.i or UNSIGNED_BIT.i or LUMINANCE_ALPHA_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_LA8_UNORM_PACK8,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(RED, RED, RED, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or LUMINANCE_ALPHA_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_L16_UNORM_PACK16,
                FormatInfo(2, Vec3i(1, 1, 1), 1, Swizzles(ZERO, ZERO, ZERO, RED), NORMALIZED_BIT.i or UNSIGNED_BIT.i or LUMINANCE_ALPHA_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_A16_UNORM_PACK16,
                FormatInfo(4, Vec3i(1, 1, 1), 2, Swizzles(RED, RED, RED, GREEN), NORMALIZED_BIT.i or UNSIGNED_BIT.i or LUMINANCE_ALPHA_BIT.i or DDS_GLI_EXT_BIT.i), //FORMAT_LA16_UNORM_PACK16,

                FormatInfo(4, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i), //FORMAT_BGR8_UNORM_PACK32,
                FormatInfo(4, Vec3i(1, 1, 1), 3, Swizzles(BLUE, GREEN, RED, ONE), NORMALIZED_BIT.i or UNSIGNED_BIT.i or SWIZZLE_BIT.i or COLORSPACE_SRGB_BIT.i), //FORMAT_BGR8_SRGB_PACK32,

                FormatInfo(1, Vec3i(1, 1, 1), 3, Swizzles(RED, GREEN, BLUE, ONE), PACKED8_BIT.i or NORMALIZED_BIT.i or UNSIGNED_BIT.i or DDS_GLI_EXT_BIT.i)                                        //FORMAT_RG3B2_UNORM_PACK8,
        )

        assert(table.size == FORMAT_COUNT, { "GLI error: format descriptor list doesn't match number of supported formats" })

        table
    }

    internal val tableS by lazy {

        val table = arrayOf(gl.Swizzle.RED, gl.Swizzle.GREEN, gl.Swizzle.BLUE, gl.Swizzle.ALPHA, gl.Swizzle.ZERO, gl.Swizzle.ONE)

        assert(table.size == SWIZZLE_COUNT, { "GLI error: swizzle descriptor list doesn't match number of supported swizzles" })

        table
    }

    // ----- load_dds.inl ------

    val FOURCC_DDS = charArrayOf('D', 'D', 'S', ' ')

    enum class DdsCubemapFlag(val i: Int) {

        CUBEMAP(0x00000200),
        CUBEMAP_POSITIVEX(0x00000400),
        CUBEMAP_NEGATIVEX(0x00000800),
        CUBEMAP_POSITIVEY(0x00001000),
        CUBEMAP_NEGATIVEY(0x00002000),
        CUBEMAP_POSITIVEZ(0x00004000),
        CUBEMAP_NEGATIVEZ(0x00008000),
        VOLUME(0x00200000),
        CUBEMAP_ALLFACES(CUBEMAP_POSITIVEX.i or CUBEMAP_NEGATIVEX.i or CUBEMAP_POSITIVEY.i or CUBEMAP_NEGATIVEY.i or
                CUBEMAP_POSITIVEZ.i or CUBEMAP_NEGATIVEZ.i)
    }

    enum class DdsFlag(val i: Int) {
        CAPS(0x00000001),
        HEIGHT(0x00000002),
        WIDTH(0x00000004),
        PITCH(0x00000008),
        PIXELFORMAT(0x00001000),
        MIPMAPCOUNT(0x00020000),
        LINEARSIZE(0x00080000),
        DEPTH(0x00800000)
    }

    enum class DdsSurfaceFlag(val i: Int) {

        COMPLEX(0x00000008),
        MIPMAP(0x00400000),
        TEXTURE(0x00001000)
    }

    class DdsPixelFormat(data: ByteBuffer) {

        var size = data.int
        var flags = data.int
        var fourCC = data.int
        var bpp = data.int
        var mask = Vec4i(data.int, data.int, data.int, data.int)

        companion object {
            val SIZE = 4 * Int.BYTES + Vec4i.size
        }
    }

    class DdsHeader(data: ByteBuffer) {

        val size = data.int
        val flags = data.int
        val height = data.int
        val width = data.int
        val pitch = data.int
        val depth = data.int
        val mipMapLevels = data.int
        val reserved = (0 until 11).map { data.int }
        val format = DdsPixelFormat(data)
        val surfaceFlags = data.int
        val cubemapFlags = data.int
        val reserved2 = (0 until 3).map { data.int }

        companion object {
            val SIZE = (7 + 11 + 2 + 3) * Int.BYTES + DdsPixelFormat.SIZE
        }

        init {
            assert(SIZE == 124, { "DDS Header size mismatch" })
        }
    }

    enum class D3d10resourceDimension {
        UNKNOWN,
        BUFFER,
        TEXTURE1D,
        TEXTURE2D,
        TEXTURE3D;

        val i = ordinal
    }

    enum class D3d10resourceMiscFlag(val i: Int) {
        GENERATE_MIPS(0x01),
        SHARED(0x02),
        TEXTURECUBE(0x04),
        SHARED_KEYEDMUTEX(0x10),
        GDI_COMPATIBLE(0x20)
    }

    enum class DdsAlphaMode {
        UNKNOWN,
        STRAIGHT,
        PREMULTIPLIED,
        OPAQUE,
        CUSTOM;

        val i = ordinal
    }

    class DdsHeader10(
            var format: Int,
            var resourceDimension: Int,
            var miscFlag: Int, // D3D10_RESOURCE_MISC_GENERATE_MIPS
            var arraySize: Int,
            var alphaFlags: Int)  // Should be 0 whenever possible to avoid D3D utility library to fail
    {

        constructor() : this(
                dx.Dxgi_format_dds.UNKNOWN.i,
                D3d10resourceDimension.UNKNOWN.i,
                0,
                0,
                DdsAlphaMode.UNKNOWN.i)

        constructor(data: ByteBuffer) : this(data.int, data.int, data.int, data.int, data.int)
    }

    /** Some formats have multiple fourcc values. This function allows remapping to the default fourcc value of a format    */
    fun remapFourCC(fourCC: Int) = when (fourCC) {
        dx.D3dfmt.BC4U.i -> dx.D3dfmt.ATI1
        dx.D3dfmt.BC4S.i -> dx.D3dfmt.AT1N
        dx.D3dfmt.BC5U.i -> dx.D3dfmt.ATI2
        dx.D3dfmt.BC5S.i -> dx.D3dfmt.AT2N
        else -> dx.D3dfmt.of(fourCC)
    }
}