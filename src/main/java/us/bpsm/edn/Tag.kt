// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

import java.io.Serializable
import us.bpsm.edn.Symbol.newSymbol

/**
 * A Tag is [Named]. Additionally it obeys the syntactic restrictions
 * defined for [edn
 * Symbols](https://github.com/edn-format/edn#symbols).
 *
 *
 * Note: Tags print with a leading hash, but this is not part of the tag's name:
 *
 * <pre>
 * `// For the tag "#foo/bar"
 * Tag t = newTag("foo", "bar");
 * t.getName()   => "bar"
 * t.getPrefix() => "foo"
 * t.toString()  => "#foo/bar"`
</pre> *
 */
class Tag private constructor(sym: Symbol?) : Named, Comparable<Tag?>, Serializable {
    private val sym: Symbol?

    /** {@inheritDoc}  */
    val prefix: String
        get() = sym.getPrefix()

    /** {@inheritDoc}  */
    val name: String
        get() = sym.getName()

    override fun toString(): String {
        return "#" + sym.toString()
    }

    @Override
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (sym == null) 0 else sym.hashCode()
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
        val other = obj as Tag
        if (sym == null) {
            if (other.sym != null) {
                return false
            }
        } else if (!sym.equals(other.sym)) {
            return false
        }
        return true
    }

    operator fun compareTo(o: Tag): Int {
        return sym.compareTo(o.sym)
    }

    companion object {
        /**
         * Return a Tag with the same prefix and name as `sym`.
         *
         * @param sym
         * a Symbol, never null
         * @return a Tag with the same prefix and name as `sym`.
         */
        fun newTag(sym: Symbol?): Tag {
            return Tag(sym)
        }

        /**
         * Provide a Tag with the given prefix and name.
         *
         *
         * Bear in mind that tags with no prefix are reserved for use by the edn
         * format itself.
         *
         * @param prefix
         * An empty String or a non-empty String obeying the restrictions
         * specified by edn. Never null.
         * @param name
         * A non-empty string obeying the restrictions specified by edn.
         * Never null.
         * @return a Keyword, never null.
         */
        fun newTag(prefix: String?, name: String?): Tag {
            return newTag(newSymbol(prefix, name))
        }

        /**
         * This is equivalent to `newTag("", name)`.
         *
         * @param name
         * A non-empty string obeying the restrictions specified by edn.
         * Never null.
         * @return a Tag without a prefix, never null.
         * @see .newTag
         */
        fun newTag(name: String?): Tag {
            return newTag(newSymbol(EMPTY, name))
        }
    }

    init {
        if (sym == null) {
            throw NullPointerException()
        }
        this.sym = sym
    }
}