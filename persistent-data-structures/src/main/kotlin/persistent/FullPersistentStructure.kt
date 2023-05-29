package persistent

import persistent.api.*

interface FullPersistentStructure<T, V: Version<T>>: FullPersistentAddable<T, V>, FullPersistentGettable<T, V>,
    FullPersistentRemovable<T, V>, FullPersistentSettable<T, V> {

    override fun add(element: T, version: V): Boolean {
        throw UnsupportedOperationException("The operation add(element, version) is unsupported.")
    }

    override fun get(index: Int, version: V): T {
        throw UnsupportedOperationException("The operation get(index, version) is unsupported.")
    }

    override fun remove(element: T, version: V): Boolean {
        throw UnsupportedOperationException("The operation remove(element, version) is unsupported.")
    }

    override fun set(index: Int, newValue: T, version: V): Void {
        throw UnsupportedOperationException("The operation set(index, newValue, version) is unsupported.")
    }

    override fun set(oldValue: T, newValue: T, version: V): Boolean {
        throw UnsupportedOperationException("The operation add(oldValue, newValue, version) is unsupported.")
    }
}

