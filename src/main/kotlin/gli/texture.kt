package gli

import glm.set
import glm.vec._3.Vec3i
import java.nio.ByteBuffer

/**
 * Created by GBarbieri on 03.04.2017.
 */

open class Texture {

    private var storage: Storage? = null

    var target = Target.INVALID

    var format = Format.INVALID
        private set

    var baseLayer = 0
        private set
    var maxLayer = 0
        private set

    var baseFace = 0
        private set
    var maxFace = 0
        private set

    var baseLevel = 0
        private set
    var maxLevel = 0
        private set

    var swizzles = Swizzles(Swizzle.ZERO)
        private set
        get() {
            val formatSwizzle = format.formatInfo.swizzles
            return Swizzles(
                    if (field.r.isChannel()) formatSwizzle[field.r.i] else field.r,
                    if (field.g.isChannel()) formatSwizzle[field.g.i] else field.g,
                    if (field.b.isChannel()) formatSwizzle[field.b.i] else field.b,
                    if (field.a.isChannel()) formatSwizzle[field.a.i] else field.a)
        }

    /** Create an empty texture instance    */
    constructor()

    /** Create a texture object and allocate a texture storage for it
     * @param target Type/Shape of the texture storage_linear
     * @param format Texel format
     * @param extent Size of the texture: width, height and depth.
     * @param layers Number of one-dimensional or two-dimensional images of identical size and format
     * @param faces 6 for cube map textures otherwise 1.
     * @param levels Number of images in the texture mipmap chain.
     * @param swizzles A mechanism to swizzle the components of a texture before they are applied according to the texture environment.
     */
    constructor(target: Target, format: Format, extent: Vec3i, layers: Int, faces: Int, levels: Int,
                swizzles: Swizzles = Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA)) {

        this.target = target
        this.format = format
        baseLayer = 0; maxLayer = layers - 1
        baseFace = 0; maxFace = faces - 1
        baseLevel = 0; maxLevel = levels - 1
        this.swizzles = swizzles

        assert(target != Target.CUBE || (target == Target.CUBE && extent.x == extent.y))
        assert(target != Target.CUBE_ARRAY || (target == Target.CUBE_ARRAY && extent.x == extent.y))
    }

    /** Create a texture object by sharing an existing texture storage_type from another texture instance.
     * This texture object is effectively a texture view where the layer, the face and the level allows identifying
     * a specific subset of the texture storage_linear source.
     * This texture object is effectively a texture view where the target and format can be reinterpreted with a
     * different compatible texture target and texture format.
     */
    constructor(texture: Texture, target: Target, format: Format,
                baseLayer: Int, maxLayer: Int, baseFace: Int, maxFace: Int, baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA)) {

        this.storage = texture.storage
        this.target = target
        this.format = format
        this.baseLayer = baseLayer; this.maxLayer = maxLayer
        this.baseFace = baseFace; this.maxFace = maxFace
        this.baseLevel = baseLevel; this.maxLevel = maxLevel
        this.swizzles = swizzles

        assert(format.blockSize == texture.format.blockSize)
        assert(target != Target._1D || (target == Target._1D && layers() == 1 && faces() == 1 && extent().y == 1 && extent().z == 1))
        assert(target != Target._1D_ARRAY || (target == Target._1D_ARRAY && layers() >= 1 && faces() == 1 && extent().y == 1 && extent().z == 1))
        assert(target != Target._2D || (target == Target._2D && layers() == 1 && faces() == 1 && extent().y >= 1 && extent().z == 1))
        assert(target != Target._2D_ARRAY || (target == Target._2D_ARRAY && layers() >= 1 && faces() == 1 && extent().y >= 1 && extent().z == 1))
        assert(target != Target._3D || (target == Target._3D && layers() == 1 && faces() == 1 && extent().y >= 1 && extent().z >= 1))
        assert(target != Target.CUBE || (target == Target.CUBE && layers() == 1 && faces() >= 1 && extent().y >= 1 && extent().z == 1))
        assert(target != Target.CUBE_ARRAY || (target == Target.CUBE_ARRAY && layers() >= 1 && faces() >= 1 && extent().y >= 1 && extent().z == 1))
    }

    /** Create a texture object by sharing an existing texture storage_type from another texture instance.
     * This texture object is effectively a texture view where the target and format can be reinterpreted
     * with a different compatible texture target and texture format.  */
    constructor(texture: Texture, target: Target, format: Format,
                swizzles: Swizzles = Swizzles(Swizzle.RED, Swizzle.GREEN, Swizzle.BLUE, Swizzle.ALPHA)) {

        storage = texture.storage
        this.target = target
        this.format = format
        baseLayer = texture.baseLayer; maxLayer = texture.maxLayer
        baseFace = texture.baseFace; maxFace = texture.maxFace
        baseLevel = texture.baseLevel; maxLevel = texture.maxLevel
        this.swizzles = swizzles

        if (empty()) return

        assert(target != Target._1D || (target == Target._1D && layers() == 1 && faces() == 1 && extent().y == 1 && extent().z == 1))
        assert(target != Target._1D_ARRAY || (target == Target._1D_ARRAY && layers() >= 1 && faces() == 1 && extent().y == 1 && extent().z == 1))
        assert(target != Target._2D || (target == Target._2D && layers() == 1 && faces() == 1 && extent().y >= 1 && extent().z == 1))
        assert(target != Target._2D_ARRAY || (target == Target._2D_ARRAY && layers() >= 1 && faces() == 1 && extent().y >= 1 && extent().z == 1))
        assert(target != Target._3D || (target == Target._3D && layers() == 1 && faces() == 1 && extent().y >= 1 && extent().z >= 1))
        assert(target != Target.CUBE || (target == Target.CUBE && layers() == 1 && faces() >= 1 && extent().y >= 1 && extent().z == 1))
        assert(target != Target.CUBE_ARRAY || (target == Target.CUBE_ARRAY && layers() >= 1 && faces() >= 1 && extent().y >= 1 && extent().z == 1))
    }

    fun empty() = storage?.empty() ?: true

    fun layers() = if (empty()) 0 else maxLayer - baseLayer + 1
    fun faces() = if (empty()) 0 else maxFace - baseFace + 1
    fun levels() = if (empty()) 0 else maxLevel - baseLayer + 1

    fun size(): Int {
        assert(!empty())
        return storage!!.size()
    }

    fun size(level: Int): Int {
        assert(!empty())
        assert(level in 0..levels())
        return storage!!.levelSize(level)
    }

    fun data(): ByteBuffer {
        assert(!empty())
        return storage!!.data()
    }

    fun data(layer: Int, face: Int, level: Int): ByteBuffer {
        assert((!empty()))
        assert(layer >= 0 && layer < layers() && face >= 0 && face < faces() && level >= 0 && level < levels())
        return storage!!.data(layer, face, level)
    }

    fun extent(level: Int = 0): Vec3i {
        assert(!empty())
        assert(level in 0 until levels())
        return storage!!.extent(level)
    }

    fun clear() {
        val data = data()
        for (i in 0..data.capacity())
            data[i] = 0
    }

    fun copy(textureSrc: Texture,
             layerSrc: Int, faceSrc: Int, levelSrc: Int,
             layerDst: Int, faceDst: Int, levelDst: Int) {

        assert(size(levelDst) == textureSrc.size(levelSrc))
        assert(layerSrc < textureSrc.layers())
        assert(layerDst < layers())
        assert(faceSrc < textureSrc.faces())
        assert(faceDst < faces())
        assert(levelSrc < textureSrc.levels())
        assert(levelDst < levels())

        val dst = data()
        val offset = storage!!.baseOffset(layerDst, faceDst, levelDst)
        val src = textureSrc.data(layerSrc, faceSrc, levelSrc)
        for (i in 0..size(levelDst))
            dst[offset + i] = src[i]
    }
}