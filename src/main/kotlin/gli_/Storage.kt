package gli_

import kool.adr
import kool.free
import glm_.glm
import glm_.size
import glm_.vec1.Vec1i
import glm_.vec2.Vec2i
import glm_.vec3.Vec3i
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * Created by GBarbieri on 03.04.2017.
 */

class Storage {

    var layers = 0
        private set
    var faces = 0
        private set
    var levels = 0
        private set
    var blockSize = 0
        private set

    private var blockCount = Vec3i(0)

    var blockExtent = Vec3i(0)
        private set

    private var extent = Vec3i(0)

    private var data: ByteBuffer? = null

    constructor()
    constructor(storage: Storage) {
        layers = storage.layers
        faces = storage.faces
        levels = storage.levels
        blockSize = storage.blockSize
        blockCount = Vec3i(storage.blockCount)
        blockExtent = Vec3i(storage.blockExtent)
        extent = Vec3i(storage.extent)
        data = MemoryUtil.memByteBuffer(storage.data!!.adr, storage.data!!.remaining())
    }

    constructor(format: Format, extent: Vec3i, layers: Int, faces: Int, levels: Int) {
        this.layers = layers
        this.faces = faces
        this.levels = levels
        this.blockSize = format.blockSize
        this.blockCount = glm.max(extent / format.blockExtend, 1)
        this.blockExtent = format.blockExtend
        this.extent = extent

        assert(layers >= 0 && faces >= 0 && levels >= 0)
        assert(glm.all(glm.greaterThan(extent, Vec3i(0))))

        data = MemoryUtil.memCalloc(layerSize(0, faces - 1, 0, levels - 1) * layers)
    }

    fun empty() = data == null
    fun notEmpty() = data != null

    fun blockCount(level: Int): Vec3i {
        assert(level in 0 until levels)
        return glm.ceilMultiple(extent(level), blockExtent) / blockExtent
    }

    fun extent(level: Int): Vec3i {
        assert(level in 0 until levels)
        return glm.max(extent shr level, 1)
    }

    fun size() = data!!.size

    fun data() = data!!

    inline fun <reified T> data() = getReinterpreter<T>(T::class).apply { data = data() }

    /** Compute the relative memory offset to access the data for a specific layer, face and level  */
    fun baseOffset(layer: Int, face: Int, level: Int): Int {

        assert(notEmpty())
        assert(layer in 0 until layers && face in 0 until faces && level in 0 until levels)

        val layerSize = layerSize(0, faces - 1, 0, levels - 1)
        val faceSize = faceSize(0, levels - 1)

        return layerSize * layer + faceSize * face + (0 until level).sumBy { levelSize(it) }
    }

    fun imageOffset(coord: Int, extend: Int): Int {
        assert(coord <= extend)
        return coord
    }

    fun imageOffset(coord: Vec1i, extend: Vec1i): Int {
        assert(glm.all(glm.lessThan(coord, extend)))
        return coord.x
    }

    fun imageOffset(coord: Vec2i, extend: Vec2i): Int {
        assert(glm.all(glm.lessThan(coord, extend)))
        return coord.x + coord.y * extend.x
    }

    fun imageOffset(coord: Vec3i, extent: Vec3i): Int {
        assert(glm.all(glm.lessThan(coord, extent)))
        return coord.x + coord.y * extent.x + coord.z * extent.x * extent.y
    }

    /** Copy a subset of a specific image of a texture  */
    fun copy(storageSrc: Storage,
             layerSrc: Int, faceSrc: Int, levelSrc: Int, blockIndexSrc: Vec3i,
             layerDst: Int, faceDst: Int, levelDst: Int, blockIndexDst: Vec3i,
             blockCount: Vec3i) {

        val baseOffsetSrc = storageSrc.baseOffset(layerSrc, faceSrc, levelSrc)
        val baseOffsetDst = baseOffset(layerDst, faceDst, levelDst)
        val imageSrc = storageSrc.data!!.adr + baseOffsetSrc
        val imageDst = data!!.adr + baseOffsetDst

        for (blockIndexZ in 0 until blockCount.z)
            for (blockIndexY in 0 until blockCount.y) {

                val blockIndex = Vec3i(0, blockIndexY, blockIndexZ)
                val offsetSrc = storageSrc.imageOffset(blockIndexSrc + blockIndex, storageSrc.extent(levelSrc)) * storageSrc.blockSize
                val offsetDst = imageOffset(blockIndexDst + blockIndex, extent(levelDst)) * blockSize
                val dataSrc = imageSrc + offsetSrc
                val dataDst = imageDst + offsetDst
                memCopy(dataSrc, dataDst, blockSize * blockCount.x)
            }
    }

    fun levelSize(level: Int): Int {
        assert(level in 0 until levels)
        return blockSize * glm.compMul(blockCount(level))
    }

    fun faceSize(baseLevel: Int, maxLevel: Int): Int {

        assert(maxLevel in 0 until levels)
        assert(baseLevel in 0 until levels)
        assert(baseLevel <= maxLevel)
        // The size of a face is the sum of the size of each level.
        return (baseLevel..maxLevel).sumBy { levelSize(it) }
    }

    fun layerSize(baseFace: Int, maxFace: Int, baseLevel: Int, maxLevel: Int): Int {

        assert(maxFace in 0 until faces)
        assert(baseFace in 0 until faces)
        assert(maxLevel in 0 until levels)
        assert(baseLevel in 0 until levels)
        // The size of a layer is the sum of the size of each face. All the faces have the same size.
        return faceSize(baseLevel, maxLevel) * (maxFace - baseFace + 1)
    }

    fun dispose() = data?.free()

    override fun equals(other: Any?): Boolean {
        return if (other !is Storage) false
        else layers == other.layers &&
                faces == other.faces &&
                levels == other.levels &&
                blockSize == other.blockSize &&
                blockCount == other.blockCount &&
                blockExtent == other.blockExtent &&
                extent == other.extent &&
                data == other.data
    }

    override fun hashCode(): Int {
        var result = layers
        result = 31 * result + faces
        result = 31 * result + levels
        result = 31 * result + blockSize
        result = 31 * result + blockCount.hashCode()
        result = 31 * result + blockExtent.hashCode()
        result = 31 * result + extent.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }
}