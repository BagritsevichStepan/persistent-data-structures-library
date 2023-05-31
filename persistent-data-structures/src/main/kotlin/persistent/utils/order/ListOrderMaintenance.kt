package persistent.utils.order

import java.util.NoSuchElementException

interface OrderMaintenanceStructure<T> {
    fun insertAfter(element: T, after: T?)
    fun isBefore(before: T, after: T): Boolean
}

interface BitLabeledTree<T: BitLabeledTreeNode<*>> {
    fun insertAfter(after: T?): T
}

interface BitLabeledTreeNode<T: BitLabel> {
    val label: T
}

class ListOrderMaintenance<T, V: BitLabeledTreeNode<*>> (private val globalNodes: BitLabeledTree<V>): OrderMaintenanceStructure<T> {
    private var tail: Node? = null
    private val nodes: MutableMap<T, Node> = HashMap()

    companion object {
        private const val MIN_LOCAL_LABEL: Long = Long.MIN_VALUE
        private const val MAX_LOCAL_LABEL: Long = Long.MAX_VALUE
        private const val LOCAL_LABEL_DELTA: Long = Int.MAX_VALUE + 8L
    }

    private inner class GlobalLabel(val labeledNode: V, private var size: Int = 0, private var lastNode: Node? = null) {

        fun insert(node: Node) {
            val mustBeSplitted = size > 0 && size >= getSize().log2()

            node.localLabel = getLocalLabel(node.prevNode, node, node.nextNode)
            node.globalLabel = this
            if (isLastNode(node)) {
                lastNode = node
            }
            size++

            if (mustBeSplitted) {
                split()
            }
        }

        private fun split() {
            val secondSize = size shr 1
            val secondGlobalLabel = GlobalLabel(
                globalNodes.insertAfter(labeledNode),
                secondSize,
                lastNode
            )

            var curNode = lastNode
            repeat(secondSize) {
                curNode?.globalLabel = secondGlobalLabel
                curNode = curNode?.prevNode ?:
                        throw IllegalStateException("There are no $secondSize elements in this global label block.")
            }

            lastNode = curNode
            size -= secondSize

            recalculateLocalLabels()
            secondGlobalLabel.recalculateLocalLabels()
        }

        private fun recalculateLocalLabels() {
            var curNode = lastNode
            var prevLocalLabel: Long = MAX_LOCAL_LABEL
            repeat(size) {
                curNode?.localLabel = prevLocalLabel - LOCAL_LABEL_DELTA
                curNode = curNode?.prevNode ?: return@repeat
                prevLocalLabel = curNode?.localLabel ?: return@repeat
            }
        }

        private fun getLocalLabel(prev: Node?, cur: Node, next: Node?): Long {
            val leftLabel: Long = prev?.localLabel ?: MIN_LOCAL_LABEL
            val rightLabel: Long = if (!isLastNode(cur))
                (next?.localLabel ?: MAX_LOCAL_LABEL)
            else
                MAX_LOCAL_LABEL
            return (leftLabel + rightLabel) shr 1
        }

        private fun isLastNode(node: Node): Boolean = node.prevNode == lastNode
    }

    override fun insertAfter(element: T, after: T?) {
        val node = insertAfter(element, if (after != null) getNode(after) else tail)
        if (node.nextNode == null) {
            tail = node
        }
    }

    private fun insertAfter(element: T, after: Node?): Node {
        val before: Node? = after?.nextNode
        val node = Node(
            0L,
            after?.globalLabel ?: GlobalLabel(globalNodes.insertAfter(null)),
            after,
            before
        )

        after?.nextNode = node
        before?.prevNode = node

        nodes[element] = node
        node.globalLabel.insert(node)
        return node
    }

    override fun isBefore(before: T, after: T): Boolean {
        val beforeNode = getNode(before)
        val afterNode = getNode(after)
        return beforeNode.compareTo(afterNode) == -1
    }

    private fun getNode(element: T) =
        nodes[element] ?: throw NoSuchElementException("Element $element is not found.")

    private fun getSize(): Int = nodes.size

    private fun Int.log2(): Int = Int.SIZE_BITS - countLeadingZeroBits()

    private inner class Node(var localLabel: Long, var globalLabel: GlobalLabel,
                             var prevNode: Node? = null, var nextNode: Node? = null) : Comparable<Node> {

        override fun compareTo(other: Node): Int {
            val thisGlobalLable: BitLabel = globalLabel.labeledNode.label
            val otherGlobalLable: BitLabel = other.globalLabel.labeledNode.label
            val globalLabelsCompareResult = thisGlobalLable.compareTo(otherGlobalLable)
            return if (globalLabelsCompareResult != 0) globalLabelsCompareResult else localLabel.compareTo(other.localLabel)
        }

        override fun equals(other: Any?): Boolean {
            if (other is ListOrderMaintenance<*, *>.Node) {
                return localLabel == other.localLabel && globalLabel == other.globalLabel
            }
            return false
        }

        override fun hashCode() = 31 * localLabel.hashCode() + globalLabel.hashCode()
    }
}




