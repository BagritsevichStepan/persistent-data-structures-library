package persistent.utils.order

import persistent.utils.logWithBase
import kotlin.math.ln

interface BitLabeledBinarySearchTree<T: BitLabeledBSTNode<*>>: BitLabeledTree<T>

interface BitLabeledBSTNode<T: BitLabel>: BitLabeledTreeNode<T> {
    val left: BitLabeledBSTNode<T>?
    val right: BitLabeledBSTNode<T>?
    val parent: BitLabeledBSTNode<T>?
    var depth: Int
    var size: Int
    var height: Int
    fun setLeft(node: BitLabeledBSTNode<T>?)
    fun setRight(node: BitLabeledBSTNode<T>?)
    fun setParent(node: BitLabeledBSTNode<T>?)
    fun setBit(bit: Boolean, index: Int)
    fun setLabel(newLabel: T)
    fun isLeaf(): Boolean = height == 0
    fun updateParentDependentProperties() {
        updateDepth()
    }
    fun updateChildrenDependentProperties() {
        updateSize()
        updateHeight()
    }
    fun updateHeight() {
        height = 1 + (left?.height ?: 0).coerceAtLeast(right?.height ?: 0)
    }
    fun updateSize() {
        size = 1 + (left?.size ?: 0) + (right?.size ?: 0)
    }
    fun updateDepth() {
        depth = 1 + (parent?.depth ?: 0)
    }
}

abstract class AbstractBitLabeledBinarySearchTree<T: BitLabeledBSTNode<*>>: BitLabeledBinarySearchTree<T> {
    protected var root: T? = null
}

class BitLabeledScapegoatTree(private val nodeFactory: () -> BitLabel128 = { BitLabel128() }):
    AbstractBitLabeledBinarySearchTree<ScapegoatTreeNode>() {

    private fun upOnTreeAndDo(fromNode: ScapegoatTreeNode?, function: (ScapegoatTreeNode?) -> Unit) {
        var node = fromNode
        while (node != null) {
            function.invoke(node)
            node = node.parent
        }
    }

    override fun insertAfter(after: ScapegoatTreeNode?): ScapegoatTreeNode {
        if (after == null) {
            root = ScapegoatTreeNode(nodeFactory)
            return root!!
        }

        val node = ScapegoatTreeNode(nodeFactory)
        if (after.right != null) {
            var curNode: ScapegoatTreeNode = after.right!!
            while (curNode.left != null) {
                  curNode = curNode.left!!
            }
            curNode.setLeftLeaf(node)
        } else {
            after.setRightLeaf(node)
        }

        findScapegoat(node)?.let { scapegot ->
            val scapegoatParent = scapegot.parent
            val scapegoatWasLeftChild = scapegoatParent?.left == scapegot

            val newRoot = removeScapegoat(scapegot)
            newRoot?.setParent(scapegoatParent)
            if (scapegoatWasLeftChild) {
                scapegoatParent?.setLeft(newRoot)
            } else {
                scapegoatParent?.setRight(newRoot)
            }

            upOnTreeAndDo(scapegoatParent) {
                it?.updateHeight()
            }
        }
        return node
    }

    private fun findScapegoat(newLeaf: ScapegoatTreeNode): ScapegoatTreeNode? {
        var scapegoat: ScapegoatTreeNode? = null
        upOnTreeAndDo(newLeaf) {
            it?.updateChildrenDependentProperties()
            if (it?.isScapegoatNode() == true) {
                scapegoat = it
            }
        }
        return scapegoat
    }

    private fun removeScapegoat(scapegoat: ScapegoatTreeNode): ScapegoatTreeNode? {
        val list: MutableList<ScapegoatTreeNode> = arrayListOf()
        flattenTree(scapegoat, list)
        return buildHeightBalancedTree(scapegoat.depth, scapegoat.label, 0, list.size, list)
    }

    private fun flattenTree(node: ScapegoatTreeNode, list: MutableList<ScapegoatTreeNode>) {
        node.left?.let { flattenTree(it, list) }
        list.add(node)
        node.right?.let { flattenTree(it, list) }
    }

    private fun buildHeightBalancedTree(depth: Int, label: BitLabel128, from: Int, to: Int, list: List<ScapegoatTreeNode>): ScapegoatTreeNode? {
        if (from >= to) {
            return null
        }
        val node: Int = (from + to) shr 1
        var left: ScapegoatTreeNode? = null
        var right: ScapegoatTreeNode? = null
        if (from + 1 < to) {
            left = buildHeightBalancedTree(
                depth + 1, label.clone()
                    .apply { setBit(false, depthBit(depth)) },
                from, node, list
            )
            right = buildHeightBalancedTree(
                depth + 1, label.clone()
                    .apply { setBit(true, depthBit(depth) - 1) },
                node + 1, to, list
            )
            left?.setParent(list[node])
            right?.setParent(list[node])
        }

        list[node].apply {
            this@apply.depth = depth
            setLabel(label)
            setLeft(left)
            setRight(right)
            updateChildrenDependentProperties()
        }
        return list[node]
    }

    private fun ScapegoatTreeNode.setLeftLeaf(leaf: ScapegoatTreeNode) {
        setLeft(leaf)
        setLeaf(leaf)
        leaf.setBit(false, depthBit(depth))
    }

    private fun ScapegoatTreeNode.setRightLeaf(leaf: ScapegoatTreeNode) {
        setRight(leaf)
        setLeaf(leaf)
        leaf.setBit(true, depthBit(depth) - 1)
    }

    private fun ScapegoatTreeNode.setLeaf(leaf: ScapegoatTreeNode) {
        leaf.apply {
            setParent(this@setLeaf)
            updateParentDependentProperties()
            setLabel(this@setLeaf.label)
        }
        updateChildrenDependentProperties()
    }

    private fun depthBit(depth: Int) = (depth shl 1) - 1
}

abstract class AbstractBitLabeledBSTNode<T: BitLabeledBSTNode<V>, V: BitLabel>: BitLabeledBSTNode<V> {
    override var height: Int = 1
    override var depth: Int = 1
    override var size: Int = 1
    override var left: T? = null
        protected set
    override var right: T? = null
        protected set
    override var parent: T? = null
        protected set
}

class ScapegoatTreeNode(private var label128: BitLabel128 = BitLabel128(),
                        private val alphaWithNegExp: Double = 10.0 / 7.0): AbstractBitLabeledBSTNode<ScapegoatTreeNode, BitLabel128>() {
    override val label: BitLabel128
        get() = label128.clone()

    constructor(nodeFactory: () -> BitLabel128): this(nodeFactory.invoke())

    fun isScapegoatNode(): Boolean = height - 1 > size.logWithBase(alphaWithNegExp)

    override fun setBit(bit: Boolean, index: Int) = label128.setBit(bit, index)

    override fun setLabel(newLabel: BitLabel128) {
        label128 = newLabel
    }

    override fun setLeft(node: BitLabeledBSTNode<BitLabel128>?) {
        if (node is ScapegoatTreeNode?) {
            left = node
        } else {
            throw IllegalArgumentException("Left child can not be updated. Node is not ScapegoatTreeNode.")
        }
    }

    override fun setRight(node: BitLabeledBSTNode<BitLabel128>?) {
        if (node is ScapegoatTreeNode?) {
            right = node
        } else {
            throw IllegalArgumentException("Right child can not be updated. Node is not ScapegoatTreeNode.")
        }
    }

    override fun setParent(node: BitLabeledBSTNode<BitLabel128>?) {
        if (node is ScapegoatTreeNode?) {
            parent = node
        } else {
            throw IllegalArgumentException("Parent can not be updated. Node is not ScapegoatTreeNode.")
        }
    }
}