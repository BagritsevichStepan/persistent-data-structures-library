package persistent.utils

interface BitLabeledBinarySearchTree<T: BitLabeledBSTNode<V>, V: BitLabel> {
    var root: T?
    var size: Int
    var maxSize: Int
    var hAlpha: Int
    var height: Int

    fun insertAfter(after: T): T
    fun find(element: V): T
}

interface BitLabeledBSTNode<T: BitLabel> {
    fun getLabel(): T
    fun getLeft(): BitLabeledBSTNode<T>?
    fun getRight(): BitLabeledBSTNode<T>?
    fun getHeight(): Int
    fun getDepth(): Int
    fun getParent(): BitLabeledBSTNode<T>?
}

class BitLabeledScapegoatTree(
    override var root: ScapegoatTreeNode?,
    override var size: Int,
    override var maxSize: Int,
    override var hAlpha: Int,
    override var height: Int
) : BitLabeledBinarySearchTree<ScapegoatTreeNode, BitLabel128>, BitLabeledTree<BitLabeledTreeNode> {
    override fun insertAfter(after: BitLabeledTreeNode): BitLabeledTreeNode {
        TODO("Not yet implemented")
    }

    override fun insertAfter(after: ScapegoatTreeNode): ScapegoatTreeNode {
        TODO("Not yet implemented")
    }

    override fun find(element: BitLabel128): ScapegoatTreeNode {
        TODO("Not yet implemented")
    }
}

class ScapegoatTreeNode: BitLabeledBSTNode<BitLabel128>, BitLabeledTreeNode {
    override var label: BitLabel
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun getLabel(): BitLabel128 {
        TODO("Not yet implemented")
    }

    override fun getLeft(): BitLabeledBSTNode<BitLabel128>? {
        TODO("Not yet implemented")
    }

    override fun getRight(): BitLabeledBSTNode<BitLabel128>? {
        TODO("Not yet implemented")
    }

    override fun getHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun getDepth(): Int {
        TODO("Not yet implemented")
    }

    override fun getParent(): BitLabeledBSTNode<BitLabel128>? {
        TODO("Not yet implemented")
    }
}