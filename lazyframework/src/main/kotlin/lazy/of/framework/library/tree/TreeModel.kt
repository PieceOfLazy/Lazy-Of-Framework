package lazy.of.framework.library.tree

/**
 * Created by zpdl
 */

interface TreeModel {
    interface OnModelCallBack {
        fun onGetTreeNode(): TreeNode<*>
        fun onChangedVisible(model: TreeModel)
        fun onChangedExpansion(model: TreeModel)
    }

    var isVisible: Boolean

    var isExpansion: Boolean

    fun setOnModelCallBack(callBack: OnModelCallBack)

    fun getNode(): TreeNode<*>?
}