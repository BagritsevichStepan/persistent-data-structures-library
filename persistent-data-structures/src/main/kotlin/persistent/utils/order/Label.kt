package persistent.utils.order

import persistent.utils.Cloneable
import persistent.utils.toULong

interface BitLabel: Comparable<BitLabel>, Cloneable<BitLabel> {
    val SIZE_BITS: Int
    fun setBit(bit: Boolean, index: Int)
    fun getLastBits(lastBitCount: Int): Array<ULong>
    fun getBits(): Array<ULong> = getLastBits(SIZE_BITS)

    override fun compareTo(other: BitLabel): Int {
        val bits = getBits()
        val otherBits = other.getBits()
        for (i in if (bits.size > otherBits.size) bits.indices else otherBits.indices) {
            val bit: ULong = if (i < bits.size) bits[i] else 0u
            val otherBit: ULong = if (i < otherBits.size) otherBits[i] else 0u
            if (bit != otherBit) {
                return bit.compareTo(otherBit)
            }
        }
        return 0
    }
}

open class BitLabel128(private var higherBits: ULong = 0u, private var lowerBits: ULong = 0u): BitLabel {
    override val SIZE_BITS: Int = ULong.SIZE_BITS shl 1

    override fun setBit(bit: Boolean, index: Int) {
        if (index <= SIZE_BITS) {
            var uLongBit: ULong = bit.toULong()
            uLongBit = uLongBit shl ((SIZE_BITS - index).coerceAtLeast(0).coerceAtMost(SIZE_BITS) % ULong.SIZE_BITS)
            if (isHigherBits(index)) {
                higherBits = higherBits or uLongBit
            } else {
                lowerBits = lowerBits or uLongBit
            }
        }
    }

    override fun getLastBits(lastBitCount: Int): Array<ULong> {
        val array: Array<ULong> = arrayOf(getLastBits(lastBitCount, higherBits))
        if (lastBitCount > ULong.SIZE_BITS) {
            array[1] = getLastBits(lastBitCount - ULong.SIZE_BITS, lowerBits)
        }
        return array
    }

    private fun getLastBits(lastBitCount: Int, from: ULong): ULong {
        val bitCountToRemove = (ULong.SIZE_BITS - lastBitCount)
            .coerceAtLeast(0)
            .coerceAtMost(ULong.SIZE_BITS)
        return (from shl bitCountToRemove) shr bitCountToRemove
    }

    override fun clone(): BitLabel128 = BitLabel128(higherBits, lowerBits)

    private fun isHigherBits(lastBitCount: Int): Boolean = lastBitCount <= ULong.SIZE_BITS

    override fun compareTo(other: BitLabel): Int {
        if (other is BitLabel128) {
            return if (higherBits != other.higherBits)
                higherBits.compareTo(other.higherBits)
            else
                lowerBits.compareTo(other.lowerBits)
        }
        return super.compareTo(other)
    }
}