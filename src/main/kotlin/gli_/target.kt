package gli_

/**
 * Created by elect on 02/04/17.
 */

enum class Target {
    INVALID,
    _1D,
    _1D_ARRAY,
    _2D,
    _2D_ARRAY,
    _3D,
    RECT,
    RECT_ARRAY,
    CUBE,
    CUBE_ARRAY;

    val i = ordinal - 1 // invalid -> -1

    val isTarget1d by lazy { this == _1D || this == _1D_ARRAY }
    val isTargetArray by lazy { this == _1D_ARRAY || this == _2D_ARRAY || this == CUBE_ARRAY }
    val isTargetCube by lazy { this == CUBE || this == CUBE_ARRAY }
    val isTargetRect by lazy { this == RECT || this == RECT_ARRAY }

    operator fun rangeTo(that: Target): TargetRange = TargetRange(this, that)

    class TargetRange(override val start: Target, override val endInclusive: Target) : ClosedRange<Target>, Iterable<Target> {
        override fun iterator() = TargetIterator(this)
        override fun contains(value: Target) = value.i in start.i..endInclusive.i
    }

    class TargetIterator(val targetRange: TargetRange) : Iterator<Target> {
        var current = targetRange.start
        var done = false
        override fun next(): Target {
            val res = current
            if (current == targetRange.endInclusive)
                done = true
            else
                current = Target.values()[current.ordinal + 1]
            return res
        }

        override fun hasNext() = if (done) false else current <= targetRange.endInclusive
    }

    companion object {
        val FIRST = _1D
        val LAST = CUBE_ARRAY
        val COUNT = LAST.i - FIRST.i + 1
        infix fun of(int: Int): Target = values().first { it.i == int }
    }
}