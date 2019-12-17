package gli_

import java.net.*

fun uriOf(str: String): URI = ClassLoader.getSystemResource(str).toURI()