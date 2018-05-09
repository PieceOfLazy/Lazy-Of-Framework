package lazy.of.framework.library.tree

/**
 * Created by zpdl
 */

interface TreeNode<M: Any> {

    var isVisible: Boolean

    var isExpansion: Boolean

    fun beginTransition()

    fun applyTo()

    fun setModel(model: M)

    fun getModel(): M

    fun getViewPosition(): Int

    fun getViewCount(): Int

    fun getParent(): TreeNode<*>?

    fun getTreePosition(viewPosition: Int): TreePosition?

    fun getNode(treePosition: TreePosition): TreeNode<*>?

    fun getNodeId(): Long

    fun getChildNodeCount(): Int

    fun getChildNode(position: Int): TreeNode<*>?

    fun changedNode()

    fun clearChildNode()

    fun addChildNode(vararg nodes: TreeNode<*>)

    fun addChildNode(position: Int, vararg nodes: TreeNode<*>)

    fun removeChildNode(position: Int, count: Int)

    fun removeChildNode(node: TreeNode<*>)

    fun replaceChildNode(from: TreeNode<*>, to: TreeNode<*>): TreeNode<*>?

    fun swapChildNode(from: TreeNode<*>, to: TreeNode<*>): Boolean

}