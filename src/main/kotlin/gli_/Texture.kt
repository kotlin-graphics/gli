package gli_

import glm_.b
import glm_.buffer.adr
import glm_.buffer.free
import glm_.glm
import glm_.set
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
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

    /** Vec1i   */
    constructor(target: Target,
                format: Format,
                extent: Vec1i,
                layers: Int,
                faces: Int,
                levels: Int,
                swizzles: Swizzles = Swizzles()
    ) :
            this(target, format, Vec3i(extent.x, 1, 1), layers, faces, levels, swizzles)

    /** Vec2i   */
    constructor(target: Target,
                format: Format,
                extent: Vec2i,
                layers: Int,
                faces: Int,
                levels: Int,
                swizzles: Swizzles = Swizzles()
    ) :
            this(target, format, Vec3i(extent.x, extent.y, 1), layers, faces, levels, swizzles)

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

    /** JVM Constructor for the C++ constructor by value    */
    constructor(texture: Texture) {

        storage = Storage(texture.storage!!)
        target = texture.target
        format = texture.format
        baseLayer = texture.baseLayer
        maxLayer = texture.maxLayer
        baseFace = texture.baseFace
        maxFace = texture.maxFace
        baseLevel = texture.baseLevel
        maxLevel = texture.maxLevel
        swizzles = Swizzles(texture.swizzles)
        cache = Cache(texture.cache)
    }

    fun empty() = storage?.empty() ?: true
    fun notEmpty() = !empty()

    fun layers() = if (empty()) 0 else maxLayer - baseLayer + 1 // TODO val get() ?
    fun faces() = if (empty()) 0 else maxFace - baseFace + 1
    fun levels() = if (empty()) 0 else maxLevel - baseLevel + 1

    val size: Int
        get() {
            assert(notEmpty())
            return cache.memorySize
        }

    inline fun <reified T> size(): Int {
        val blockSize = getSize(T::class)
        assert(notEmpty() && format.blockSize == blockSize)
        return size / blockSize
    }

    @JvmName("size_")
    inline fun <reified T> size(level: Int): Int {
        val blockData = getSize(T::class)
        assert(notEmpty() && format.blockSize == blockData)

        return size(level) / blockData
    }

    fun size(level: Int): Int {
        assert(notEmpty() && level in 0..levels())
        return cache.memorySize(level)
    }

    inline fun <reified T> data() = getReinterpreter<T>(T::class).apply { data = data() }
    fun data(): ByteBuffer {
        assert(notEmpty())
        return memByteBuffer(cache.baseAddress(0, 0, 0), size)
    }

    inline fun <reified T> data(layer: Int, face: Int, level: Int): reinterpreter<T> = data(T::class, layer, face, level)
    fun <T> data(clazz: KClass<*>, layer: Int, face: Int, level: Int) = getReinterpreter<T>(clazz).apply { data = data(layer, face, level) }
    fun data(layer: Int, face: Int, level: Int): ByteBuffer {
        assert((notEmpty()))
        assert(layer in 0 until layers() && face in 0 until faces() && level in 0 until levels())
        val size = storage!!.levelSize(level)
        return memByteBuffer(cache.baseAddress(layer, face, level), size)
    }

    fun extent(level: Int = 0): Vec3i {
        assert(notEmpty())
        assert(level in 0 until levels())
        return cache.extent(level)
    }

    fun clear() = data().run { for (i in 0 until capacity()) set(i, 0.b) }

    infix fun clear(texel: Any) {
        assert(notEmpty() && format.blockSize == getSize(texel::class))
        _clear(data(), texel)
    }

    fun clear(layer: Int, face: Int, level: Int, blockData: Any) {
        assert(notEmpty() && layer in 0 until layers() && face in 0 until faces() && level in 0 until levels())
        assert(format.blockSize == getSize(blockData::class))
        _clear(data(layer, face, level), blockData)
    }

    fun <T : Any> clear(
            layer: Int, face: Int, level: Int,
            texelOffset: Vec3i, texelExtent: Vec3i,
            blockData: T
    ) {
        val baseOffset = storage!!.baseOffset(layer, face, level)
        val baseAddress = storage!!.data().adr + baseOffset

        val blockOffset = texelOffset / storage!!.blockExtent
        val blockExtent = texelExtent / storage!!.blockExtent + blockOffset
        while (blockOffset.z < blockExtent.z) {
            while (blockOffset.y < blockExtent.y) {
                while (blockOffset.x < blockExtent.x) {
                    val offset = storage!!.imageOffset(blockOffset, extent(level)) * storage!!.blockSize
                    val blockAddress = baseAddress + offset
                    val reinterpreter = getReinterpreter<T>(blockData::class)
                    reinterpreter.data = memByteBuffer(blockAddress, getSize(blockData::class))
                    reinterpreter[0] = blockData

                    blockOffset.x++
                }
                blockOffset.y++
            }
            blockOffset.z++
        }
    }

    /** Copy a specific image of a texture  */
    fun copy(textureSrc: Texture,
             layerSrc: Int, faceSrc: Int, levelSrc: Int,
             layerDst: Int, faceDst: Int, levelDst: Int
    ) {
        assert(size(levelDst) == textureSrc.size(levelSrc))
        assert(layerSrc < textureSrc.layers())
        assert(layerDst < layers())
        assert(faceSrc < textureSrc.faces())
        assert(faceDst < faces())
        assert(levelSrc < textureSrc.levels())
        assert(levelDst < levels())

        memCopy(textureSrc.data(layerSrc, faceSrc, levelSrc).adr,
                data(layerDst, faceDst, levelDst).adr,
                size(levelDst))
    }

    /** Copy a subset of a specific image of a texture  */
    fun copy(
            textureSrc: Texture,
            layerSrc: Int, faceSrc: Int, levelSrc: Int, offsetSrc: Vec3i,
            layerDst: Int, faceDst: Int, levelDst: Int, offsetDst: Vec3i,
            extent: Vec3i
    ) {
        val blockExtent = storage!!.blockExtent
        storage!!.copy(
                textureSrc.storage!!,
                layerSrc, faceSrc, levelSrc, offsetSrc / blockExtent,
                layerDst, faceDst, levelDst, offsetDst / blockExtent,
                extent / blockExtent)
    }

    inline fun <reified T> swizzles(swizzles: Swizzles) = swizzles(T::class, swizzles)
    fun swizzles(clazz: KClass<*>, swizzles: Swizzles) {
        when (clazz) {
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
    }

    fun imageOffset(coord: Vec3i, extent: Vec3i) = storage!!.imageOffset(coord, extent)

    inline fun <reified T> load(texelCoord: Vec3i, layer: Int, face: Int, level: Int): T = load(T::class, texelCoord, layer, face, level)
    fun <T> load(clazz: KClass<*>, texelCoord: Vec3i, layer: Int, face: Int, level: Int): T {
        assert(notEmpty() && !format.isCompressed && format.blockSize == getSize(clazz))
        val imageOffset = imageOffset(texelCoord, extent(level))
        assert(imageOffset < size(level))
        return data<T>(clazz, layer, face, level)[imageOffset]
    }

    //    fun store(texelCoord: Vec1i, layer: Int, face: Int, level: Int, texel: Any) = store(Vec3i(texelCoord.x, 1, 1), layer, face, level, texel) TODO check
//    fun store(texelCoord: Vec2i, layer: Int, face: Int, level: Int, texel: Any) = store(Vec3i(texelCoord.x, texelCoord.y, 1), layer, face, level, texel)
    inline fun <reified T> store(texelCoord: Vec3i, layer: Int, face: Int, level: Int, texel: T) {

        assert(notEmpty() && !format.isCompressed)
        val extent = extent(level)
        assert(glm.all(glm.lessThan(texelCoord, extent)))

        val imageOffset = imageOffset(texelCoord, extent)

        val blockSize = getSize(T::class)
        assert(format.blockSize == blockSize && imageOffset < size(level) / blockSize)
        data<T>(layer, face, level)[imageOffset] = texel
    }

    open fun dispose() = storage?.data()?.free()

    override fun equals(other: Any?) = when {
        other !is Texture -> false
        empty() && other.empty() -> true
        empty() != other.empty() -> false
        target != other.target -> false
        layers() != other.layers() -> false
        faces() != other.faces() -> false
        levels() != other.levels() -> false
        format != other.format -> false
        size != other.size -> false
        else -> equalData(other)
    }

    private fun equalData(b: Texture): Boolean {

        assert(size == b.size)

        if (data() == b.data()) return true

        for (layerIndex in 0 until layers())
            for (faceIndex in 0 until faces())
                for (levelIndex in 0 until levels()) {
                    val size = size(levelIndex)
                    val dataA = data(layerIndex, faceIndex, levelIndex)
                    val dataB = b.data(layerIndex, faceIndex, levelIndex)
                    for (i in 0 until size)
                        if (dataA[i] != dataB[i])
                            return false
                }
        return true
    }

    override fun hashCode(): Int {
        var result = storage?.hashCode() ?: 0
        result = 31 * result + target.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + baseLayer
        result = 31 * result + maxLayer
        result = 31 * result + baseFace
        result = 31 * result + maxFace
        result = 31 * result + baseLevel
        result = 31 * result + maxLevel
        result = 31 * result + cache.hashCode()
        return result
    }
}