package lazy.of.framework.library.tree

/**
 * Created by zpdl
 */

class TreeModelImpl(isVisible: Boolean = true, isExpansion: Boolean = true): TreeModel {

    override var isVisible: Boolean = isVisible
        set(value) {
            if (field != value) {
                field = value
                callback?.onChangedVisible(this@TreeModelImpl)
            }
        }

    override var isExpansion: Boolean = isExpansion
        set(value) {
            if (field != value) {
                field = value
                callback?.onChangedExpansion(this@TreeModelImpl)
            }
        }

    private var callback: TreeModel.OnModelCallBack? = null

    override fun setOnModelCallBack(callback: TreeModel.OnModelCallBack) {
        this.callback = callback
    }

    override fun getNode(): TreeNode<*>? = callback?.onGetTreeNode()
}