package gli_

import gli_.detail.FORMAT_PROPERTY_BGRA_FORMAT_BIT
import gli_.detail.FORMAT_PROPERTY_BGRA_TYPE_BIT
import gli_.gl.ExternalFormat.*
import gli_.gl.InternalFormat.*
import gli_.gl.TypeFormat.*
import glm_.has
import java.nio.ByteBuffer
import java.nio.IntBuffer
import gli_.gl.ExternalFormat.NONE as NONE_

/**
 * Created by elect on 02/04/17.
 */


/**  Translation class to convert GLI enums into OpenGL values  */
object gl {

    var profile = Profile.GL33
        set(value) {
            field = value
            updateTable()
        }

    init {
        updateTable()
    }

    fun translate(texture: Texture): Pair<Target, Format> = translate(texture.target) to translate(texture.format, texture.swizzles)

    fun translate(target: gli_.Target): Target = tableTargets[target.i]

    fun translate(format: gli_.Format, swizzles: gli_.Swizzles): Format {

        assert(format.isValid)

        val formatDesc = tableFormatDesc[format - gli_.Format.FIRST]

        return Format(formatDesc.internal, formatDesc.external, formatDesc.type, computeSwizzle(formatDesc, swizzles))
    }

    private fun computeSwizzle(formatDesc: FormatDesc, swizzles: gli_.Swizzles): Swizzles {

        if (!profile.hasSwizzle)
            return Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA)

        val isExternalBGRA = ((formatDesc.properties has FORMAT_PROPERTY_BGRA_FORMAT_BIT) && !profile.hasSwizzle)
                || (formatDesc.properties has FORMAT_PROPERTY_BGRA_TYPE_BIT)

        return detail.translate(if (isExternalBGRA) gli_.Swizzles(swizzles.b, swizzles.g, swizzles.r, swizzles.a) else swizzles)
    }

    fun find(internalFormat: InternalFormat, externalFormat: ExternalFormat, type: TypeFormat): gli_.Format {
        /*  Important, filter internalFormat by i, because some values may be double, such as:
            Internal.LUMINANCE8 has same value of Internal.L8   */
        val format = tableFormatDesc.find { it.internal.i == internalFormat.i && it.external == externalFormat && it.type == type }
        return when (format) {
            null -> gli_.Format.UNDEFINED
            else -> gli_.Format.of(tableFormatDesc.indexOf(format) + gli_.Format.FIRST.i) // +FIRST to offset UNDEFINED
        }
    }

    private val tableTargets: Array<Target> by lazy {
        arrayOf(
                Target._1D,
                Target._1D_ARRAY,
                Target._2D,
                Target._2D_ARRAY,
                Target._3D,
                Target.RECT,
                Target.RECT_ARRAY,
                Target.CUBE,
                Target.CUBE_ARRAY)
                .apply {
                    assert(size == gli_.Target.COUNT) { "GLI error: target descriptor list doesn't match number of supported targets" }
                }
    }

    private class FormatDesc(val internal: InternalFormat, val external: ExternalFormat, val type: TypeFormat, val properties: Int)

    enum class InternalFormat(val i: Int) {

        RGB_UNORM(0x1907), //GL_RGB
        BGR_UNORM(0x80E0), //GL_BGR
        RGBA_UNORM(0x1908), //GL_RGBA
        BGRA_UNORM(0x80E1), //GL_BGRA
        BGRA8_UNORM(0x93A1), //GL_BGRA8_EXT

        // unorm formats
        R8_UNORM(0x8229), //GL_R8
        RG8_UNORM(0x822B), //GL_RG8
        RGB8_UNORM(0x8051), //GL_RGB8
        RGBA8_UNORM(0x8058), //GL_RGBA8

        R16_UNORM(0x822A), //GL_R16
        RG16_UNORM(0x822C), //GL_RG16
        RGB16_UNORM(0x8054), //GL_RGB16
        RGBA16_UNORM(0x805B), //GL_RGBA16

        RGB10A2_UNORM(0x8059), //GL_RGB10_A2
        RGB10A2_SNORM_EXT(0xFFFC),

        // snorm formats
        R8_SNORM(0x8F94), //GL_R8_SNORM
        RG8_SNORM(0x8F95), //GL_RG8_SNORM
        RGB8_SNORM(0x8F96), //GL_RGB8_SNORM
        RGBA8_SNORM(0x8F97), //GL_RGBA8_SNORM

        R16_SNORM(0x8F98), //GL_R16_SNORM
        RG16_SNORM(0x8F99), //GL_RG16_SNORM
        RGB16_SNORM(0x8F9A), //GL_RGB16_SNORM
        RGBA16_SNORM(0x8F9B), //GL_RGBA16_SNORM

        // unsigned integer formats
        R8U(0x8232), //GL_R8UI
        RG8U(0x8238), //GL_RG8UI
        RGB8U(0x8D7D), //GL_RGB8UI
        RGBA8U(0x8D7C), //GL_RGBA8UI

        R16U(0x8234), //GL_R16UI
        RG16U(0x823A), //GL_RG16UI
        RGB16U(0x8D77), //GL_RGB16UI
        RGBA16U(0x8D76), //GL_RGBA16UI

        R32U(0x8236), //GL_R32UI
        RG32U(0x823C), //GL_RG32UI
        RGB32U(0x8D71), //GL_RGB32UI
        RGBA32U(0x8D70), //GL_RGBA32UI

        RGB10A2U(0x906F), //GL_RGB10_A2UI
        RGB10A2I_EXT(0xFFFB),

        // signed integer formats
        R8I(0x8231), //GL_R8I
        RG8I(0x8237), //GL_RG8I
        RGB8I(0x8D8F), //GL_RGB8I
        RGBA8I(0x8D8E), //GL_RGBA8I

        R16I(0x8233), //GL_R16I
        RG16I(0x8239), //GL_RG16I
        RGB16I(0x8D89), //GL_RGB16I
        RGBA16I(0x8D88), //GL_RGBA16I

        R32I(0x8235), //GL_R32I
        RG32I(0x823B), //GL_RG32I
        RGB32I(0x8D83), //GL_RGB32I
        RGBA32I(0x8D82), //GL_RGBA32I

        // Floating formats
        R16F(0x822D), //GL_R16F
        RG16F(0x822F), //GL_RG16F
        RGB16F(0x881B), //GL_RGB16F
        RGBA16F(0x881A), //GL_RGBA16F

        R32F(0x822E), //GL_R32F
        RG32F(0x8230), //GL_RG32F
        RGB32F(0x8815), //GL_RGB32F
        RGBA32F(0x8814), //GL_RGBA32F

        R64F_EXT(0xFFFA), //GL_R64F
        RG64F_EXT(0xFFF9), //GL_RG64F
        RGB64F_EXT(0xFFF8), //GL_RGB64F
        RGBA64F_EXT(0xFFF7), //GL_RGBA64F

        // sRGB formats
        SR8(0x8FBD), //GL_SR8_EXT
        SRG8(0x8FBE), //GL_SRG8_EXT
        SRGB8(0x8C41), //GL_SRGB8
        SRGB8_ALPHA8(0x8C43), //GL_SRGB8_ALPHA8

        // Packed formats
        RGB9E5(0x8C3D), //GL_RGB9_E5
        RG11B10F(0x8C3A), //GL_R11F_G11F_B10F
        RG3B2(0x2A10), //GL_R3_G3_B2
        R5G6B5(0x8D62), //GL_RGB565
        RGB5A1(0x8057), //GL_RGB5_A1
        RGBA4(0x8056), //GL_RGBA4

        RG4_EXT(0xFFFE),

        // Luminance Alpha formats
        LA4(0x8043), //GL_LUMINANCE4_ALPHA4
        L8(0x8040), //GL_LUMINANCE8
        A8(0x803C), //GL_ALPHA8
        LA8(0x8045), //GL_LUMINANCE8_ALPHA8
        L16(0x8042), //GL_LUMINANCE16
        A16(0x803E), //GL_ALPHA16
        LA16(0x8048), //GL_LUMINANCE16_ALPHA16

        // Depth formats
        D16(0x81A5), //GL_DEPTH_COMPONENT16
        D24(0x81A6), //GL_DEPTH_COMPONENT24
        D16S8_EXT(0xFFF6),
        D24S8(0x88F0), //GL_DEPTH24_STENCIL8
        D32(0x81A7), //GL_DEPTH_COMPONENT32
        D32F(0x8CAC), //GL_DEPTH_COMPONENT32F
        D32FS8X24(0x8CAD), //GL_DEPTH32F_STENCIL8
        S8_EXT(0x8D48), //GL_STENCIL_INDEX8

        // Compressed formats
        RGB_DXT1(0x83F0), //GL_COMPRESSED_RGB_S3TC_DXT1_EXT
        RGBA_DXT1(0x83F1), //GL_COMPRESSED_RGBA_S3TC_DXT1_EXT
        RGBA_DXT3(0x83F2), //GL_COMPRESSED_RGBA_S3TC_DXT3_EXT
        RGBA_DXT5(0x83F3), //GL_COMPRESSED_RGBA_S3TC_DXT5_EXT
        R_ATI1N_UNORM(0x8DBB), //GL_COMPRESSED_RED_RGTC1
        R_ATI1N_SNORM(0x8DBC), //GL_COMPRESSED_SIGNED_RED_RGTC1
        RG_ATI2N_UNORM(0x8DBD), //GL_COMPRESSED_RG_RGTC2
        RG_ATI2N_SNORM(0x8DBE), //GL_COMPRESSED_SIGNED_RG_RGTC2
        RGB_BP_UNSIGNED_FLOAT(0x8E8F), //GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT
        RGB_BP_SIGNED_FLOAT(0x8E8E), //GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT
        RGB_BP_UNORM(0x8E8C), //GL_COMPRESSED_RGBA_BPTC_UNORM
        RGB_PVRTC_4BPPV1(0x8C00), //GL_COMPRESSED_RGB_PVRTC_4BPPV1_IMG
        RGB_PVRTC_2BPPV1(0x8C01), //GL_COMPRESSED_RGB_PVRTC_2BPPV1_IMG
        RGBA_PVRTC_4BPPV1(0x8C02), //GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG
        RGBA_PVRTC_2BPPV1(0x8C03), //GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG
        RGBA_PVRTC_4BPPV2(0x9137), //GL_COMPRESSED_RGBA_PVRTC_4BPPV2_IMG
        RGBA_PVRTC_2BPPV2(0x9138), //GL_COMPRESSED_RGBA_PVRTC_2BPPV2_IMG
        ATC_RGB(0x8C92), //GL_ATC_RGB_AMD
        ATC_RGBA_EXPLICIT_ALPHA(0x8C93), //GL_ATC_RGBA_EXPLICIT_ALPHA_AMD
        ATC_RGBA_INTERPOLATED_ALPHA(0x87EE), //GL_ATC_RGBA_INTERPOLATED_ALPHA_AMD

        RGB_ETC(0x8D64), //GL_COMPRESSED_RGB8_ETC1
        RGB_ETC2(0x9274), //GL_COMPRESSED_RGB8_ETC2
        RGBA_PUNCHTHROUGH_ETC2(0x9276), //GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2
        RGBA_ETC2(0x9278), //GL_COMPRESSED_RGBA8_ETC2_EAC
        R11_EAC(0x9270), //GL_COMPRESSED_R11_EAC
        SIGNED_R11_EAC(0x9271), //GL_COMPRESSED_SIGNED_R11_EAC
        RG11_EAC(0x9272), //GL_COMPRESSED_RG11_EAC
        SIGNED_RG11_EAC(0x9273), //GL_COMPRESSED_SIGNED_RG11_EAC

        RGBA_ASTC_4x4(0x93B0), //GL_COMPRESSED_RGBA_ASTC_4x4_KHR
        RGBA_ASTC_5x4(0x93B1), //GL_COMPRESSED_RGBA_ASTC_5x4_KHR
        RGBA_ASTC_5x5(0x93B2), //GL_COMPRESSED_RGBA_ASTC_5x5_KHR
        RGBA_ASTC_6x5(0x93B3), //GL_COMPRESSED_RGBA_ASTC_6x5_KHR
        RGBA_ASTC_6x6(0x93B4), //GL_COMPRESSED_RGBA_ASTC_6x6_KHR
        RGBA_ASTC_8x5(0x93B5), //GL_COMPRESSED_RGBA_ASTC_8x5_KHR
        RGBA_ASTC_8x6(0x93B6), //GL_COMPRESSED_RGBA_ASTC_8x6_KHR
        RGBA_ASTC_8x8(0x93B7), //GL_COMPRESSED_RGBA_ASTC_8x8_KHR
        RGBA_ASTC_10x5(0x93B8), //GL_COMPRESSED_RGBA_ASTC_10x5_KHR
        RGBA_ASTC_10x6(0x93B9), //GL_COMPRESSED_RGBA_ASTC_10x6_KHR
        RGBA_ASTC_10x8(0x93BA), //GL_COMPRESSED_RGBA_ASTC_10x8_KHR
        RGBA_ASTC_10x10(0x93BB), //GL_COMPRESSED_RGBA_ASTC_10x10_KHR
        RGBA_ASTC_12x10(0x93BC), //GL_COMPRESSED_RGBA_ASTC_12x10_KHR
        RGBA_ASTC_12x12(0x93BD), //GL_COMPRESSED_RGBA_ASTC_12x12_KHR

        // sRGB formats
        SRGB_DXT1(0x8C4C), //GL_COMPRESSED_SRGB_S3TC_DXT1_EXT
        SRGB_ALPHA_DXT1(0x8C4D), //GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT1_EXT
        SRGB_ALPHA_DXT3(0x8C4E), //GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT3_EXT
        SRGB_ALPHA_DXT5(0x8C4F), //GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT5_EXT
        SRGB_BP_UNORM(0x8E8D), //GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM
        SRGB_PVRTC_2BPPV1(0x8A54), //GL_COMPRESSED_SRGB_PVRTC_2BPPV1_EXT
        SRGB_PVRTC_4BPPV1(0x8A55), //GL_COMPRESSED_SRGB_PVRTC_4BPPV1_EXT
        SRGB_ALPHA_PVRTC_2BPPV1(0x8A56), //GL_COMPRESSED_SRGB_ALPHA_PVRTC_2BPPV1_EXT
        SRGB_ALPHA_PVRTC_4BPPV1(0x8A57), //GL_COMPRESSED_SRGB_ALPHA_PVRTC_4BPPV1_EXT
        SRGB_ALPHA_PVRTC_2BPPV2(0x93F0), //COMPRESSED_SRGB_ALPHA_PVRTC_2BPPV2_IMG
        SRGB_ALPHA_PVRTC_4BPPV2(0x93F1), //GL_COMPRESSED_SRGB_ALPHA_PVRTC_4BPPV2_IMG
        SRGB8_ETC2(0x9275), //GL_COMPRESSED_SRGB8_ETC2
        SRGB8_PUNCHTHROUGH_ALPHA1_ETC2(0x9277), //GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2
        SRGB8_ALPHA8_ETC2_EAC(0x9279), //GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC
        SRGB8_ALPHA8_ASTC_4x4(0x93D0), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4_KHR
        SRGB8_ALPHA8_ASTC_5x4(0x93D1), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4_KHR
        SRGB8_ALPHA8_ASTC_5x5(0x93D2), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5_KHR
        SRGB8_ALPHA8_ASTC_6x5(0x93D3), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5_KHR
        SRGB8_ALPHA8_ASTC_6x6(0x93D4), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6_KHR
        SRGB8_ALPHA8_ASTC_8x5(0x93D5), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5_KHR
        SRGB8_ALPHA8_ASTC_8x6(0x93D6), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6_KHR
        SRGB8_ALPHA8_ASTC_8x8(0x93D7), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8_KHR
        SRGB8_ALPHA8_ASTC_10x5(0x93D8), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5_KHR
        SRGB8_ALPHA8_ASTC_10x6(0x93D9), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6_KHR
        SRGB8_ALPHA8_ASTC_10x8(0x93DA), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR
        SRGB8_ALPHA8_ASTC_10x10(0x93DB), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10_KHR
        SRGB8_ALPHA8_ASTC_12x10(0x93DC), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10_KHR
        SRGB8_ALPHA8_ASTC_12x12(0x93DD), //GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12_KHR

        ALPHA8(0x803C),
        ALPHA16(0x803E),
        LUMINANCE8(0x8040),
        LUMINANCE16(0x8042),
        LUMINANCE8_ALPHA8(0x8045),
        LUMINANCE16_ALPHA16(0x8048),

        R8_USCALED_GTC(0xF000),
        R8_SSCALED_GTC(0xF001),
        RG8_USCALED_GTC(0xF002),
        RG8_SSCALED_GTC(0xF003),
        RGB8_USCALED_GTC(0xF004),
        RGB8_SSCALED_GTC(0xF005),
        RGBA8_USCALED_GTC(0xF006),
        RGBA8_SSCALED_GTC(0xF007),
        RGB10A2_USCALED_GTC(0xF008),
        RGB10A2_SSCALED_GTC(0xF009),
        R16_USCALED_GTC(0xF00A),
        R16_SSCALED_GTC(0xF00B),
        RG16_USCALED_GTC(0xF00C),
        RG16_SSCALED_GTC(0xF00D),
        RGB16_USCALED_GTC(0xF00E),
        RGB16_SSCALED_GTC(0xF00F),
        RGBA16_USCALED_GTC(0xF010),
        RGBA16_SSCALED_GTC(0xF011);

        companion object {
            infix fun of(int: Int) = values().first { it.i == int }
        }
    }

    enum class ExternalFormat(val i: Int) {

        NONE(0), //GL_NONE
        RED(0x1903), //GL_RED
        RG(0x8227), //GL_RG
        RGB(0x1907), //GL_RGB
        BGR(0x80E0), //GL_BGR
        RGBA(0x1908), //GL_RGBA
        BGRA(0x80E1), //GL_BGRA
        RED_INTEGER(0x8D94), //GL_RED_INTEGER
        RG_INTEGER(0x8228), //GL_RG_INTEGER
        RGB_INTEGER(0x8D98), //GL_RGB_INTEGER
        BGR_INTEGER(0x8D9A), //GL_BGR_INTEGER
        RGBA_INTEGER(0x8D99), //GL_RGBA_INTEGER
        BGRA_INTEGER(0x8D9B), //GL_BGRA_INTEGER
        DEPTH(0x1902), //GL_DEPTH_COMPONENT
        DEPTH_STENCIL(0x84F9), //GL_DEPTH_STENCIL
        STENCIL(0x1901), //GL_STENCIL_INDEX

        LUMINANCE(0x1909), //GL_LUMINANCE
        ALPHA(0x1906), //GL_ALPHA
        LUMINANCE_ALPHA(0x190A), //GL_LUMINANCE_ALPHA

        SRGB_EXT(0x8C40), //SRGB_EXT
        SRGB_ALPHA_EXT(0x8C42);          //SRGB_ALPHA_EXT

        companion object {
            infix fun of(int: Int) = values().first { it.i == int }
        }
    }

    enum class TypeFormat(val i: Int) {

        NONE(0), //GL_NONE
        I8(0x1400), //GL_BYTE
        U8(0x1401), //GL_UNSIGNED_BYTE
        I16(0x1402), //GL_SHORT
        U16(0x1403), //GL_UNSIGNED_SHORT
        I32(0x1404), //GL_INT
        U32(0x1405), //GL_UNSIGNED_INT
        I64(0x140E), //GL_INT64_ARB
        U64(0x140F), //GL_UNSIGNED_INT64_ARB
        F16(0x140B), //GL_HALF_FLOAT
        F16_OES(0x8D61), //GL_HALF_FLOAT_OES
        F32(0x1406), //GL_FLOAT
        F64(0x140A), //GL_DOUBLE
        UINT32_RGB9_E5_REV(0x8C3E), //GL_UNSIGNED_INT_5_9_9_9_REV
        UINT32_RG11B10F_REV(0x8C3B), //GL_UNSIGNED_INT_10F_11F_11F_REV
        UINT8_RG3B2(0x8032), //GL_UNSIGNED_BYTE_3_3_2
        UINT8_RG3B2_REV(0x8362), //GL_UNSIGNED_BYTE_2_3_3_REV
        UINT16_RGB5A1(0x8034), //GL_UNSIGNED_SHORT_5_5_5_1
        UINT16_RGB5A1_REV(0x8366), //GL_UNSIGNED_SHORT_1_5_5_5_REV
        UINT16_R5G6B5(0x8363), //GL_UNSIGNED_SHORT_5_6_5
        UINT16_R5G6B5_REV(0x8364), //GL_UNSIGNED_SHORT_5_6_5_REV
        UINT16_RGBA4(0x8033), //GL_UNSIGNED_SHORT_4_4_4_4
        UINT16_RGBA4_REV(0x8365), //GL_UNSIGNED_SHORT_4_4_4_4_REV
        UINT32_RGBA8(0x8035), //GL_UNSIGNED_SHORT_8_8_8_8
        UINT32_RGBA8_REV(0x8367), //GL_UNSIGNED_SHORT_8_8_8_8_REV
        UINT32_RGB10A2(0x8036), //GL_UNSIGNED_INT_10_10_10_2
        UINT32_RGB10A2_REV(0x8368), //GL_UNSIGNED_INT_2_10_10_10_REV

        UINT8_RG4_REV_GTC(0xFFFD),
        UINT16_A1RGB5_GTC(0xFFFC);

        companion object {
            infix fun of(int: Int) = values().first { it.i == int }
        }
    }

    enum class Target(val i: Int) {

        _1D(0x0DE0),
        _1D_ARRAY(0x8C18),
        _2D(0x0DE1),
        _2D_ARRAY(0x8C1A),
        _3D(0x806F),
        RECT(0x84F5),
        RECT_ARRAY(0x84F5), // Not supported by OpenGL
        CUBE(0x8513),
        CUBE_ARRAY(0x9009)
    }

    enum class Swizzle(val i: Int) {

        RED(0x1903), //GL_RED
        GREEN(0x1904), //GL_GREEN
        BLUE(0x1905), //GL_BLUE
        ALPHA(0x1906), //GL_ALPHA
        ZERO(0x0000), //GL_ZERO
        ONE(0x0001); //GL_ONE
    }

    enum class Profile {

        ES20,
        ES30,
        GL32,
        GL33,
        KTX;

        val i = ordinal

        val hasSwizzle get() = this == ES30 || this == GL33

        companion object {

        }
    }

    data class Swizzles(var r: Swizzle, var g: Swizzle, var b: Swizzle, var a: Swizzle) {

        constructor(s: Swizzle) : this(s, s, s, s)
        constructor() : this(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA)

        operator fun get(i: Int) = when (i) {
            0 -> r
            1 -> g
            2 -> b
            3 -> a
            else -> throw Error()
        }

        infix fun to(buffer: ByteBuffer): ByteBuffer = buffer.putInt(0, r.i).putInt(1, g.i).putInt(2, b.i).putInt(3, a.i)
        infix fun to(intBuffer: IntBuffer): IntBuffer = intBuffer.put(0, r.i).put(1, g.i).put(2, b.i).put(3, a.i)
    }

    class Format(val internal: InternalFormat, val external: ExternalFormat, val type: TypeFormat, val swizzles: Swizzles)

    private lateinit var tableFormatDesc: Array<FormatDesc>

    fun updateTable() {

        val hasSwizzle = profile.hasSwizzle
        val externalBGR = if (hasSwizzle) ExternalFormat.RGB else ExternalFormat.BGR
        val externalBGRA = if (hasSwizzle) ExternalFormat.RGBA else ExternalFormat.BGRA
        val externalBGRInt = if (hasSwizzle) ExternalFormat.RGB_INTEGER else ExternalFormat.BGR_INTEGER
        val externalBGRAInt = if (hasSwizzle) ExternalFormat.RGBA_INTEGER else ExternalFormat.BGRA_INTEGER

        val externalSRGB8 = if (profile != Profile.ES20) ExternalFormat.RGB else ExternalFormat.SRGB_EXT
        val externalSRGB8_A8 = if (profile != Profile.ES20) ExternalFormat.RGBA else ExternalFormat.SRGB_ALPHA_EXT

        val internalBGRA = if (profile == Profile.ES20) InternalFormat.BGRA8_UNORM else InternalFormat.RGBA8_UNORM
        val internalRGBETC = if (profile == Profile.ES20) InternalFormat.RGB_ETC else InternalFormat.RGB_ETC2

        val internalLuminance8 = if (hasSwizzle) InternalFormat.R8_UNORM else InternalFormat.LUMINANCE8
        val internalAlpha8 = if (hasSwizzle) InternalFormat.R8_UNORM else InternalFormat.ALPHA8
        val internalLuminanceAlpha8 = if (hasSwizzle) InternalFormat.RG8_UNORM else InternalFormat.LUMINANCE8_ALPHA8

        val internalLuminance16 = if (hasSwizzle) InternalFormat.R16_UNORM else InternalFormat.LUMINANCE16
        val internalAlpha16 = if (hasSwizzle) InternalFormat.R16_UNORM else InternalFormat.ALPHA16
        val internalLuminanceAlpha16 = if (hasSwizzle) InternalFormat.RG16_UNORM else InternalFormat.LUMINANCE16_ALPHA16

        val externalLuminance = if (hasSwizzle) ExternalFormat.RED else ExternalFormat.LUMINANCE
        val externalAlpha = if (hasSwizzle) ExternalFormat.RED else ExternalFormat.ALPHA
        val externalLuminanceAlpha = if (hasSwizzle) ExternalFormat.RG else ExternalFormat.LUMINANCE_ALPHA

        val typeF16 = if (profile == Profile.ES20) TypeFormat.F16_OES else TypeFormat.F16

        tableFormatDesc = arrayOf(
                FormatDesc(RG4_EXT, RG, UINT8_RG4_REV_GTC, 0), //FORMAT_R4G4_UNORM,
                FormatDesc(RGBA4, RGBA, UINT16_RGBA4_REV, 0), //FORMAT_RGBA4_UNORM,
                FormatDesc(RGBA4, RGBA, UINT16_RGBA4, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGRA4_UNORM,
                FormatDesc(R5G6B5, RGB, UINT16_R5G6B5_REV, 0), //FORMAT_R5G6B5_UNORM,
                FormatDesc(R5G6B5, RGB, UINT16_R5G6B5, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_B5G6R5_UNORM,
                FormatDesc(RGB5A1, RGBA, UINT16_RGB5A1_REV, 0), //FORMAT_RGB5A1_UNORM,
                FormatDesc(RGB5A1, RGBA, UINT16_RGB5A1, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR5A1_UNORM,
                FormatDesc(RGB5A1, RGBA, UINT16_A1RGB5_GTC, 0), //FORMAT_A1RGB5_UNORM,

                FormatDesc(R8_UNORM, RED, U8, 0), //FORMAT_R8_UNORM,
                FormatDesc(R8_SNORM, RED, I8, 0), //FORMAT_R8_SNORM,
                FormatDesc(R8_USCALED_GTC, RED, U8, 0), //FORMAT_R8_USCALED,
                FormatDesc(R8_SSCALED_GTC, RED, I8, 0), //FORMAT_R8_SSCALED,
                FormatDesc(R8U, RED_INTEGER, U8, 0), //FORMAT_R8_UINT,
                FormatDesc(R8I, RED_INTEGER, I8, 0), //FORMAT_R8_SINT,
                FormatDesc(SR8, RED, U8, 0), //FORMAT_R8_SRGB,

                FormatDesc(RG8_UNORM, RG, U8, 0), //FORMAT_RG8_UNORM,
                FormatDesc(RG8_SNORM, RG, I8, 0), //FORMAT_RG8_SNORM,
                FormatDesc(RG8_USCALED_GTC, RG, U8, 0), //FORMAT_RG8_USCALED,
                FormatDesc(RG8_SSCALED_GTC, RG, I8, 0), //FORMAT_RG8_SSCALED,
                FormatDesc(RG8U, RG_INTEGER, U8, 0), //FORMAT_RG8_UINT,
                FormatDesc(RG8I, RG_INTEGER, I8, 0), //FORMAT_RG8_SINT,
                FormatDesc(SRG8, RG, U8, 0), //FORMAT_RG8_SRGB,

                FormatDesc(RGB8_UNORM, RGB, U8, 0), //FORMAT_RGB8_UNORM,
                FormatDesc(RGB8_SNORM, RGB, I8, 0), //FORMAT_RGB8_SNORM,
                FormatDesc(RGB8_USCALED_GTC, RGB, U8, 0), //FORMAT_RGB8_USCALED,
                FormatDesc(RGB8_SSCALED_GTC, RGB, I8, 0), //FORMAT_RGB8_SSCALED,
                FormatDesc(RGB8U, RGB_INTEGER, U8, 0), //FORMAT_RGB8_UINT,
                FormatDesc(RGB8I, RGB_INTEGER, I8, 0), //FORMAT_RGB8_SINT,
                FormatDesc(SRGB8, externalSRGB8, U8, 0), //FORMAT_RGB8_SRGB,

                FormatDesc(RGB8_UNORM, externalBGR, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_UNORM_PACK8,
                FormatDesc(RGB8_SNORM, externalBGR, I8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_SNORM_PACK8,
                FormatDesc(RGB8_USCALED_GTC, externalBGR, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_USCALED_PACK8,
                FormatDesc(RGB8_SSCALED_GTC, externalBGR, I8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_SSCALED_PACK8,
                FormatDesc(RGB8U, externalBGRInt, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_UINT_PACK8,
                FormatDesc(RGB8I, externalBGRInt, I8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_SINT_PACK8,
                FormatDesc(SRGB8, externalBGR, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGR8_SRGB_PACK8,

                FormatDesc(RGBA8_UNORM, RGBA, U8, 0), //FORMAT_RGBA8_UNORM_PACK8,
                FormatDesc(RGBA8_SNORM, RGBA, I8, 0), //FORMAT_RGBA8_SNORM_PACK8,
                FormatDesc(RGBA8_USCALED_GTC, RGBA, U8, 0), //FORMAT_RGBA8_USCALED_PACK8,
                FormatDesc(RGBA8_SSCALED_GTC, RGBA, I8, 0), //FORMAT_RGBA8_SSCALED_PACK8,
                FormatDesc(RGBA8U, RGBA_INTEGER, U8, 0), //FORMAT_RGBA8_UINT_PACK8,
                FormatDesc(RGBA8I, RGBA_INTEGER, I8, 0), //FORMAT_RGBA8_SINT_PACK8,
                FormatDesc(SRGB8_ALPHA8, externalSRGB8_A8, U8, 0), //FORMAT_RGBA8_SRGB_PACK8,

                FormatDesc(internalBGRA, externalBGRA, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_UNORM_PACK8,
                FormatDesc(RGBA8_SNORM, externalBGRA, I8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_SNORM_PACK8,
                FormatDesc(RGBA8_USCALED_GTC, externalBGRA, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_USCALED_PACK8,
                FormatDesc(RGBA8_SSCALED_GTC, externalBGRA, I8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_SSCALED_PACK8,
                FormatDesc(RGBA8U, externalBGRAInt, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_UINT_PACK8,
                FormatDesc(RGBA8I, externalBGRAInt, I8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_SINT_PACK8,
                FormatDesc(SRGB8_ALPHA8, externalBGRA, U8, FORMAT_PROPERTY_BGRA_FORMAT_BIT), //FORMAT_BGRA8_SRGB_PACK8,

                FormatDesc(RGBA8_UNORM, RGBA, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_UNORM_PACK32,
                FormatDesc(RGBA8_SNORM, RGBA, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_SNORM_PACK32,
                FormatDesc(RGBA8_USCALED_GTC, RGBA, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_USCALED_PACK32,
                FormatDesc(RGBA8_SSCALED_GTC, RGBA, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_SSCALED_PACK32,
                FormatDesc(RGBA8U, RGBA_INTEGER, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_UINT_PACK32,
                FormatDesc(RGBA8I, RGBA_INTEGER, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_SINT_PACK32,
                FormatDesc(SRGB8_ALPHA8, RGBA, UINT32_RGBA8_REV, 0), //FORMAT_ABGR8_SRGB_PACK32,

                FormatDesc(RGB10A2_UNORM, RGBA, UINT32_RGB10A2_REV, 0), //FORMAT_RGB10A2_UNORM_PACK32,
                FormatDesc(RGB10A2_SNORM_EXT, RGBA, UINT32_RGB10A2_REV, 0), //FORMAT_RGB10A2_SNORM_PACK32,
                FormatDesc(RGB10A2_USCALED_GTC, RGBA, UINT32_RGB10A2_REV, 0), //FORMAT_RGB10A2_USCALE_PACK32,
                FormatDesc(RGB10A2_SSCALED_GTC, RGBA, UINT32_RGB10A2_REV, 0), //FORMAT_RGB10A2_SSCALE_PACK32,
                FormatDesc(RGB10A2U, RGBA_INTEGER, UINT32_RGB10A2_REV, 0), //FORMAT_RGB10A2_UINT_PACK32,
                FormatDesc(RGB10A2I_EXT, RGBA_INTEGER, UINT32_RGB10A2_REV, 0), //FORMAT_RGB10A2_SINT_PACK32,

                FormatDesc(RGB10A2_UNORM, RGBA, UINT32_RGB10A2, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR10A2_UNORM_PACK32,
                FormatDesc(RGB10A2_SNORM_EXT, RGBA, UINT32_RGB10A2, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR10A2_SNORM_PACK32,
                FormatDesc(RGB10A2_USCALED_GTC, RGBA, UINT32_RGB10A2, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR10A2_USCALE_PACK32,
                FormatDesc(RGB10A2_SSCALED_GTC, RGBA, UINT32_RGB10A2, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR10A2_SSCALE_PACK32,
                FormatDesc(RGB10A2U, RGBA_INTEGER, UINT32_RGB10A2, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR10A2_UINT_PACK32,
                FormatDesc(RGB10A2I_EXT, RGBA_INTEGER, UINT32_RGB10A2, FORMAT_PROPERTY_BGRA_TYPE_BIT), //FORMAT_BGR10A2_SINT_PACK32,

                FormatDesc(R16_UNORM, RED, U16, 0), //FORMAT_R16_UNORM_PACK16,
                FormatDesc(R16_SNORM, RED, I16, 0), //FORMAT_R16_SNORM_PACK16,
                FormatDesc(R16_USCALED_GTC, RED, U16, 0), //FORMAT_R16_USCALED_PACK16,
                FormatDesc(R16_SSCALED_GTC, RED, I16, 0), //FORMAT_R16_SSCALED_PACK16,
                FormatDesc(R16U, RED_INTEGER, U16, 0), //FORMAT_R16_UINT_PACK16,
                FormatDesc(R16I, RED_INTEGER, I16, 0), //FORMAT_R16_SINT_PACK16,
                FormatDesc(R16F, RED, typeF16, 0), //FORMAT_R16_SFLOAT_PACK16,

                FormatDesc(RG16_UNORM, RG, U16, 0), //FORMAT_RG16_UNORM_PACK16,
                FormatDesc(RG16_SNORM, RG, I16, 0), //FORMAT_RG16_SNORM_PACK16,
                FormatDesc(RG16_USCALED_GTC, RG, U16, 0), //FORMAT_RG16_USCALED_PACK16,
                FormatDesc(RG16_SSCALED_GTC, RG, I16, 0), //FORMAT_RG16_SSCALED_PACK16,
                FormatDesc(RG16U, RG_INTEGER, U16, 0), //FORMAT_RG16_UINT_PACK16,
                FormatDesc(RG16I, RG_INTEGER, I16, 0), //FORMAT_RG16_SINT_PACK16,
                FormatDesc(RG16F, RG, typeF16, 0), //FORMAT_RG16_SFLOAT_PACK16,

                FormatDesc(RGB16_UNORM, RGB, U16, 0), //FORMAT_RGB16_UNORM_PACK16,
                FormatDesc(RGB16_SNORM, RGB, I16, 0), //FORMAT_RGB16_SNORM_PACK16,
                FormatDesc(RGB16_USCALED_GTC, RGB, U16, 0), //FORMAT_RGB16_USCALED_PACK16,
                FormatDesc(RGB16_SSCALED_GTC, RGB, I16, 0), //FORMAT_RGB16_USCALED_PACK16,
                FormatDesc(RGB16U, RGB_INTEGER, U16, 0), //FORMAT_RGB16_UINT_PACK16,
                FormatDesc(RGB16I, RGB_INTEGER, I16, 0), //FORMAT_RGB16_SINT_PACK16,
                FormatDesc(RGB16F, RGB, typeF16, 0), //FORMAT_RGB16_SFLOAT_PACK16,

                FormatDesc(RGBA16_UNORM, RGBA, U16, 0), //FORMAT_RGBA16_UNORM_PACK16,
                FormatDesc(RGBA16_SNORM, RGBA, I16, 0), //FORMAT_RGBA16_SNORM_PACK16,
                FormatDesc(RGBA16_USCALED_GTC, RGBA, U16, 0), //FORMAT_RGBA16_USCALED_PACK16,
                FormatDesc(RGBA16_SSCALED_GTC, RGBA, I16, 0), //FORMAT_RGBA16_SSCALED_PACK16,
                FormatDesc(RGBA16U, RGBA_INTEGER, U16, 0), //FORMAT_RGBA16_UINT_PACK16,
                FormatDesc(RGBA16I, RGBA_INTEGER, I16, 0), //FORMAT_RGBA16_SINT_PACK16,
                FormatDesc(RGBA16F, RGBA, typeF16, 0), //FORMAT_RGBA16_SFLOAT_PACK16,

                FormatDesc(R32U, RED_INTEGER, U32, 0), //FORMAT_R32_UINT_PACK32,
                FormatDesc(R32I, RED_INTEGER, I32, 0), //FORMAT_R32_SINT_PACK32,
                FormatDesc(R32F, RED, F32, 0), //FORMAT_R32_SFLOAT_PACK32,

                FormatDesc(RG32U, RG_INTEGER, U32, 0), //FORMAT_RG32_UINT_PACK32,
                FormatDesc(RG32I, RG_INTEGER, I32, 0), //FORMAT_RG32_SINT_PACK32,
                FormatDesc(RG32F, RG, F32, 0), //FORMAT_RG32_SFLOAT_PACK32,

                FormatDesc(RGB32U, RGB_INTEGER, U32, 0), //FORMAT_RGB32_UINT_PACK32,
                FormatDesc(RGB32I, RGB_INTEGER, I32, 0), //FORMAT_RGB32_SINT_PACK32,
                FormatDesc(RGB32F, RGB, F32, 0), //FORMAT_RGB32_SFLOAT_PACK32,

                FormatDesc(RGBA32U, RGBA_INTEGER, U32, 0), //FORMAT_RGBA32_UINT_PACK32,
                FormatDesc(RGBA32I, RGBA_INTEGER, I32, 0), //FORMAT_RGBA32_SINT_PACK32,
                FormatDesc(RGBA32F, RGBA, F32, 0), //FORMAT_RGBA32_SFLOAT_PACK32,

                FormatDesc(R64F_EXT, RED, U64, 0), //FORMAT_R64_UINT_PACK64,
                FormatDesc(R64F_EXT, RED, I64, 0), //FORMAT_R64_SINT_PACK64,
                FormatDesc(R64F_EXT, RED, F64, 0), //FORMAT_R64_SFLOAT_PACK64,

                FormatDesc(RG64F_EXT, RG, U64, 0), //FORMAT_RG64_UINT_PACK64,
                FormatDesc(RG64F_EXT, RG, I64, 0), //FORMAT_RG64_SINT_PACK64,
                FormatDesc(RG64F_EXT, RG, F64, 0), //FORMAT_RG64_SFLOAT_PACK64,

                FormatDesc(RGB64F_EXT, RGB, U64, 0), //FORMAT_RGB64_UINT_PACK64,
                FormatDesc(RGB64F_EXT, RGB, I64, 0), //FORMAT_RGB64_SINT_PACK64,
                FormatDesc(RGB64F_EXT, RGB, F64, 0), //FORMAT_RGB64_SFLOAT_PACK64,

                FormatDesc(RGBA64F_EXT, RGBA, U64, 0), //FORMAT_RGBA64_UINT_PACK64,
                FormatDesc(RGBA64F_EXT, RGBA, I64, 0), //FORMAT_RGBA64_SINT_PACK64,
                FormatDesc(RGBA64F_EXT, RGBA, F64, 0), //FORMAT_RGBA64_SFLOAT_PACK64,

                FormatDesc(RG11B10F, RGB, UINT32_RG11B10F_REV, 0), //FORMAT_RG11B10_UFLOAT_PACK32,
                FormatDesc(RGB9E5, RGB, UINT32_RGB9_E5_REV, 0), //FORMAT_RGB9E5_UFLOAT_PACK32,

                FormatDesc(D16, DEPTH, NONE, 0), //FORMAT_D16_UNORM_PACK16,
                FormatDesc(D24, DEPTH, NONE, 0), //FORMAT_D24_UNORM,
                FormatDesc(D32F, DEPTH, NONE, 0), //FORMAT_D32_UFLOAT,
                FormatDesc(S8_EXT, STENCIL, NONE, 0), //FORMAT_S8_UNORM,
                FormatDesc(D16S8_EXT, DEPTH, NONE, 0), //FORMAT_D16_UNORM_S8_UINT_PACK32,
                FormatDesc(D24S8, DEPTH_STENCIL, NONE, 0), //FORMAT_D24_UNORM_S8_UINT_PACK32,
                FormatDesc(D32FS8X24, DEPTH_STENCIL, NONE, 0), //FORMAT_D32_SFLOAT_S8_UINT_PACK64,

                FormatDesc(RGB_DXT1, NONE_, NONE, 0), //FORMAT_RGB_DXT1_UNORM_BLOCK8,
                FormatDesc(SRGB_DXT1, NONE_, NONE, 0), //FORMAT_RGB_DXT1_SRGB_BLOCK8,
                FormatDesc(RGBA_DXT1, NONE_, NONE, 0), //FORMAT_RGBA_DXT1_UNORM_BLOCK8,
                FormatDesc(SRGB_ALPHA_DXT1, NONE_, NONE, 0), //FORMAT_RGBA_DXT1_SRGB_BLOCK8,
                FormatDesc(RGBA_DXT3, NONE_, NONE, 0), //FORMAT_RGBA_DXT3_UNORM_BLOCK16,
                FormatDesc(SRGB_ALPHA_DXT3, NONE_, NONE, 0), //FORMAT_RGBA_DXT3_SRGB_BLOCK16,
                FormatDesc(RGBA_DXT5, NONE_, NONE, 0), //FORMAT_RGBA_DXT5_UNORM_BLOCK16,
                FormatDesc(SRGB_ALPHA_DXT5, NONE_, NONE, 0), //FORMAT_RGBA_DXT5_SRGB_BLOCK16,
                FormatDesc(R_ATI1N_UNORM, NONE_, NONE, 0), //FORMAT_R_ATI1N_UNORM_BLOCK8,
                FormatDesc(R_ATI1N_SNORM, NONE_, NONE, 0), //FORMAT_R_ATI1N_SNORM_BLOCK8,
                FormatDesc(RG_ATI2N_UNORM, NONE_, NONE, 0), //FORMAT_RG_ATI2N_UNORM_BLOCK16,
                FormatDesc(RG_ATI2N_SNORM, NONE_, NONE, 0), //FORMAT_RG_ATI2N_SNORM_BLOCK16,
                FormatDesc(RGB_BP_UNSIGNED_FLOAT, NONE_, NONE, 0), //FORMAT_RGB_BP_UFLOAT_BLOCK16,
                FormatDesc(RGB_BP_SIGNED_FLOAT, NONE_, NONE, 0), //FORMAT_RGB_BP_SFLOAT_BLOCK16,
                FormatDesc(RGB_BP_UNORM, NONE_, NONE, 0), //FORMAT_RGB_BP_UNORM,
                FormatDesc(SRGB_BP_UNORM, NONE_, NONE, 0), //FORMAT_RGB_BP_SRGB,

                FormatDesc(internalRGBETC, NONE_, NONE, 0), //FORMAT_RGB_ETC2_UNORM_BLOCK8,
                FormatDesc(SRGB8_ETC2, NONE_, NONE, 0), //FORMAT_RGB_ETC2_SRGB_BLOCK8,
                FormatDesc(RGBA_PUNCHTHROUGH_ETC2, NONE_, NONE, 0), //FORMAT_RGBA_ETC2_PUNCHTHROUGH_UNORM,
                FormatDesc(SRGB8_PUNCHTHROUGH_ALPHA1_ETC2, NONE_, NONE, 0), //FORMAT_RGBA_ETC2_PUNCHTHROUGH_SRGB,
                FormatDesc(RGBA_ETC2, NONE_, NONE, 0), //FORMAT_RGBA_ETC2_UNORM_BLOCK16,
                FormatDesc(SRGB8_ALPHA8_ETC2_EAC, NONE_, NONE, 0), //FORMAT_RGBA_ETC2_SRGB_BLOCK16,
                FormatDesc(R11_EAC, NONE_, NONE, 0), //FORMAT_R11_EAC_UNORM,
                FormatDesc(SIGNED_R11_EAC, NONE_, NONE, 0), //FORMAT_R11_EAC_SNORM,
                FormatDesc(RG11_EAC, NONE_, NONE, 0), //FORMAT_RG11_EAC_UNORM,
                FormatDesc(SIGNED_RG11_EAC, NONE_, NONE, 0), //FORMAT_RG11_EAC_SNORM,

                FormatDesc(RGBA_ASTC_4x4, NONE_, NONE, 0), //FORMAT_RGBA_ASTC4X4_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_4x4, NONE_, NONE, 0), //FORMAT_RGBA_ASTC4X4_SRGB,
                FormatDesc(RGBA_ASTC_5x4, NONE_, NONE, 0), //FORMAT_RGBA_ASTC5X4_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_5x4, NONE_, NONE, 0), //FORMAT_RGBA_ASTC5X4_SRGB,
                FormatDesc(RGBA_ASTC_5x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC5X5_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_5x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC5X5_SRGB,
                FormatDesc(RGBA_ASTC_6x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC6X5_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_6x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC6X5_SRGB,
                FormatDesc(RGBA_ASTC_6x6, NONE_, NONE, 0), //FORMAT_RGBA_ASTC6X6_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_6x6, NONE_, NONE, 0), //FORMAT_RGBA_ASTC6X6_SRGB,
                FormatDesc(RGBA_ASTC_8x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC8X5_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_8x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC8X5_SRGB,
                FormatDesc(RGBA_ASTC_8x6, NONE_, NONE, 0), //FORMAT_RGBA_ASTC8X6_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_8x6, NONE_, NONE, 0), //FORMAT_RGBA_ASTC8X6_SRGB,
                FormatDesc(RGBA_ASTC_8x8, NONE_, NONE, 0), //FORMAT_RGBA_ASTC8X8_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_8x8, NONE_, NONE, 0), //FORMAT_RGBA_ASTC8X8_SRGB,
                FormatDesc(RGBA_ASTC_10x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X5_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_10x5, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X5_SRGB,
                FormatDesc(RGBA_ASTC_10x6, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X6_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_10x6, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X6_SRGB,
                FormatDesc(RGBA_ASTC_10x8, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X8_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_10x8, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X8_SRGB,
                FormatDesc(RGBA_ASTC_10x10, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X10_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_10x10, NONE_, NONE, 0), //FORMAT_RGBA_ASTC10X10_SRGB,
                FormatDesc(RGBA_ASTC_12x10, NONE_, NONE, 0), //FORMAT_RGBA_ASTC12X10_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_12x10, NONE_, NONE, 0), //FORMAT_RGBA_ASTC12X10_SRGB,
                FormatDesc(RGBA_ASTC_12x12, NONE_, NONE, 0), //FORMAT_RGBA_ASTC12X12_UNORM,
                FormatDesc(SRGB8_ALPHA8_ASTC_12x12, NONE_, NONE, 0), //FORMAT_RGBA_ASTC12X12_SRGB,

                FormatDesc(RGB_PVRTC_4BPPV1, NONE_, NONE, 0), //FORMAT_RGB_PVRTC1_8X8_UNORM_BLOCK32,
                FormatDesc(SRGB_PVRTC_2BPPV1, NONE_, NONE, 0), //FORMAT_RGB_PVRTC1_8X8_SRGB_BLOCK32,
                FormatDesc(RGB_PVRTC_2BPPV1, NONE_, NONE, 0), //FORMAT_RGB_PVRTC1_16X8_UNORM_BLOCK32,
                FormatDesc(SRGB_PVRTC_4BPPV1, NONE_, NONE, 0), //FORMAT_RGB_PVRTC1_16X8_SRGB_BLOCK32,
                FormatDesc(RGBA_PVRTC_4BPPV1, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC1_8X8_UNORM_BLOCK32,
                FormatDesc(SRGB_ALPHA_PVRTC_2BPPV1, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC1_8X8_SRGB_BLOCK32,
                FormatDesc(RGBA_PVRTC_2BPPV1, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC1_16X8_UNORM_BLOCK32,
                FormatDesc(SRGB_ALPHA_PVRTC_4BPPV1, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC1_16X8_SRGB_BLOCK32,
                FormatDesc(RGBA_PVRTC_4BPPV2, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC2_4X4_UNORM_BLOCK8,
                FormatDesc(SRGB_ALPHA_PVRTC_4BPPV2, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC2_4X4_SRGB_BLOCK8,
                FormatDesc(RGBA_PVRTC_2BPPV2, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC2_8X4_UNORM_BLOCK8,
                FormatDesc(SRGB_ALPHA_PVRTC_2BPPV2, NONE_, NONE, 0), //FORMAT_RGBA_PVRTC2_8X4_SRGB_BLOCK8,

                FormatDesc(RGB_ETC, NONE_, NONE, 0), //FORMAT_RGB_ETC_UNORM_BLOCK8,
                FormatDesc(ATC_RGB, NONE_, NONE, 0), //FORMAT_RGB_ATC_UNORM_BLOCK8,
                FormatDesc(ATC_RGBA_EXPLICIT_ALPHA, NONE_, NONE, 0), //FORMAT_RGBA_ATCA_UNORM_BLOCK16,
                FormatDesc(ATC_RGBA_INTERPOLATED_ALPHA, NONE_, NONE, 0), //FORMAT_RGBA_ATCI_UNORM_BLOCK16,

                FormatDesc(internalLuminance8, externalLuminance, U8, 0), //FORMAT_L8_UNORM_PACK8,
                FormatDesc(internalAlpha8, externalAlpha, U8, 0), //FORMAT_A8_UNORM_PACK8,
                FormatDesc(internalLuminanceAlpha8, externalLuminanceAlpha, U8, 0), //FORMAT_LA8_UNORM_PACK8,
                FormatDesc(internalLuminance16, externalLuminance, U16, 0), //FORMAT_L16_UNORM_PACK16,
                FormatDesc(internalAlpha16, externalAlpha, U16, 0), //FORMAT_A16_UNORM_PACK16,
                FormatDesc(internalLuminanceAlpha16, externalLuminanceAlpha, U16, 0), //FORMAT_LA16_UNORM_PACK16,

                FormatDesc(RGB8_UNORM, externalBGRA, U8, 0), //FORMAT_BGRX8_UNORM,
                FormatDesc(SRGB8, externalBGRA, U8, 0), //FORMAT_BGRX8_SRGB,

                FormatDesc(RG3B2, RGB, UINT8_RG3B2_REV, 0)                    //FORMAT_RG3B2_UNORM,
        )
                .apply { assert(size == gli_.Format.COUNT) { "GLI error: format descriptor list doesn't match number of supported formats" } }
    }
}
