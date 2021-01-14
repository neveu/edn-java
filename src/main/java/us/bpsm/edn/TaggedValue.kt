// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

import java.io.Serializable

/**
 * A Tagged value that received no specific handling because the Parser
 * was not configured with a handler for its tag.
 */
class TaggedValue private constructor(tag: Tag, value: Object) : Serializable {
    private val tag: Tag
    private val value: Object?

    /**
     * Returns this TaggedValue's tag, which is never null.
     * @return never null.
     */
    fun getTag(): Tag {
        return tag
    }

    fun getValue(): Object? {
        return value
    }

    @Override
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + tag.hashCode()
        result = prime * result + if (value == null) 0 else value.hashCode()
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
        val other = obj as TaggedValue
        if (!tag.equals(other.tag)) {
            return false
        }
        if (value == null) {
            if (other.value != null) {
                return false
            }
        } else if (!value.equals(other.value)) {
            return false
        }
        return true
    }

    @Override
    override fun toString(): String {
        return tag.toString() + " " + value
    }

    companion object {
        /**
         * Return a tagged value for the given tag and value (some edn data).
         * The tag must not be null.
         * @param tag not null.
         * @param value may be null.
         * @return a TaggedValue, never null.
         */
        fun newTaggedValue(tag: Tag?, value: Object?): TaggedValue {
            if (tag == null) {
                throw IllegalArgumentException("tag must not be null")
            }
            return TaggedValue(tag, value)
        }
    }

    init {
        this.tag = tag
        this.value = value
    }
}