package persistent.api

interface PartialPersistentAddable<T> {
    fun add(element: T): Boolean
}

interface FullPersistentAddable<T, V: Version<T>> {
    fun add(element: T, version: V): Boolean
}