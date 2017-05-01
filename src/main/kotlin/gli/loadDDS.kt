package gli

import glm.b
import java.io.File
import java.io.RandomAccessFile
import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import glm.glm
import glm.set
import java.nio.ByteOrder
import glm.vec3.Vec3i

/**
 * Created by GBarbieri on 05.04.2017.
 */


/** Loads a texture storage_linear from DDS memory. Returns an empty storage_linear in case of failure.
 *  @param path Path of the file to open including filaname and filename extension */
fun loadDDS(path: String) = loadDDS(Texture::javaClass.javaClass.classLoader.getResource(path).toURI())

/** Loads a texture storage_linear from DDS memory. Returns an empty storage_linear in case of failure.
 *  @param uri Uri of the file to open including filaname and filename extension */
fun loadDDS(uri: URI): Texture {

    val channel = RandomAccessFile(File(uri), "r").channel
    val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())

    val texture = loadDDS(buffer.order(ByteOrder.nativeOrder()))

    channel.close()

    return texture
}

/** Loads a texture storage_linear from DDS file. Returns an empty storage_linear in case of failure.   */
fun loadDDS(data: ByteBuffer): Texture {

    assert(data.capacity() >= detail.FOURCC_DDS.size)

    if (!detail.FOURCC_DDS.all { data.get() == it.b })
        return Texture()

    assert(data.remaining() >= detail.DdsHeader.SIZE)

    val header = detail.DdsHeader(data)

    val header10 = with(header.format) {
        if ((flags has dx.Ddpf.FOURCC.i) && (fourCC == dx.D3dfmt.DX10.i || fourCC == dx.D3dfmt.GLI1.i))
            detail.DdsHeader10(data)
        else
            detail.DdsHeader10()
    }

    fun has(format: Format) = glm.all(glm.equal(header.format.mask, dx.translate(format).mask))

    val format = with(header.format) {
        if ((flags has (dx.Ddpf.RGB.i or dx.Ddpf.ALPHAPIXELS.i or dx.Ddpf.ALPHA.i or dx.Ddpf.YUV.i or dx.Ddpf.LUMINANCE.i)) && bpp != 0)
            when (bpp) {
                8 -> {
                    when {
                        has(Format.RG4_UNORM_PACK8) -> Format.RG4_UNORM_PACK8
                        has(Format.RG4_UNORM_PACK8) -> Format.RG4_UNORM_PACK8
                        has(Format.L8_UNORM_PACK8) -> Format.L8_UNORM_PACK8
                        has(Format.A8_UNORM_PACK8) -> Format.A8_UNORM_PACK8
                        has(Format.R8_UNORM_PACK8) -> Format.R8_UNORM_PACK8
                        has(Format.RG3B2_UNORM_PACK8) -> Format.RG3B2_UNORM_PACK8
                        else -> throw Error()
                    }
                }
                16 -> {
                    when {
                        has(Format.RGBA4_UNORM_PACK16) -> Format.RGBA4_UNORM_PACK16
                        has(Format.BGRA4_UNORM_PACK16) -> Format.BGRA4_UNORM_PACK16
                        has(Format.R5G6B5_UNORM_PACK16) -> Format.R5G6B5_UNORM_PACK16
                        has(Format.B5G6R5_UNORM_PACK16) -> Format.B5G6R5_UNORM_PACK16
                        has(Format.RGB5A1_UNORM_PACK16) -> Format.RGB5A1_UNORM_PACK16
                        has(Format.BGR5A1_UNORM_PACK16) -> Format.BGR5A1_UNORM_PACK16
                        has(Format.LA8_UNORM_PACK8) -> Format.LA8_UNORM_PACK8
                        has(Format.RG8_UNORM_PACK8) -> Format.RG8_UNORM_PACK8
                        has(Format.L16_UNORM_PACK16) -> Format.L16_UNORM_PACK16
                        has(Format.A16_UNORM_PACK16) -> Format.A16_UNORM_PACK16
                        has(Format.R16_UNORM_PACK16) -> Format.R16_UNORM_PACK16
                        else -> throw Error()
                    }
                }
                24 -> {
                    when {
                        has(Format.RGB8_UNORM_PACK8) -> Format.RGB8_UNORM_PACK8
                        has(Format.BGR8_UNORM_PACK8) -> Format.BGR8_UNORM_PACK8
                        else -> throw Error()
                    }
                }
                32 -> {
                    when {
                        has(Format.BGR8_UNORM_PACK32) -> Format.BGR8_UNORM_PACK32
                        has(Format.BGRA8_UNORM_PACK8) -> Format.BGRA8_UNORM_PACK8
                        has(Format.RGBA8_UNORM_PACK8) -> Format.RGBA8_UNORM_PACK8
                        has(Format.RGB10A2_UNORM_PACK32) -> Format.RGB10A2_UNORM_PACK32
                        has(Format.LA16_UNORM_PACK16) -> Format.LA16_UNORM_PACK16
                        has(Format.RG16_UNORM_PACK16) -> Format.RG16_UNORM_PACK16
                        has(Format.R32_SFLOAT_PACK32) -> Format.R32_SFLOAT_PACK32
                        else -> throw Error()
                    }
                }
                else -> throw Error()
            }
        else if ((flags has dx.Ddpf.FOURCC.i) && (fourCC != dx.D3dfmt.DX10.i) && (fourCC != dx.D3dfmt.GLI1.i))
            dx.find(detail.remapFourCC(fourCC))
        else if (fourCC == dx.D3dfmt.DX10.i || fourCC == dx.D3dfmt.GLI1.i)
            dx.find(dx.D3dfmt.of(fourCC), dx.DxgiFormat(header10.format))
        else throw Error()
    }

    val mipMapCount = if (header.flags has detail.DdsFlag.MIPMAPCOUNT.i) header.mipMapLevels else 1
    var faceCount = 1
    if (header.cubemapFlags has detail.DdsCubemapFlag.CUBEMAP.i)
        faceCount = glm.bitCount(header.cubemapFlags and detail.DdsCubemapFlag.CUBEMAP_ALLFACES.i)

    var depthCount = 1
    if (header.cubemapFlags has detail.DdsCubemapFlag.VOLUME.i)
        depthCount = header.depth

    val texture = Texture(getTarget(header, header10), format, Vec3i(header.width, header.height, depthCount),
            glm.max(header10.arraySize, 1), faceCount, mipMapCount)

    assert(data.capacity() == data.position() + texture.size())

    repeat(texture.size()) { texture.data()[it] = data.get() }

    return texture
}

fun getTarget(header: detail.DdsHeader, header10: detail.DdsHeader10) = when {

    header.cubemapFlags has detail.DdsCubemapFlag.CUBEMAP.i ->
        if (header10.arraySize > 1) Target.CUBE_ARRAY
        else Target.CUBE

    header10.arraySize > 1 ->
        if (header.flags has detail.DdsFlag.HEIGHT.i) Target._2D_ARRAY
        else Target._1D_ARRAY

    header10.resourceDimension == detail.D3d10resourceDimension.TEXTURE1D.i -> Target._1D

    header10.resourceDimension == detail.D3d10resourceDimension.TEXTURE3D.i || (header.flags has detail.DdsFlag.DEPTH.i)
            || (header.cubemapFlags has detail.DdsCubemapFlag.VOLUME.i) -> Target._3D

    else -> Target._2D
}

































