package persistent.api

interface Version<T> {
    fun getVersion(): T
}