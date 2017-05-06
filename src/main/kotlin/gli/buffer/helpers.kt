package gli.buffer

import com.jogamp.opengl.util.GLBuffers
import glm.BYTES
import java.nio.*

/**
 * Created by elect on 05/03/17.
 */


fun FloatArray.toFloatBuffer(): FloatBuffer = GLBuffers.newDirectFloatBuffer(this)

fun FloatArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Float.BYTES)
    forEachIndexed { i, float -> res.putFloat(i * Float.BYTES, float) }
    return res
}

fun DoubleArray.toDoubleBuffer(): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(this)
fun DoubleArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Double.BYTES)
    forEachIndexed { i, double -> res.putDouble(i * Double.BYTES, double) }
    return res
}

fun ByteArray.toByteBuffer(): ByteBuffer = GLBuffers.newDirectByteBuffer(this)

fun ShortArray.toShortBuffer(): ShortBuffer = GLBuffers.newDirectShortBuffer(this)
fun ShortArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Short.BYTES)
    forEachIndexed { i, short -> res.putShort(i * Short.BYTES, short) }
    return res
}

fun IntArray.toIntBuffer(): IntBuffer = GLBuffers.newDirectIntBuffer(this)
fun IntArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Int.BYTES)
    forEachIndexed { i, int -> res.putInt(i * Int.BYTES, int) }
    return res
}

fun LongArray.toLongBuffer(): LongBuffer = GLBuffers.newDirectLongBuffer(this)
fun LongArray.toByteBuffer(): ByteBuffer {
    val res = byteBufferBig(size * Long.BYTES)
    forEachIndexed { i, long -> res.putLong(i * Long.BYTES, long) }
    return res
}

fun floatBufferOf(vararg floats: Float): FloatBuffer = GLBuffers.newDirectFloatBuffer(floats)
fun floatBufferOf(vararg elements: Number): FloatBuffer = GLBuffers.newDirectFloatBuffer(elements.map(Number::toFloat).toFloatArray())

fun doubleBufferOf(vararg elements: Double): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(elements)
fun doubleBufferOf(vararg elements: Number): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(elements.map(Number::toDouble).toDoubleArray())

fun byteBufferOf(vararg elements: Byte): ByteBuffer = GLBuffers.newDirectByteBuffer(elements)
fun byteBufferOf(vararg elements: Number): ByteBuffer = GLBuffers.newDirectByteBuffer(elements.map(Number::toByte).toByteArray())

fun shortBufferOf(vararg elements: Short): ShortBuffer = GLBuffers.newDirectShortBuffer(elements)
fun shortBufferOf(vararg elements: Number): ShortBuffer = GLBuffers.newDirectShortBuffer(elements.map(Number::toShort).toShortArray())

fun intBufferOf(vararg elements: Int): IntBuffer = GLBuffers.newDirectIntBuffer(elements)
fun intBufferOf(vararg elements: Number): IntBuffer = GLBuffers.newDirectIntBuffer(elements.map(Number::toInt).toIntArray())

fun longBufferOf(vararg elements: Long): LongBuffer = GLBuffers.newDirectLongBuffer(elements)
fun longBufferOf(vararg elements: Number): LongBuffer = GLBuffers.newDirectLongBuffer(elements.map(Number::toLong).toLongArray())


fun floatBufferBig(size: Int): FloatBuffer = GLBuffers.newDirectFloatBuffer(size)
fun doubleBufferBig(size: Int): DoubleBuffer = GLBuffers.newDirectDoubleBuffer(size)

fun byteBufferBig(size: Int): ByteBuffer = GLBuffers.newDirectByteBuffer(size)
fun shortBufferBig(size: Int): ShortBuffer = GLBuffers.newDirectShortBuffer(size)
fun intBufferBig(size: Int): IntBuffer = GLBuffers.newDirectIntBuffer(size)
fun longBufferBig(size: Int): LongBuffer = GLBuffers.newDirectLongBuffer(size)


// i.e: clear color buffer
fun FloatBuffer.put(vararg floats: Float): FloatBuffer {
    floats.forEachIndexed { i, f -> put(i, f) }
    return this
}


fun byteBuffersBig(sizeA: Int, sizeB: Int) = Pair(byteBufferBig(sizeA), byteBufferBig(sizeB))
fun byteBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(byteBufferBig(sizeA), byteBufferBig(sizeB), byteBufferBig(sizeC))
//fun byteBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(byteBufferBig(sizeA), byteBufferBig(sizeB), byteBufferBig(sizeC), byteBufferBig(sizeD))
//fun byteBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(byteBufferBig(sizeA), byteBufferBig(sizeB), byteBufferBig(sizeC), byteBufferBig(sizeD), byteBufferBig(sizeE))

fun shortBuffersBig(sizeA: Int, sizeB: Int) = Pair(shortBufferBig(sizeA), shortBufferBig(sizeB))
fun shortBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(shortBufferBig(sizeA), shortBufferBig(sizeB), shortBufferBig(sizeC))
//fun shortBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(shortBufferBig(sizeA), shortBufferBig(sizeB), shortBufferBig(sizeC), shortBufferBig(sizeD))
//fun shortBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(shortBufferBig(sizeA), shortBufferBig(sizeB), shortBufferBig(sizeC), shortBufferBig(sizeD), shortBufferBig(sizeE))

fun intBuffersBig(sizeA: Int, sizeB: Int) = Pair(intBufferBig(sizeA), intBufferBig(sizeB))
fun intBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(intBufferBig(sizeA), intBufferBig(sizeB), intBufferBig(sizeC))
//fun intBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(intBufferBig(sizeA), intBufferBig(sizeB), intBufferBig(sizeC), intBufferBig(sizeD))
//fun intBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(intBufferBig(sizeA), intBufferBig(sizeB), intBufferBig(sizeC), intBufferBig(sizeD), intBufferBig(sizeE))

fun longBuffersBig(sizeA: Int, sizeB: Int) = Pair(longBufferBig(sizeA), longBufferBig(sizeB))
fun longBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(longBufferBig(sizeA), longBufferBig(sizeB), longBufferBig(sizeC))
//fun longBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(longBufferBig(sizeA), longBufferBig(sizeB), longBufferBig(sizeC), longBufferBig(sizeD))
//fun longBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(longBufferBig(sizeA), longBufferBig(sizeB), longBufferBig(sizeC), longBufferBig(sizeD), longBufferBig(sizeE))

fun floatBuffersBig(sizeA: Int, sizeB: Int) = Pair(floatBufferBig(sizeA), floatBufferBig(sizeB))
fun floatBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(floatBufferBig(sizeA), floatBufferBig(sizeB), floatBufferBig(sizeC))
//fun floatBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(floatBufferBig(sizeA), floatBufferBig(sizeB), floatBufferBig(sizeC), floatBufferBig(sizeD))
//fun floatBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(floatBufferBig(sizeA), floatBufferBig(sizeB), floatBufferBig(sizeC), floatBufferBig(sizeD), floatBufferBig(sizeE))

fun doubleBuffersBig(sizeA: Int, sizeB: Int) = Pair(doubleBufferBig(sizeA), doubleBufferBig(sizeB))
fun doubleBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int) = Triple(doubleBufferBig(sizeA), doubleBufferBig(sizeB), doubleBufferBig(sizeC))
//fun doubleBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int) = Quadruple(doubleBufferBig(sizeA), doubleBufferBig(sizeB), doubleBufferBig(sizeC), doubleBufferBig(sizeD))
//fun doubleBuffersBig(sizeA: Int, sizeB: Int, sizeC: Int, sizeD: Int, sizeE: Int) = Quintuple(doubleBufferBig(sizeA), doubleBufferBig(sizeB), doubleBufferBig(sizeC), doubleBufferBig(sizeD), doubleBufferBig(sizeE))
