package gli_

import gli_.buffer.bufferBig
import gli_.detail.has
import gli_.detail.or
import gli_.dx.has
import glm_.L
import glm_.b
import glm_.size
import org.lwjgl.system.MemoryUtil.memAddress
import org.lwjgl.system.MemoryUtil.memCopy
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

interface saveDds {

    private fun getFourcc(requireDX10Header: Boolean, format: Format, dxFormat: dx.Format) = when {
        requireDX10Header -> when {
            format.formatInfo.flags has detail.Cap.DDS_GLI_EXT_BIT -> dx.D3dfmt.GLI1
            else -> dx.D3dfmt.DX10
        }
        else -> if (dxFormat.ddPixelFormat has dx.Ddpf.FOURCC) dxFormat.d3DFormat else dx.D3dfmt.UNKNOWN
    }


    fun getDimension(target: Target): Int {

        val table = arrayOf(
                detail.D3d10resourceDimension.TEXTURE1D,        //TARGET_1D,
                detail.D3d10resourceDimension.TEXTURE1D,        //TARGET_1D_ARRAY,
                detail.D3d10resourceDimension.TEXTURE2D,        //TARGET_2D,
                detail.D3d10resourceDimension.TEXTURE2D,        //TARGET_2D_ARRAY,
                detail.D3d10resourceDimension.TEXTURE3D,        //TARGET_3D,
                detail.D3d10resourceDimension.TEXTURE2D,        //TARGET_RECT,
                detail.D3d10resourceDimension.TEXTURE2D,        //TARGET_RECT_ARRAY,
                detail.D3d10resourceDimension.TEXTURE2D,        //TARGET_CUBE,
                detail.D3d10resourceDimension.TEXTURE2D         //TARGET_CUBE_ARRAY
        )
        assert(table.size == TARGET_COUNT, { "Table needs to be updated" })
        return table[target.i].i
    }

    /** Save a texture storage_linear to a DDS file.
     *  @param texture Source texture to save
     *  @param filename string for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to DDS anyway but keep the requested filename extension.
     *  @return returns false if the function fails to save the file.   */
    fun saveDds(texture: Texture, filename: String): Boolean {

        if (texture.empty()) return false   // TODO check combinations

        return saveDds(texture, Paths.get(filename))
    }

    /** Save a texture storage_linear to a DDS file.
     *
     *  @param texture Source texture to save
     *  @param path path for where to save the file. It must include the filaname and filename extension.
     *  This function ignores the filename extension in the path and save to DDS anyway but keep the requested filename extension.
     *  @return Returns false if the function fails to save the file.   */
    fun saveDds(texture: Texture, path: Path): Boolean {

        if (texture.empty()) return false

        val dxFormat = dx.translate(texture.format)

        val requireDX10Header = dxFormat.d3DFormat == dx.D3dfmt.GLI1 || dxFormat.d3DFormat == dx.D3dfmt.DX10 ||
                texture.target.isTargetArray || texture.target.isTarget1d

        var length = texture.size() + detail.FOURCC_DDS.size + detail.DdsHeader.SIZE
        if (requireDX10Header) length += detail.DdsHeader10.SIZE
        val data = bufferBig(length)

        detail.FOURCC_DDS.forEach { data.put(it.b) }

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

            to(data)
        }

        if (requireDX10Header) detail.DdsHeader10().apply {
            arraySize = texture.layers()
            resourceDimension = getDimension(texture.target)
            miscFlag = 0    //Storage.levels() > 0 ? detail::D3D10_RESOURCE_MISC_GENERATE_MIPS : 0;
            format = dxFormat.dxgiFormat.i
            alphaFlags = detail.DdsAlphaMode.UNKNOWN.i

            to(data)
        }

        val src = texture.data()
        val dst = data
        memCopy(memAddress(src), memAddress(dst), src.capacity())

        FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
            data.ptr = 0
            while (data.hasRemaining()) it.write(data)
            it.truncate(data.size.L)
        }

        return true
    }
}