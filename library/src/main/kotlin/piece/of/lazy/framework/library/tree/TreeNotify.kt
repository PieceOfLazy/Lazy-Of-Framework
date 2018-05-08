package piece.of.lazy.framework.library.tree

/**
 * Created by zpdl
 */
internal interface TreeNotify {
    fun treeSetChanged()
    fun treeMoved(fromPosition: Int, toPosition: Int)
    fun treeChanged(position: Int, count: Int)
    fun treeInserted(position: Int, count: Int)
    fun treeRemoved(position: Int, count: Int)
}