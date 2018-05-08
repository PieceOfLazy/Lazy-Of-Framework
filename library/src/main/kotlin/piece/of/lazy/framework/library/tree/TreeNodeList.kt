package piece.of.lazy.framework.library.tree

/**
 * Created by zpdl
 */

internal class TreeNodeList(private val node: TreeNodeImpl<*>) {

    private var _viewCount: Int = 0
    private val _list: MutableList<TreeNodeImpl<*>> = mutableListOf()

    private var _updateTraversals = false

    internal fun viewCount(): Int {
        return _viewCount
    }

    internal fun notifyTraversals() {
        _updateTraversals = true
        node.notifyTraversals()
    }

    internal fun getCount(): Int = _list.size

    internal fun get(index: Int): TreeNodeImpl<*>? {
        return if (index in 0 until _list.size) {
            _list[index]
        } else
            null
    }

    internal fun setNodeId(root: TreeRoot) {
        for (node in _list) {
            node.setNodeId(root)
        }
    }

    internal fun clear() {
        node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.REMOVE, 0, viewCount()))

        for (node in _list) {
            node.parent = null
        }
        _list.clear()

        node.notifyTraversals()
    }

    internal fun add(nodes: MutableList<TreeNodeImpl<*>>) {
        add(_list.size, nodes)
    }

    internal fun add(position: Int, nodes: MutableList<TreeNodeImpl<*>>) {
        val startNodePosition: Int
        val startViewPosition: Int
        if (position < _list.size) {
            startNodePosition = position
            startViewPosition = _list[startNodePosition].viewPosition
        } else {
            startNodePosition = _list.size
            startViewPosition = viewCount()
        }

        var addedViewCount = 0
        for (node in nodes) {
            node.parent = this.node
            node.viewPosition =  startViewPosition + addedViewCount
            node.getRoot()?.let {
                node.setNodeId(it)
            }
            node.traversals()
            addedViewCount += node.getViewCount()
        }

        if (addedViewCount > 0) {
            node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.INSERT, startViewPosition, addedViewCount))
        }
        _list.addAll(startNodePosition, nodes)

        _viewCount = startViewPosition + addedViewCount
        for(i in startNodePosition until _list.size) {
            _list[i].viewPosition = _viewCount
            _viewCount += _list[i].getViewCount()
        }
        node.notifyTraversals()
    }

    internal fun remove(node: TreeNodeImpl<*>) {
        val index = _list.indexOf(node)
        if (index >= 0) {
            remove(index, 1)
        }
    }

    internal fun remove(position: Int, count: Int) {
        var removedViewPosition = -1
        var removedViewCount = 0

        for (i: Int in 0 until count) {
            if(position in 0 until _list.size) {
                if(removedViewPosition < 0) {
                    removedViewPosition = _list[position].viewPosition
                }
                removedViewCount += _list[position].getViewCount()
            }
        }

        if (removedViewPosition >= 0 && removedViewCount > 0) {
            node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.REMOVE, removedViewPosition, removedViewCount))
        }

        for (i: Int in 0 until count) {
            if(position in 0 until _list.size) {
                val node = _list.removeAt(position)
                @Suppress("UNNECESSARY_SAFE_CALL")
                node?.parent = null
            }
        }

        _viewCount = removedViewPosition
        for(i in position until _list.size) {
            _list[i].viewPosition = _viewCount
            _viewCount += _list[i].getViewCount()
        }
        node.notifyTraversals()
    }

    internal fun indexOf(node: TreeNodeImpl<*>): Int {
        return _list.indexOf(node)
    }

    internal fun set(position: Int, node: TreeNodeImpl<*>) {
        if(position in 0 until _list.size) {
            node.parent = this.node
            node.viewPosition = _list[position].viewPosition
            _list[position] = node
        }
    }

    internal fun replace(from: TreeNodeImpl<*>, to: TreeNodeImpl<*>): TreeNodeImpl<*>? {
        val index = _list.indexOf(from)
        if(index !in 0 until _list.size) {
            return null
        }
        to.traversals()

        val viewPosition = from.viewPosition
        val fromViewCount = from.getViewCount()
        val toViewCount = to.getViewCount()

        to.parent = this.node
        to.viewPosition = viewPosition
        _list[index] = to

        if(from.getModel()::class == to.getModel()::class) {
            to.setNodeId(from.getNodeId())
            this.node.getRoot()?.let {
                replaceNodeId(from, to, it)
            }
        } else {
            this.node.getRoot()?.let {
                to.setNodeId(it)
            }
        }

        when {
            fromViewCount > toViewCount -> {
                node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.CHANGE, viewPosition, toViewCount))
                node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.REMOVE, viewPosition + toViewCount, fromViewCount - toViewCount))
                notifyTraversals()
            }
            fromViewCount < toViewCount -> {
                node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.CHANGE, viewPosition, fromViewCount))
                node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.INSERT, viewPosition + fromViewCount, toViewCount - fromViewCount))
                notifyTraversals()
            }
            toViewCount > 0 -> node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.CHANGE, viewPosition, toViewCount))
        }

        return to
    }

    private fun replaceNodeId(from: TreeNodeImpl<*>, to: TreeNodeImpl<*>, treeRoot: TreeRoot) {
        val fromNodeList = from.treeNodeList
        val toNodeList = to.treeNodeList

        if(toNodeList == null || toNodeList.getCount() <= 0) {
            return
        }

        if(fromNodeList == null || fromNodeList.getCount() <= 0) {
            toNodeList.setNodeId(treeRoot)
            return
        }

        var index = 0
        while(index < fromNodeList.getCount() && index < toNodeList.getCount()) {
            val fromChildNode = fromNodeList.get(index)
            val toChildNode = toNodeList.get(index)
            if(fromChildNode == null || toChildNode == null) {
                break
            }

            if(fromChildNode.getModel()::class == toChildNode.getModel()::class) {
                toChildNode.setNodeId(fromChildNode.getNodeId())
                replaceNodeId(fromChildNode, toChildNode, treeRoot)
                index++
            } else {
                break
            }
        }

        for (i: Int in index until toNodeList.getCount()) {
            val toChildNode = toNodeList.get(index) ?: continue
            toChildNode.setNodeId(treeRoot)
        }
    }

    internal fun swap(from: TreeNodeImpl<*>, to: TreeNodeImpl<*>): Boolean {
        if(from.getViewCount() != 1 || to.getViewCount() != 1) {
            return false
        }

        val fromViewPosition = this.node.getTreePosition(from)
        val toViewPosition = this.node.getTreePosition(to)

        if(fromViewPosition == null || toViewPosition == null) {
            return false
        }

        val fromIndex = from.parent?.treeNodeList?.indexOf(from) ?: -1
        val toIndex = to.parent?.treeNodeList?.indexOf(to) ?: -1
        if(fromIndex < 0 || toIndex < 0) {
            return false
        }

        this.node.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.MOVE, fromViewPosition.getViewPosition(), toViewPosition.getViewPosition()))

        from.parent?.treeNodeList?.set(fromIndex, to)
        to.parent?.treeNodeList?.set(toIndex, from)

        return true
    }

    internal fun getTreePosition(viewPosition: Int): TreePosition? {
        var low = 0
        var high = getCount() - 1
        var mid = (low + high) / 2
        while (low <= high) {
            val node = get(mid)
            if (node == null) {
                high = (low + high) / 2
                mid = (low + high) / 2
                continue
            }

            val cPos = node.getViewPosition()
            val cCnt = node.getViewCount()
            if (cCnt <= 0) {
                mid++
                continue
            }

            if (cPos > viewPosition) {
                high = if (mid < high) mid - 1 else high - 1
            } else if (cPos + cCnt - 1 < viewPosition) {
                low = mid + 1
            } else {
                break
            }
            mid = (low + high) / 2
        }
        val node = get(mid)
        return if (node != null) {
            TreePosition.compose(mid,
                    node.getTreePosition(viewPosition - node.getViewPosition()))
        } else
            null
    }

    internal fun traversals(): Int {
        if (_updateTraversals) {
            _viewCount = 0
            for (childNode in _list) {
                childNode.viewPosition = _viewCount
                _viewCount += childNode.traversals()
            }
        }
        return _viewCount
    }
}