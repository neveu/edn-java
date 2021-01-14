// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

/**
 * EdnSyntaxException is thrown when a syntax error is discovered
 * during parsing.
 */
class EdnSyntaxException : EdnException {
    constructor() : super() {}
    constructor(msg: String?, cause: Throwable?) : super(msg, cause) {}
    constructor(msg: String?) : super(msg) {}
    constructor(cause: Throwable?) : super(cause) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}