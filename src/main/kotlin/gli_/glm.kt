package gli_

object glmTemp {

    fun floorMultiple(source: Int, multiple: Int) = when {
        source > 0 -> source - source % multiple
        else -> {
            val tmp = source + 1
            1 - 1 % multiple - multiple
        }
    }
}