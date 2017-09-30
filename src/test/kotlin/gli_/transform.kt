//package gli_
//
//import glm_.vec1.Vec1i
//import glm_.vec4.Vec4ub
//import io.kotlintest.specs.StringSpec
//
//class transform : StringSpec() {
//
//    init {
//
//        transformTexture1d()
//        transform_array::test < gli::texture1d_array > ()
//        transform::test < gli::texture2d > ()
//        transform_array::test < gli::texture2d_array > ()
//        transform::test < gli::texture3d > ()
//        transform::test < gli::texture_cube > ()
//        transform_array::test < gli::texture_cube_array > ()
//    }
//
//    fun average(a: Vec4ub, b: Vec4ub) = Vec4ub((a + b) / 2)
//
//    fun transformTexture1d() {
//
//        val textureA=Texture1d (Format.RGBA8_UNORM_PACK8, Vec1i(4))
//        textureA clear Vec4ub(255, 127, 0, 255)
//        val textureB =Texture1d(Format.RGBA8_UNORM_PACK8, Vec1i(4))
//        textureB clear Vec4ub(255, 127, 64, 192)
//        val textureO=Texture1d (Format.RGBA8_UNORM_PACK8, Vec1i(4))
//
//        gli::transform < gli::u8vec4 > (textureO, TextureA, TextureB, average)
//
//        gli::u8vec4 const * const data = textureO.template data < gli ::u8vec4 > ()
//        for (gli:: texture1d::size_type TexelIndex = 0, TexelCount = TextureO.template size<gli::u8vec4>(); TexelIndex < TexelCount; ++TexelIndex)
//        {
//            *(data + TexelIndex) == gli::u8vec4(255, 127, 32, 223) ? 0 : 1
//            GLI_ASSERT(!Error)
//        }
//    }
//
//    namespace transform_array
//    {
//        template < typename texture_type >
//        int test ()
//        {
//            int Error = 0
//
//            texture_type TextureA (gli::FORMAT_RGBA8_UNORM_PACK8, typename texture_type::extent_type(4), 2)
//            TextureA.clear(gli::u8vec4(255, 127, 0, 255))
//            texture_type TextureB (gli::FORMAT_RGBA8_UNORM_PACK8, typename texture_type::extent_type(4), 2)
//            TextureB.clear(gli::u8vec4(255, 127, 64, 192))
//            texture_type TextureO (gli::FORMAT_RGBA8_UNORM_PACK8, typename texture_type::extent_type(4), 2)
//
//            gli::transform < gli::u8vec4 > (TextureO, TextureA, TextureB, average)
//
//            gli::u8vec4 const * const data = TextureO.template data < gli ::u8vec4 > ()
//            for (typename texture_type ::size_type TexelIndex = 0, TexelCount = TextureO.template size<gli::u8vec4>(); TexelIndex < TexelCount; ++TexelIndex)
//            {
//                *(data + TexelIndex) == gli::u8vec4(255, 127, 32, 223) ? 0 : 1
//                GLI_ASSERT(!Error)
//            }
//
//            return Error
//        }
//    }
//}