package persistent.api

interface PartialPersistentGettable<T> {
    fun get(index: Int): T
}

interface FullPersistentGettable<T, V: Version<T>> {
    fun get(index: Int, version: V): T
}