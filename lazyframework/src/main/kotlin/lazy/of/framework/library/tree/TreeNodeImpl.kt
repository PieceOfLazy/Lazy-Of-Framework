package lazy.of.framework.library.tree

/**
 * Created by zpdl
 */

open class TreeNodeImpl<M: Any>(private var model: M) : TreeNode<M> {

    override var isVisible: Boolean
        get() = _model?.isVisible ?: true
        set(value) {
            _model?.isVisible = value
        }

    override var isExpansion: Boolean
        get() = _model?.isExpansion ?: false
        set(value) {
            _model?.isExpansion = value
        }

    private val modelCallBack = object : TreeModel.OnModelCallBack {

        override fun onGetTreeNode(): TreeNode<*> = this@TreeNodeImpl

        override fun onChangedVisible(model: TreeModel) {
            if(model.isVisible) {
                parent?.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.INSERT, viewPosition, 1))
            } else {
                parent?.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.REMOVE, viewPosition, 1))
            }
            parent?.treeNodeList?.notifyTraversals()
        }

        override fun onChangedExpansion(model: TreeModel) {
            changedNode()

            val viewPosition = this@TreeNodeImpl.viewPosition + if(model.isVisible) 1 else 0
            if(model.isExpansion) {
                treeNodeList?.let {
                    if(it.viewCount() > 0) {
                        parent?.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.INSERT, viewPosition, it.viewCount()))
                        parent?.treeNodeList?.notifyTraversals()
                    }
                }
            } else {
                treeNodeList?.let {
                    if(it.viewCount() > 0) {
                        parent?.notifyCompose(TreeNotifyParam(TreeNotifyParam.STATE.REMOVE, viewPosition, it.viewCount()))
                        parent?.treeNodeList?.notifyTraversals()
                    }
                }
            }
        }
    }

    internal var parent: TreeNodeImpl<*>? = null

    internal var viewPosition: Int = 0

    private var _model: TreeModel? = null
    internal val treeNodeList: TreeNodeList? by lazy {
        if(_model != null) TreeNodeList(this) else null
    }

    private var nodeId: Long = 0

    init {
        _model = if(model is TreeModel) {
            model as TreeModel
        } else {
            null
        }

        _model?.setOnModelCallBack(modelCallBack)
    }

    override fun beginTransition() {
        getRoot()?.beginTransition()
    }

    override fun applyTo() {
        getRoot()?.applyTo()
    }

    override fun setModel(model: M) {
        this.model = model
    }

    override fun getModel(): M {
        return this.model
    }

    override fun getViewPosition(): Int = viewPosition

    override fun getViewCount(): Int {
        var viewCount = if(isVisible) 1 else 0
        if(isExpansion) {
            treeNodeList?.let {
                viewCount += it.viewCount()
            }
        }
        return viewCount
    }

    override fun getParent(): TreeNode<*>? = parent

    internal open fun getRoot(): TreeRoot? = parent?.getRoot()

    internal fun getTreePosition(node: TreeNodeImpl<*>): TreePosition? {
        var treePosition = TreePosition(intArrayOf(node.viewPosition))
        var parentNode = node.parent
        while(parentNode != this) {
            if(parentNode == null) {
                return null
            }

            treePosition = TreePosition.compose(parentNode.viewPosition, treePosition)
            parentNode = parentNode.parent
        }

        return treePosition
    }

    override fun getTreePosition(viewPosition: Int): TreePosition? {
        if(viewPosition in 0 until getViewCount()) {
            var index = viewPosition
            if (isVisible) {
                if (index == 0) {
                    return null
                }
                index--
            }
            return treeNodeList?.getTreePosition(index)
        }
        return null
    }

    override fun getNode(treePosition: TreePosition): TreeNode<*>? {
        var result: TreeNode<*>? = this
        for(position in treePosition.position) {
            if(result == null) {
                return null
            }
            result = result.getChildNode(position)
        }

        return result
    }

    internal open fun setNodeId(root: TreeRoot) {
        nodeId = root.nextNodeId()
        treeNodeList?.setNodeId(root)
    }

    internal open fun setNodeId(nodeId: Long) {
        this.nodeId = nodeId
    }

    override fun getNodeId(): Long = nodeId

    override fun getChildNodeCount(): Int = treeNodeList?.getCount() ?: 0

    override fun getChildNode(position: Int): TreeNode<*>? = treeNodeList?.get(position)

    override fun changedNode() {
        parent?.notifyCompose(TreeNotifyParam(
                TreeNotifyParam.STATE.CHANGE,
                viewPosition,
                1
        ))
    }

    override fun clearChildNode() {
        treeNodeList?.clear()
    }

    override fun addChildNode(vararg nodes: TreeNode<*>) {
        val nodesImpl = mutableListOf<TreeNodeImpl<*>>()
        for (i: Int in 0 until nodes.size) {
            if(nodes[i] is TreeNodeImpl<*>) {
                nodesImpl.add(nodes[i] as TreeNodeImpl<*>)
            }
        }

        treeNodeList?.add(nodesImpl)
    }

    override fun addChildNode(position: Int, vararg nodes: TreeNode<*>) {
        val nodesImpl = mutableListOf<TreeNodeImpl<*>>()
        for (i: Int in 0 until nodes.size) {
            if(nodes[i] is TreeNodeImpl<*>) {
                nodesImpl.add(nodes[i] as TreeNodeImpl<*>)
            }
        }

        treeNodeList?.add(position, nodesImpl)
    }

    override fun removeChildNode(node: TreeNode<*>) {
        if(node is TreeNodeImpl<*>) {
            treeNodeList?.remove(node)
        }
    }

    override fun removeChildNode(position: Int, count: Int) {
        treeNodeList?.remove(position, count)
    }

    override fun replaceChildNode(from: TreeNode<*>, to: TreeNode<*>): TreeNodeImpl<*>? {
        if(from is TreeNodeImpl<*> && to is TreeNodeImpl<*>) {
            return treeNodeList?.replace(from, to)
        }
        return null
    }

    override fun swapChildNode(from: TreeNode<*>, to: TreeNode<*>): Boolean {
        if(from !is TreeNodeImpl<*> || to !is TreeNodeImpl<*>) {
            return false
        }

        return treeNodeList?.swap(from, to) ?: false
    }

    internal open fun notifyCompose(param: TreeNotifyParam) {
        if(isExpansion) {
            parent?.let {
                param.position += (viewPosition + if (isVisible) 1 else 0)
                it.notifyCompose(param)
            }
        }
    }

    internal open fun notifyTraversals() {
        if(isExpansion) {
            parent?.treeNodeList?.notifyTraversals()
        }
    }

    internal fun traversals(): Int {
        var viewCount = if(isVisible) 1 else 0
        if(isExpansion) {
            treeNodeList?.let {
                viewCount += it.traversals()
            }
        }
        return viewCount
    }
}