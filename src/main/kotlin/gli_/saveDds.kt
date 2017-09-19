package gli_

import gli_.detail.has
import gli_.detail.or
import gli_.dx.has
import gli_.dx.or
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

interface saveDds {

    private fun getFourcc(requireDX10Header: Boolean, format: Format, dxFormat: dx.Format) = when {
        requireDX10Header -> when {
            format.formatInfo.flags has detail.Cap.DDS_GLI_EXT_BIT -> dx.D3dfmt.GLI1
            else -> dx.D3dfmt.DX10
        }
        else -> if (dxFormat.ddPixelFormat has dx.Ddpf.FOURCC) dxFormat.d3DFormat else dx.D3dfmt.UNKNOWN
    }

    /** Save a texture storage_linear to a DDS file.
     *  @param texture Source texture to save
     *  @param path Path for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to DDS anyway but keep the requested filename extension.
     *  @return returns false if the function fails to save the file.   */
    fun saveDds(texture: Texture, filename: String): Boolean {

        if (texture.empty()) return false

        val stream = RandomAccessFile(filename, "w")
        val channel = stream.channel
        if(!channel.isOpen) return false

        val result = saveDds(texture, channel)
    }

    /** Save a texture storage_linear to a DDS file.
     *
     *  @param texture Source texture to save
     *  @param channel Storage for the DDS container. The function resizes the containers to fit the necessary storage_linear.
     *  @return Returns false if the function fails to save the file.   */
    fun saveDds(texture: Texture, channel: FileChannel): Boolean {

        if (texture.empty()) return false

        val dxFormat = dx.translate(texture.format)

        val requireDX10Header = dxFormat.d3DFormat == dx.D3dfmt.GLI1 || dxFormat.d3DFormat == dx.D3dfmt.DX10 ||
                texture.target.isTargetArray || texture.target.isTarget1d

        val buffer = ByteBuffer.allocate(texture.size() + detail.FOURCC_DDS.size + detail.DdsHeader.SIZE +
                if (requireDX10Header) detail.DdsHeader10.SIZE else 0)

        var offset = 0
        for (c in detail.FOURCC_DDS) buffer.putChar(offset++, c)

        val header = detail.DdsHeader()

        val desc = texture.format.formatInfo

        var caps = detail.DdsFlag.CAPS or detail.DdsFlag.WIDTH or detail.DdsFlag.PIXELFORMAT or detail.DdsFlag.MIPMAPCOUNT
        if (!texture.target.isTarget1d)
            caps = caps or detail.DdsFlag.HEIGHT
        if (texture.target == Target._3D)
            caps = caps or detail.DdsFlag.DEPTH
        //caps |= Storage.levels() > 1 ? detail::DDSD_MIPMAPCOUNT : 0;
        caps = caps or if (desc.flags has detail.Cap.COMPRESSED_BIT) detail.DdsFlag.LINEARSIZE else detail.DdsFlag.PITCH

        with(header) {
            size = detail.DdsHeader.SIZE
            flags = caps
            width = texture.extent().x
            height = texture.extent().y
            pitch = if (desc.flags has detail.Cap.COMPRESSED_BIT) texture.size() / texture.faces() else 32
            depth = if (texture.extent().z > 1) texture.extent().z else 0
            mipMapLevels = texture.levels()
            with(format) {
                size = detail.DdsPixelFormat.SIZE
                flags = if (requireDX10Header) dx.Ddpf.FOURCC.i else dxFormat.ddPixelFormat.i
                fourCC = getFourcc(requireDX10Header, texture.format, dxFormat).i
                bpp = texture.format.bitsPerPixel
                mask = dxFormat.mask
            }
            //header.surfaceFlags = detail::DDSCAPS_TEXTURE | (Storage.levels() > 1 ? detail::DDSCAPS_MIPMAP : 0);
            surfaceFlags = detail.DdsSurfaceFlag.TEXTURE or detail.DdsSurfaceFlag.MIPMAP
            cubemapFlags = 0

            // Cubemap
            if (texture.faces() > 1) {
                assert(texture.faces() == 6)
                cubemapFlags = cubemapFlags or detail.DdsCubemapFlag.CUBEMAP_ALLFACES or detail.DdsCubemapFlag.CUBEMAP
            }

            // Texture3D
            if (texture.extent().z > 1)
                cubemapFlags = cubemapFlags or detail.DdsCubemapFlag.VOLUME
        }
        offset += sizeof(detail::dds_header)

        if (requireDX10Header) {
            detail::dds_header10& Header10 = *reinterpret_cast<detail::dds_header10*>(&Memory[0]+Offset)
            offset += sizeof(detail::dds_header10)

            Header10.ArraySize = static_cast < std::uint32_t > (Texture.layers())
            Header10.ResourceDimension = detail::get_dimension(Texture.target())
            Header10.MiscFlag = 0//Storage.levels() > 0 ? detail::D3D10_RESOURCE_MISC_GENERATE_MIPS : 0;
            Header10.Format = dxFormat.DXGIFormat
            Header10.AlphaFlags = detail::DDS_ALPHA_MODE_UNKNOWN
        }


        val channel = Files.n
        std::memcpy(& Memory [0] + offset, Texture.data(), Texture.size())

        return true
    }
}