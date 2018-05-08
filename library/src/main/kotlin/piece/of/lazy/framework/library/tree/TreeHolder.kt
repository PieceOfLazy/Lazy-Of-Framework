package piece.of.lazy.framework.library.tree

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import piece.of.lazy.framework.library.piece.PieceHolder
import kotlin.reflect.KClass

/**
 * Created by zpdl
 */

abstract class TreeHolder<H : RecyclerView.ViewHolder, I : Any>(private val holderClass: KClass<H>, private val itemClass: KClass<I>): PieceHolder<H, I>(holderClass, itemClass) {

    fun bindViewHolder(context: Context?, holder: RecyclerView.ViewHolder?, item: Any, treePosition: TreePosition) {
        if (context != null && holder != null) {
            val castHolder = castHolder(holder)
            val castItem = castItem(item)

            if (castHolder != null && castItem != null) {
                onBindViewHolder(context, castHolder, castItem, treePosition)
            }
        }
    }

    protected fun getBindNode(holder: H): TreeNode<I>? {
        val parent = holder.itemView.parent
        if (parent != null && parent is RecyclerView) {
            val adapter = parent.adapter
            if (adapter != null && adapter is TreeAdapter) {
                val node = adapter.getNode(holder)
                if (node != null && isBindItem(node.getModel())) {
                    @Suppress("UNCHECKED_CAST")
                    return node as TreeNode<I>?
                }
            }
        }
        return null
    }

    override fun onBindViewHolder(context: Context, holder: H, item: I, position: Int) {
        onBindViewHolder(context, holder, item, TreePosition(intArrayOf(position)))
    }

    abstract protected fun onBindViewHolder(context: Context, holder: H, item: I, position: TreePosition)

}
