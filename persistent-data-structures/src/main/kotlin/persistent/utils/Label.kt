package persistent.utils

interface BitLabel: Comparable<BitLabel> {
    val SIZE_BITS: Int

    fun addBit(indexFromLeft: Int)
    fun getLast32Bits(): UInt
    fun getLast64Bits(): ULong
    fun getLast128Bits(): Pair<ULong, ULong>
    fun getLastBits(lastBitCount: Int): BitLabel
}

class BitLabel128(private var higherBits: ULong = 0u, private var lowerBits: ULong = 0u): BitLabel {
    override val SIZE_BITS: Int
        get() = 128

    override fun addBit(indexFromLeft: Int) {
        if (indexFromLeft <= SIZE_BITS) {
            var bit: ULong = 1u
            bit = bit shl ((SIZE_BITS - indexFromLeft).coerceAtLeast(0).coerceAtMost(SIZE_BITS)
                            % ULong.SIZE_BITS)
            if (isHigherBits(indexFromLeft)) {
                higherBits = higherBits or bit
            } else {
                lowerBits = lowerBits or bit
            }
        }
    }

    override fun getLast32Bits(): UInt = (getLastBits(UInt.SIZE_BITS, higherBits) shl (ULong.SIZE_BITS - UInt.SIZE_BITS)).toUInt()

    override fun getLast64Bits(): ULong = higherBits

    override fun getLast128Bits(): Pair<ULong, ULong> = Pair(higherBits, lowerBits)

    override fun getLastBits(lastBitCount: Int): BitLabel = BitLabel128(
        higherBits = getLastBits(lastBitCount, higherBits),
        lowerBits = getLastBits(lastBitCount - ULong.SIZE_BITS, lowerBits)
    )

    private fun getLastBits(lastBitCount: Int, from: ULong): ULong {
        val bitCountToRemove = (ULong.SIZE_BITS - lastBitCount)
            .coerceAtLeast(0)
            .coerceAtMost(ULong.SIZE_BITS)
        return (from shl bitCountToRemove) shr bitCountToRemove
    }

    private fun isHigherBits(lastBitCount: Int): Boolean = lastBitCount <= ULong.SIZE_BITS

    override fun compareTo(other: BitLabel): Int {
        val (otherHigherBits, otherLowerBits) = other.getLast128Bits()
        return if (higherBits != otherHigherBits)
            higherBits.compareTo(otherHigherBits)
        else
            lowerBits.compareTo(otherLowerBits)
    }
}