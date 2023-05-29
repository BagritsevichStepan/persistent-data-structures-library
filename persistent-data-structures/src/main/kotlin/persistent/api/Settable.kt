package persistent.api

interface PartialPersistentSettable<T> {
    fun set(index: Int, newValue: T): Void
    fun set(oldValue: T, newValue: T): Boolean
}

interface FullPersistentSettable<T, V: Version<T>> {
    fun set(index: Int, newValue: T, version: V): Void
    fun set(oldValue: T, newValue: T, version: V): Boolean
}