// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

import java.io.InvalidObjectException

/**
 * A Keyword is [Named]. Additionally it obeys the syntactic
 * restrictions defined for [edn Keywords](https://github.com/edn-format/edn#keywords).
 *
 *
 * Note: Keywords print with a leading colon, but this is not part of the
 * keyword's name:
 *
 * <pre>
 * `// For the keyword ":foo/bar"
 * Keyword k = newKeyword("foo", "bar");
 * k.getName()   => "bar"
 * k.getPrefix() => "foo"
 * k.toString()  => ":foo/bar"`
</pre> *
 */
class Keyword private constructor(sym: Symbol?) : Named, Comparable<Keyword?>, Serializable {
    private val sym: Symbol

    /** {@inheritDoc}  */
    val prefix: String
        get() = sym.getPrefix()

    /** {@inheritDoc}  */
    val name: String
        get() = sym.getName()

    override fun toString(): String {
        return ":" + sym.toString()
    }

    operator fun compareTo(o: Keyword): Int {
        return if (this == o) {
            0
        } else sym.compareTo(o.sym)
    }

    private fun writeReplace(): Object {
        return SerializationProxy(sym)
    }

    @Throws(InvalidObjectException::class)
    private fun readObject(stream: ObjectInputStream) {
        throw InvalidObjectException("only proxy can be serialized")
    }

    private class SerializationProxy(sym: Symbol) : Serializable {
        private val sym: Symbol
        @Throws(ObjectStreamException::class)
        private fun readResolve(): Object {
            return newKeyword(sym)
        }

        companion object {
            private const val serialVersionUID = 1L
        }

        init {
            this.sym = sym
        }
    }

    companion object {
        fun newKeyword(sym: Symbol?): Keyword {
            return INTERNER.intern(sym, Keyword(sym))
        }

        /**
         * Provide a Keyword with the given prefix and name.
         *
         *
         * Keywords are interned, which means that any two keywords which are equal
         * (by value) will also be identical (by reference).
         *
         * @param prefix
         * An empty String or a non-empty String obeying the restrictions
         * specified by edn. Never null.
         * @param name
         * A non-empty string obeying the restrictions specified by edn.
         * Never null.
         * @return a Keyword, never null.
         */
        fun newKeyword(prefix: String?, name: String?): Keyword {
            return newKeyword(newSymbol(prefix, name))
        }

        /**
         * This is equivalent to `newKeyword("", name)`.
         *
         * @param name
         * A non-empty string obeying the restrictions specified by edn.
         * Never null.
         * @return a Keyword without a prefix, never null.
         * @see .newKeyword
         */
        fun newKeyword(name: String?): Keyword {
            return newKeyword(newSymbol(EMPTY, name))
        }

        private val INTERNER: Interner<Symbol, Keyword> = Interner<Symbol, Keyword>()
    }

    /**
     * Return a Keyword with the same prefix and name as `sym`.
     * @param sym a Symbol, never null
     */
    init {
        if (sym == null) {
            throw NullPointerException()
        }
        this.sym = sym
    }
}