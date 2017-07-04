package gli.buffer

import glm_.BYTES
import java.nio.*

/**
 * Created by elect on 05/03/17.
 */


internal fun FloatArray.toFloatBuffer(): FloatBuffer = ByteBuffer.allocateDirect(size * Float.BYTES)
        .order(ByteOrder.nativeOrder()).also { for (i in indices) it.putFloat(i * Float.BYTES, this[i]) }.asFloatBuffer()

internal fun FloatArray.toByteBuffer() = byteBufferBig(size * Float.BYTES).also { for (i in indices) it.putFloat(i * Float.BYTES, this[i]) }


internal fun DoubleArray.toDoubleBuffer(): DoubleBuffer = ByteBuffer.allocateDirect(size * Double.BYTES)
        .order(ByteOrder.nativeOrder()).also { for (i in indices) it.putDouble(i * Double.BYTES, this[i]) }.asDoubleBuffer()

internal fun DoubleArray.toByteBuffer() = byteBufferBig(size * Double.BYTES).also { for (i in indices) it.putDouble(i * Double.BYTES, this[i]) }


internal fun ByteArray.toByteBuffer(): ByteBuffer = ByteBuffer.allocateDirect(size)
        .order(ByteOrder.nativeOrder()).also { for (i in indices) it.put(i, this[i]) }


internal fun ShortArray.toShortBuffer(): ShortBuffer = ByteBuffer.allocateDirect(size * Short.BYTES)
        .order(ByteOrder.nativeOrder()).also { for (i in indices) it.putShort(i * Short.BYTES, this[i]) }.asShortBuffer()

internal fun ShortArray.toByteBuffer() = byteBufferBig(size * Short.BYTES).also { for (i in indices) it.putShort(i * Short.BYTES, this[i]) }


internal fun IntArray.toIntBuffer(): IntBuffer = ByteBuffer.allocateDirect(size * Int.BYTES)
        .order(ByteOrder.nativeOrder()).also { for (i in indices) it.putInt(i * Int.BYTES, this[i]) }.asIntBuffer()

internal fun IntArray.toByteBuffer() = byteBufferBig(size * Int.BYTES).also { for (i in indices) it.putInt(i * Int.BYTES, this[i]) }


internal fun LongArray.toLongBuffer(): LongBuffer = ByteBuffer.allocateDirect(size * Long.BYTES)
        .order(ByteOrder.nativeOrder()).also { for (i in indices) it.putLong(i * Long.BYTES, this[i]) }.asLongBuffer()

internal fun LongArray.toByteBuffer() = byteBufferBig(size * Long.BYTES).also { for (i in indices) it.putLong(i * Long.BYTES, this[i]) }

internal fun floatBufferOf(vararg floats: Float) = floats.toFloatBuffer()
internal fun floatBufferOf(vararg numbers: Number) = numbers.map(Number::toFloat).toFloatArray().toFloatBuffer()

internal fun doubleBufferOf(vararg doubles: Double) = doubles.toDoubleBuffer()
internal fun doubleBufferOf(vararg numbers: Number) = numbers.map(Number::toDouble).toDoubleArray().toDoubleBuffer()

internal fun byteBufferOf(vararg bytes: Byte) = bytes.toByteBuffer()
internal fun byteBufferOf(vararg numbers: Number) = numbers.map(Number::toByte).toByteArray().toByteBuffer()

internal fun shortBufferOf(vararg shorts: Short) = shorts.toShortBuffer()
internal fun shortBufferOf(vararg numbers: Number) = numbers.map(Number::toShort).toShortArray().toShortBuffer()

internal fun intBufferOf(vararg ints: Int) = ints.toIntBuffer()
internal fun intBufferOf(vararg numbers: Number) = numbers.map(Number::toInt).toIntArray().toIntBuffer()

internal fun longBufferOf(vararg longs: Long) = longs.toLongBuffer()
internal fun longBufferOf(vararg numbers: Number) = numbers.map(Number::toLong).toLongArray().toLongBuffer()


internal fun floatBufferBig(size: Int): FloatBuffer = ByteBuffer.allocateDirect(size * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer()
internal fun doubleBufferBig(size: Int): DoubleBuffer = ByteBuffer.allocateDirect(size * Double.BYTES).order(ByteOrder.nativeOrder()).asDoubleBuffer()

internal fun byteBufferBig(size: Int): ByteBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())
internal fun shortBufferBig(size: Int): ShortBuffer = ByteBuffer.allocateDirect(size * Short.BYTES).order(ByteOrder.nativeOrder()).asShortBuffer()
internal fun intBufferBig(size: Int): IntBuffer = ByteBuffer.allocateDirect(size * Int.BYTES).order(ByteOrder.nativeOrder()).asIntBuffer()
internal fun longBufferBig(size: Int): LongBuffer = ByteBuffer.allocateDirect(size * Long.BYTES).order(ByteOrder.nativeOrder()).asLongBuffer()


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
