package piece.of.lazy.framework.library.tree

/**
 * Created by zpdl
 */

class TreeModelImpl(isVisible: Boolean = true, isExpansion: Boolean = true): TreeModel {

    override var isVisible: Boolean = isVisible
        set(value) {
            if (field != value) {
                field = value
                listener?.onChangedVisible(this@TreeModelImpl)
            }
        }

    override var isExpansion: Boolean = isExpansion
        set(value) {
            if (field != value) {
                field = value
                listener?.onChangedExpansion(this@TreeModelImpl)
            }
        }

    private var listener: TreeModel.OnModelCallBack? = null

    override fun setOnModelCallBack(l: TreeModel.OnModelCallBack) {
        listener = l
    }

    override fun getNode(): TreeNode<*>? = listener?.onGetTreeNode()
}