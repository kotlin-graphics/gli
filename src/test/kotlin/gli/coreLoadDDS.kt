package gli

import io.kotlintest.specs.StringSpec

/**
 * Created by GBarbieri on 05.04.2017.
 */

class coreLoadDDS : StringSpec() {

    init {

        "load dds" {

            val params = arrayListOf(
                    Pair("kueken7_bgrx8_unorm.dds", Format.BGR8_UNORM_PACK32))

            params.forEach { test(it) }
        }


    }

    fun test(params: Pair<String, Format>) {
        val textureA = gli.loadDDS(javaClass.getResource("/" + params.first).toURI())
    }
}