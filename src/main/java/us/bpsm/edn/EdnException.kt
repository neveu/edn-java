// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

/**
 * EdnException is thrown when something goes wrong during the
 * operation of edn-java.  During parsing, this generally, the
 * indicates some kind of syntax error in the input source
 * (see [EdnSyntaxException]), or an I/O error (see
 * [EdnIOException]).
 */
class EdnException : RuntimeException {
    constructor() : super() {}
    constructor(msg: String?, cause: Throwable?) : super(msg, cause) {}
    constructor(msg: String?) : super(msg) {}
    constructor(cause: Throwable?) : super(cause) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}