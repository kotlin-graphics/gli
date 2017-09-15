package gli

import gli.buffer.destroy
import glm_.BYTES
import glm_.glm
import glm_.vec3.Vec3i
import glm_.vec4.Vec4b
import glm_.vec4.Vec4t
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memAddress
import org.lwjgl.system.MemoryUtil.memByteBuffer
import java.nio.ByteBuffer

/**
 * Created by elect on 08/04/17.
 */

/** Image, representation for a single texture level    */
class Image {

    private var storage: Storage? = null

    var format = Format.INVALID
        private set

    var baseLevel = 0
        private set

    var size = 0
        private set

    private var data: ByteBuffer? = null

    /** Create an empty image instance  */
    constructor()

    /** Create an image object and allocate an image storoge for it.    */
    constructor(format: Format, extent: Vec3i) {
        storage = Storage(format, extent, 1, 1, 1)
        this.format = format
        baseLevel = 0
        data = MemoryUtil.memByteBuffer(MemoryUtil.memAddress(storage!!.data()), storage!!.data().remaining())
        size = computeSize(0)
    }

    /** Create an image object by sharing an existing image storage_linear from another image instance.
     *  This image object is effectively an image view where format can be reinterpreted with a different
     *  compatible image format.
     * For formats to be compatible, the block size of source and destination must match.   */
    constructor(image: Image, format: Format) {
        storage = Storage(image.storage!!)
        this.format = format
        baseLevel = image.baseLevel
        data = MemoryUtil.memByteBuffer(MemoryUtil.memAddress(image.data), image.data!!.remaining())
        size = image.size
        assert(format.blockSize == image.format.blockSize)
    }

    /** Create an image object by sharing an existing image storage_linear from another image instance.
     *  This image object is effectively an image view where the layer, the face and the level allows identifying
     *  a specific subset of the image storage_linear source.
     *  This image object is effectively a image view where the format can be reinterpreted
     *  with a different compatible image format.    */
    constructor(storage: Storage, format: Format, baseLayer: Int, baseFace: Int, baseLevel: Int) {
        this.storage = Storage(storage)
        this.format = format
        this.baseLevel = baseLevel
        data = computeData(baseLayer, baseFace, baseLevel)
        size = computeSize(baseLevel)
    }

    fun computeData(baseLayer: Int, baseFace: Int, baseLevel: Int): ByteBuffer {
        val baseOffset = storage!!.baseOffset(baseLayer, baseFace, baseLevel)
        return memByteBuffer(memAddress(storage!!.data()) + baseOffset, storage!!.data().remaining() - baseOffset)
    }

    fun computeSize(level: Int): Int {
        assert(notEmpty())
        return storage!!.levelSize(level)
    }

    /** Return whether the image instance is empty, no storage_linear or description have been assigned to the instance.    */
    fun empty() = storage?.empty() ?: true

    fun notEmpty() = !empty()

    /** Return the dimensions of an image instance: width, height and depth.    */
    fun extent(): Vec3i {

        assert(notEmpty())

        val srcExtent = storage!!.extent(baseLevel)
        val dstExtent = srcExtent * format.blockExtend / storage!!.blockExtend

        return glm.max(dstExtent, 1)
    }

    /** Return the memory size of an image instance storage_linear in bytes.    */
    fun size(): Int {
        assert(notEmpty())
        return size
    }

    /** Return the number of blocks contained in an image instance storage_linear.
     * genType size must match the block size conresponding to the image format.    */
    //    template <typename genType> TODO
//    size_type size() const;

    /** Return a pointer to the beginning of the image instance data.   */
    fun data(): ByteBuffer? {
        assert(notEmpty())
        return data
    }

    inline fun <reified T> data() = when (T::class) {
        Vec4b::class -> Vec4bData.apply { data = data()!! }
        else -> throw Error()
    }


    /** Clear the entire image storage_linear with zeros    */
    fun clear() {
        assert(notEmpty())
        repeat(storage!!.data().capacity()) { storage!!.data().put(it, 0) }
    }

    /** Clear the entire image storage_linear with Texel which type must match the image storage_linear format block size
     *  If the type of genType doesn't match the type of the image format, no conversion is performed and the data will
     *  be reinterpreted as if is was of the image format.  */
    fun clear(texel: Any) {
        assert(notEmpty())
        when (texel) {
            is Byte -> {
                assert(format.blockSize == Byte.BYTES)
                for (i in 0 until size) data!!.put(i, texel)
            }
            is Long -> {
                assert(format.blockSize == Long.BYTES)
                for (i in 0 until size step Long.BYTES) data!!.putLong(i, texel)
            }
            else -> throw Error()
        }
    }

    /** Load the texel located at TexelCoord coordinates.
     *  It's an error to call this function if the format is compressed.
     *  It's an error if TexelCoord values aren't between [0, dimensions].  */
    fun <T : Number> load(texelCoord: Vec3i, res: Vec4t<T>): Vec4t<T> {
        assert(notEmpty())
        assert(!format.isCompressed)
//        assert(Vec4t.SIZE) TODO
        return res.put(data!!, textel_linear_addressing(extent(), texelCoord) * res.instanceSize())
    }
//
//    /// Store the texel located at TexelCoord coordinates.
//    /// It's an error to call this function if the format is compressed.
//    /// It's an error if TexelCoord values aren't between [0, dimensions].
//    template <typename genType>
//    void store(extent_type const & TexelCoord, genType const & Data);

    override fun equals(other: Any?) = when {
        other !is Image -> false
        extent() != other.extent() -> false
        size != other.size -> false
        else -> memCmp(other.data!!)
    }

    private fun memCmp(b: ByteBuffer): Boolean {
        for(i in 0 until size)
            if(data()!!.get(i) != b[i])
                return false
        return true
    }

    companion object {
        fun textel_linear_addressing(extent: Vec3i, texelCoord: Vec3i): Int {
            assert(glm.all(glm.lessThan(texelCoord, extent)))
            return texelCoord.x + extent.x * (texelCoord.y + extent.y * texelCoord.z)
        }
    }

    fun dispose() = data!!.destroy()
}