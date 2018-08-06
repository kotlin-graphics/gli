package gli_

import gli_.dx.D3dfmt.*
import gli_.dx.Ddpf.*
import gli_.dx.Dxgi_format_dds.*
import gli_.dx.Dxgi_format_gli.*
import glm_.i
import glm_.vec4.Vec4i
import gli_.detail.has
import gli_.detail.hasnt

/**
 * Created by elect on 02/04/17.
 */

/** Translation class to convert GLI enums into DirectX enums   */
object dx {

    fun translate(format: gli_.Format): dx.Format {
        assert(format.i in FORMAT_FIRST.i..FORMAT_LAST.i)
        return table[format.i - FORMAT_FIRST.i]
    }

    fun find(fourCC: D3dfmt) = gli_.Format.of((FORMAT_FIRST.i..FORMAT_LAST.i)
            .firstOrNull { table[it - FORMAT_FIRST.i].d3DFormat == fourCC } ?: FORMAT_INVALID)

    fun find(fourCC: D3dfmt, format: DxgiFormat): gli_.Format {

        assert(fourCC == DX10 || fourCC == GLI1)

        var result = FORMAT_INVALID

        for (currentFormat in FORMAT_FIRST..FORMAT_LAST) {

            val info = currentFormat.formatInfo

            val dxFormat = table[currentFormat.i - FORMAT_FIRST.i]

            if (fourCC == GLI1 && info.flags has detail.Cap.DDS_GLI_EXT_BIT && dxFormat.dxgiFormat.gli == format.gli) {
                result = currentFormat.i
                break
            }

            if (fourCC == DX10 && info.flags hasnt detail.Cap.DDS_GLI_EXT_BIT && dxFormat.dxgiFormat.dds == format.dds) {
                result = currentFormat.i
                break
            }
        }
        return gli_.Format.of(result)
    }

    fun isDdsExt(target: Target, format: gli_.Format): Boolean {

        val dxFormat = translate(format)

        val useDdsExt = format.formatInfo.flags has detail.Cap.DDS_GLI_EXT_BIT

        return ((dxFormat.ddPixelFormat.i has FOURCC) && dxFormat.d3DFormat == GLI1) || (target.isTargetArray || target.isTarget1d) && useDdsExt
    }

    fun GLI_MAKEFOURCC(ch0: Char, ch1: Char, ch2: Char, ch3: Char) = (ch3.i shl 24) or (ch2.i shl 16) or (ch1.i shl 8) or ch0.i

    enum class D3dfmt(val i: Int) {

        UNKNOWN(0),

        R8G8B8(20),
        A8R8G8B8(21),
        X8R8G8B8(22),
        R5G6B5(23),
        X1R5G5B5(24),
        A1R5G5B5(25),
        A4R4G4B4(26),
        R3G3B2(27),
        A8(28),
        A8R3G3B2(29),
        X4R4G4B4(30),
        A2B10G10R10(31),
        A8B8G8R8(32),
        X8B8G8R8(33),
        G16R16(34),
        A2R10G10B10(35),
        A16B16G16R16(36),

        A8P8(40),
        P8(41),

        L8(50),
        A8L8(51),
        A4L4(52),

        V8U8(60),
        L6V5U5(61),
        X8L8V8U8(62),
        Q8W8V8U8(63),
        V16U16(64),
        A2W10V10U10(67),

        UYVY(GLI_MAKEFOURCC('U', 'Y', 'V', 'Y')),
        R8G8_B8G8(GLI_MAKEFOURCC('R', 'G', 'B', 'G')),
        YUY2(GLI_MAKEFOURCC('Y', 'U', 'Y', '2')),
        G8R8_G8B8(GLI_MAKEFOURCC('G', 'R', 'G', 'B')),
        DXT1(GLI_MAKEFOURCC('D', 'X', 'T', '1')),
        DXT2(GLI_MAKEFOURCC('D', 'X', 'T', '2')),
        DXT3(GLI_MAKEFOURCC('D', 'X', 'T', '3')),
        DXT4(GLI_MAKEFOURCC('D', 'X', 'T', '4')),
        DXT5(GLI_MAKEFOURCC('D', 'X', 'T', '5')),

        ATI1(GLI_MAKEFOURCC('A', 'T', 'I', '1')),
        AT1N(GLI_MAKEFOURCC('A', 'T', '1', 'N')),
        ATI2(GLI_MAKEFOURCC('A', 'T', 'I', '2')),
        AT2N(GLI_MAKEFOURCC('A', 'T', '2', 'N')),

        BC4U(GLI_MAKEFOURCC('B', 'C', '4', 'U')),
        BC4S(GLI_MAKEFOURCC('B', 'C', '4', 'S')),
        BC5U(GLI_MAKEFOURCC('B', 'C', '5', 'U')),
        BC5S(GLI_MAKEFOURCC('B', 'C', '5', 'S')),

        ETC(GLI_MAKEFOURCC('E', 'T', 'C', ' ')),
        ETC1(GLI_MAKEFOURCC('E', 'T', 'C', '1')),
        ATC(GLI_MAKEFOURCC('A', 'T', 'C', ' ')),
        ATCA(GLI_MAKEFOURCC('A', 'T', 'C', 'A')),
        ATCI(GLI_MAKEFOURCC('A', 'T', 'C', 'I')),

        POWERVR_2BPP(GLI_MAKEFOURCC('P', 'T', 'C', '2')),
        POWERVR_4BPP(GLI_MAKEFOURCC('P', 'T', 'C', '4')),

        D16_LOCKABLE(70),
        D32(71),
        D15S1(73),
        D24S8(75),
        D24X8(77),
        D24X4S4(79),
        D16(80),

        D32F_LOCKABLE(82),
        D24FS8(83),

        L16(81),

        VERTEXDATA(100),
        INDEX16(101),
        INDEX32(102),

        Q16W16V16U16(110),

        MULTI2_ARGB8(GLI_MAKEFOURCC('M', 'E', 'T', '1')),

        R16F(111),
        G16R16F(112),
        A16B16G16R16F(113),

        R32F(114),
        G32R32F(115),
        A32B32G32R32F(116),

        CxV8U8(117),

        DX10(GLI_MAKEFOURCC('D', 'X', '1', '0')),

        GLI1(GLI_MAKEFOURCC('G', 'L', 'I', '1')),

        FORCE_DWORD(0x7fffffff);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class Dxgi_format_dds(val i: Int) {
        UNKNOWN(0),
        R32G32B32A32_TYPELESS(1),
        R32G32B32A32_FLOAT(2),
        R32G32B32A32_UINT(3),
        R32G32B32A32_SINT(4),
        R32G32B32_TYPELESS(5),
        R32G32B32_FLOAT(6),
        R32G32B32_UINT(7),
        R32G32B32_SINT(8),
        R16G16B16A16_TYPELESS(9),
        R16G16B16A16_FLOAT(10),
        R16G16B16A16_UNORM(11),
        R16G16B16A16_UINT(12),
        R16G16B16A16_SNORM(13),
        R16G16B16A16_SINT(14),
        R32G32_TYPELESS(15),
        R32G32_FLOAT(16),
        R32G32_UINT(17),
        R32G32_SINT(18),
        R32G8X24_TYPELESS(19),
        D32_FLOAT_S8X24_UINT(20),
        R32_FLOAT_X8X24_TYPELESS(21),
        X32_TYPELESS_G8X24_UINT(22),
        R10G10B10A2_TYPELESS(23),
        R10G10B10A2_UNORM(24),
        R10G10B10A2_UINT(25),
        R11G11B10_FLOAT(26),
        R8G8B8A8_TYPELESS(27),
        R8G8B8A8_UNORM(28),
        R8G8B8A8_UNORM_SRGB(29),
        R8G8B8A8_UINT(30),
        R8G8B8A8_SNORM(31),
        R8G8B8A8_SINT(32),
        R16G16_TYPELESS(33),
        R16G16_FLOAT(34),
        R16G16_UNORM(35),
        R16G16_UINT(36),
        R16G16_SNORM(37),
        R16G16_SINT(38),
        R32_TYPELESS(39),
        D32_FLOAT(40),
        R32_FLOAT(41),
        R32_UINT(42),
        R32_SINT(43),
        R24G8_TYPELESS(44),
        D24_UNORM_S8_UINT(45),
        R24_UNORM_X8_TYPELESS(46),
        X24_TYPELESS_G8_UINT(47),
        R8G8_TYPELESS(48),
        R8G8_UNORM(49),
        R8G8_UINT(50),
        R8G8_SNORM(51),
        R8G8_SINT(52),
        R16_TYPELESS(53),
        R16_FLOAT(54),
        D16_UNORM(55),
        R16_UNORM(56),
        R16_UINT(57),
        R16_SNORM(58),
        R16_SINT(59),
        R8_TYPELESS(60),
        R8_UNORM(61),
        R8_UINT(62),
        R8_SNORM(63),
        R8_SINT(64),
        A8_UNORM(65),
        R1_UNORM(66),
        R9G9B9E5_SHAREDEXP(67),
        R8G8_B8G8_UNORM(68),
        G8R8_G8B8_UNORM(69),
        BC1_TYPELESS(70),
        BC1_UNORM(71),
        BC1_UNORM_SRGB(72),
        BC2_TYPELESS(73),
        BC2_UNORM(74),
        BC2_UNORM_SRGB(75),
        BC3_TYPELESS(76),
        BC3_UNORM(77),
        BC3_UNORM_SRGB(78),
        BC4_TYPELESS(79),
        BC4_UNORM(80),
        BC4_SNORM(81),
        BC5_TYPELESS(82),
        BC5_UNORM(83),
        BC5_SNORM(84),
        B5G6R5_UNORM(85),
        B5G5R5A1_UNORM(86),
        B8G8R8A8_UNORM(87),
        B8G8R8X8_UNORM(88),
        R10G10B10_XR_BIAS_A2_UNORM(89),
        B8G8R8A8_TYPELESS(90),
        B8G8R8A8_UNORM_SRGB(91),
        B8G8R8X8_TYPELESS(92),
        B8G8R8X8_UNORM_SRGB(93),
        BC6H_TYPELESS(94),
        BC6H_UF16(95),
        BC6H_SF16(96),
        BC7_TYPELESS(97),
        BC7_UNORM(98),
        BC7_UNORM_SRGB(99),
        AYUV(100),
        Y410(101),
        Y416(102),
        NV12(103),
        P010(104),
        P016(105),
        _420_OPAQUE(106),
        YUY2(107),
        Y210(108),
        Y216(109),
        NV11(110),
        AI44(111),
        IA44(112),
        P8(113),
        A8P8(114),
        B4G4R4A4_UNORM(115),

        P208(130),
        V208(131),
        V408(132),
        ASTC_4X4_TYPELESS(133),
        ASTC_4X4_UNORM(134),
        ASTC_4X4_UNORM_SRGB(135),
        ASTC_5X4_TYPELESS(137),
        ASTC_5X4_UNORM(138),
        ASTC_5X4_UNORM_SRGB(139),
        ASTC_5X5_TYPELESS(141),
        ASTC_5X5_UNORM(142),
        ASTC_5X5_UNORM_SRGB(143),
        ASTC_6X5_TYPELESS(145),
        ASTC_6X5_UNORM(146),
        ASTC_6X5_UNORM_SRGB(147),
        ASTC_6X6_TYPELESS(149),
        ASTC_6X6_UNORM(150),
        ASTC_6X6_UNORM_SRGB(151),
        ASTC_8X5_TYPELESS(153),
        ASTC_8X5_UNORM(154),
        ASTC_8X5_UNORM_SRGB(155),
        ASTC_8X6_TYPELESS(157),
        ASTC_8X6_UNORM(158),
        ASTC_8X6_UNORM_SRGB(159),
        ASTC_8X8_TYPELESS(161),
        ASTC_8X8_UNORM(162),
        ASTC_8X8_UNORM_SRGB(163),
        ASTC_10X5_TYPELESS(165),
        ASTC_10X5_UNORM(166),
        ASTC_10X5_UNORM_SRGB(167),
        ASTC_10X6_TYPELESS(169),
        ASTC_10X6_UNORM(170),
        ASTC_10X6_UNORM_SRGB(171),
        ASTC_10X8_TYPELESS(173),
        ASTC_10X8_UNORM(174),
        ASTC_10X8_UNORM_SRGB(175),
        ASTC_10X10_TYPELESS(177),
        ASTC_10X10_UNORM(178),
        ASTC_10X10_UNORM_SRGB(179),
        ASTC_12X10_TYPELESS(181),
        ASTC_12X10_UNORM(182),
        ASTC_12X10_UNORM_SRGB(183),
        ASTC_12X12_TYPELESS(185),
        ASTC_12X12_UNORM(186),
        ASTC_12X12_UNORM_SRGB(187);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class Dxgi_format_gli {
        UNKNOWN,
        R64_UINT_GLI,
        R64_SINT_GLI,
        R64_FLOAT_GLI,
        R64G64_UINT_GLI,
        R64G64_SINT_GLI,
        R64G64_FLOAT_GLI,
        R64G64B64_UINT_GLI,
        R64G64B64_SINT_GLI,
        R64G64B64_FLOAT_GLI,
        R64G64B64A64_UINT_GLI,
        R64G64B64A64_SINT_GLI,
        R64G64B64A64_FLOAT_GLI,

        RG4_UNORM_GLI,
        RGBA4_UNORM_GLI,
        R5G6B5_UNORM_GLI,
        R5G5B5A1_UNORM_GLI,
        A1B5G5R5_UNORM_GLI,

        R8_SRGB_GLI,
        R8_USCALED_GLI,
        R8_SSCALED_GLI,

        R8G8_SRGB_GLI,
        R8G8_USCALED_GLI,
        R8G8_SSCALED_GLI,

        R8G8B8_UNORM_GLI,
        R8G8B8_SNORM_GLI,
        R8G8B8_USCALED_GLI,
        R8G8B8_SSCALED_GLI,
        R8G8B8_UINT_GLI,
        R8G8B8_SINT_GLI,
        R8G8B8_SRGB_GLI,

        B8G8R8_UNORM_GLI,
        B8G8R8_SNORM_GLI,
        B8G8R8_USCALED_GLI,
        B8G8R8_SSCALED_GLI,
        B8G8R8_UINT_GLI,
        B8G8R8_SINT_GLI,
        B8G8R8_SRGB_GLI,

        R8G8B8A8_USCALED_GLI,
        R8G8B8A8_SSCALED_GLI,

        B8G8R8A8_SNORM_GLI,
        B8G8R8A8_USCALED_GLI,
        B8G8R8A8_SSCALED_GLI,
        B8G8R8A8_UINT_GLI,
        B8G8R8A8_SINT_GLI,

        R8G8B8A8_PACK_UNORM_GLI,
        R8G8B8A8_PACK_SNORM_GLI,
        R8G8B8A8_PACK_USCALED_GLI,
        R8G8B8A8_PACK_SSCALED_GLI,
        R8G8B8A8_PACK_UINT_GLI,
        R8G8B8A8_PACK_SINT_GLI,
        R8G8B8A8_PACK_SRGB_GLI,

        R10G10B10A2_SNORM_GLI,
        R10G10B10A2_USCALED_GLI,
        R10G10B10A2_SSCALED_GLI,
        R10G10B10A2_SINT_GLI,

        B10G10R10A2_UNORM_GLI,
        B10G10R10A2_SNORM_GLI,
        B10G10R10A2_USCALED_GLI,
        B10G10R10A2_SSCALED_GLI,
        B10G10R10A2_UINT_GLI,
        B10G10R10A2_SINT_GLI,

        R16_USCALED_GLI,
        R16_SSCALED_GLI,
        R16G16_USCALED_GLI,
        R16G16_SSCALED_GLI,

        R16G16B16_UNORM_GLI,
        R16G16B16_SNORM_GLI,
        R16G16B16_USCALED_GLI,
        R16G16B16_SSCALED_GLI,
        R16G16B16_UINT_GLI,
        R16G16B16_SINT_GLI,
        R16G16B16_FLOAT_GLI,

        R16G16B16A16_USCALED_GLI,
        R16G16B16A16_SSCALED_GLI,

        S8_UINT_GLI,
        D16_UNORM_S8_UINT_GLI,
        D24_UNORM_GLI,

        L8_UNORM_GLI,
        A8_UNORM_GLI,
        LA8_UNORM_GLI,
        L16_UNORM_GLI,
        A16_UNORM_GLI,
        LA16_UNORM_GLI,

        R3G3B2_UNORM_GLI,

        BC1_RGB_UNORM_GLI,
        BC1_RGB_SRGB_GLI,
        RGB_ETC2_UNORM_GLI,
        RGB_ETC2_SRGB_GLI,
        RGBA_ETC2_A1_UNORM_GLI,
        RGBA_ETC2_A1_SRGB_GLI,
        RGBA_ETC2_UNORM_GLI,
        RGBA_ETC2_SRGB_GLI,
        R11_EAC_UNORM_GLI,
        R11_EAC_SNORM_GLI,
        RG11_EAC_UNORM_GLI,
        RG11_EAC_SNORM_GLI,

        RGB_PVRTC1_8X8_UNORM_GLI,
        RGB_PVRTC1_8X8_SRGB_GLI,
        RGB_PVRTC1_16X8_UNORM_GLI,
        RGB_PVRTC1_16X8_SRGB_GLI,
        RGBA_PVRTC1_8X8_UNORM_GLI,
        RGBA_PVRTC1_8X8_SRGB_GLI,
        RGBA_PVRTC1_16X8_UNORM_GLI,
        RGBA_PVRTC1_16X8_SRGB_GLI,
        RGBA_PVRTC2_8X8_UNORM_GLI,
        RGBA_PVRTC2_8X8_SRGB_GLI,
        RGBA_PVRTC2_16X8_UNORM_GLI,
        RGBA_PVRTC2_16X8_SRGB_GLI,

        RGB_ETC_UNORM_GLI,
        RGB_ATC_UNORM_GLI,
        RGBA_ATCA_UNORM_GLI,
        RGBA_ATCI_UNORM_GLI;

        val i = ordinal

        companion object {
            infix fun of(i: Int) = Dxgi_format_dds.values().first { it.i == i }
        }
    }

    class DxgiFormat(var i: Int = Dxgi_format_dds.UNKNOWN.i) {

        val dds
            get() = Dxgi_format_dds.of(i)
        val gli
            get() = Dxgi_format_gli.of(i)

        constructor(dds: Dxgi_format_dds) : this(dds.i)
        constructor(gli: Dxgi_format_gli) : this(gli.i)
    }

    enum class Ddpf(val i: Int) {
        ALPHAPIXELS(0x1),
        ALPHA(0x2),
        FOURCC(0x4),
        RGB(0x40),
        YUV(0x200),
        LUMINANCE(0x20000),
        LUMINANCE_ALPHA(LUMINANCE.i or ALPHA.i),
        RGBAPIXELS(RGB.i or ALPHAPIXELS.i),
        RGBA(RGB.i or ALPHA.i),
        LUMINANCE_ALPHAPIXELS(LUMINANCE.i or ALPHAPIXELS.i);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    infix fun Ddpf.has(b: Ddpf) = (i and b.i) != 0
    infix fun Int.has(b: Ddpf) = (this and b.i) != 0
    infix fun Int.hasnt(b: Ddpf) = (this and b.i) == 0
    infix fun Ddpf.or(b: Ddpf) = i or b.i
    infix fun Int.or(b: Ddpf) = this or b.i

    class Format(val ddPixelFormat: Ddpf, val d3DFormat: D3dfmt, val dxgiFormat: DxgiFormat, val mask: Vec4i) {
        constructor(ddPixelFormat: Ddpf, d3DFormat: D3dfmt, dxgiFormatDDS: Dxgi_format_dds, mask: Vec4i)
                : this(ddPixelFormat, d3DFormat, DxgiFormat(dxgiFormatDDS), mask)

        constructor(ddPixelFormat: Ddpf, d3DFormat: D3dfmt, dxgiFormatGLI: Dxgi_format_gli, mask: Vec4i)
                : this(ddPixelFormat, d3DFormat, DxgiFormat(dxgiFormatGLI), mask)
    }

    private val table by lazy {
        val table = arrayOf(
                Format(FOURCC, GLI1, RG4_UNORM_GLI, Vec4i(0x000F, 0x00F0, 0x0000, 0x0000)), //FORMAT_RG4_UNORM,
                Format(FOURCC, GLI1, RGBA4_UNORM_GLI, Vec4i(0x000F, 0x00F0, 0x0F00, 0xF000)), //FORMAT_RGBA4_UNORM,
                Format(FOURCC, A4R4G4B4, B4G4R4A4_UNORM, Vec4i(0x0F00, 0x00F0, 0x000F, 0xF000)), //FORMAT_BGRA4_UNORM,
                Format(FOURCC, GLI1, R5G6B5_UNORM_GLI, Vec4i(0x001f, 0x07e0, 0xf800, 0x0000)), //FORMAT_R5G6B5_UNORM,
                Format(FOURCC, R5G6B5, B5G6R5_UNORM, Vec4i(0xf800, 0x07e0, 0x001f, 0x0000)), //FORMAT_B5G6R5_UNORM,
                Format(FOURCC, GLI1, R5G5B5A1_UNORM_GLI, Vec4i(0x001f, 0x03e0, 0x7c00, 0x8000)), //FORMAT_RGB5A1_UNORM,
                Format(FOURCC, A1R5G5B5, B5G5R5A1_UNORM, Vec4i(0x7c00, 0x03e0, 0x001f, 0x8000)), //FORMAT_BGR5A1_UNORM,
                Format(FOURCC, GLI1, A1B5G5R5_UNORM_GLI, Vec4i(0x7c00, 0x03e0, 0x001f, 0x8000)), //FORMAT_A1RGB5_UNORM,

                Format(FOURCC, DX10, R8_UNORM, Vec4i(0x00FF0000, 0x00000000, 0x00000000, 0x00000000)), //FORMAT_R8_UNORM,
                Format(FOURCC, DX10, R8_SNORM, Vec4i(0)), //FORMAT_R8_SNORM,
                Format(FOURCC, GLI1, R8_USCALED_GLI, Vec4i(0x00FF0000, 0x00000000, 0x00000000, 0x00000000)), //FORMAT_R8_USCALED,
                Format(FOURCC, GLI1, R8_SSCALED_GLI, Vec4i(0)), //FORMAT_R8_SSCALED,
                Format(FOURCC, DX10, R8_UINT, Vec4i(0)), //FORMAT_R8_UINT,
                Format(FOURCC, DX10, R8_SINT, Vec4i(0)), //FORMAT_R8_SINT,
                Format(FOURCC, GLI1, R8_SRGB_GLI, Vec4i(0)), //FORMAT_R8_SRGB,

                Format(FOURCC, DX10, R8G8_UNORM, Vec4i(0x00FF0000, 0x0000FF00, 0x00000000, 0x00000000)), //FORMAT_RG8_UNORM,
                Format(FOURCC, DX10, R8G8_SNORM, Vec4i(0)), //FORMAT_RG8_SNORM,
                Format(FOURCC, GLI1, R8G8_USCALED_GLI, Vec4i(0x00FF0000, 0x0000FF00, 0x00000000, 0x00000000)), //FORMAT_RG8_USCALED,
                Format(FOURCC, GLI1, R8G8_SSCALED_GLI, Vec4i(0)), //FORMAT_RG8_SSCALED,
                Format(FOURCC, DX10, R8G8_UINT, Vec4i(0)), //FORMAT_RG8_UINT,
                Format(FOURCC, DX10, R8G8_SINT, Vec4i(0)), //FORMAT_RG8_SINT,
                Format(FOURCC, GLI1, R8G8_SRGB_GLI, Vec4i(0)), //FORMAT_RG8_SRGB,

                Format(RGB, GLI1, R8G8B8_UNORM_GLI, Vec4i(0x000000FF, 0x0000FF00, 0x00FF0000, 0x00000000)), //FORMAT_RGB8_UNORM,
                Format(FOURCC, GLI1, R8G8B8_SNORM_GLI, Vec4i(0)), //FORMAT_RGB8_SNORM,
                Format(FOURCC, GLI1, R8G8B8_USCALED_GLI, Vec4i(0)), //FORMAT_RGB8_USCALED,
                Format(FOURCC, GLI1, R8G8B8_SSCALED_GLI, Vec4i(0)), //FORMAT_RGB8_SSCALED,
                Format(FOURCC, GLI1, R8G8B8_UINT_GLI, Vec4i(0)), //FORMAT_RGB8_UINT,
                Format(FOURCC, GLI1, R8G8B8_SINT_GLI, Vec4i(0)), //FORMAT_RGB8_SINT,
                Format(FOURCC, GLI1, R8G8B8_SRGB_GLI, Vec4i(0)), //FORMAT_RGB8_SRGB,

                Format(RGB, R8G8B8, B8G8R8_UNORM_GLI, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0x00000000)), //FORMAT_BGR8_UNORM,
                Format(FOURCC, GLI1, B8G8R8_SNORM_GLI, Vec4i(0)), //FORMAT_BGR8_SNORM,
                Format(FOURCC, GLI1, B8G8R8_USCALED_GLI, Vec4i(0)), //FORMAT_BGR8_USCALED,
                Format(FOURCC, GLI1, B8G8R8_SSCALED_GLI, Vec4i(0)), //FORMAT_BGR8_SSCALED,
                Format(FOURCC, GLI1, B8G8R8_UINT_GLI, Vec4i(0)), //FORMAT_BGR8_UINT,
                Format(FOURCC, GLI1, B8G8R8_SINT_GLI, Vec4i(0)), //FORMAT_BGR8_SINT,
                Format(FOURCC, GLI1, B8G8R8_SRGB_GLI, Vec4i(0)), //FORMAT_BGR8_SRGB,

                Format(FOURCC, DX10, R8G8B8A8_UNORM, Vec4i(0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000)), //FORMAT_RGBA8_UNORM,
                Format(FOURCC, DX10, R8G8B8A8_SNORM, Vec4i(0)), //FORMAT_RGBA8_SNORM,
                Format(FOURCC, GLI1, R8G8B8A8_USCALED_GLI, Vec4i(0)), //FORMAT_RGBA8_USCALED,
                Format(FOURCC, GLI1, R8G8B8A8_SSCALED_GLI, Vec4i(0)), //FORMAT_RGBA8_SSCALED,
                Format(FOURCC, DX10, R8G8B8A8_UINT, Vec4i(0)), //FORMAT_RGBA8_UINT,
                Format(FOURCC, DX10, R8G8B8A8_SINT, Vec4i(0)), //FORMAT_RGBA8_SINT,
                Format(FOURCC, DX10, R8G8B8A8_UNORM_SRGB, Vec4i(0)), //FORMAT_RGBA8_SRGB,

                Format(FOURCC, A8R8G8B8, B8G8R8A8_UNORM, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000)), //FORMAT_BGRA8_UNORM,
                Format(FOURCC, GLI1, B8G8R8A8_SNORM_GLI, Vec4i(0)), //FORMAT_BGRA8_SNORM,
                Format(FOURCC, GLI1, B8G8R8A8_USCALED_GLI, Vec4i(0)), //FORMAT_BGRA8_USCALED,
                Format(FOURCC, GLI1, B8G8R8A8_SSCALED_GLI, Vec4i(0)), //FORMAT_BGRA8_SSCALED,
                Format(FOURCC, GLI1, B8G8R8A8_UINT_GLI, Vec4i(0)), //FORMAT_BGRA8_UINT,
                Format(FOURCC, GLI1, B8G8R8A8_SINT_GLI, Vec4i(0)), //FORMAT_BGRA8_SINT,
                Format(FOURCC, DX10, B8G8R8A8_UNORM_SRGB, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000)), //FORMAT_BGRA8_SRGB,

                Format(FOURCC, GLI1, R8G8B8A8_PACK_UNORM_GLI, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000)), //FORMAT_ABGR8_UNORM,
                Format(FOURCC, GLI1, R8G8B8A8_PACK_SNORM_GLI, Vec4i(0)), //FORMAT_ABGR8_SNORM,
                Format(FOURCC, GLI1, R8G8B8A8_PACK_USCALED_GLI, Vec4i(0)), //FORMAT_ABGR8_USCALED,
                Format(FOURCC, GLI1, R8G8B8A8_PACK_SSCALED_GLI, Vec4i(0)), //FORMAT_ABGR8_SSCALED,
                Format(FOURCC, GLI1, R8G8B8A8_PACK_UINT_GLI, Vec4i(0)), //FORMAT_ABGR8_UINT,
                Format(FOURCC, GLI1, R8G8B8A8_PACK_SINT_GLI, Vec4i(0)), //FORMAT_ABGR8_SINT,
                Format(FOURCC, GLI1, R8G8B8A8_PACK_SRGB_GLI, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0xFF000000)), //FORMAT_ABGR8_SRGB,

                Format(FOURCC, DX10, R10G10B10A2_UNORM, Vec4i(0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000)), //FORMAT_RGB10A2_UNORM_PACK32,
                Format(FOURCC, GLI1, R10G10B10A2_SNORM_GLI, Vec4i(0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000)), //FORMAT_RGB10A2_SNORM_PACK32,
                Format(FOURCC, GLI1, R10G10B10A2_USCALED_GLI, Vec4i(0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000)), //FORMAT_RGB10A2_USCALED_PACK32,
                Format(FOURCC, GLI1, R10G10B10A2_SSCALED_GLI, Vec4i(0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000)), //FORMAT_RGB10A2_SSCALED_PACK32,
                Format(FOURCC, DX10, R10G10B10A2_UINT, Vec4i(0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000)), //FORMAT_RGB10A2_UINT_PACK32,
                Format(FOURCC, GLI1, R10G10B10A2_SINT_GLI, Vec4i(0x000003FF, 0x000FFC00, 0x3FF00000, 0xC0000000)), //FORMAT_RGB10A2_SINT_PACK32,

                Format(FOURCC, GLI1, B10G10R10A2_UNORM_GLI, Vec4i(0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000)), //FORMAT_BGR10A2_UNORM_PACK32,
                Format(FOURCC, GLI1, B10G10R10A2_SNORM_GLI, Vec4i(0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000)), //FORMAT_BGR10A2_SNORM_PACK32,
                Format(FOURCC, GLI1, B10G10R10A2_USCALED_GLI, Vec4i(0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000)), //FORMAT_BGR10A2_USCALED_PACK32,
                Format(FOURCC, GLI1, B10G10R10A2_SSCALED_GLI, Vec4i(0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000)), //FORMAT_BGR10A2_SSCALED_PACK32,
                Format(FOURCC, GLI1, B10G10R10A2_UINT_GLI, Vec4i(0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000)), //FORMAT_BGR10A2_UINT_PACK32,
                Format(FOURCC, GLI1, B10G10R10A2_SINT_GLI, Vec4i(0x3FF00000, 0x000FFC00, 0x000003FF, 0xC0000000)), //FORMAT_BGR10A2_SINT_PACK32,

                Format(FOURCC, DX10, R16_UNORM, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x00000000)), //FORMAT_R16_UNORM_PACK16,
                Format(FOURCC, DX10, R16_SNORM, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x00000000)), //FORMAT_R16_SNORM_PACK16,
                Format(FOURCC, GLI1, R16_USCALED_GLI, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x00000000)), //FORMAT_R16_USCALED_PACK16,
                Format(FOURCC, GLI1, R16_SSCALED_GLI, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x00000000)), //FORMAT_R16_SSCALED_PACK16,
                Format(FOURCC, DX10, R16_UINT, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x0000000)), //FORMAT_R16_UINT_PACK16,
                Format(FOURCC, DX10, R16_SINT, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x0000000)), //FORMAT_R16_SINT_PACK16,
                Format(FOURCC, R16F, R16_FLOAT, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x0000000)), //FORMAT_R16_SFLOAT_PACK16,

                Format(FOURCC, G16R16, R16G16_UNORM, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_UNORM_PACK16,
                Format(FOURCC, DX10, R16G16_SNORM, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_SNORM_PACK16,
                Format(FOURCC, GLI1, R16G16_USCALED_GLI, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_USCALED_PACK16,
                Format(FOURCC, GLI1, R16G16_SSCALED_GLI, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_SSCALED_PACK16,
                Format(FOURCC, DX10, R16G16_UINT, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_UINT_PACK16,
                Format(FOURCC, DX10, R16G16_SINT, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_SINT_PACK16,
                Format(FOURCC, G16R16F, R16G16_FLOAT, Vec4i(0x0000FFFF, 0xFFFF0000, 0x00000000, 0x00000000)), //FORMAT_RG16_SFLOAT_PACK16,

                Format(FOURCC, GLI1, R16G16B16_UNORM_GLI, Vec4i(0)), //FORMAT_RGB16_UNORM_PACK16,
                Format(FOURCC, GLI1, R16G16B16_SNORM_GLI, Vec4i(0)), //FORMAT_RGB16_SNORM_PACK16,
                Format(FOURCC, GLI1, R16G16B16_USCALED_GLI, Vec4i(0)), //FORMAT_RGB16_USCALED_PACK16,
                Format(FOURCC, GLI1, R16G16B16_SSCALED_GLI, Vec4i(0)), //FORMAT_RGB16_SSCALED_PACK16,
                Format(FOURCC, GLI1, R16G16B16_UINT_GLI, Vec4i(0)), //FORMAT_RGB16_UINT_PACK16,
                Format(FOURCC, GLI1, R16G16B16_SINT_GLI, Vec4i(0)), //FORMAT_RGB16_SINT_PACK16,
                Format(FOURCC, GLI1, R16G16B16_FLOAT_GLI, Vec4i(0)), //FORMAT_RGB16_SFLOAT_PACK16,

                Format(FOURCC, A16B16G16R16, R16G16B16A16_UNORM, Vec4i(0)), //FORMAT_RGBA16_UNORM_PACK16,
                Format(FOURCC, DX10, R16G16B16A16_SNORM, Vec4i(0)), //FORMAT_RGBA16_SNORM_PACK16,
                Format(FOURCC, GLI1, R16G16B16A16_USCALED_GLI, Vec4i(0)), //FORMAT_RGBA16_USCALED_PACK16,
                Format(FOURCC, GLI1, R16G16B16A16_SSCALED_GLI, Vec4i(0)), //FORMAT_RGBA16_SSCALED_PACK16,
                Format(FOURCC, DX10, R16G16B16A16_UINT, Vec4i(0)), //FORMAT_RGBA16_UINT_PACK16,
                Format(FOURCC, DX10, R16G16B16A16_SINT, Vec4i(0)), //FORMAT_RGBA16_SINT_PACK16,
                Format(FOURCC, A16B16G16R16F, R16G16B16A16_FLOAT, Vec4i(0)), //FORMAT_RGBA16_SFLOAT_PACK16,

                Format(FOURCC, DX10, R32_UINT, Vec4i(0)), //FORMAT_R32_UINT_PACK32,
                Format(FOURCC, DX10, R32_SINT, Vec4i(0)), //FORMAT_R32_SINT_PACK32,
                Format(FOURCC, R32F, R32_FLOAT, Vec4i(0xFFFFFFFF, 0x0000000, 0x0000000, 0x0000000)), //FORMAT_R32_SFLOAT_PACK32,

                Format(FOURCC, DX10, R32G32_UINT, Vec4i(0)), //FORMAT_RG32_UINT_PACK32
                Format(FOURCC, DX10, R32G32_SINT, Vec4i(0)), //FORMAT_RG32_SINT_PACK32,
                Format(FOURCC, G32R32F, R32G32_FLOAT, Vec4i(0)), //FORMAT_RG32_SFLOAT_PACK32,

                Format(FOURCC, DX10, R32G32B32_UINT, Vec4i(0)), //FORMAT_RGB32_UINT_PACK32,
                Format(FOURCC, DX10, R32G32B32_SINT, Vec4i(0)), //FORMAT_RGB32_SINT_PACK32,
                Format(FOURCC, DX10, R32G32B32_FLOAT, Vec4i(0)), //FORMAT_RGB32_SFLOAT_PACK32,

                Format(FOURCC, DX10, R32G32B32A32_UINT, Vec4i(0)), //FORMAT_RGBA32_UINT_PACK32,
                Format(FOURCC, DX10, R32G32B32A32_SINT, Vec4i(0)), //FORMAT_RGBA32_SINT_PACK32,
                Format(FOURCC, A32B32G32R32F, R32G32B32A32_FLOAT, Vec4i(0)), //FORMAT_RGBA32_SFLOAT_PACK32,

                Format(FOURCC, GLI1, R64_UINT_GLI, Vec4i(0)), //FORMAT_R64_UINT_PACK64,
                Format(FOURCC, GLI1, R64_SINT_GLI, Vec4i(0)), //FORMAT_R64_SINT_PACK64,
                Format(FOURCC, GLI1, R64_FLOAT_GLI, Vec4i(0)), //FORMAT_R64_SFLOAT_PACK64,

                Format(FOURCC, GLI1, R64G64_UINT_GLI, Vec4i(0)), //FORMAT_RG64_UINT_PACK64,
                Format(FOURCC, GLI1, R64G64_SINT_GLI, Vec4i(0)), //FORMAT_RG64_SINT_PACK64,
                Format(FOURCC, GLI1, R64G64_FLOAT_GLI, Vec4i(0)), //FORMAT_RG64_SFLOAT_PACK64,

                Format(FOURCC, GLI1, R64G64B64_UINT_GLI, Vec4i(0)), //FORMAT_RGB64_UINT_PACK64,
                Format(FOURCC, GLI1, R64G64B64_SINT_GLI, Vec4i(0)), //FORMAT_RGB64_SINT_PACK64,
                Format(FOURCC, GLI1, R64G64B64_FLOAT_GLI, Vec4i(0)), //FORMAT_RGB64_SFLOAT_PACK64,

                Format(FOURCC, GLI1, R64G64B64A64_UINT_GLI, Vec4i(0)), //FORMAT_RGBA64_UINT_PACK64,
                Format(FOURCC, GLI1, R64G64B64A64_SINT_GLI, Vec4i(0)), //FORMAT_RGBA64_SINT_PACK64,
                Format(FOURCC, GLI1, R64G64B64A64_FLOAT_GLI, Vec4i(0)), //FORMAT_RGBA64_SFLOAT_PACK64,

                Format(FOURCC, DX10, R11G11B10_FLOAT, Vec4i(0)), //FORMAT_RG11B10_UFLOAT,
                Format(FOURCC, DX10, R9G9B9E5_SHAREDEXP, Vec4i(0)), //FORMAT_RGB9E5_UFLOAT,

                Format(FOURCC, DX10, D16_UNORM, Vec4i(0)), //FORMAT_D16_UNORM_PACK16,
                Format(FOURCC, GLI1, D24_UNORM_GLI, Vec4i(0)), //FORMAT_D24_UNORM,
                Format(FOURCC, DX10, D32_FLOAT, Vec4i(0)), //FORMAT_D32_SFLOAT_PACK32,
                Format(FOURCC, GLI1, S8_UINT_GLI, Vec4i(0)), //FORMAT_S8_UINT_PACK8,
                Format(FOURCC, GLI1, D16_UNORM_S8_UINT_GLI, Vec4i(0)), //FORMAT_D16_UNORM_S8_UINT_PACK32,
                Format(FOURCC, DX10, D24_UNORM_S8_UINT, Vec4i(0)), //FORMAT_D24_UNORM_S8_UINT_PACK32,
                Format(FOURCC, DX10, D32_FLOAT_S8X24_UINT, Vec4i(0)), //FORMAT_D32_SFLOAT_S8_UINT_PACK64,

                Format(FOURCC, GLI1, BC1_RGB_UNORM_GLI, Vec4i(0)), //FORMAT_RGB_DXT1_UNORM_BLOCK8,
                Format(FOURCC, GLI1, BC1_RGB_SRGB_GLI, Vec4i(0)), //FORMAT_RGB_DXT1_SRGB_BLOCK8,
                Format(FOURCC, DXT1, BC1_UNORM, Vec4i(0)), //FORMAT_RGBA_DXT1_UNORM_BLOCK8,
                Format(FOURCC, DX10, BC1_UNORM_SRGB, Vec4i(0)), //FORMAT_RGBA_DXT1_SRGB_BLOCK8,
                Format(FOURCC, DXT3, BC2_UNORM, Vec4i(0)), //FORMAT_RGBA_DXT3_UNORM_BLOCK16,
                Format(FOURCC, DX10, BC2_UNORM_SRGB, Vec4i(0)), //FORMAT_RGBA_DXT3_SRGB_BLOCK16,
                Format(FOURCC, DXT5, BC3_UNORM, Vec4i(0)), //FORMAT_RGBA_DXT5_UNORM_BLOCK16,
                Format(FOURCC, DX10, BC3_UNORM_SRGB, Vec4i(0)), //FORMAT_RGBA_DXT5_SRGB_BLOCK16,
                Format(FOURCC, ATI1, BC4_UNORM, Vec4i(0)), //FORMAT_R_ATI1N_UNORM_BLOCK8,
                Format(FOURCC, AT1N, BC4_SNORM, Vec4i(0)), //FORMAT_R_ATI1N_SNORM_BLOCK8,
                Format(FOURCC, ATI2, BC5_UNORM, Vec4i(0)), //FORMAT_RG_ATI2N_UNORM_BLOCK16,
                Format(FOURCC, AT2N, BC5_SNORM, Vec4i(0)), //FORMAT_RG_ATI2N_SNORM_BLOCK16,
                Format(FOURCC, DX10, BC6H_UF16, Vec4i(0)), //FORMAT_RGB_BP_UFLOAT_BLOCK16,
                Format(FOURCC, DX10, BC6H_SF16, Vec4i(0)), //FORMAT_RGB_BP_SFLOAT_BLOCK16,
                Format(FOURCC, DX10, BC7_UNORM, Vec4i(0)), //FORMAT_RGB_BP_UNORM,
                Format(FOURCC, DX10, BC7_UNORM_SRGB, Vec4i(0)), //FORMAT_RGB_BP_SRGB,

                Format(FOURCC, GLI1, RGB_ETC2_UNORM_GLI, Vec4i(0)), //FORMAT_RGB_ETC2_UNORM_BLOCK8,
                Format(FOURCC, GLI1, RGB_ETC2_SRGB_GLI, Vec4i(0)), //FORMAT_RGB_ETC2_SRGB_BLOCK8,
                Format(FOURCC, GLI1, RGBA_ETC2_A1_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_ETC2_A1_UNORM_BLOCK8,
                Format(FOURCC, GLI1, RGBA_ETC2_A1_SRGB_GLI, Vec4i(0)), //FORMAT_RGBA_ETC2_A1_SRGB_BLOCK8,
                Format(FOURCC, GLI1, RGBA_ETC2_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_ETC2_UNORM_BLOCK16,
                Format(FOURCC, GLI1, RGBA_ETC2_SRGB_GLI, Vec4i(0)), //FORMAT_RGBA_ETC2_SRGB_BLOCK16,
                Format(FOURCC, GLI1, R11_EAC_UNORM_GLI, Vec4i(0)), //FORMAT_R_EAC_UNORM_BLOCK8,
                Format(FOURCC, GLI1, R11_EAC_SNORM_GLI, Vec4i(0)), //FORMAT_R_EAC_SNORM_BLOCK8,
                Format(FOURCC, GLI1, RG11_EAC_UNORM_GLI, Vec4i(0)), //FORMAT_RG_EAC_UNORM_BLOCK8,
                Format(FOURCC, GLI1, RG11_EAC_SNORM_GLI, Vec4i(0)), //FORMAT_RG_EAC_SNORM_BLOCK8,

                Format(FOURCC, DX10, ASTC_4X4_UNORM, Vec4i(0)), //FORMAT_ASTC_4x4_UNORM,
                Format(FOURCC, DX10, ASTC_4X4_UNORM_SRGB, Vec4i(0)), //FORMAT_ASTC_4x4_SRGB,
                Format(FOURCC, DX10, ASTC_5X4_UNORM, Vec4i(0)), //RGBA_ASTC_5x4,
                Format(FOURCC, DX10, ASTC_5X4_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_5x4,
                Format(FOURCC, DX10, ASTC_5X5_UNORM, Vec4i(0)), //RGBA_ASTC_5x5,
                Format(FOURCC, DX10, ASTC_5X5_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_5x5,
                Format(FOURCC, DX10, ASTC_6X5_UNORM, Vec4i(0)), //RGBA_ASTC_6x5,
                Format(FOURCC, DX10, ASTC_6X5_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_6x5,
                Format(FOURCC, DX10, ASTC_6X6_UNORM, Vec4i(0)), //RGBA_ASTC_6x6,
                Format(FOURCC, DX10, ASTC_6X6_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_6x6,
                Format(FOURCC, DX10, ASTC_8X5_UNORM, Vec4i(0)), //RGBA_ASTC_8x5,
                Format(FOURCC, DX10, ASTC_8X5_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_8x5,
                Format(FOURCC, DX10, ASTC_8X6_UNORM, Vec4i(0)), //RGBA_ASTC_8x6,
                Format(FOURCC, DX10, ASTC_8X6_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_8x6,
                Format(FOURCC, DX10, ASTC_8X8_UNORM, Vec4i(0)), //RGBA_ASTC_8x8,
                Format(FOURCC, DX10, ASTC_8X8_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_8x8,
                Format(FOURCC, DX10, ASTC_10X5_UNORM, Vec4i(0)), //RGBA_ASTC_10x5,
                Format(FOURCC, DX10, ASTC_10X5_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_10x5,
                Format(FOURCC, DX10, ASTC_10X6_UNORM, Vec4i(0)), //RGBA_ASTC_10x6,
                Format(FOURCC, DX10, ASTC_10X6_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_10x6,
                Format(FOURCC, DX10, ASTC_10X8_UNORM, Vec4i(0)), //RGBA_ASTC_10x8,
                Format(FOURCC, DX10, ASTC_10X8_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_10x8,
                Format(FOURCC, DX10, ASTC_10X10_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_10x10,
                Format(FOURCC, DX10, ASTC_10X10_UNORM, Vec4i(0)), //RGBA_ASTC_10x10,
                Format(FOURCC, DX10, ASTC_12X10_UNORM, Vec4i(0)), //RGBA_ASTC_12x10,
                Format(FOURCC, DX10, ASTC_12X10_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_12x10,
                Format(FOURCC, DX10, ASTC_12X12_UNORM, Vec4i(0)), //RGBA_ASTC_12x12,
                Format(FOURCC, DX10, ASTC_12X12_UNORM_SRGB, Vec4i(0)), //SRGB_ALPHA_ASTC_12x12,

                Format(FOURCC, POWERVR_4BPP, RGB_PVRTC1_8X8_UNORM_GLI, Vec4i(0)), //FORMAT_RGB_PVRTC1_8X8_UNORM_BLOCK32,
                Format(FOURCC, GLI1, RGB_PVRTC1_8X8_SRGB_GLI, Vec4i(0)), //FORMAT_RGB_PVRTC1_8X8_SRGB_BLOCK32,
                Format(FOURCC, POWERVR_2BPP, RGB_PVRTC1_16X8_UNORM_GLI, Vec4i(0)), //FORMAT_RGB_PVRTC1_16X8_UNORM_BLOCK32,
                Format(FOURCC, GLI1, RGB_PVRTC1_16X8_SRGB_GLI, Vec4i(0)), //FORMAT_RGB_PVRTC1_16X8_SRGB_BLOCK32,
                Format(FOURCC, GLI1, RGBA_PVRTC1_8X8_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC1_8X8_UNORM_BLOCK32,
                Format(FOURCC, GLI1, RGBA_PVRTC1_8X8_SRGB_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC1_8X8_SRGB_BLOCK32,
                Format(FOURCC, GLI1, RGBA_PVRTC1_16X8_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC1_16X8_UNORM_BLOCK32,
                Format(FOURCC, GLI1, RGBA_PVRTC1_16X8_SRGB_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC1_16X8_SRGB_BLOCK32,
                Format(FOURCC, GLI1, RGBA_PVRTC2_8X8_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC2_8X8_UNORM,
                Format(FOURCC, GLI1, RGBA_PVRTC2_8X8_SRGB_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC2_8X8_SRGB,
                Format(FOURCC, GLI1, RGBA_PVRTC2_16X8_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC2_16X8_UNORM,
                Format(FOURCC, GLI1, RGBA_PVRTC2_16X8_SRGB_GLI, Vec4i(0)), //FORMAT_RGBA_PVRTC2_16X8_SRGB,

                Format(FOURCC, ETC, RGB_ETC_UNORM_GLI, Vec4i(0)), //FORMAT_RGB_ETC_UNORM_BLOCK8,
                Format(FOURCC, ATC, RGB_ATC_UNORM_GLI, Vec4i(0)), //FORMAT_RGB_ATC_UNORM_BLOCK8,
                Format(FOURCC, ATCA, RGBA_ATCA_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_ATCA_UNORM_BLOCK16,
                Format(FOURCC, ATCI, RGBA_ATCI_UNORM_GLI, Vec4i(0)), //FORMAT_RGBA_ATCI_UNORM_BLOCK16,

                Format(LUMINANCE, L8, L8_UNORM_GLI, Vec4i(0x000000FF, 0x00000000, 0x00000000, 0x00000000)), //L8_UNORM,
                Format(ALPHA, A8, A8_UNORM_GLI, Vec4i(0x00000000, 0x00000000, 0x00000000, 0x000000FF)), //A8_UNORM,
                Format(LUMINANCE_ALPHA, A8L8, LA8_UNORM_GLI, Vec4i(0x000000FF, 0x00000000, 0x00000000, 0x0000FF00)), //LA8_UNORM,
                Format(LUMINANCE, L16, L16_UNORM_GLI, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0x00000000)), //L16_UNORM,
                Format(ALPHA, GLI1, A16_UNORM_GLI, Vec4i(0x00000000, 0x00000000, 0x00000000, 0x0000FFFF)), //A16_UNORM,
                Format(LUMINANCE_ALPHA, GLI1, LA16_UNORM_GLI, Vec4i(0x0000FFFF, 0x00000000, 0x00000000, 0xFFFF0000)), //LA16_UNORM,

                Format(FOURCC, DX10, B8G8R8X8_UNORM, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0x00000000)), //FORMAT_BGR8_UNORM_PACK32,
                Format(FOURCC, DX10, B8G8R8X8_UNORM_SRGB, Vec4i(0x00FF0000, 0x0000FF00, 0x000000FF, 0x00000000)), //FORMAT_BGR8_SRGB_PACK32,

                Format(FOURCC, GLI1, R3G3B2_UNORM_GLI, Vec4i(0x70, 0x38, 0xC0, 0x00))                                    //FORMAT_RG3B2_UNORM,
        )

        assert(table.size == FORMAT_COUNT, { "GLI error: format descriptor list doesn't match number of supported formats" })

        table
    }
}