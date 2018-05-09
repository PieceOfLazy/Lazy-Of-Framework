package lazy.of.framework.library.tree

/**
 * Created by zpdl
 */

class TreeModelEmpty: TreeModel {

    override var isVisible: Boolean
        get() = false
        set(value) {}

    override var isExpansion: Boolean
        get() = true
        set(value) {}

    private var listener: TreeModel.OnModelCallBack? = null

    override fun setOnModelCallBack(l: TreeModel.OnModelCallBack) {
        listener = l
    }

    override fun getNode(): TreeNode<*>? = listener?.onGetTreeNode()
}