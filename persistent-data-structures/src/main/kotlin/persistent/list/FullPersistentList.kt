package persistent.list

import persistent.FullPersistentStructure

interface FullPersistentList<T, V: PersistentListVersion<T>>: FullPersistentStructure<T, V> {
}