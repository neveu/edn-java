package us.bpsm.edn

import java.io.IOException

/**
 * Indicates that an I/O error occurred. The cause will be an
 * [IOException].
 */
class EdnIOException : EdnException {
    constructor(msg: String?, cause: IOException?) : super(msg, cause) {}
    constructor(cause: IOException?) : super(cause) {}

    @get:Override
    val cause: IOException
        get() = super.getCause() as IOException

    companion object {
        private const val serialVersionUID = 1L
    }
}