package gli_

import java.nio.ByteBuffer

interface loadKtx {

    fun loadKtx10(data: ByteBuffer)    {

        val header = ktx.Header10(data)

        var offset = ktx.Header10.size

        // Skip key value data
        offset += header.bytesOfKeyValueData

        gl.profile = gl.Profile.KTX
        val format = gl.find(
                gl.InternalFormat.>(Header.GLInternalFormat),
                static_cast<gli::gl::external_format>(Header.GLFormat),
                static_cast<gli::gl::type_format>(Header.GLType));
        GLI_ASSERT(format != static_cast<format>(gli::FORMAT_INVALID));

        texture::size_type const BlockSize = block_size(format);

        texture Texture(
                detail::get_target(Header),
        format,
        texture::extent_type(
                Header.PixelWidth,
                std::max<texture::size_type>(Header.PixelHeight, 1),
        std::max<texture::size_type>(Header.PixelDepth, 1)),
        std::max<texture::size_type>(Header.NumberOfArrayElements, 1),
        std::max<texture::size_type>(Header.NumberOfFaces, 1),
        std::max<texture::size_type>(Header.NumberOfMipmapLevels, 1));

        for(texture::size_type Level = 0, Levels = Texture.levels(); Level < Levels; ++Level)
        {
            offset += sizeof(std::uint32_t);

            for(texture::size_type Layer = 0, Layers = Texture.layers(); Layer < Layers; ++Layer)
            for(texture::size_type Face = 0, Faces = Texture.faces(); Face < Faces; ++Face)
            {
                texture::size_type const FaceSize = Texture.size(Level);

                std::memcpy(Texture.data(Layer, Face, Level), Data + Offset, FaceSize);

                offset += std::max(BlockSize, glm::ceilMultiple(FaceSize, static_cast<texture::size_type>(4)));
            }
        }

        return Texture;
    }
}