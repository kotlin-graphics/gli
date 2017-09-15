package gli

import gli.buffer.destroy
import glm_.BYTES
import glm_.b
import glm_.set
import glm_.vec3.Vec3i
import glm_.vec4.Vec4b
import glm_.vec4.Vec4ub
import org.lwjgl.system.MemoryUtil.memByteBuffer
import java.nio.ByteBuffer
import kotlin.reflect.KClass

/**
 * Created by GBarbieri on 03.04.2017.
 */

open class Texture {

    protected var storage: Storage? = null

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
        get() {
            val formatSwizzle = format.formatInfo.swizzles
            return with(field) {
                Swizzles(
                        if (r.isChannel()) formatSwizzle[r.i] else r,
                        if (g.isChannel()) formatSwizzle[g.i] else g,
                        if (b.isChannel()) formatSwizzle[b.i] else b,
                        if (a.isChannel()) formatSwizzle[a.i] else a)
            }
        }
        private set

    lateinit var cache: Cache

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
    constructor(target: Target,
                format: Format,
                extent: Vec3i,
                layers: Int,
                faces: Int,
                levels: Int,
                swizzles: Swizzles = Swizzles()
    ) {
        storage = Storage(format, extent, layers, faces, levels)
        this.target = target
        this.format = format
        baseLayer = 0; maxLayer = layers - 1
        baseFace = 0; maxFace = faces - 1
        baseLevel = 0; maxLevel = levels - 1
        this.swizzles = swizzles
        this.cache = Cache(storage!!, format, baseLayer, layers(), baseFace, maxFace, baseLevel, maxLevel)

        assert(target != Target.CUBE || (target == Target.CUBE && extent.x == extent.y))
        assert(target != Target.CUBE_ARRAY || (target == Target.CUBE_ARRAY && extent.x == extent.y))
    }

    /** Create a texture object by sharing an existing texture storage_type from another texture instance.
     * This texture object is effectively a texture view where the layer, the face and the level allows identifying
     * a specific subset of the texture storage_linear source.
     * This texture object is effectively a texture view where the target and format can be reinterpreted with a
     * different compatible texture target and texture format.
     */
    constructor(texture: Texture,
                target: Target,
                format: Format,
                baseLayer: Int, maxLayer: Int,
                baseFace: Int, maxFace: Int,
                baseLevel: Int, maxLevel: Int,
                swizzles: Swizzles = Swizzles()
    ) {
        storage = Storage(texture.storage!!)
        this.target = target
        this.format = format
        this.baseLayer = baseLayer; this.maxLayer = maxLayer
        this.baseFace = baseFace; this.maxFace = maxFace
        this.baseLevel = baseLevel; this.maxLevel = maxLevel
        this.swizzles = swizzles
        cache = Cache(storage!!, format, baseLayer, layers(), baseFace, maxFace, baseLevel, maxLevel)

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
    constructor(texture: Texture,
                target: Target,
                format: Format,
                swizzles: Swizzles = Swizzles()
    ) {
        storage = Storage(texture.storage!!)
        this.target = target
        this.format = format
        baseLayer = texture.baseLayer; maxLayer = texture.maxLayer
        baseFace = texture.baseFace; maxFace = texture.maxFace
        baseLevel = texture.baseLevel; maxLevel = texture.maxLevel
        this.swizzles = swizzles
        cache = Cache(storage!!, format, baseLayer, layers(), baseFace, maxFace, baseLevel, maxLevel)

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
    fun notEmpty() = !empty()

    fun layers() = if (empty()) 0 else maxLayer - baseLayer + 1 // TODO val get() ?
    fun faces() = if (empty()) 0 else maxFace - baseFace + 1
    fun levels() = if (empty()) 0 else maxLevel - baseLevel + 1

    fun size(): Int {
        assert(notEmpty())
        return cache.memorySize
    }

    fun size(level: Int): Int {
        assert(notEmpty())
        assert(level in 0..levels())
        return storage!!.levelSize(level)
    }

    fun data(): ByteBuffer {
        assert(notEmpty())
        return memByteBuffer(cache.getBaseAddress(0, 0, 0), size())
    }

    fun data(layer: Int, face: Int, level: Int): Long {
        assert((notEmpty()))
        assert(layer in 0 until layers() && face in 0 until faces() && level in 0 until levels())
        return cache.getBaseAddress(layer, face, level)
    }

    fun setData(unitOffset: Int, texel: Vec4b) {
        val baseOffset = storage!!.baseOffset(baseLayer, baseFace, baseLevel)
        texel.to(storage!!.data(), baseOffset + unitOffset * Vec4b.size)
    }

    fun getData(unitOffset: Int, texel: Vec4b = Vec4b()): Vec4b {
        val baseOffset = storage!!.baseOffset(baseLayer, baseFace, baseLevel)
        texel.to(storage!!.data(), baseOffset + unitOffset * Vec4b.size)
        return texel
    }

    fun extent(level: Int = 0): Vec3i {
        assert(notEmpty())
        assert(level in 0 until levels())
        return storage!!.extent(level)
    }

    fun clear() = data().run { for (i in 0 until capacity()) set(i, 0) }

    fun clear(texel: Vec4b) {
        assert(notEmpty())
        assert(format.blockSize == Vec4ub.size)
        val data = data()
        for (i in 0 until data.capacity() step 4) {
            data[i] = texel.r
            data[i + 1] = texel.g
            data[i + 2] = texel.b
            data[i + 3] = texel.a
        }
    }

    fun clear(texel: Long) {
        assert(notEmpty())
        assert(format.blockSize == Long.BYTES)
        val data = data()
        for (i in 0 until data.capacity() step Long.BYTES)
            data.putLong(i, texel)
    }

    inline fun <reified T : Any> clear(red: Int, green: Int, blue: Int, alpha: Int) {
        assert(notEmpty())
        val data = data()
        when (T::class) {
            Vec4b::class -> {
                assert(format.blockSize == Vec4b.size)
                val r = red.b
                val g = green.b
                val b = blue.b
                val a = alpha.b
                for (i in 0 until data.capacity() step 4) {
                    data[i] = r
                    data[i + 1] = g
                    data[i + 2] = b
                    data[i + 3] = a
                }
            }
            else -> throw Error()
        }
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
//        for (i in 0 until size(levelDst)) TODO
//            dst[offset + i] = src[i]
    }

    fun swizzles(kClass: KClass<*>, swizzles: Swizzles) = when (kClass) {
        Vec4b::class, Vec4ub::class -> {
            val texel = Vec4b()
            val data = data()
            for (i in 0 until data.capacity() step Vec4b.length) {
                texel.put(data, i)
                for (j in 0 until Vec4b.length)
                    data[i + j] = texel[swizzles[j].i]
            }
        }
        else -> throw Error("unsupported texel type")
    }

    open fun dispose() = storage?.data()?.destroy()

    override fun equals(other: Any?) = when {
        other !is Texture -> false
        empty() && other.empty() -> true
        empty() != other.empty() -> false
        target != other.target -> false
        layers() != other.layers() -> false
        faces() != other.faces() -> false
        levels() != other.levels() -> false
        format != other.format -> false
        size() != other.size() -> false
        else -> equalData(other)
    }

    fun equalData(b: Texture): Boolean {

        assert(size() == b.size())

        if (data() == b.data()) return true

        for (layerIndex in 0 until layers())
            for (faceIndex in 0 until faces())
                for (levelIndex in 0 until levels()) {
                    val size = size(levelIndex)
                    val dataA = memByteBuffer(data(layerIndex, faceIndex, levelIndex), size)
                    val dataB = memByteBuffer(b.data(layerIndex, faceIndex, levelIndex), size)
                    for (i in 0 until size)
                        if (dataA[i] != dataB[i])
                            return false
                }
        return true
    }
}