// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

import us.bpsm.edn.util.CharClassify.isDigit
import us.bpsm.edn.util.CharClassify.symbolStart
import us.bpsm.edn.util.CharClassify
import java.io.Serializable

/**
 * A Symbol is [Named]. Additionally it obeys the syntactic
 * restrictions defined for
 * [edn Symbols](https://github.com/edn-format/edn#symbols).
 */
class Symbol private constructor(prefix: String, name: String) : Named, Comparable<Symbol?>, Serializable {
    /**
     * {@inheritDoc}
     */
    val prefix: String?

    /**
     * {@inheritDoc}
     */
    val name: String

    @Override
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + getClass().getName().hashCode()
        result = prime * result + name.hashCode()
        result = prime * result + (prefix?.hashCode() ?: 0)
        return result
    }

    @Override
    override fun equals(obj: Object?): Boolean {
        if (this == obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (getClass() !== obj.getClass()) {
            return false
        }
        val other = obj as Symbol
        if (!name.equals(other.name)) {
            return false
        }
        if (prefix == null) {
            if (other.prefix != null) {
                return false
            }
        } else if (!prefix.equals(other.prefix)) {
            return false
        }
        return true
    }

    @Override
    override fun toString(): String {
        return if (prefix!!.length() === 0) {
            name
        } else prefix.toString() + "/" + name
    }

    operator fun compareTo(right: Symbol): Int {
        val cmp = prefix!!.compareTo(right.prefix!!)
        return if (cmp != 0) cmp else name.compareTo(right.name)
    }

    companion object {
        /**
         * Provide a Symbol with the given prefix and name.
         *
         * @param prefix
         * An empty String or a non-empty String obeying the
         * restrictions specified by edn. Never null.
         * @param name
         * A non-empty string obeying the restrictions specified by edn.
         * Never null.
         * @return a Symbol, never null.
         */
        fun newSymbol(prefix: String, name: String): Symbol {
            checkArguments(prefix, name)
            return Symbol(prefix, name)
        }

        /**
         * Equivalent to `newSymbol("", name)`.
         *
         * @param name
         * A non-empty string obeying the restrictions specified by edn.
         * Never null.
         * @return a Symbol with the given name and no prefix.
         */
        fun newSymbol(name: String): Symbol {
            return newSymbol(EMPTY, name)
        }

        private fun checkArguments(prefix: String?, name: String?) {
            if (prefix == null) {
                throw EdnException("prefix must not be null.")
            }
            if (name == null) {
                throw EdnException("name must not be null.")
            }
            checkName("name", name)
            if (prefix.length() !== 0) {
                checkName("prefix", prefix)
            }
        }

        private fun checkName(label: String, ident: String) {
            if (ident.length() === 0) {
                throw EdnSyntaxException("The " + label +
                        " must not be empty.")
            }
            val first: Char = ident.charAt(0)
            if (isDigit(first)) {
                throw EdnSyntaxException("The " + label + " '" + ident
                        + "' must not begin with a digit.")
            }
            if (!symbolStart(first)) {
                throw EdnSyntaxException("The " + label + " '" + ident
                        + "' begins with a forbidden character.")
            }
            if ((first == '.' || first == '-')
                    && ident.length() > 1 && isDigit(ident.charAt(1))) {
                throw EdnSyntaxException("The " + label + " '" + ident
                        + "' begins with a '-' or '.' followed by digit, "
                        + "which is forbidden.")
            }
            val n: Int = ident.length()
            for (i in 1 until n) {
                if (!CharClassify.symbolConstituent(ident.charAt(i))) {
                    throw EdnSyntaxException("The " + label + " '" + ident
                            + "' contains the illegal character '"
                            + ident.charAt(i) + "' at offset " + i + ".")
                }
            }
        }
    }

    init {
        this.prefix = if (prefix.length() > 0) prefix else EMPTY
        this.name = name
    }
}