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

    private var callback: TreeModel.OnModelCallBack? = null

    override fun setOnModelCallBack(callback: TreeModel.OnModelCallBack) {
        this.callback = callback
    }

    override fun getNode(): TreeNode<*>? = callback?.onGetTreeNode()
}