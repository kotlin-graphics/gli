package gli.buffer

import com.jogamp.opengl.util.GLBuffers
import glm_.BYTES
import java.nio.*

/**
 * Created by elect on 05/03/17.
 */


internal fun FloatArray.toFloatBuffer(): FloatBuffer = GLBuffers.newDirectFloatBuffer(this)

internal fun FloatArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Float.BYTES)
    forEachIndexed { i, float -> res.putFloat(i * Float.BYTES, float) }
    return res
}

internal fun DoubleArray.toDoubleBuffer(): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(this)
internal fun DoubleArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Double.BYTES)
    forEachIndexed { i, double -> res.putDouble(i * Double.BYTES, double) }
    return res
}

internal fun ByteArray.toByteBuffer(): ByteBuffer = GLBuffers.newDirectByteBuffer(this)

internal fun ShortArray.toShortBuffer(): ShortBuffer = GLBuffers.newDirectShortBuffer(this)
internal fun ShortArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Short.BYTES)
    forEachIndexed { i, short -> res.putShort(i * Short.BYTES, short) }
    return res
}

internal fun IntArray.toIntBuffer(): IntBuffer = GLBuffers.newDirectIntBuffer(this)
internal fun IntArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Int.BYTES)
    forEachIndexed { i, int -> res.putInt(i * Int.BYTES, int) }
    return res
}

internal fun LongArray.toLongBuffer(): LongBuffer = GLBuffers.newDirectLongBuffer(this)
internal fun LongArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Long.BYTES)
    forEachIndexed { i, long -> res.putLong(i * Long.BYTES, long) }
    return res
}

internal fun floatBufferOf(vararg floats: Float): FloatBuffer = GLBuffers.newDirectFloatBuffer(floats)
internal fun floatBufferOf(vararg elements: Number): FloatBuffer = GLBuffers.newDirectFloatBuffer(elements.map(Number::toFloat).toFloatArray())

internal fun doubleBufferOf(vararg elements: Double): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(elements)
internal fun doubleBufferOf(vararg elements: Number): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(elements.map(Number::toDouble).toDoubleArray())

internal fun byteBufferOf(vararg elements: Byte): ByteBuffer = GLBuffers.newDirectByteBuffer(elements)
internal fun byteBufferOf(vararg elements: Number): ByteBuffer = GLBuffers.newDirectByteBuffer(elements.map(Number::toByte).toByteArray())

internal fun shortBufferOf(vararg elements: Short): ShortBuffer = GLBuffers.newDirectShortBuffer(elements)
internal fun shortBufferOf(vararg elements: Number): ShortBuffer = GLBuffers.newDirectShortBuffer(elements.map(Number::toShort).toShortArray())

internal fun intBufferOf(vararg elements: Int): IntBuffer = GLBuffers.newDirectIntBuffer(elements)
internal fun intBufferOf(vararg elements: Number): IntBuffer = GLBuffers.newDirectIntBuffer(elements.map(Number::toInt).toIntArray())

internal fun longBufferOf(vararg elements: Long): LongBuffer = GLBuffers.newDirectLongBuffer(elements)
internal fun longBufferOf(vararg elements: Number): LongBuffer = GLBuffers.newDirectLongBuffer(elements.map(Number::toLong).toLongArray())


internal fun floatBufferBig(size: Int): FloatBuffer = GLBuffers.newDirectFloatBuffer(size)
internal fun doubleBufferBig(size: Int): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(size)

internal fun byteBufferBig(size: Int): ByteBuffer = GLBuffers.newDirectByteBuffer(size)
internal fun shortBufferBig(size: Int): ShortBuffer = GLBuffers.newDirectShortBuffer(size)
internal fun intBufferBig(size: Int): IntBuffer = GLBuffers.newDirectIntBuffer(size)
internal fun longBufferBig(size: Int): LongBuffer = GLBuffers.newDirectLongBuffer(size)


// i.e: clear color buffer
internal fun FloatBuffer.put(vararg floats: Float): FloatBuffer {
    floats.forEachIndexed { i, f -> put(i, f) }
    return this
}


internal fun byteBuffersBig(sizeA: Int, sizeB: Int) = Pair(byteBufferBig(sizeA), byteBufferBig(sizeB))
internal fun byteBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(byteBufferBig(sizeA), byteBufferBig(sizeB), byteBufferBig(sizeC))
//internal fun byteBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(byteBufferBig(sizeA), byteBufferBig(sizeB), byteBufferBig(sizeC), byteBufferBig(sizeD))
//internal fun byteBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(byteBufferBig(sizeA), byteBufferBig(sizeB), byteBufferBig(sizeC), byteBufferBig(sizeD), byteBufferBig(sizeE))

internal fun shortBuffersBig(sizeA: Int, sizeB: Int) = Pair(shortBufferBig(sizeA), shortBufferBig(sizeB))
internal fun shortBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(shortBufferBig(sizeA), shortBufferBig(sizeB), shortBufferBig(sizeC))
//internal fun shortBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(shortBufferBig(sizeA), shortBufferBig(sizeB), shortBufferBig(sizeC), shortBufferBig(sizeD))
//internal fun shortBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(shortBufferBig(sizeA), shortBufferBig(sizeB), shortBufferBig(sizeC), shortBufferBig(sizeD), shortBufferBig(sizeE))

internal fun intBuffersBig(sizeA: Int, sizeB: Int) = Pair(intBufferBig(sizeA), intBufferBig(sizeB))
internal fun intBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(intBufferBig(sizeA), intBufferBig(sizeB), intBufferBig(sizeC))
//internal fun intBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(intBufferBig(sizeA), intBufferBig(sizeB), intBufferBig(sizeC), intBufferBig(sizeD))
//internal fun intBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(intBufferBig(sizeA), intBufferBig(sizeB), intBufferBig(sizeC), intBufferBig(sizeD), intBufferBig(sizeE))

internal fun longBuffersBig(sizeA: Int, sizeB: Int) = Pair(longBufferBig(sizeA), longBufferBig(sizeB))
internal fun longBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(longBufferBig(sizeA), longBufferBig(sizeB), longBufferBig(sizeC))
//internal fun longBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(longBufferBig(sizeA), longBufferBig(sizeB), longBufferBig(sizeC), longBufferBig(sizeD))
//internal fun longBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(longBufferBig(sizeA), longBufferBig(sizeB), longBufferBig(sizeC), longBufferBig(sizeD), longBufferBig(sizeE))

internal fun floatBuffersBig(sizeA: Int, sizeB: Int) = Pair(floatBufferBig(sizeA), floatBufferBig(sizeB))
internal fun floatBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(floatBufferBig(sizeA), floatBufferBig(sizeB), floatBufferBig(sizeC))
//internal fun floatBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(floatBufferBig(sizeA), floatBufferBig(sizeB), floatBufferBig(sizeC), floatBufferBig(sizeD))
//internal fun floatBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(floatBufferBig(sizeA), floatBufferBig(sizeB), floatBufferBig(sizeC), floatBufferBig(sizeD), floatBufferBig(sizeE))

internal fun doubleBuffersBig(sizeA: Int, sizeB: Int) = Pair(doubleBufferBig(sizeA), doubleBufferBig(sizeB))
internal fun doubleBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(doubleBufferBig(sizeA), doubleBufferBig(sizeB), doubleBufferBig(sizeC))
//internal fun doubleBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(doubleBufferBig(sizeA), doubleBufferBig(sizeB), doubleBufferBig(sizeC), doubleBufferBig(sizeD))
//internal fun doubleBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(doubleBufferBig(sizeA), doubleBufferBig(sizeB), doubleBufferBig(sizeC), doubleBufferBig(sizeD), doubleBufferBig(sizeE))
