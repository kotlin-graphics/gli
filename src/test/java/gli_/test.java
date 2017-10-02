package gli_;

public class test {

    public static void main(String[] args) {


    }

    public static int createTexture(String filename) {

        gli_.gli gli = gli_.gli.INSTANCE;
        Texture texture = gli.load(filename);
        if (texture.empty())
            return 0;

        gli.getGl().setProfile(gli.getGl().INSTANCE.Profile.GL33);
//        val format = gli.gl.translate(texture.format, texture.swizzles)
//        val target = gli.gl.translate(texture.target)
//        assert(texture.format.isCompressed && target == gli.gl.Target._2D)
//
//        val textureName = intBufferBig(1)
//        GL11.glGenTextures(textureName)
//        glBindTexture(target, textureName)
//        glTexParameteri(target, GL12.GL_TEXTURE_BASE_LEVEL, 0)
//        glTexParameteri(target, GL12.GL_TEXTURE_MAX_LEVEL, texture.levels() - 1)
//        glTexParameteriv(target, GL33.GL_TEXTURE_SWIZZLE_RGBA, format.swizzles)
//        glTexStorage2D(target, texture.levels(), format.internal, texture.extent())
//        for (level in 0 until texture.levels()) {
//            glCompressedTexSubImage2D(
//                    target, level, 0, 0, texture.extent(level),
//                    format.internal, texture.data(0, 0, level))
//        }
//        return textureName[0]
    }
}
