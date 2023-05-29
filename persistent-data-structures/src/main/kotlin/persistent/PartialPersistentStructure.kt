package persistent

import persistent.api.*

interface PartialPersistentStructure<T>: PartialPersistentAddable<T>, PartialPersistentGettable<T>,
    PartialPersistentRemovable<T>, PartialPersistentSettable<T> {

    override fun add(element: T): Boolean {
        throw UnsupportedOperationException("The operation add(element) is unsupported.")
    }

    override fun get(index: Int): T {
        throw UnsupportedOperationException("The operation get(index) is unsupported.")
    }

    override fun remove(element: T): Boolean {
        throw UnsupportedOperationException("The operation remove(element) is unsupported.")
    }

    override fun set(index: Int, newValue: T): Void {
        throw UnsupportedOperationException("The operation set(index, newValue) is unsupported.")
    }

    override fun set(oldValue: T, newValue: T): Boolean {
        throw UnsupportedOperationException("The operation add(oldValue, newValue) is unsupported.")
    }
}