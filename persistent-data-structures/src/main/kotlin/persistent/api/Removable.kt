package persistent.api

interface PartialPersistentRemovable<T> {
    fun remove(element: T): Boolean
}

interface FullPersistentRemovable<T, V: Version<T>> {
    fun remove(element: T, version: V): Boolean
}