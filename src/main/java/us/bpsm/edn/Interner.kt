// (c) 2012 B Smith-Mannschott -- Distributed under the Eclipse Public License
package us.bpsm.edn

import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.Map
import java.util.concurrent.ConcurrentHashMap

internal class Interner<K, V> {
    private val table: ConcurrentHashMap<K, Reference<V>> = ConcurrentHashMap<K, Reference<V>>()
    private val refQueue: ReferenceQueue<V> = ReferenceQueue<V>()
    fun intern(key: K, value: V): V {
        while (true) {
            clearDeadEntries()
            val newRef: WeakReference<V> = WeakReference<V>(value, refQueue)
            val existingRef: Reference<V> = table.putIfAbsent(key, newRef)
                    ?: // newRef has been entered into the cache
                    return value

            // existingRef was found in the cache; newRef is garbage
            val existingValue: V = existingRef.get()
            if (existingValue != null) {
                return existingValue
            }

            // existingRef's referent has been collected out from under us
            table.remove(key, existingRef)
        }
    }

    private fun clearDeadEntries() {
        if (refQueue.poll() == null) {
            // empty queue indicates that there's nothing to clear
            return
        }
        while (refQueue.poll() != null) {
            // wait until there's nothing more in flight in the queue
        }
        for (me in table.entrySet()) {
            val ref: Reference<V> = me.getValue()
            if (ref != null && ref.get() == null) {
                table.remove(me.getKey(), ref)
            }
        }
    }
}